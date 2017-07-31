package com.madhouse.platform.premiummad.media.toutiao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ToutiaoMaterialUploadApiTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(ToutiaoMaterialUploadApiTask.class);

	@Value("${toutiao.uploadMaterialUrl}")
	private String uploadMaterialUrl;

	@Value("${mh_toutiao_mapping_ios_1}")
	private String mh_toutiao_mapping_ios_1;
	@Value("${mh_toutiao_mapping_android_1}")
	private String mh_toutiao_mapping_android_1;

	@Value("${mh_toutiao_mapping_ios_2}")
	private String mh_toutiao_mapping_ios_2;
	@Value("${mh_toutiao_mapping_android_2}")
	private String mh_toutiao_mapping_android_2;

	public void uploadMaterial() {
		LOGGER.info("++++++++++Toutiao upload material begin+++++++++++");
		LOGGER.info("++++++++++Toutiao upload material end+++++++++++");
	}
}
