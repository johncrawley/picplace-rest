package com.jacdev.picplacerest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ResourceNotFoundException extends RuntimeException {

	public ResourceNotFoundException() {
		// TODO Auto-generated constructor stub
	}

}
