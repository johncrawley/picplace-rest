package com.jacdev.picplacerest.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
	@Value("${location.absolute_path_prefix}")	private String ABSOLUTE_PATH_PREFIX;
	
	public FilepathResolver() {}
	

	public String getPhotoPath(String userId, long photoId, PhotoSize size) {
		return ABSOLUTE_PATH_PREFIX + getPhotoCreationPath(userId, photoId, size);
	}
	
	
	public String getPhotoCreationPath(String userId, long photoId, PhotoSize size) {
		return getPhotoDir(userId, size) + photoId + PHOTO_FILE_EXTENSION;
	}
	

	public String getPhotoFileExtension() {
		return this.PHOTO_FILE_EXTENSION; 
	}
	
	
	public List<String> getAllPhotoDirs(String userId){
		List <String> directories = new ArrayList<>();
		directories.add(getPhotoDir(userId, PhotoSize.THUMBNAIL));
		directories.add(getPhotoDir(userId, PhotoSize.MEDIUM));
		directories.add(getPhotoDir(userId, PhotoSize.LARGE));
		return directories;
	}
	
	
	public String getPhotoDir(String userId, PhotoSize size) {
		String path = getUserDir(userId) + File.separator + getPhotoDir(size);
		return path;
	}
	

	public String getUserDir(String userId) {
		String path = IMAGES_LOCATION + userId;
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
