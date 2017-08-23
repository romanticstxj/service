package com.madhouse.platform.premiummad.media.iqiyi.response;

public class IQiyiCustomerResponse {

	private String code;

	// 错误信息，只有code为4001时才返回该项
	private String desc;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
