package com.madhouse.platform.premiummad.constant;

/**
 * 媒体是否需要广告位信息
 */
public enum MediaNeedAdspace {
	SOHUNEWS(100227, false), 
	SOHUTV(-1, false),
	TENCENT_NOT_OTV(100233, true),
	TENCENT(-2, true),
	DIANPING(100126, true),
	MOJI(100104, true),
	IQYI(100133, false),
	LETV(100199, false),
	MOMO(100131, false),
	TOUTIAO(100106, false),
	VALUEMAKER(100156, false),
	FUNADX(100198, false),
	WEIBO(100226, false);

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
