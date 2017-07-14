package com.madhouse.platform.premiummad.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.apache.commons.lang3.CharEncoding;

public class StringUtils {

	/**
	 * 如果一个字符串为null，就是返回false
	 * 
	 * @param str
	 * @return 2015年1月29日下午3:24:34
	 * @author xiejun
	 */
	public static boolean hasEmpty(String... str) {
		for (String string : str) {
			if (isEmpty(string)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 计算字符串的MD5
	 * 
	 * @param str
	 * @return
	 */
	public static String getMD5(String str) {
		if (str != null) {
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				byte[] temp = md.digest(str.getBytes());
				return byteToHexString(temp);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 将指定byte数组转换成16位字符串
	 * 
	 * @param bytes
	 * @return 2015年3月3日下午1:36:36
	 * @author xiejun
	 */
	public static String byteToHexString(byte[] bytes) {
		StringBuilder hexString = new StringBuilder();
		for (byte b : bytes) {
			String hex = Integer.toString(b & 0xFF, 16);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			hexString.append(hex.toUpperCase());
		}
		return hexString.toString().substring(8, 24);
	}

	/**
	 * 判断是否非null
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(Object str) {
		return (str == null || "".equals(str) || "".equals(str.toString().trim()));
	}
	
	/**
	 * 把idList转为逗号分隔的idStr
	 * @param idList
	 * @return
	 */
	public static String getIdsStr(List<Integer> idList){
		return org.springframework.util.StringUtils.collectionToDelimitedString(idList, ",");
	}
	
	/**
	 * 把逗号分隔的idStr转换成id数组
	 * @param ids
	 * @return
	 */
	public static String[] splitIds(String ids){
		return org.springframework.util.StringUtils.tokenizeToStringArray(ids, ",");
	}

	/**
	 * 首字母大写
	 * 
	 * @param name
	 * @return
	 */
	public static String toFirstUpperCase(String name) {
		if (name != null && !name.isEmpty()) {
			String other = "";
			if (name.length() > 1) {
				other = name.substring(1);
			}
			return name.substring(0, 1).toUpperCase() + other;
		}
		return "";
	}

	/**
	 * 以UTF-8 encode字符串
	 * 
	 * @param str
	 *            字符串
	 * @return encode str
	 */
	public static String encode(String str) {
		String encodeStr = "";
		try {
			encodeStr = URLEncoder.encode(str, CharEncoding.UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encodeStr;
	}
}
