package com.jacdev.picplacerest.model.user;

import javax.persistence.Embeddable;

import org.springframework.security.core.GrantedAuthority;

@Embeddable
public class UserRole implements GrantedAuthority{
	
	private String authority;
	public UserRole() {
		
	}
	
	public UserRole(String authority) {
		this.authority = authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	
	public String getAuthority() {
		return this.authority;
	}
	

}
