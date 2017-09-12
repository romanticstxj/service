package com.madhouse.platform.premiummad.constant;

/**
 * 媒体是否需要广告位信息
 */
public enum MediaNeedAdspace {
	SOHUNEWS(100000, false), 
	SOHUTV(100001, false),
	TENCENT_NOT_OTV(100002, true),
	TENCENT(100003, true),
	DIANPING(100004, true),
	MOJI(100005, true),
	IQYI(100006, false),
	LETV(100007, false),
	MOMO(100008, false),
	TOUTIAO(100009, false),
	VALUEMAKER(100010, false),
	FUNADX(100011, false),
	WEIBO(100012, false);

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
