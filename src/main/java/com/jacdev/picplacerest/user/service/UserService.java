package com.jacdev.picplacerest.user.service;

import com.jacdev.picplacerest.user.UserForm;

public interface UserService {
	
	
	public boolean exists(String username);
	public UserRequestStatus createUser(UserForm userForm);
}
