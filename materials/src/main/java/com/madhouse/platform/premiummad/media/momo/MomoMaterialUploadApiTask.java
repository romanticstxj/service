package com.madhouse.platform.premiummad.media.momo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MomoMaterialUploadApiTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(MomoMaterialUploadApiTask.class);

	@Value("${momo_upload_url}")
	private String uploadMaterialUrl;
	@Value("${momo_upload_dspid}")
	private String dspId;
	@Value("${momo_upload_appkey}")
	private String appkey;

	/**
	 * 上传物料
	 */
	public void uploadMaterial() {
		LOGGER.info("++++++++++Momo upload material begin+++++++++++");
		LOGGER.info("++++++++++Momo upload material end+++++++++++");
	}
}
