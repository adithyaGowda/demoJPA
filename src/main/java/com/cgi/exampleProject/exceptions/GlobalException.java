package com.cgi.exampleProject.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(value = MemberNotFoundException.class)
    public ResponseEntity<Object> notFoundException(MemberNotFoundException exception) {
        return new ResponseEntity(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
	
    
    @ExceptionHandler(value = MemberAlreadyFoundException.class)
    public ResponseEntity<Object> alreadyExistsException(MemberAlreadyFoundException exception){
		return new ResponseEntity("Member ALready Exists!", HttpStatus.CONFLICT);
    	
    }
    
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> globalException(Exception exception){
    	return new ResponseEntity("Database Connectivity Lost", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}
