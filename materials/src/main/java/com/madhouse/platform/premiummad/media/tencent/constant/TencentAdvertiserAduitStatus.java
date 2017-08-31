package com.madhouse.platform.premiummad.media.tencent.constant;

/**
 * 腾讯审核结果
 */
public enum TencentAdvertiserAduitStatus {
	AUDITED(1, "审核通过"), 
	REJUSED(3, "审核未通过"), 
	UNAUDIT(8, "待审核");

	int value;
	String descrip;

	TencentAdvertiserAduitStatus(int value, String descrip) {
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
		for (TencentAdvertiserAduitStatus item : TencentAdvertiserAduitStatus.values()) {
			if (item.getValue() == value) {
				return item.getDescrip();
			}
		}
		return null;
	}

}
