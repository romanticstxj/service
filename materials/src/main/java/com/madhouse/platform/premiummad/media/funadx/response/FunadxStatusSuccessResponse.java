package com.madhouse.platform.premiummad.media.funadx.response;

public class FunadxStatusSuccessResponse {
	private Integer result;
	private FunadxStatusSuccessDetailResponse message;

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public FunadxStatusSuccessDetailResponse getMessage() {
		return message;
	}

	public void setMessage(FunadxStatusSuccessDetailResponse message) {
		this.message = message;
	}
}
