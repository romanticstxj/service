package com.madhouse.platform.premiummad.model;

public class OperationResultModel {
	/**
	 * 操作是否成功
	 */
	private boolean successful = Boolean.TRUE;
	
	/**
	 * 错误信息
	 */
	private String errorMessage;
	
	/**
	 * 错误信息Code
	 */
	private String errorCode;

	public boolean isSuccessful() {
		return successful;
	}

	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}
