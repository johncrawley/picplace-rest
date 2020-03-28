package com.jacdev.picplacerest.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;

import com.jacdev.picplacerest.entity.Photo;
import com.jacdev.picplacerest.utils.PhotoSize;

public interface PhotoFileRepository {

	//public boolean save(byte[] bytes, String userId, long photoId);
	//public Optional<String> save(byte[] bytes, String userId, long photoId);
	public Photo save(Photo photo, byte[] bytes);
	public void attachThumbnailData(Page<Photo> photos);
	public String getImageData(String userId, long photoId, PhotoSize photoSize);
	public void attachPhotoData(Photo photo, PhotoSize photoSize);
	public boolean delete(String userId, long photoId);
	
	public String getPath(Photo photo, PhotoSize photoSize);

	public void attachPhotoBytes(Photo photo, PhotoSize photoSize);

}
