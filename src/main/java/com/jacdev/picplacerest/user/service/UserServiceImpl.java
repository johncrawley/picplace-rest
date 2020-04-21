package com.jacdev.picplacerest.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.jacdev.picplacerest.photo.repository.PhotoFileRepository;
import com.jacdev.picplacerest.user.UserEntity;
import com.jacdev.picplacerest.user.UserForm;
import com.jacdev.picplacerest.user.repository.UserRepository;


@Service
public class UserServiceImpl implements UserService {

	
	@Autowired UserRepository userRepository;
	@Autowired PhotoFileRepository photoFileRepository;
	@Autowired BCryptPasswordEncoder passwordEncoder;
	
	
	public UserServiceImpl() {
	}
	

	@Override
	public boolean exists(String username) {
		return userRepository.findById(username).isPresent();
	}
	
	@Override
	public UserRequestStatus createUser(UserForm form) {
		
		boolean wereDirectoriesCreated = photoFileRepository.createUserDirs(form.getUsername());
		if(wereDirectoriesCreated) {
			createUserRecord(form);
		}
		return UserRequestStatus.USER_ADDED; //TODO: need to return different status based on possible errors
	}
	
	
	private void createUserRecord(UserForm form) {
		UserEntity user = new UserEntity();
		user.setUsername(form.getUsername());
		user.setPassword(createEncryptedPasswordFrom(form));
		user.setEmail(form.getEmail());
		System.out.println("searching for username: " + form.getUsername());
		UserEntity userEntity = userRepository.findByUsername(form.getUsername());
		if(userEntity == null) {
			System.out.println("Name not found!");
		}
		user.addRole("USER");
		user.setEnabled(true);
		userRepository.save(user);
	}

	
	public String createEncryptedPasswordFrom(UserForm form) {
		String pw = form.getPassword();
		return passwordEncoder.encode(pw);
	}

}
