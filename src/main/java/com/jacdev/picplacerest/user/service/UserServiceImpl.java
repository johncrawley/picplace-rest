package com.jacdev.picplacerest.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jacdev.picplacerest.user.UserEntity;
import com.jacdev.picplacerest.user.UserForm;
import com.jacdev.picplacerest.user.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	
	@Autowired UserRepository userRepository;
	
	public UserServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean exists(String username) {
		return userRepository.findById(username).isPresent();
	}
	
	@Override
	public boolean createUser(UserForm form) {
		UserEntity user = new UserEntity();
		user.setUsername(form.getEmail());
		user.setPassword(form.getPassword());
		
		UserEntity savedUser = userRepository.save(user);
		//TODO: figure out how to return a boolean, or just return the entity
		return false;
	}

}
