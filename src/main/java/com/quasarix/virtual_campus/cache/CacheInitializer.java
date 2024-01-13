/**
 * Filename: CacheInitializer.java
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
package com.quasarix.virtual_campus.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.quasarix.virtual_campus.dao.ds1.model.ConfigParameter;
import com.quasarix.virtual_campus.dao.ds1.model.RolePermission;
import com.quasarix.virtual_campus.dao.ds1.repository.ConfigParameterrepository;
import com.quasarix.virtual_campus.dao.ds1.repository.RolePermissionRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ARUN A J
 */
@Component
@Slf4j
public class CacheInitializer {

	@Autowired
	private RolePermissionRepository rolePermissionRepository;
	
	@Autowired
	private ConfigParameterrepository configParameterrepository;
	
	@PostConstruct
	private void initlizeCache() {
		loadCache();
	}
	
	public void loadCache() {
		loadConfigurationParameters();
		loadRolesAndPermissions();
	}
	public void loadConfigurationParameters(){
		List<ConfigParameter> configParameterValues = configParameterrepository.findAll();
		if (!configParameterValues.isEmpty()) {
			Map<String, ConfigParameter> setValuesToMap = new HashMap<String, ConfigParameter>();
			configParameterValues.forEach(value -> {
				setValuesToMap.put(value.getParameterName(), value);
			});
			AppCache.setConfigParameter(setValuesToMap);
			log.info("Configuration parameters are added to the AppCache");
		}
		else {
			log.warn("Unable to add configuration parameters to the AppCache");
		}
	}
	
	
	public void loadRolesAndPermissions(){

		List<RolePermission> rolepermission = rolePermissionRepository.findAll();
		if (rolepermission.isEmpty()) {
			log.warn("Unable to fetch roles and permission from RolePermissionRepository");
		}
		else {
			AppCache.setRolesAndPermissions(rolepermission);
			log.info("Roles and permissions are added to the AppCache");
		}
		
	}
	
}

