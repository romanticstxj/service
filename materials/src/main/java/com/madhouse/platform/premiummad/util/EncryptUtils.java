package com.madhouse.platform.premiummad.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.util.Base64Utils;

public class EncryptUtils {

	public static String generateToken(String agid, String appid, String appkey, String time) {

		String sha1key = appid + appkey + time;
		StringBuffer sign = new StringBuffer();
		try {
			MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");
			digest.update(sha1key.getBytes());
			byte messageDigest[] = digest.digest();
			// 字节数组转换为 十六进制 数
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					sign.append(0);
				}
				sign.append(shaHex);
			}

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		String key = agid + "," + appid + "," + time + "," + sign;
		return Base64Utils.encodeToString(key.getBytes());
	}

}
