package com.jacdev.picplacerest.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jacdev.picplacerest.photo.Photo;
import com.jacdev.picplacerest.photo.service.PhotoService;
import com.jacdev.picplacerest.user.UserForm;
import com.jacdev.picplacerest.user.service.UserRequestStatus;
import com.jacdev.picplacerest.user.service.UserService;


@RestController
class RestEndpointController {

	
	@Autowired private PhotoService photoService;
	@Autowired private UserService userService;
	private String photoBaseUrl = "/svc/photo";
	
	
	@CrossOrigin
	@PostMapping(value = "/svc/uploadFile", 
				consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
				produces = {MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {

		Photo photo = photoService.createPhoto(file, getUsername());
		if(photo == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		URI photoUri = createURI(photo.getId());
		return photoUri == null ? 
					ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
				: 	ResponseEntity.created(photoUri).build();
	}
	
	
	private URI createURI(Long photoId) {
		String photoUrl = photoBaseUrl + "?id=" + photoId;
		try {
			return new URI(photoUrl); 
		}catch (URISyntaxException e) {
				e.printStackTrace();
				return null;
		}
	}
	
	
	@CrossOrigin
	@GetMapping(value = "/svc/photoIdsPage")
	public ResponseEntity<Page<Photo>> getPhotoIdsPage(Pageable page, @RequestParam(value = "photoSize", required=false) String size){
		Page<Photo> photoPage = photoService.getPhotosDetails(getUsername(), size, page);
		return photoPage.isEmpty() ? ResponseEntity.noContent().build() 
								: ResponseEntity.ok().body(photoPage);
	}
	
	
	@GetMapping(value = "/svc/photo", produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<byte[]> getPhoto(
				@RequestParam(value = "id") Long id, 
				@RequestParam(value="size", required=false) String size,
				Pageable page) throws IOException {
				
		byte[] bytes = photoService.get(id, size);
		if(bytes.length == 0) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
	}
	

	@GetMapping(path = "/svc/available", params="username")
	public ResponseEntity<String> usernameAvailable(@RequestParam(name="username") String username){
		if(userService.exists(username)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists.");
		}
		return ResponseEntity.ok().body("Username is available.");
	}
	
	
	@CrossOrigin
	@PostMapping(path = "/svc/signup")
	public ResponseEntity<String> createUser(@RequestBody UserForm userForm){
		
		UserRequestStatus userRequestStatus =  userService.createUser(userForm);
		HttpStatus httpStatus = userRequestStatus == UserRequestStatus.USER_ADDED ?
				HttpStatus.CREATED : HttpStatus.FORBIDDEN;
		
		return ResponseEntity.status(httpStatus).body(userRequestStatus.getMessage());
	}
	
	
	@CrossOrigin
	@DeleteMapping(path="/svc/user")
	public ResponseEntity<String> deteUser(@RequestParam(name="username") String username){
		System.out.println("RestEndpointController, entered deleteUser, username: " + username);
		userService.deleteUser(username);
		return ResponseEntity.ok().build();
	}
	
	
	
	
	@GetMapping(value = "/svc/photocount")
	public ResponseEntity<String> getPhotoCount(){
		String username = getUsername();
		long count = photoService.getPhotoCountFor(username);
		return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body("" + count);
	}
	

	private String getUsername() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth == null ? null : auth.getName();
	}
	
	
	private void log(String msg) {
		System.out.println("RestEndpointController: " + msg);
	}

	
	
	
}
