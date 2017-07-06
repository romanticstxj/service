package com.madhouse.platform.premiummad.constant;

/**
 * 素材媒体审核状态枚举
 */
public enum MaterialMediaStatusCode {
	MMSC10001(-1, "未通过"), 
	MMSC10002(0, "待审核"), 
	MMSC10003(1, "审核中"), 
	MMSC10004(2, "审核通过");

	int value;
	String descrip;

	MaterialMediaStatusCode(int value, String descrip) {
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
