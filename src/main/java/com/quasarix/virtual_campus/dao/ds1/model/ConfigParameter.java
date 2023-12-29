/**
 * Filename: ConfigParameter.java © Copyright 2023 Quasarix. ALL RIGHTS RESERVED. All rights, title and interest (including all intellectual property
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
package com.quasarix.virtual_campus.dao.ds1.model;

import java.sql.Timestamp;
import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
/**
 * @author ARUN A J
 */

@Getter
@Setter
@Entity
@Table(name = "CNFG_CONFIG_PARAMETERS")
public class ConfigParameter {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PARAMETER_ID")
	private Long parameterId;

	@Column(name = "PARAMETER_NAME", length = 300, nullable = false)
	private String parameterName;

	@Column(name = "PARAMETER_VALUE", length = 600, nullable = false)
	private String parameterValue;

	@Column(name = "PARAMETER_TYPE", length = 60)
	private String parameterType;

	@Column(name = "DESCRIPTION", length = 600)
	private String description;

	@Column(name = "CREATE_DATE")
	private Timestamp createDate;

	@Column(name = "CHANGE_DATE")
	private Date changeDate;
}
