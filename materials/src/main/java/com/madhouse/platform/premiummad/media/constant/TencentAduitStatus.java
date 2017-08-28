package com.madhouse.platform.premiummad.media.constant;

/**
 * 腾讯审核结果
 */
public enum TencentAduitStatus {
	AUDITED(1, "审核通过"), 
	REJUSED(2, "审核未通过"), 
	UNAUDIT(3, "待审核");

	int value;
	String descrip;

	TencentAduitStatus(int value, String descrip) {
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
		for (TencentAduitStatus item : TencentAduitStatus.values()) {
			if (item.getValue() == value) {
				return item.getDescrip();
			}
		}
		return null;
	}

}
