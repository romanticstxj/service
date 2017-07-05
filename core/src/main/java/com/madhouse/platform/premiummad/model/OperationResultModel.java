package com.madhouse.platform.premiummad.model;

public class OperationResultModel {
	/**
	 * 操作是否成功
	 */
	private boolean successful = Boolean.FALSE;
	
	/**
	 * 错误信息
	 */
	private String errorMessage;

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
}
