package com.madhouse.platform.premiummad.exception;

import com.madhouse.platform.premiummad.constant.StatusCode;

public class InvalidArgumentException extends RuntimeException{

	private static final long serialVersionUID = -2617979370394070253L;

	protected StatusCode statusCode;

	public StatusCode getStatusCode() {
		return statusCode;
	}

	public InvalidArgumentException(StatusCode statusCode, String message) {
		super(message);
		this.statusCode = statusCode;
	}

	public InvalidArgumentException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
