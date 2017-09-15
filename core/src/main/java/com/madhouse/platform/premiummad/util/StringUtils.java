package com.madhouse.platform.premiummad.util;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang3.CharEncoding;
import org.springframework.util.Base64Utils;

import com.madhouse.platform.premiummad.constant.SystemConstant;

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
	
	public static Map<String, Integer> getFileMD5(InputStream file) {
		Map<String, Integer> md5LenghtLength = new HashMap<String, Integer>();
	    try {
    	    MessageDigest md = MessageDigest.getInstance("MD5");
    	    byte[] buffer = new byte[1024];
    	    int length = -1;
    	    int totalLength = 0;
    	    while ((length = file.read(buffer, 0, 1024)) != -1) {
    	    	totalLength = totalLength + length;
    	        md.update(buffer, 0, length);
    	    }
    	    BigInteger bigInt = new BigInteger(1, md.digest());
    	    md5LenghtLength.put(bigInt.toString(16), totalLength);
    	    return md5LenghtLength;
	    } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
       
	}

	public static String getMD5(byte[] data, int offset, int len) {
		if (data != null && data.length > 0) {
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				md.update(data, offset, len);
				byte[] temp = md.digest();

				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < temp.length; ++i) {
					String str = Integer.toString(temp[i] & 0xff, 16);
					if (str.length() < 2) {
						sb.append("0");
					}
					sb.append(str);
				}
				return sb.toString();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 将指定byte数组转换成32位字符串
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
		return hexString.toString();
	}
	
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
	 * 
	 * @param idList
	 * @return
	 */
	public static String getIdsStr(List<Integer> idList) {
		return org.springframework.util.StringUtils.collectionToDelimitedString(idList, ",");
	}

	/**
	 * 把逗号分隔的idStr转换成id数组
	 * 
	 * @param ids
	 * @return
	 */
	public static String[] splitToStringArray(String ids) {
		return org.springframework.util.StringUtils.tokenizeToStringArray(ids, ",");
	}

	/**
	 * 把逗号分隔的idStr转换成int类型的id数组
	 * @param ids
	 * @return
	 */
	public static int[] splitToIntArray(String ids) {
		if (ids == null) {
			return null;
		}
		int ret[] = new int[ids.length()];
		StringTokenizer toKenizer = new StringTokenizer(ids, ",");
		int i = 0;
		while (toKenizer.hasMoreElements()) {
			ret[i++] = Integer.valueOf(toKenizer.nextToken());
		}
		return ret;

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

	 /**
	 * <p>
	 * Checks if a CharSequence is whitespace, empty ("") or null.
	 * </p>
	 *
	 * <pre>
	 * StringUtils.isBlank(null)      = true
	 * StringUtils.isBlank("")        = true
	 * StringUtils.isBlank(" ")       = true
	 * StringUtils.isBlank("bob")     = false
	 * StringUtils.isBlank("  bob  ") = false
	 * </pre>
	 *
	 * @param cs
	 *            the CharSequence to check, may be null
	 * @return {@code true} if the CharSequence is null, empty or whitespace
	 * @since 2.0
	 * @since 3.0 Changed signature from isBlank(String) to
	 *        isBlank(CharSequence)
	 */
	public static boolean isBlank(final CharSequence cs) {
		int strLen;
		if (cs == null || (strLen = cs.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(cs.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>
	 * Checks if the CharSequence contains only Unicode digits. A decimal point
	 * is not a Unicode digit and returns false.
	 * </p>
	 *
	 * <p>
	 * {@code null} will return {@code false}. An empty CharSequence
	 * (length()=0) will return {@code false}.
	 * </p>
	 *
	 * <p>
	 * Note that the method does not allow for a leading sign, either positive
	 * or negative. Also, if a String passes the numeric test, it may still
	 * generate a NumberFormatException when parsed by Integer.parseInt or
	 * Long.parseLong, e.g. if the value is outside the range for int or long
	 * respectively.
	 * </p>
	 *
	 * <pre>
	 * StringUtils.isNumeric(null)   = false
	 * StringUtils.isNumeric("")     = false
	 * StringUtils.isNumeric("  ")   = false
	 * StringUtils.isNumeric("123")  = true
	 * StringUtils.isNumeric("\u0967\u0968\u0969")  = true
	 * StringUtils.isNumeric("12 3") = false
	 * StringUtils.isNumeric("ab2c") = false
	 * StringUtils.isNumeric("12-3") = false
	 * StringUtils.isNumeric("12.3") = false
	 * StringUtils.isNumeric("-123") = false
	 * StringUtils.isNumeric("+123") = false
	 * </pre>
	 *
	 * @param cs
	 *            the CharSequence to check, may be null
	 * @return {@code true} if only contains digits, and is non-null
	 * @since 3.0 Changed signature from isNumeric(String) to
	 *        isNumeric(CharSequence)
	 * @since 3.0 Changed "" to return false and not true
	 */
	public static boolean isNumeric(final CharSequence cs) {
		if (isEmpty(cs)) {
			return false;
		}
		final int sz = cs.length();
		for (int i = 0; i < sz; i++) {
			if (Character.isDigit(cs.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	public static Double convertCurrencyFentoYuan(Integer currencyFen) {
		// TODO Auto-generated method stub
		Double currencyYuan = (double) currencyFen / 100;
		return currencyYuan;
	}

	public static Integer convertCurrencyYuanToFen(Double currencyYuan) {
		Integer currencyFen = Integer.parseInt(
				new DecimalFormat(SystemConstant.OtherConstant.ZERO).format(currencyYuan * SystemConstant.OtherConstant.RATIO_FEN_TO_YUAN));
		return currencyFen;
	}

	public static int multiValueToSingleValue(int[] multiValue) {
		int result = 0;
		if(multiValue == null || multiValue.length == 0){
			return result;
		}
		for (int i = 0; i < multiValue.length; i++) {
			result |= multiValue[i];
		}
		return result;
	}

	public static String singleValueToMultiValue(Integer singleValue) {

		if(singleValue == null || singleValue.intValue() <= 0){
			return "";
		}
		
		int temp = Integer.parseInt(new DecimalFormat(SystemConstant.OtherConstant.ZERO)
				.format(Math.floor(Math.log(singleValue) / Math.log(SystemConstant.OtherConstant.BASE_FACTOR))));
		int nearestBaseNumber = (int) Math.pow(SystemConstant.OtherConstant.BASE_FACTOR, temp);
		List<Integer> result = new ArrayList<Integer>();
		do {
			if ((singleValue & nearestBaseNumber) != 0) { // 把多选项添加进结果集中
				result.add(nearestBaseNumber);
			}
			singleValue -= nearestBaseNumber; // 继续找下一个多选项
			nearestBaseNumber >>= 1; // 多选因子减少一位
		} while (nearestBaseNumber > 0);
		Collections.reverse(result); // 多选项从小到大排列
		return getIdsStr(result);
	}
	
	public static int convertMultiChoiceToSingleChoice(String multiChoice) {
        int[] splitedMultiChoices = StringUtils.splitToIntArray(multiChoice);
        int singleChoice = StringUtils.multiValueToSingleValue(splitedMultiChoices);
        return singleChoice;
	}
	
	public static String convertSingleChoiceToMultiChoice(Integer singleChoice) {
        String multiChoice = StringUtils.singleValueToMultiValue(singleChoice);
        return multiChoice;
	}
	
	public static String formalizeUrls(String urls){
		if(urls == null || urls.length() == 0){
			return "";
		}
		String[] splittedUrls = urls.split("\\|");
		StringBuilder sb = new StringBuilder();
		for(String splittedUrl: splittedUrls){
			if(splittedUrl != null){
				String[] twiceSplittedUrls = splittedUrl.split(SystemConstant.DB.impUrlsDelimiter);
				if(twiceSplittedUrls != null && twiceSplittedUrls.length == 2){
					sb.append(twiceSplittedUrls[1]).append("|");
				}
			}
		}
		if(sb != null && sb.length() > 0){
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}
}
