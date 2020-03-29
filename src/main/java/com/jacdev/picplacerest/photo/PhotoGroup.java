package com.jacdev.picplacerest.photo;

import java.util.List;

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
