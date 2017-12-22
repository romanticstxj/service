package com.madhouse.platform.premiummad.media.autohome.constant;

/**
 * 广告形式
 */
public enum AutohomeAuditStatus {
	UNAUDIT(0, "未审核"), 
	AUDITED(1, "审核通过"), 
	REJECTED(2, "拒绝");

	int value;
	String descrip;

	AutohomeAuditStatus(int value, String descrip) {
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
		for (AutohomeAuditStatus item : AutohomeAuditStatus.values()) {
			if (item.getValue() == value) {
				return item.getDescrip();
			}
		}
		return null;
	}

}
