package com.madhouse.platform.smartexchange.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
}
