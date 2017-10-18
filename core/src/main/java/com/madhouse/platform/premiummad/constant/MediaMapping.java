package com.madhouse.platform.premiummad.constant;

/**
 * 媒体映射关系
 */
public enum MediaMapping {
	SOHUNEWS(100227, "搜狐新闻"), 
	SOHUTV(-1, "搜狐TV"),
	TENCENT_NOT_OTV(100233, "腾讯_非OTV_腾讯ADX"),
	TENCENT(-2, "腾讯_OTV"),
	DIANPING(100126, "大众点评_美团"),
	MOJI(100104, "墨迹天气"),
	IQYI(100133, "爱奇艺"),
	LETV(100199, "乐视"),
	MOMO(100131, "陌陌"),
	TOUTIAO(100106, "今日头条"),
	VALUEMAKER(100156, "网易新闻"),//万流客
	FUNADX(100198, "风行"),
	WEIBO(100226, "新浪微博");

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
