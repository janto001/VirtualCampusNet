/**
 * Filename: AuthServiceImpl.java © Copyright 2023 Quasarix. ALL RIGHTS RESERVED. All rights, title and interest (including all intellectual property
 * rights) in this software and any derivative works based upon or derived from this software belongs exclusively to Quasarix. Access to this software
 * is forbidden to anyone except current employees of Quasarix and its affiliated companies who have executed non-disclosure agreements explicitly
 * covering such access. While in the employment of Quasarix or its affiliate companies as the case may be, employees may use this software
 * internally, solely in the course of employment, for the sole purpose of developing new functionalities, features, procedures, routines,
 * customizations or derivative works, or for the purpose of providing maintenance or support for the software. Save as expressly permitted above, no
 * license or right thereto is hereby granted to anyone, either directly, by implication or otherwise. On the termination of employment, the license
 * granted to employee to access the software shall terminate and the software should be returned to the employer, without retaining any copies. This
 * software is (i) proprietary to Quasarix; (ii) is of significant value to it; (iii) contains trade secrets of Quasarix; (iv) is not publicly
 * available; and (v) constitutes the confidential information of Quasarix. Any use, reproduction, modification, distribution, public performance or
 * display of this software or through the use of this software without the prior, express written consent of Quasarix is strictly prohibited and may
 * be in violation of applicable laws.
 */
