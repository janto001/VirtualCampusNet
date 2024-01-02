/**
 * Filename: OtpServiceImplementation.java
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
package com.quasarix.virtual_campus.service.otp;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.quasarix.virtual_campus.cache.AppCache;

import lombok.extern.slf4j.Slf4j;


/**
 * @author ARUN A J
 */

@Slf4j
@Service
public class OtpServiceImpl implements OtpService {
	
	private static final Integer EXPIRE_MINS = Integer.parseInt(AppCache.getConfigParameter().get("OTPExpireTime").getParameterValue());
	private LoadingCache<String, Integer> otpCache;

	public OtpServiceImpl() {
		super();
		otpCache = CacheBuilder.newBuilder().expireAfterWrite(EXPIRE_MINS, TimeUnit.MINUTES).build(new CacheLoader<String, Integer>() {
			public Integer load(String key) {
				return 0;
			}
		});
	}

	@Override
	public int generateOTP(String key) {
		Random random = new Random();
		int otp = 100000 + random.nextInt(900000);
		otpCache.put(key, otp);
		log.debug("Otp generated | userName:{} ",key);
		return otp;
	}

	@Override
	public int getOtp(String key) {
		try {
			log.debug("Getting otp from otpCache | userName : {}",key);
			return otpCache.get(key);
		}
		catch (Exception e) {
			log.error("Unable to find otp | userName:{} ",key,e);
			return 0;
		}
	}
	
	public void clearOTP(String key) {
		log.debug("Clear otp from the otp cache | userName:{} "+key);
		otpCache.invalidate(key);
	}

	
}

