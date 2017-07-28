package com.madhouse.platform.premiummad.constant;

/**
 * 媒体是否需要广告位信息
 */
public enum MediaNeedAdspace {
	SOHUNEWS(100000, false), 
	SOHUTV(100001, false), 
	TENCENT(100002, false), 
	DIANPING(100003, true),
	MOJI(100004, true),
	IQIYI(100005, false);

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
