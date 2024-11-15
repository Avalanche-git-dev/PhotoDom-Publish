package com.app.userservice.exception;

public class InvalidFieldException extends UserException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidFieldException(String message) {
        super(message);
    }
}