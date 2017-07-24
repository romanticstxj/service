package com.madhouse.platform.premiummad.constant;

/**
 * 媒体映射关系
 */
public enum MediaMapping {
	SOHUNEWS(100000, "搜狐新闻"), 
	SOHUTV(100001, "搜狐TV"),
	TENCENT(100002, "腾讯"),
	DIANPING(100003, "美团点评");

	int value;
	String descrip;

	MediaMapping(int value, String descrip) {
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
