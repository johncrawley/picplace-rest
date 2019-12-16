package com.jacdev.picplacerest.repository.user;

import org.springframework.data.repository.CrudRepository;

import com.jacdev.picplacerest.model.user.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, String> {

	public UserEntity findByEmail(String email);
	public UserEntity findByUsername(String username);
}
