package com.madhouse.platform.premiummad.media.model;

import java.util.List;

public class MojiMaterialUploadResponse {
	private String code;
	private String message;
	private MojiMaterialUploadDataResponse data;
	
	public MojiMaterialUploadDataResponse getData() {
		return data;
	}
	public void setData(MojiMaterialUploadDataResponse data) {
		this.data = data;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
