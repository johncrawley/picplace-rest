package com.jacdev.picplacerest.model.user;


import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.assertj.core.util.Arrays;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;


@Entity
public class AppUser{
    
    private Long userId;
    private String username,firstName,lastName, gender, email,countryCode;
    private String encrytedPassword;
    private boolean enabled;
    private boolean authenticated;

    public AppUser() {

    }
    
	@Id
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }



	public AppUser(Long userId, String userName, String firstName, String lastName, 
            boolean enabled, String gender,
            String email,String countryCode, String encrytedPassword) {
        super();
        this.userId = userId;
        this.username = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.enabled = enabled;
        this.gender = gender;
        this.email = email;
        this.countryCode= countryCode;
        this.encrytedPassword = encrytedPassword;
    }


    public String getUserName() {
        return username;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEncrytedPassword() {
        return encrytedPassword;
    }

    public void setEncrytedPassword(String encrytedPassword) {
        this.encrytedPassword = encrytedPassword;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

}

