package com.jacdev.picplacerest.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jacdev.picplacerest.repository.user.UserRepository;

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

}
