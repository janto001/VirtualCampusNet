/**
 * Filename: UserProfile.java
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

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author anto.jayaraj
 */
@Getter
@Setter
@Entity
@Table(name = "user_profile")
public class UserProfile {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "last_name", nullable = false)
	private String lastName;

	@Column(name = "email", unique = true)
	private String email;

	@Column(name = "phone_number", unique = true)
	private String phoneNumber;

	@Column(name = "birth_date", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date birthDate;

	@Column(name = "gender", nullable = false)
	private String gender;

	@Column(name = "bio", columnDefinition = "TEXT")
	private String bio;

	@Column(name = "profile_picture_url")
	private String profilePictureUrl;

	@Column(name = "cover_photo_url")
	private String coverPhotoUrl;

	@Column(name = "registration_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date registrationDate;

	@Column(name = "last_login")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLogin;

	@Column(name = "user_role_Id", nullable = false)
	private Integer userRoleId;

	@ManyToOne
	@JoinColumn(name = "user_role_Id", insertable = false, updatable = false)
	private UserRole userRole;
}

