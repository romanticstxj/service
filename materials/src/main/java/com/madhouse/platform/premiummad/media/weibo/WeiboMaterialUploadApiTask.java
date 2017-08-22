package com.madhouse.platform.premiummad.media.weibo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WeiboMaterialUploadApiTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(WeiboMaterialUploadApiTask.class);
	private static String CREATIVE_BANNER = "001";
	private static String CREATIVE_FEED = "002";
	private static String CREATIVE_FEED_ACTIVITY = "003";
	private static String CREATIVE_FEED_VIDEO = "004";

	@Value("${weibo.materialUploadUrl}")
	private String uploadMaterialUrl;
	
	@Value("${weibo.dspid}")
	private String dspid;
	
	@Value("${weibo.token}")
	private String token;
	
	public void uploadMaterial(){
		LOGGER.info("++++++++++Weibo upload material begin+++++++++++");
		LOGGER.info("++++++++++Weibo upload material end+++++++++++");
	}
}
