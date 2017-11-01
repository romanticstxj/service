package com.madhouse.platform.premiummad.constant;

/**
 * 媒体类型也媒体ID映射关系
 * 一个媒体类型可能对应多个具体的媒体ID，多个用，隔开
 */
public enum MediaTypeMapping {
	SOHUNEWS(101, "100227"), 
	SOHUTV(102, ""),
	TENCENT_NOT_OTV(103, "100233"),
	TENCENT(104, ""),
	WEIBO(105, "100226"),
	MOMO(106, "100131"),
	MOJI(107, "100104"),
	VALUEMAKER(108, "100156"),
	TOUTIAO(109, "100106"),
	LETV(110, "100199"),
	IQYI(111, "100133"),
	FUNADX(112, "100198"),
	DIANPING(113, "100126");

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