package com.quasarix.virtual_campus.service.auth;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.quasarix.virtual_campus.cache.AppCache;
import com.quasarix.virtual_campus.dao.ds1.model.UserLogin;
import com.quasarix.virtual_campus.dao.ds1.model.UserProfile;
import com.quasarix.virtual_campus.dao.ds1.repository.UserLoginRepository;
import com.quasarix.virtual_campus.dao.ds1.repository.UserProfileRepository;
import com.quasarix.virtual_campus.dto.login.LoginRequest;
import com.quasarix.virtual_campus.dto.login.LoginResponse;
import com.quasarix.virtual_campus.dto.login.OtpRequest;
import com.quasarix.virtual_campus.dto.login.OtpResponse;
import com.quasarix.virtual_campus.dto.login.SignupRequest;
import com.quasarix.virtual_campus.security.jwt.JwtUtils;
import com.quasarix.virtual_campus.security.services.UserDetailsAndOidcUserImpl;
import com.quasarix.virtual_campus.service.otp.OtpService;
import com.quasarix.virtual_campus.service.otp.SmsSendService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author anto.jayaraj
 */
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private UserLogin userLogin;
	@Autowired
	private UserProfile userProfile;
	@Autowired
	private UserLoginRepository userLoginRepository;
	@Autowired
	private UserProfileRepository userProfileRepository;
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private OtpService OtpService;
	@Autowired
	private SmsSendService smsService;

	@Override
	public LoginResponse populateLoginResponse(LoginRequest loginRequest) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		UserDetailsAndOidcUserImpl userDetails = (UserDetailsAndOidcUserImpl) authentication.getPrincipal();
		List<String> authorities = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());
		UserLogin userLogin = userLoginRepository.findUserByUserName(loginRequest.getUsername());
		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
		if (userLogin.getLoginAttempts() >= Integer
				.parseInt(AppCache.getConfigParameter().get("MaximumAttempts").getParameterValue())) {
			userLogin.setIsLocked(1);
		}
		if (userLogin.getLoginAttempts() >= Integer.parseInt(AppCache.getConfigParameter().get("MaximumAttempts").getParameterValue())
				&& (hoursDifference(currentTimestamp, userLogin.getLastLogin()))==Long.parseLong(AppCache.getConfigParameter().get("hoursDifferent").getParameterValue())) {
			userLogin.setIsLocked(0);
			userLogin.setLoginAttempts(0);
		}
		userLogin.setLoginAttempts(userLogin.getLoginAttempts() + 1);
		userLogin.setLastLogin(currentTimestamp);
		try {
			userLoginRepository.save(userLogin);
			log.debug("last_login and Login_Attemps columns for user_login table are updated | userName:{}",loginRequest.getUsername());
		}
		catch (Exception ex) {
			log.error("Error occure while update last_login and Login_Attemps columns for user_login table", ex);
			return new LoginResponse(false, String.valueOf(HttpStatus.BAD_REQUEST), "Sign in failed");
		}

		log.debug("User Login succes | userName:{}" + loginRequest.getUsername());
		return new LoginResponse(jwt, authorities, true, String.valueOf(HttpStatus.OK), "Sign in success");
	}

	private long hoursDifference(Timestamp timestamp1, Timestamp timestamp2) {
		long difference = Math.abs(timestamp1.getTime() - timestamp2.getTime());
		long differenceInHours = difference / (60 * 60 * 1000);
		return differenceInHours;
	}

	@Override
	public LoginResponse populateSignupResponse(SignupRequest signupRequest) {
		if (!jwtUtils.getUserNameFromJwtToken(signupRequest.getAuthentication()).equals(signupRequest.getUsername())) {
			log.info("Un-verified user name | userName:{}",signupRequest.getUsername());
			return new LoginResponse(false, String.valueOf(HttpStatus.UNAUTHORIZED), "Please enter the verified user name");
		}

		if (userLoginRepository.existsByUserName(signupRequest.getUsername())) {
			log.info("User is already exist | userName:{}",signupRequest.getUsername());
			return new LoginResponse(false, String.valueOf(HttpStatus.FORBIDDEN), "User is already exist, please login");
		}
		userProfile.setFirstName(signupRequest.getFirstName());
		userProfile.setLastName(signupRequest.getLastName());
		Date birthDate = null;
		try {
			birthDate = new SimpleDateFormat("dd/MM/yyyy").parse(signupRequest.getBirthDate());
			userProfile.setBirthDate(birthDate);
		}
		catch (ParseException e) {
			log.error("unabe to parse date of birth | userName:{}",signupRequest.getUsername(), e);
		}
		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
		userProfile.setGender(signupRequest.getGender());
		userProfile.setEmail(signupRequest.getEmail());
		userProfile.setPhoneNumber(signupRequest.getMsisdn());
		userProfile.setUserRoleId(1);
		userProfile.setRegistrationDate(currentTimestamp);
		userLogin.setUserName(signupRequest.getUsername());
		userLogin.setPassword(encoder.encode(signupRequest.getPassword()));
		userLogin.setLastLogin(currentTimestamp);
		userLogin.setLoginAttempts(0);
		userLogin.setIsLocked(0);
		userLogin.setUserProfile(userProfile);
		try {
			userProfileRepository.save(userProfile);
			if (signupRequest.getEmail() != null) {
				long profileId = Long.parseLong(userProfileRepository.getProfileByEmail(signupRequest.getEmail()));
				userLogin.setUserId(profileId);
			}
			else {
				long profileId = Long.parseLong(userProfileRepository.getProfileByPhoneNuber(signupRequest.getMsisdn()));
				userLogin.setUserId(profileId);
			}
			userLoginRepository.save(userLogin);
			log.debug("User profile table created | userName:{}",signupRequest.getUsername());
		}
		catch (Exception e) {
			log.error("Unable to create the user in database",e);
			return new LoginResponse(false, String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR), "Unable to create the user in database");
		}
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(signupRequest.getUsername(), signupRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		UserDetailsAndOidcUserImpl userDetails = (UserDetailsAndOidcUserImpl) authentication.getPrincipal();
		List<String> authorities = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());
		log.info("Signup Success | userName:{} " + signupRequest.getUsername());
		return new LoginResponse(jwt, authorities, true, String.valueOf(HttpStatus.OK), "Signup success");
	}

	@Override
	public OtpResponse sendEmailOtp(OtpRequest otpRequest) {

		try {
			int otp = OtpService.generateOTP(otpRequest.getEmail());
			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setFrom(AppCache.getConfigParameter().get("mailId").getParameterValue());
			msg.setTo(otpRequest.getEmail());
			msg.setSubject("OTP verification");
			msg.setText("Your OTP is : " + otp);
			javaMailSender.send(msg);
			log.info("Successfully send email OTP | mailId:{}",otpRequest.getEmail());
			return new OtpResponse(true, "Otp send to the mail", String.valueOf(HttpStatus.OK));
		}
		catch (Exception e) {
			log.error("Failed to send email OTP | mailId:{}" + otpRequest.getEmail(),e);
			return new OtpResponse(false, "Otp Can't send to the mail", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
		}
	}

	@Override
	public OtpResponse validateEmailOtp(OtpRequest otpRequest) {
		if (OtpService.validateOTP(otpRequest.getEmail(), otpRequest.getOtp())) {
			OtpService.clearOTP(otpRequest.getEmail());
			String jwt = jwtUtils.generateJwtToken(otpRequest.getEmail());
			log.info("Email OTP is verified | userName:{}" + otpRequest.getEmail());
			return new OtpResponse(true, "Email OTP is verified", String.valueOf(HttpStatus.OK), jwt);
		}
		log.info("Failed to validate OTP | userName:{}" + otpRequest.getEmail());
		return new OtpResponse(false, "Email OTP is invalid", String.valueOf(HttpStatus.UNAUTHORIZED));
	}

	@Override
	public OtpResponse sendMsisdnOtp(OtpRequest otpRequest) {
		try {
			int otp = OtpService.generateOTP(otpRequest.getMsisdn());
			smsService.sendSMS(String.valueOf(otp), otpRequest.getMsisdn());
			log.info("MSISDN OTP is send | msisdnNmber:{}" + otpRequest.getMsisdn());
			return new OtpResponse(true, "OTP is send to the phone number", String.valueOf(HttpStatus.OK));
		}
		catch (Exception ex) {
			log.error("Unable to sent otp | msisdnNuber:{} ",otpRequest.getMsisdn(),ex);
			return new OtpResponse(false, "unable to sent otp to the given nuber", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
		}
	}

	@Override
	public OtpResponse validateMsisdnOtp(OtpRequest otpRequest) {
		if (OtpService.validateOTP(otpRequest.getMsisdn(),otpRequest.getOtp())) {
			OtpService.clearOTP(otpRequest.getMsisdn());
			String jwt = jwtUtils.generateJwtToken(otpRequest.getMsisdn());
			log.info("msisdn OTP is verified | msisdnNumber:{}" + otpRequest.getMsisdn());
			return new OtpResponse(true, "SMS OTP is verified", String.valueOf(HttpStatus.OK), jwt);
		}
		log.info("Failed to validate msisdn OTP | msisdnNumber:{}",otpRequest.getMsisdn());
		return new OtpResponse(false, "Invalid SMS-OTP", String.valueOf(HttpStatus.UNAUTHORIZED));
	}
}
