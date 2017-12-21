package com.madhouse.platform.premiummad.constant;

/**
 * 媒体是否需要广告位信息
 */
public enum MediaNeedAdspace {
	SOHUNEWS(101, false), 
	SOHUTV(102, false),
	TENCENT_NOT_OTV(103, true),
	TENCENT(104, true),
	DIANPING(113, true),
	MOJI(107, true),
	IQYI(111, false),
	LETV(110, false),
	MOMO(106, false),
	TOUTIAO(109, false),
	VALUEMAKER(108, false),
	FUNADX(112, false),
	AUTOHOME(114, false),
	WEIBO(105, false);

	int key;
	boolean value;

	MediaNeedAdspace(int key, boolean value) {
		this.key = key;
		this.value = value;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public boolean isValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}

	public static boolean getValue(int key) {
		for (MediaNeedAdspace item : MediaNeedAdspace.values()) {
			if (item.getKey() == key) {
				return item.isValue();
			}
		}
		return false;
	}
}
