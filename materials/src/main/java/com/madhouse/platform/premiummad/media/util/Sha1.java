package com.madhouse.platform.premiummad.media.util;

import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class Sha1 {
	@Value("${moji.secret}")
	private String secret;

	/**
	 * SHA1 安全加密算法
	 * 
	 * @param maps
	 *            参数key-value map集合
	 * @return
	 * @throws DigestException
	 */
	public String SHA1(Map<String, String> maps) {
		Object[] key = maps.keySet().toArray();
		Arrays.sort(key);
		HashMap<Object, String> t = new HashMap<Object, String>();
		String s = "";
		for (int i = 0; i < key.length; i++) {
			t.put(key[i], maps.get(key[i]));
			if (i != key.length - 1) {
				s += key[i] + "=" + maps.get(key[i]) + "&";
			} else {
				s += key[i] + "=" + maps.get(key[i]);
			}
		}
		System.out.println(s);
		// String secret="Ol6vTh9naOnAZaKT";
		String sign = sha1(s + secret);
		return sign;
	}

	public static String sha1(String s) {
		try {
			// Create MD5 Hash
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();
			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++)
				hexString.append(String.format("%02x", 0xFF & messageDigest[i]));
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}
}
