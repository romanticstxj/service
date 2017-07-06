package com.madhouse.platform.premiummad.constant;

/**
 * 审核方
 */
public enum AuditeeCode {
	AC10001(1, "我方"), 
	AC10002(2, "媒体方");

	int value;
	String descrip;

	AuditeeCode(int value, String descrip) {
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
