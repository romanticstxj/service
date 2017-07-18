package com.madhouse.platform.premiummad.constant;

/**
 * 广告主审核方式
 */
public enum AdvertiserAuditMode {
	AAM10001(0, "不审核"), 
	AAM10002(1, "平台审核"), 
	AAM10003(2, "媒体审核");

	int value;
	String descrip;

	AdvertiserAuditMode(int value, String descrip) {
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
