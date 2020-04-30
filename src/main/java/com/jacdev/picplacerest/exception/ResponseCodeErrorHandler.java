package com.jacdev.picplacerest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.jacdev.picplacerest.photo.repository.DirectoryDeletionException;

@ControllerAdvice
public class ResponseCodeErrorHandler {

	public ResponseCodeErrorHandler() {
		// TODO Auto-generated constructor stub
	}
	
	@ExceptionHandler(ResourceAlreadyDeletedException.class)
	public ResponseEntity<?> handle() {
		return new ResponseEntity<String>( HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(DirectoryDeletionException.class)
	public ResponseEntity<String> handleCouldntDeleteDirectory(){
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}


	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<String> handleBadCredentials(){
		return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
	}

	
	
}
