package com.madhouse.platform.premiummad.rule;

import org.apache.commons.lang3.StringUtils;

public class BaseRule {
	/**
	 * 解析以 , 分割的字符串
	 * @param str
	 * @return
	 */
	public static String[] parseStringToArray(String str) {
		if (StringUtils.isBlank(str)) {
			return null;
		}

		return str.split(",");
	}
}
