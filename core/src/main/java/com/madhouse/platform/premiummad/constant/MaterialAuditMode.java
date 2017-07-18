package com.madhouse.platform.premiummad.constant;

/**
 * 素材审核方式
 */
public enum MaterialAuditMode {
	MAM10001(0, "不审核"), 
	MAM10002(1, "平台审核"), 
	MAM10003(2, "媒体审核");

	int value;
	String descrip;

	MaterialAuditMode(int value, String descrip) {
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
