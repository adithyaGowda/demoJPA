package com.cgi.exampleProject.exceptions;

public class MemberNotFoundException extends Exception {

	private String message;
	

	public MemberNotFoundException() {
		
	}


	public MemberNotFoundException(String message) {
		super();
		this.message = message;
	}
	
	
}
