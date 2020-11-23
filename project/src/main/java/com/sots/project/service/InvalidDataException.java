package com.sots.project.service;

public class InvalidDataException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public InvalidDataException() {
		super();
	}
	
	public InvalidDataException(String message) {
		super(message);
	}

}
