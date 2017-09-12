package com.madhouse.platform.premiummad.media.funadx.constant;

public enum IFunadxConstant{
	RESPONSE_SUCCESS(0,"执行成功"),
	RESPONSE_OATH_FAILUE(1,"系统认证失败"),
	RESPONSE_PARAS_ERROR(2,"请求参数错误"),
	RESPONSE_OTHER_ERROR(3,"其他错误"),
	
	UPLOAD_SUCCESS(0,"上传成功"),
	UPLOAD_PARAS_ERROR(101,"参数错误"),
	UPLOAD_UNSUPPORT_TYPE(102,"不支持的文件格式"),
	UPLOAD_DOLOAD_FAILURE(201,"文件下载失败"),
	UPLOAD_OVER_SIZE(202,"文件大小超过限制"),
	UPLOAD_UNKNOW_ERROR(999,"未知错误"),
	
	M_STATUS_APPROVED(1,"审核已通过"),
	M_STATUS_REFUSED(-1,"审核未通过"),
	M_STATUS_UNAUDITED(0,"待审核");
	
	int value;
	String description;

	IFunadxConstant(int value, String description) {
		this.value = value;
		this.description = description;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}