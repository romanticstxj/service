package com.madhouse.platform.premiummad.media.yiche;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.madhouse.platform.premiummad.media.yiche.util.YicheCommonUtil;
import com.madhouse.platform.premiummad.util.HttpUtils;

@Component
public class YicheGetTempApiTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(YicheMaterialStatusApiTask.class);

	@Value("${yiche.dspId}")
	private String dspId;

	@Value("${yiche.signKey}")
	private String signKey;

	public void getTemp() {
		String tempRequest = buildRequest();
		LOGGER.info("request: " + tempRequest);
		Map<String, Object> resultMap = HttpUtils.get("http://openapi.yiche.com/ssp-manager/api/temp/get" + "?" + tempRequest);
		String tempResposne = resultMap.get(HttpUtils.RESPONSE_BODY_KEY).toString();
		LOGGER.info("response: " + tempResposne);
	}

	private String buildRequest() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("tagId", "a8aa681aaa4588a8dbd3b42b26d59a1a");
		paramMap.put("timestamp", String.valueOf(System.currentTimeMillis()));
		paramMap.put("dspId", dspId);
		paramMap.put("sign", YicheCommonUtil.getSign(paramMap, signKey));
		return YicheCommonUtil.getRequest(paramMap);
	}
}
