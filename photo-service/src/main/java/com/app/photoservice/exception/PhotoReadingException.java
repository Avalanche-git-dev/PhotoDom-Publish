package com.app.photoservice.exception;

public class PhotoReadingException extends NotFoundException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PhotoReadingException(String message) {
		super(message);
	}

}
