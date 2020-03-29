package com.jacdev.picplacerest.photo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.jacdev.picplacerest.photo.Photo;

public interface PhotoRepository extends PagingAndSortingRepository<Photo, Long>{ 
	
	public Page<Photo> findByUserId(String userId, Pageable page);
	public List<Photo> findByUserId(String userId);
	public Long countByUserId(String userId);
}
