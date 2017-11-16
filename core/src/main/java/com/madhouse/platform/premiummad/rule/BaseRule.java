package com.madhouse.platform.premiummad.rule;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.madhouse.platform.premiummad.constant.FieldType;
import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.exception.BusinessException;
import com.madhouse.platform.premiummad.util.BeanUtils;
import com.madhouse.platform.premiummad.util.DateUtils;

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
	
	public static void checkStatus(Integer status){
		if(status != null){
			if(status != 0 && status != 1){ //如果状态值不正确
				throw new BusinessException(StatusCode.SC20008);
			}
		}
	}
	
	public static void validateDto(Object dto){
		String fieldName = BeanUtils.hasEmptyField(dto);
        if (fieldName != null)
        	throw new BusinessException(StatusCode.SC20002, FieldType.getChineseMessage(fieldName) + "不能为空");
	}
	
	public static void validateAndProcessDto(Object dto){
		String fieldName = BeanUtils.processEmptyField(dto);
        if (fieldName != null)
        	throw new BusinessException(StatusCode.SC20002, FieldType.getChineseMessage(fieldName) + "不能为空");
	}
	
	/**
	 * url 校验
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isUrl(String url) {
		if (url == null || url.isEmpty()) {
			return false;
		}
		// URL encoder 为了支持中文文件名
		try {
			if (url.startsWith("https://")) {
				url = "https://" + URLEncoder.encode(url.replace("https://", ""), "UTF-8");
			} else if (url.startsWith("http://")) {
				url = "http://" + URLEncoder.encode(url.replace("http://", ""), "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Pattern pattern = Pattern.compile("^(https?):\\/\\/[\\w\\-]+(\\.[\\w\\-]+)+([\\w\\-\\.,@?^=%&:\\/~\\+#]*[\\w\\-\\@?^=%&\\/~\\+#])?$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(url);
		return matcher.matches();
	}
	
	/**
	 * 校验是否是合法的日期
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isDate(String date, String format) {
		Pattern datePattern = null;
		if (DateUtils.FORMATE_YYYY_MM_DD.equals(format)) {
			datePattern = Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))\\-((((0?" + "[13578])|(1[02]))\\-((0?[1-9])|([1-2][0-9])|(3[01])))" + "|(((0?[469])|(11))\\-((0?[1-9])|([1-2][0-9])|(30)))|" + "(0?2\\-((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][12" + "35679])|([13579][01345789]))\\-((((0?[13578])|(1[02]))" + "\\-((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))" + "\\-((0?[1-9])|([1-2][0-9])|(30)))|(0?2\\-((0?[" + "1-9])|(1[0-9])|(2[0-8]))))))");
			return datePattern.matcher(date).matches();
		}
		return false;
	}
	
	/**
	 * 根据指定的日期format 解析成 date 类型
	 * 
	 * @param dateStr
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static Date parseToDate(String dateStr, String format) throws ParseException {
		// 如果时间为空，返回当前日期
		if (StringUtils.isBlank(dateStr)) {
			return new Date();
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(dateStr);
	}
	
	/**
	 * 将date format 指定格式
	 * 
	 * @param date
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static String parseToDateStr(Date date, String format) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
}
