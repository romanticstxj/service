package com.madhouse.platform.premiummad.media.yiche.response;

public class BaseResponse {
	// 响应状态码
	private int errorCode;

	// 错误描述
	private String errorMsg;

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
