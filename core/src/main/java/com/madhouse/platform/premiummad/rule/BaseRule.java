package com.madhouse.platform.premiummad.rule;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class BaseRule {	
	/**
	 * 解析以 , 分割的字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String[] parseStringToDistinctArray(String str) {
		if (StringUtils.isBlank(str)) {
			return null;
		}

		// 去重
		List<String> distinctList = new ArrayList<String>();

		for (String item : str.split(",")) {
			if (!distinctList.contains(item.trim())) {
				distinctList.add(item.trim());
			}
		}

		return parseListToArray(distinctList);
	}

	public static String[] parseListToDistinctArray(List<Integer> list) {
		if (list == null) {
			return null;
		}

		// 去重
		List<String> distinctList = new ArrayList<String>();
		for (Integer item : list) {
			if (!distinctList.contains(item.toString())) {
				distinctList.add(item.toString());
			}
		}

		return parseListToArray(distinctList);
	}

	public static String[] parseListToArray(List<String> list) {
		String[] strArray = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			strArray[i] = String.valueOf(list.get(i));
		}
		return strArray;
	}
}
