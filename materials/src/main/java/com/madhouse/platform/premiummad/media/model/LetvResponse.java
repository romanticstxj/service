package com.madhouse.platform.premiummad.media.model;

import java.util.Map;

public class LetvResponse {

	private Integer result;
	private Map<Object, Object> message;

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public Map<Object, Object> getMessage() {
		return message;
	}

	public void setMessage(Map<Object, Object> message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "LetvResponse{" + "result=" + result + ", message=" + message + '}';
	}
}
