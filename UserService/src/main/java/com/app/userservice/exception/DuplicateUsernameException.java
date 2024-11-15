package com.app.userservice.exception;

public class DuplicateUsernameException extends UserException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DuplicateUsernameException(String message) {
        super(message);
    }
}