package com.madhouse.platform.premiummad.constant;

/**
 * 广告主媒体审核状态枚举
 */
public enum AdvertiserStatusCode {
	ASC10001(-1, "未通过"), 
	ASC10002(0, "待提交"), 
	ASC10003(1, "待审核"), 
	ASC10004(2, "审核通过");

	int value;
	String descrip;

	AdvertiserStatusCode(int value, String descrip) {
		this.value = value;
		this.descrip = descrip;
	}

	public int getValue() {
		return value;
	}

	public String getDescrip() {
		return descrip;
	}

	public static String getDescrip(int value) {
		for (AdvertiserStatusCode item : AdvertiserStatusCode.values()) {
			if (item.getValue() == value) {
				return item.getDescrip();
			}
		}
		return "";
	}
}
