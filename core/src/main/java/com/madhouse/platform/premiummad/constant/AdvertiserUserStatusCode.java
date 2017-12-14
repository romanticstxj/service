package com.madhouse.platform.premiummad.constant;

/**
 * 素材媒体审核状态枚举
 */
public enum AdvertiserUserStatusCode {
	AUC10001(-1, "未通过"), 
	AUC10002(0, "待提交"), 
	AUC10003(1, "待审核"), 
	AUC10004(2, "审核通过");

	int value;
	String descrip;

	AdvertiserUserStatusCode(int value, String descrip) {
		this.value = value;
		this.descrip = descrip;
	}

	public int getValue() {
		return value;
	}

	public String getDescrip() {
		return descrip;
	}

}
