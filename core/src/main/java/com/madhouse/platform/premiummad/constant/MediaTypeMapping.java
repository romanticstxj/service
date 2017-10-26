package com.madhouse.platform.premiummad.constant;

/**
 * 媒体类型也媒体ID映射关系
 * 一个媒体类型可能对应多个具体的媒体ID，多个用，隔开
 */
public enum MediaTypeMapping {
	SOHUNEWS(1, "100227"), 
	SOHUTV(2, ""),
	TENCENT_NOT_OTV(3, "100233"),
	TENCENT(4, ""),
	WEIBO(5, "100226"),
	MOMO(6, "100131"),
	MOJI(7, "100104"),
	VALUEMAKER(8, "100156"),
	TOUTIAO(9, "100106"),
	LETV(10, "100199"),
	IQYI(11, "100133"),
	FUNADX(12, "100198"),
	DIANPING(13, "100126");

	int groupId;
	String value;

	MediaTypeMapping(int groupId, String value) {
		this.groupId = groupId;
		this.value = value;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static String getValue(int groupId) {
		for (MediaTypeMapping item : MediaTypeMapping.values()) {
			if (item.getGroupId() == groupId) {
				return item.getValue();
			}
		}
		return "";
	}
}
