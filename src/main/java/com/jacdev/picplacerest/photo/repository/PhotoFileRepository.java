package com.jacdev.picplacerest.photo.repository;

import org.springframework.data.domain.Page;

import com.jacdev.picplacerest.photo.Photo;
import com.jacdev.picplacerest.utils.PhotoSize;

public interface PhotoFileRepository {

	public Photo save(Photo photo, byte[] bytes);
	public void attachThumbnailData(Page<Photo> photos);
	public String getImageData(String userId, long photoId, PhotoSize photoSize);
	public void attachPhotoData(Photo photo, PhotoSize photoSize);
	public boolean delete(String userId, long photoId);
	public boolean createUserDirs(String username);	
	public String getPath(Photo photo, PhotoSize photoSize);
	public void attachPhotoBytes(Photo photo, PhotoSize photoSize);
}
