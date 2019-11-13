package com.cgi.exampleProject.exceptions;

public class MemberAlreadyFoundException extends Exception {

	private String message;
	
	

	public MemberAlreadyFoundException() {
	
	}



	public MemberAlreadyFoundException(String message) {
		super();
		this.message = message;
	}
	
}
