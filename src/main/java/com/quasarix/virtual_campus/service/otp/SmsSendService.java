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

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.quasarix.virtual_campus.cache.AppCache;

/**
 * @author ARUN A J
 */
@Service
public class SmsSendService {
	
	@Autowired
	private HttpsConnection httpsConnection;
	private String autherizationKey;
	private String rootUrl;

	public void sendSMS(String message, String number) throws Exception {

		autherizationKey = AppCache.getConfigParameter().get("fast2smsAutherisationKey").getParameterValue();
		rootUrl = AppCache.getConfigParameter().get("sendOTPBaseURL").getParameterValue();

	
			message = URLEncoder.encode(message, "UTF-8");
			List<String> param = new ArrayList<String>();
			List<String> paramValue = new ArrayList<String>();
			param.add("authorization");
			paramValue.add(autherizationKey);
			param.add("variables_values");
			paramValue.add(message);
			param.add("numbers");
			paramValue.add(number);
			String url = setURL(rootUrl, param, paramValue);
			httpsConnection.getConnection(url);
	}

	public String setURL(String root, List<String> parameter, List<String> parameterValue) throws Exception {

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
		return sb.toString();
	}

}
