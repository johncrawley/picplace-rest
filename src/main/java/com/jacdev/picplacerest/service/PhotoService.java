package com.jacdev.picplacerest.service;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.jacdev.picplacerest.entity.Photo;
import com.jacdev.picplacerest.form.PhotoForm;
import com.jacdev.picplacerest.utils.PhotoSize;

public interface PhotoService {
	public Page<Photo> getThumbnails(String username, Pageable pageable);
	public List<Photo> getAllPhotos(String username);
	public Photo getPhoto(String photoId);
	public Photo getPhoto(long photoId);
	public int getNumberOfPages(Pageable page);
	public int getPhotosPerPage(Pageable page);
	public long getPhotoCountFor(String userId);
	public List<Long> getPhotoIds(String userId);
	public void deletePhoto(String userId, long photoId);
	
	public Photo createPhoto(MultipartFile file, String username);
	public byte[] get(long id, String size);
	public Page<Photo> getPhotosDetails(String userId, String photoSizeStr, Pageable page);

	
	//public Page<Integer> getThumbnailIds(String userId, Pageable pageable);
	//public Photo getThumbnail(String userId, Integer photoId);
	//public void deletePhoto(String userId, Integer photoId);
}
