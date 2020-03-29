package com.jacdev.picplacerest.user.repository;

import org.springframework.data.repository.CrudRepository;

import com.jacdev.picplacerest.user.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, String> {

	public UserEntity findByEmail(String email);
	public UserEntity findByUsername(String username);
}
