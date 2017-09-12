package com.madhouse.platform.premiummad.media.moji.response;

public class MojiMaterialStatusResponse {
	private String code;
	private MojiMaterialStatusDataResponse data;
	private String message;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public MojiMaterialStatusDataResponse getData() {
		return data;
	}

	public void setData(MojiMaterialStatusDataResponse data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
