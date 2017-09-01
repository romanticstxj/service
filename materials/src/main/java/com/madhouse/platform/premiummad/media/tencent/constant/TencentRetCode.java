package com.madhouse.platform.premiummad.media.tencent.constant;

/**
 * 腾讯返回code
 */
public enum TencentRetCode {
	SUCCESS(0,"执行成功"),
    ALL_FAILED(1,"全部执行失败"),
    PART_SUCCESS(2,"部分执行成功"),
    OTHER_FAILED(3,"系统认证失败或者系统内部错误");

	int value;
	String descrip;

	TencentRetCode(int value, String descrip) {
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
		for (TencentRetCode item : TencentRetCode.values()) {
			if (item.getValue() == value) {
				return item.getDescrip();
			}
		}
		return "";
	}
}
