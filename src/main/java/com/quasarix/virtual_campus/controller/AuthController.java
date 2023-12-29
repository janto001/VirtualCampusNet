/**
 * Filename: AuthController.java
 *
 * © Copyright 2023 Quasarix. ALL RIGHTS RESERVED.

 * All rights, title and interest (including all intellectual property rights) in this software and any derivative works based upon or derived from
 * this software belongs exclusively to Quasarix.

 * Access to this software is forbidden to anyone except current employees of Quasarix and its affiliated companies who have executed non-disclosure
 * agreements explicitly covering such access. While in the employment of Quasarix or its affiliate companies as the case may be, employees may use
 * this software internally, solely in the course of employment, for the sole purpose of developing new functionalities, features, procedures,
 * routines, customizations or derivative works, or for the purpose of providing maintenance or support for the software. Save as expressly permitted
 * above, no license or right thereto is hereby granted to anyone, either directly, by implication or otherwise. On the termination of employment,
 * the license granted to employee to access the software shall terminate and the software should be returned to the employer, without retaining any
 * copies.

 * This software is (i) proprietary to Quasarix; (ii) is of significant value to it; (iii) contains trade secrets of Quasarix; (iv) is not publicly
 * available; and (v) constitutes the confidential information of Quasarix.

 * Any use, reproduction, modification, distribution, public performance or display of this software or through the use of this software without the
 * prior, express written consent of Quasarix is strictly prohibited and may be in violation of applicable laws.
 *
 */
package com.quasarix.virtual_campus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.quasarix.virtual_campus.dto.login.LoginRequest;
import com.quasarix.virtual_campus.dto.login.LoginResponse;
import com.quasarix.virtual_campus.dto.login.OtpRequest;
import com.quasarix.virtual_campus.dto.login.OtpResponse;
import com.quasarix.virtual_campus.dto.login.SignupRequest;
import com.quasarix.virtual_campus.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * @author anto.jayaraj
 */
@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	private AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid
	@RequestBody
	LoginRequest loginRequest) {
		log.debug("login request|userName:{}", loginRequest.getUsername());
		LoginResponse loginResponse = authService.populateLoginResponse(loginRequest);
		if (loginResponse.isSuccess()) {
			return new ResponseEntity<>(loginResponse, HttpStatus.OK);
		}
		else if (loginResponse.getStatusCode().equalsIgnoreCase(Integer.toString(HttpStatus.UNAUTHORIZED.value()))) {
			return new ResponseEntity<>(loginResponse, HttpStatus.UNAUTHORIZED);
		}
		else {
			return new ResponseEntity<>(loginResponse, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/signup")
	public ResponseEntity<?> createUser(@Valid
	@RequestBody
	SignupRequest signUpRequest) {
		log.debug("signup request|userName:{}", signUpRequest.getUsername());
		LoginResponse loginResponse = authService.populateSignupResponse(signUpRequest);
		if (loginResponse.isSuccess()) {
			return new ResponseEntity<>(loginResponse, HttpStatus.CREATED);
		}
		else {
			return new ResponseEntity<>(loginResponse, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/sendEmailOtp")
	public ResponseEntity<?> sendEmailOtp(@Valid
	@RequestBody
	OtpRequest otpRequest) {
		log.debug("email otp request|userName:{}", otpRequest.getEmail());
		OtpResponse otpResponse = authService.sendEmailOtp(otpRequest);
		if (otpResponse.isSuccess()) {
			return new ResponseEntity<>(otpResponse, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(otpResponse, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/validateEmailOtp")
	public ResponseEntity<?> validateEmailOtp(@Valid
	@RequestBody
	OtpRequest otpRequest) {
		log.debug("email otp validation request|userName:{}", otpRequest.getEmail());
		OtpResponse otpResponse = authService.validateEmailOtp(otpRequest);
		if (otpResponse.isSuccess()) {
			return new ResponseEntity<>(otpResponse, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(otpResponse, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/sendMsisdnOtp")
	public ResponseEntity<?> sendMsisdnOtp(@Valid
	@RequestBody
	OtpRequest otpRequest) {
		log.info("msisdn otp request|userName:{}", otpRequest.getMsisdn());
		log.debug("msisdn otp request|userName:{}", otpRequest.getMsisdn());
		OtpResponse otpResponse = authService.sendMsisdnOtp(otpRequest);
		if (otpResponse.isSuccess()) {
			return new ResponseEntity<>(otpResponse, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(otpResponse, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/validateMsisdnOtp")
	public ResponseEntity<?> validateMsisdnOtp(@Valid
	@RequestBody
	OtpRequest otpRequest) {
		log.debug("msisdn otp validation request|userName:{}", otpRequest.getEmail());
		OtpResponse otpResponse = authService.validateMsisdnOtp(otpRequest);
		if (otpResponse.isSuccess()) {
			return new ResponseEntity<>(otpResponse, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(otpResponse, HttpStatus.BAD_REQUEST);
		}
	}

}

