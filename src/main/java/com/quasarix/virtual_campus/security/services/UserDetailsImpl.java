/**
 * Filename: UserDetailsImpl.java
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
package com.quasarix.virtual_campus.security.services;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.quasarix.virtual_campus.cache.RolePermissionCache;
import com.quasarix.virtual_campus.dao.ds1.model.RolePermission;
import com.quasarix.virtual_campus.dao.ds1.model.UserLogin;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author anto.jayaraj
 */
@Slf4j
@Getter
@Setter
public class UserDetailsImpl implements UserDetails, OidcUser {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String username;
	private String email;
	private String gender;
	private String phoneNumber;
	private int isLocked;
	@JsonIgnore
	private String password;
	private Collection<? extends GrantedAuthority> authorities;
	private Map<String, Object> attributes;
	private OidcUser oidcUser;

	public UserDetailsImpl() {
	}

	public UserDetailsImpl(OidcUser oidcUser) {
		System.out.println(oidcUser);
		this.oidcUser = oidcUser;
		this.email = oidcUser.getEmail();
		this.username = oidcUser.getName();
		this.gender = oidcUser.getGender();
	}

	public UserDetailsImpl(Long id, String username, String email, String password, Collection<? extends GrantedAuthority> authorities,
			Map<String, Object> attributes) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.authorities = authorities;
		this.attributes = attributes;
	}

	public UserDetailsImpl(Long id, String username, String email, String phoneNumber, int isLocked, String password,
			Collection<? extends GrantedAuthority> authorities) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.isLocked = isLocked;
		this.password = password;
		this.authorities = authorities;
	}

	public UserDetailsImpl(Long id, String username, int isLocked, String password, Collection<? extends GrantedAuthority> authorities) {
		super();
		this.id = id;
		this.username = username;
		this.isLocked = isLocked;
		this.password = password;
		this.authorities = authorities;
	}

	public static UserDetails build(UserLogin userProfile) {
		log.debug("Setup userdetailsImpl : ", userProfile.getUserName());
		List<RolePermission> rolepermission = RolePermissionCache.getRoleAndPermissionCache();
		Set<String> permisionForRoles = new HashSet<String>();
		rolepermission.forEach(value -> {
			if (value.getUserRole().getRoleId() == userProfile.getUserProfile().getUserRoleId()) {
				permisionForRoles.add(value.getUserPermission().getPermissionName());
			}
		});
		List<GrantedAuthority> authorities = permisionForRoles.stream()
				.map(grantList -> new SimpleGrantedAuthority("ROLE_" + grantList))
				.collect(Collectors.toList());
		return new UserDetailsImpl(userProfile.getUserId(), userProfile.getUserName(), userProfile.getIsLocked(), userProfile.getPassword(),
				authorities);
	}

	@Override
	public Map<String, Object> getAttributes() {
		return oidcUser.getAttributes();
	}

	@Override
	public String getName() {
		return this.username;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return this.authorities;
	}

	@Override
	public String getPassword() {

		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {

		if (isLocked == 0) {
			return true;
		}
		else {
			return false;
		}
		// return this.isLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
		// return this.isLocked;
	}

	@Override
	public Map<String, Object> getClaims() {
		return oidcUser.getClaims();
	}

	@Override
	public OidcUserInfo getUserInfo() {
		return oidcUser.getUserInfo();
	}

	@Override
	public OidcIdToken getIdToken() {
		return oidcUser.getIdToken();
	}

}

