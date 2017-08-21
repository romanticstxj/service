package com.madhouse.platform.premiummad.rule;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.exception.BusinessException;
import com.madhouse.platform.premiummad.util.BeanUtils;

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
        	throw new BusinessException(StatusCode.SC20002, fieldName + " cannot be null");
	}
}
