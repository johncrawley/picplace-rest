package com.jacdev.picplacerest.controller;

import java.util.List;

import com.jacdev.picplacerest.entity.Photo;

public class PhotoGroup {
	

	private boolean isLast;
	private List<Photo> photos;

	public PhotoGroup(List<Photo> photos, boolean isLast) {
		this.photos = photos;
		this.isLast = isLast;
	}
	
	public boolean isLast() {
		return isLast;
	}
	
	public List<Photo> getPhotos(){
		return photos;
	}

	
}
