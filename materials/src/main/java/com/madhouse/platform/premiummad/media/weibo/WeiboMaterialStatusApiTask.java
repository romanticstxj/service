package com.madhouse.platform.premiummad.media.weibo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WeiboMaterialStatusApiTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(WeiboMaterialStatusApiTask.class);

	@Value("${weibo.materialStatusUrl}")
	private String materialStatusUrl;
	
	@Value("${weibo.dspid}")
	private String dspid;
	
	@Value("${weibo.token}")
	private String token;
	
	public void getStatus(){
		LOGGER.info("++++++++++Weibo get material status begin+++++++++++");
		LOGGER.info("++++++++++Weibo get material status end+++++++++++");
	}
}
