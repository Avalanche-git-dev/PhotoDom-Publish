package com.app.userservice.exception;

public class DuplicateEmailException extends UserException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DuplicateEmailException(String message) {
        super(message);
    }
}