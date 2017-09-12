package com.madhouse.platform.premiummad.media.letv.response;

import java.util.Map;

public class LetvResponse {

	private Integer result;
	private Map<String, String> message;

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public Map<String, String> getMessage() {
		return message;
	}

	public void setMessage(Map<String, String> message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "LetvResponse{" + "result=" + result + ", message=" + message + '}';
	}
}
