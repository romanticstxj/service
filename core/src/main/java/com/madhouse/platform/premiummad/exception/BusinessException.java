package com.madhouse.platform.premiummad.exception;

import com.madhouse.platform.premiummad.constant.StatusCode;

public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = -369036765037730525L;

	protected StatusCode statusCode;

	public StatusCode getStatusCode() {
		return statusCode;
	}

	public BusinessException(StatusCode statusCode) {
//		super(statusCode.getDescrip());
		this.statusCode = statusCode;
	}

	public BusinessException(StatusCode statusCode, String message) {
		super(message);
		this.statusCode = statusCode;
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

}
