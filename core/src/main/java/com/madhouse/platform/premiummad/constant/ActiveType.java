package com.madhouse.platform.premiummad.constant;

/**
 * 广告主所属行业
 */
public enum ActiveType {
	AT10000(0, "无"),
	AT10001(1, "应用内打开"),
	AT10002(2, "App 下载"),
	AT10003(3, "浏览器打开"),
	AT10004(4, "拨打电话 拨");
	
	int value;
	String descrip;

	ActiveType(int value, String descrip) {
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
