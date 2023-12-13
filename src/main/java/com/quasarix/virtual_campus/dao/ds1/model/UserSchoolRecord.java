/**
 * Filename: UserSchoolRecord.java
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
package com.quasarix.virtual_campus.dao.ds1.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * @author anto.jayaraj
 */
@Getter
@Setter
@Entity
@Table(name = "user_school_record")
public class UserSchoolRecord {
	@Id
	@Column(name = "record_id")
	private int recordId;

	@Column(name = "user_id")
	private int userId;

	@Column(name = "enrollment_year")
	private int enrollmentYear;

	@Column(name = "school_last_year")
	private int schoolLastYear;

	@Column(name = "last_class_id")
	private int lastClassId;

	@Column(name = "school_division_Id")
	private int schoolDivisionId;

	@Column(name = "school_id")
	private int schoolId;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
	private UserProfile userProfile;

	@ManyToOne
	@JoinColumn(name = "school_id", referencedColumnName = "school_id", insertable = false, updatable = false)
	private SchoolDetails schoolDetails;

	@ManyToOne
	@JoinColumn(name = "school_division_Id", referencedColumnName = "division_id", insertable = false, updatable = false)
	private EducationDivision educationDivision;

	@ManyToOne
	@JoinColumn(name = "last_class_id", referencedColumnName = "class_id", insertable = false, updatable = false)
	private EducationClassDetails educationClassDetails;
}

