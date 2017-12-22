package com.madhouse.platform.premiummad.media.autohome.util;

public class AutohomeCommonUtil {

//	private String getSign(Map<String,String>params,String signKey){
//		    Set<String> keySet = params.keySet();
//		    List<String> keyList = new ArrayList<>();
//		    keyList.addAll(keySet);
//		    Collections.sort(keyList);
//		    List<String> paramList = new ArrayList<>();
//		    for(String key: keyList) {
//		        paramList.add(key+"="+params.get(key));
//		    }
//		    paramList.add("timestamp="+System.currentTimeMillis());
//		    String str = StringUtils.join(paramList, " ");
//		    String sign = StringUtils.encode(str+signKey);
//		    return sign;
//		}
//
//	//"POST"json
//		private String getSign(JSONObject obj, String signKey) {
//		    obj.put("timestamp", System.currentTimeMillis());
//		    String str = obj.toJSONString();
//		    String sign = MD5Util.encode(str+signKey);
//		    return sign;
//}
}
