package com.madhouse.platform.premiummad.constant;

/**
 * 广告类型
 */
public enum AdvertiserType {
	AT10001(1, "横幅广告"), 
	AT10002(2, "视频广告"), 
	AT10003(3, "信息流广告");
	
	int value;
	String descrip;

	AdvertiserType(int value, String descrip) {
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
		for (AdvertiserType item : AdvertiserType.values()) {
			if (item.getValue() == value) {
				return item.getDescrip();
			}
		}
		return null;
	}

}
