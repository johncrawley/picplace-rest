package com.jacdev.picplacerest.form;


import org.springframework.web.multipart.MultipartFile;

public class PhotoForm {

	private MultipartFile file;
	private String title;
	private String username;
	
	public PhotoForm() {
		// TODO Auto-generated constructor stub
	}
	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile photoFile) {
		this.file = photoFile;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	
	

}
