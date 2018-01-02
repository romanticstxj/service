package com.madhouse.platform.premiummad.media.autohome.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.alibaba.fastjson.JSONObject;
import com.madhouse.platform.premiummad.util.StringUtils;

public class AutohomeCommonUtil {
	
	/**
	 * 构建请求 get URL
	 * @param params
	 * @return
	 */
	public static String getRequest(Map<String, String> params) {
		List<String> keyList = new ArrayList<>();
		keyList.addAll(params.keySet());
		Collections.sort(keyList);
		List<String> paramList = new ArrayList<>();
		for (String key : keyList) {
			paramList.add(key + "=" + params.get(key));
		}
		return org.apache.commons.lang3.StringUtils.join(paramList, "&");
	}

	/**
	 * 普通的Get和POST
	 * 
	 * @param params
	 * @param signKey
	 * @return
	 */
	public static String getSign(Map<String, String> params, String signKey) {
		Set<String> keySet = params.keySet();
		List<String> keyList = new ArrayList<>();
		keyList.addAll(keySet);
		Collections.sort(keyList);
		List<String> paramList = new ArrayList<>();
		for (String key : keyList) {
			paramList.add(key + "=" + params.get(key));
		}
		paramList.add("timestamp=" + System.currentTimeMillis());
		String str = org.apache.commons.lang3.StringUtils.join(paramList, "&");
		String sign = StringUtils.getMD5(StringUtils.encode(str + signKey));
		return sign;
	}

	/**
	 * POST JSON
	 * 
	 * @param obj
	 * @param signKey
	 * @return
	 */
	public static String getSign(JSONObject obj, String signKey) {
		obj.put("timestamp", System.currentTimeMillis());
		String str = obj.toJSONString();
		String sign = StringUtils.getMD5(StringUtils.encode(str + signKey));
		return sign;
	}
}
