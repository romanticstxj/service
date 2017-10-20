package com.madhouse.platform.premiummad.constant;

/**
 * 媒体类型也媒体ID映射关系
 * 一个媒体类型可能对应多个具体的媒体ID，多个用，隔开
 */
public enum MediaTypeMapping {
	SOHUNEWS(1001, "100227"), 
	SOHUTV(1002, ""),
	TENCENT_NOT_OTV(1003, "100233"),
	TENCENT(1004, ""),
	DIANPING(1005, "100126"),
	MOJI(1006, "100104"),
	IQYI(1007, "100133"),
	LETV(1008, "100199"),
	MOMO(1009, "100131"),
	TOUTIAO(1010, "100106"),
	VALUEMAKER(1011, "100156"),
	FUNADX(1012, "100198"),
	WEIBO(1013, "100226");

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
