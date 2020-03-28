package com.jacdev.picplacerest.service;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import com.jacdev.picplacerest.entity.Photo;
import com.jacdev.picplacerest.exception.BadRequestException;
import com.jacdev.picplacerest.repository.PhotoFileRepository;
import com.jacdev.picplacerest.repository.PhotoRepository;
import com.jacdev.picplacerest.utils.PhotoSize;


@Service
public class PhotoServiceImpl implements PhotoService {

	@Autowired private PhotoRepository photoRepository;
	@Autowired private PhotoFileRepository photoFileRepository;
	@Value("${file.directory}") private String language;
	private long itemsCount; 

	@Autowired private ResourceLoader resourceLoader;


	@Value("${location.thumbnail_directory}") 	private String THUMBNAIL_DIR;
	@Value("${location.medium_directory}") 		private String MEDIUM_DIR;
	@Value("${location.large_directory}") 		private String LARGE_DIR;
	
	

	public PhotoServiceImpl() {
		// TODO Auto-generated constructor stub
	}
	

	public Photo getPhoto(String photoId) {
		long id = Long.valueOf(photoId).longValue();
		System.out.println("photo ID = " + photoId);
		Optional<Photo> optional = photoRepository.findById(id);
		Photo photo = optional.orElse(new Photo());
		if(photo.isInitialised()) {
			photoFileRepository.attachPhotoData(photo, PhotoSize.MEDIUM);
		}else {
			System.out.println("photo is not initialised!");
		}
		return photo;
	}

	
	@Override
	public Photo getPhoto(long id) {
		Optional<Photo> optional = photoRepository.findById(id);
		Photo photo = optional.orElse(new Photo());
		if(photo.isInitialised()) {
			photoFileRepository.attachPhotoBytes(photo, PhotoSize.MEDIUM);
		}else {
			System.out.println("photo is not initialised!");
		}
		return photo;
	}

	
	@Override
	public byte[] get(long id, String size) {
		
		String path = queryPath(id, size);
		Resource resource = resourceLoader.getResource(path);
		
		try {
			return StreamUtils.copyToByteArray(resource.getInputStream());
		}
		catch(IOException e) {
			return new byte[0];
		}
	}

	
	private String queryPath(long id, String sizeStr) {

		Optional<Photo> optional = photoRepository.findById(id);
		Photo photo = optional.orElse(new Photo());
		PhotoSize photoSize = getPhotoSize(sizeStr);
		return photoFileRepository.getPath(photo, photoSize);
	}
	
	
	
	private PhotoSize getPhotoSize(String size) {
		return isNullOrEmpty(size) ? PhotoSize.MEDIUM : PhotoSize.valueOf(size.toUpperCase());
	}
	
	private boolean isNullOrEmpty(String str) {
		return str == null || str.isEmpty();
	}
	
	
	

	public Page<Photo> getPhotosDetails(String userId, String sizeStr, Pageable page){	
		Page <Photo> photoPage =  photoRepository.findByUserId(userId, page);
		
		PhotoSize photoSize = getPhotoSize(sizeStr);
		return photoRepository.findByUserId(userId, page);
	}
	
	
	
	
	public boolean deletePhoto(String userId, int photoId) {
		
		long id = Long.valueOf(photoId).longValue();
		Optional<Photo> optional = photoRepository.findById(id);
		if(!optional.isPresent()) {
			return false;
		}
		photoRepository.delete(optional.get());
		photoFileRepository.delete(userId, photoId);
		
		return false;
	}
	

	@Override
	@Transactional
	public Photo createPhoto(MultipartFile photoFile, String username) {

		Photo photo = new Photo();
		try {
			byte [] bytes = photoFile.getBytes();
			photo.setUserId(username);
			photo.setTitle("");
			photo = photoRepository.save(photo);
			photo = photoFileRepository.save(photo, bytes);
		}catch(IOException e) {
			return null;
		}
		return photo;
	}
	
		
	@Override
	public int getNumberOfPages(Pageable page) {
		itemsCount = photoRepository.count();
		int itemsPerPage = page.getPageSize();
		int numberOfPages =  (int) (itemsCount / itemsPerPage);
		
		return numberOfPages == 0 ? 1 : numberOfPages;
	}
	

	
	@Override
	public long getPhotoCountFor(String userId) {
		return photoRepository.countByUserId(userId);
	}
	
	
	@Override
	public int getPhotosPerPage(Pageable page) {
		int pageSize = page.getPageSize();
		return pageSize == 0 ? 20 : pageSize;
	}

	
	@Override
	public Page<Photo> getThumbnails(String username, Pageable pageable) {
		Page<Photo> photos = photoRepository.findByUserId(username, pageable);
		
		photoFileRepository.attachThumbnailData(photos);
		return photos;
	}
	/*
	@Override List<Integer> getThumbnailIds(String userId, Pageable pageable){
		
		Page<Photo> photos = photoRepository.findByUserId(userId,  pageable);
		
		return photos.stream().map(u -> u.getId()).collect(Collectors.toList());
	}
*/
	@Override
	public List<Photo> getAllPhotos(String username) {
		return photoRepository.findByUserId(username);
	}
	
	@Override
	public void deletePhoto(String userId, long photoId) {
		
		validateUserAndResource(userId, photoId);
		photoRepository.deleteById(photoId);
		photoFileRepository.delete(userId, photoId);
	}
	

	public List<Long> getPhotoIds(String userId){
		return photoRepository.findByUserId(userId).stream().map(x -> x.getId()).collect(Collectors.toList());
	}
	
	
	private void validateUserAndResource(String userId, Long photoId) {
		Optional<Photo> optional = photoRepository.findById(photoId);
		if(!optional.isPresent()) {
			throw new ResourceNotFoundException();
		}
		Photo photo = optional.get();
		if(!userId.equals(photo.getUserId())) {
			throw new BadRequestException();
		}
	}
	
	
}
