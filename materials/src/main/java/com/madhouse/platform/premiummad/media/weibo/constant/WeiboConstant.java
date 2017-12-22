package com.madhouse.platform.premiummad.media.weibo.constant;

public enum WeiboConstant{
	//API接口调用返回表示码
	RESPONSE_SUCCESS(0,"执行成功"),
	RESPONSE_OATH_FAILUE(1,"系统认证失败"),
	RESPONSE_PARAS_ERROR(2,"请求参数错误"),
	RESPONSE_OTHER_ERROR(3,"其他错误"),
	
	//广告主审核状态码
	C_STATUS_APPROVED(1,"审核通过"),
	C_STATUS_REFUSED(2,"审核不通过"),
	C_STATUS_UNAUDITED(0,"待审核"),
	
	//物料审核状态码
	M_STATUS_APPROVED(1,"审核通过"),
	M_STATUS_REFUSED(-1,"审核不通过"),
	M_STATUS_UNAUDITED(0,"待审核");
	
	int value;
	String description;

	WeiboConstant(int value, String description) {
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