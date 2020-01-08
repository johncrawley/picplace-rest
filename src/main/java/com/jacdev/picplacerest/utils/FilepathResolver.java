package com.jacdev.picplacerest.utils;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FilepathResolver {

	@Value("${location.images}")				private String IMAGES_LOCATION;
	@Value("${location.use_home_dir}")			private boolean USE_HOME_DIR;
	@Value("${location.thumbnail_directory}") 	private String THUMBNAIL_DIR;
	@Value("${location.medium_directory}") 		private String MEDIUM_DIR;
	@Value("${location.large_directory}") 		private String LARGE_DIR;
	@Value("${photo.extension}")				private String PHOTO_FILE_EXTENSION;
	
	public FilepathResolver() {
		
	}
	
	public String getPhotoPath(String userId, long photoId, PhotoSize size) {
		return getPhotoDir(userId, size) + photoId + PHOTO_FILE_EXTENSION;
	}

	public String getPhotoFileExtension() {
		return this.PHOTO_FILE_EXTENSION; 
	}
	
	public String getPhotoDir(String userId, PhotoSize size) { 

		String path = IMAGES_LOCATION +  userId + File.separator + getPhotoDir(size);

		if(USE_HOME_DIR) {
			return System.getProperty("user.home") + File.separator + path;
		} 
		return path;
	}
	
	
	private String getPhotoDir(PhotoSize size) {
		String dir = "";
		switch(size) {
			case THUMBNAIL: dir = THUMBNAIL_DIR; break;
			case MEDIUM:    dir = MEDIUM_DIR;    break;
			case LARGE:     dir = LARGE_DIR;
		}
		return dir;
	}
	
}
