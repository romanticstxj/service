package com.madhouse.platform.premiummad.constant;

/**
 * 广告主媒体审核状态枚举
 */
public enum AdvertiserMediaStatusCode {
	AMSC10001(-1, "未通过"), 
	AMSC10002(0, "待审核"), 
	AMSC10003(1, "审核中"), 
	AMSC10004(2, "审核通过");

	int value;
	String descrip;

	AdvertiserMediaStatusCode(int value, String descrip) {
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
