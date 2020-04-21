package com.jacdev.picplacerest.user.service;

public enum UserRequestStatus {
	
	USER_ADDED("User Added Successfully"),
	EMAIL_ALREADY_USED("There is already an account for the given email address."),
	USER_ALREADY_EXISTS("The given user name already exists.");
	
	private String message;
	
	private UserRequestStatus(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

}
