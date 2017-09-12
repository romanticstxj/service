package com.madhouse.platform.premiummad.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class MacroReplaceUtil {
	/**
	 * 宏替换点击链接
	 * 
	 * @param clickUrl
	 * @return
	 */
	public static String macroReplaceClickUrl(Map<String, String> macroClickMap, String clickUrl) {
		Iterator<Entry<String, String>> iterator = macroClickMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = iterator.next();
			clickUrl = clickUrl.replaceAll(escapeExprSpecialWord(entry.getKey()), escapeExprSpecialWord(entry.getValue()));
		}
		return clickUrl;
	}

	/**
	 * 宏替换展示链接
	 * 
	 * @param imageUrl
	 * @return
	 */
	public static String macroReplaceImageUrl(Map<String, String> macroImageMap, String imageUrl) {
		Iterator<Entry<String, String>> iterator = macroImageMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = iterator.next();
			imageUrl = imageUrl.replaceAll(escapeExprSpecialWord(entry.getKey()), escapeExprSpecialWord(entry.getValue()));
		}
		return imageUrl;
	}

	public static String getStr(String oldStr, String findStr, String replaceStr) {
		StringBuffer buffer = new StringBuffer(oldStr);
		int num = buffer.indexOf(findStr);
		if (num != -1) {
			buffer.replace(num + 1, oldStr.length(), replaceStr);
		}
		return buffer.toString();
	}

	/**
	 * 转义正则特殊字符 （$()*+.[]?\^{},|）
	 * 
	 * @param keyword
	 * @return
	 */
	public static String escapeExprSpecialWord(String keyword) {
		if (!StringUtils.isBlank(keyword)) {
			String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" };
			for (String key : fbsArr) {
				if (keyword.contains(key)) {
					keyword = keyword.replace(key, "\\" + key);
				}
			}
		}
		return keyword;
	}
}
