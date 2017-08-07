package com.madhouse.platform.premiummad.constant;

/**
 * 媒体映射关系
 */
public enum MediaMapping {
	SOHUNEWS(100000, "搜狐新闻"), 
	SOHUTV(100001, "搜狐TV"),
	TENCENT_NOT_OTV(100002, "腾讯_非otv"),
	TENCENT(100003, "腾讯"),
	DIANPING(100004, "美团点评"),
	MOJI(100005, "墨迹天气"),
	IQYI(100006, "爱奇艺"),
	LETV(100007, "乐视"),
	MOMO(100008, "陌陌"),
	TOUTIAO(100009, "今日头条"),
	VALUEMAKER(100010, "万流客");

	int value;
	String descrip;

	MediaMapping(int value, String descrip) {
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
		for (MediaMapping item : MediaMapping.values()) {
			if (item.getValue() == value) {
				return item.getDescrip();
			}
		}
		return "";
	}
}
