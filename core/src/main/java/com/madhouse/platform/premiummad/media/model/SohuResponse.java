package com.madhouse.platform.premiummad.media.model;

public class SohuResponse {

	private boolean status;
	private int code;
	private String message;
	private Object content;

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "{" + "status=" + status + ", code=" + code + ", message='" + message + '\'' + ", content=" + content + '}';
	}
}
