package com.madhouse.platform.premiummad.media.funadx.response;

import java.util.List;

public class FunadxUploadResponse {
	private Integer result;
	private List<FunadxDetailResponse> message;

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public List<FunadxDetailResponse> getMessage() {
		return message;
	}

	public void setMessage(List<FunadxDetailResponse> message) {
		this.message = message;
	}
}
