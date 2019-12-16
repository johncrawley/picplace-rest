package com.jacdev.picplacerest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

//import com.jacdev.picplacerest.photoutils.PhotoSize;
//import com.jacdev.picplacerest.service.PhotoService;



@RestController
class RestEndpointController {

	
	@RequestMapping(value = "svc/v1/public/temp")
	public Temp getTemp() {
		return new Temp();
	}

	@GetMapping(value = "svc/test1")
	public Temp test1() {
		
		return new Temp("Jim");
	}
	@GetMapping(value = "svc/test2")
	public Temp test2() {
		return new Temp();
	}
	@GetMapping(value = "svc/test3")
	public Temp test3() {
		
		return new Temp("Ivor");
	}
	@GetMapping(value = "svc/test4")
	public Temp test4() {
		
		return new Temp("Amy");
	}
	
	@RequestMapping(value = "svc/v1/private/accounts/{accountNumber}")
	public String getPrivateAccountData(@PathVariable final int accountNumber) {
		return "Private Account Linked To " + accountNumber;
	}
	@RequestMapping(value = "svc/v1/private/admin/accounts/{accountNumber}")
	public String getPrivateAccountDataAdmin(@PathVariable final int accountNumber) {
		return "Private Account with extra info Linked To " + accountNumber;
	}
	
	/*
	
	@Autowired PhotoService photoService;
	
	@GetMapping(value = "svc/v1/private/photos")
	public List<Long> getPhotoIds(){
		
		return photoService.getPhotoIds(getUsername());
		
	}
	
	@DeleteMapping(value = "svc/v1/private/photos/{photoId}")
	@ResponseStatus(HttpStatus.OK)
	public void deletePhoto(@PathVariable("photoId") long photoId) {
		System.out.println("RestEndpointController - deletePhoto()  : id: "+  photoId);
		photoService.deletePhoto(getUsername(), photoId);
	}
	


	@GetMapping(value = "svc/v1/private/photos/th/{photoId}")
	public String getThumbnailImage(@PathVariable("photoId") long photoId) {
		String username = getUsername();
		return photoService.getImageBase64(username, photoId, PhotoSize.THUMBNAIL);
	}

	@GetMapping(value = "svc/v1/private/photos/{photoId}")
	public String getMediumImage(@PathVariable("photoId") long photoId) {
		String username = getUsername();
		return photoService.getImageBase64(username, photoId, PhotoSize.MEDIUM);
	}

	@GetMapping(value = "svc/v1/private/photos/orig/{photoId}")
	public String getOriginalImage(@PathVariable("photoId") long photoId) {
		String username = getUsername();
		return photoService.getImageBase64(username, photoId, PhotoSize.LARGE);
	}

	*/
	
	   private String getUsername() {
		   Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		   return auth == null ? null : auth.getName();
	   }

	
	class Temp{
		String name = "joe";
		String address = "123 Fake St.";
		int age = 42;
		
		public Temp() {}
		
		public Temp(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		public String getAddress() {
			return address;
		}
		public int getAge() {
			return age;
		}
		
	}
	
	
}
