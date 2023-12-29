/**
 * Filename: SmsSendService.java © Copyright 2023 Quasarix. ALL RIGHTS RESERVED. All rights, title and interest (including all intellectual property
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
package com.quasarix.virtual_campus.service.otp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import org.springframework.stereotype.Service;
import com.quasarix.virtual_campus.cache.AppCache;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ARUN A J
 */
@Slf4j
@Service
public class SmsSendServiceImpl implements ThirdPartySMSService {
	private String autherizationKey;
	private String rootUrl;

	@SuppressWarnings("static-access")
	@Override
	public void sendSMS(String message, String number) throws Exception {

		autherizationKey = AppCache.globalCache.getGlobalVariableCache().get("fast2smsAutherisationKey").getParameterValue();
		rootUrl = AppCache.globalCache.getGlobalVariableCache().get("sendOTPBaseURL").getParameterValue();

		try {
			message = URLEncoder.encode(message, "UTF-8");
			List<String> param = new ArrayList<String>();
			List<String> paramValue = new ArrayList<String>();
			param.add("authorization");
			paramValue.add(autherizationKey);
			param.add("variables_values");
			paramValue.add(message);
			param.add("numbers");
			paramValue.add(number);
			HttpsURLConnection con = setURL(rootUrl, param, paramValue);
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			con.setRequestProperty("cache-control", "no-cache");
			StringBuffer response = new StringBuffer();
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			while (true) {
				String line = br.readLine();
				if (line == null) {
					break;
				}
				response.append(line);
			}
			log.debug("OTP sent successfully to the mobile :" + number);
			log.info("OTP sent successfully.");
		}
		catch (Exception e) {

			log.debug("Failed to send OTP to the number :" + number);
			log.info("Failed to send OTP.");
			throw e;
		}

	}

	public HttpsURLConnection setURL(String root, List<String> parameter, List<String> parameterValue) throws Exception {

		StringBuilder sb = new StringBuilder();
		sb.append(root);
		if (!root.contains("?")) {
			sb.append("?");
		}
		else {
			sb.append("&");
		}
		for (int i = 0; i < parameter.size(); i++) {
			sb.append(parameter.get(i));
			sb.append("=");
			sb.append(parameterValue.get(i));
			if (parameter.size() - 1 > i) {
				sb.append("&");
			}
		}
		String myUrl = sb.toString();
		URL url = new URL(myUrl);
		log.info("Url for send OTP is : " + url);
		return (HttpsURLConnection) url.openConnection();
	}

}
