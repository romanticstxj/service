package com.madhouse.platform.premiummad.media.toutiao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ToutiaoMaterialStatusApiTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(ToutiaoMaterialStatusApiTask.class);

	@Value("${toutiao.statusUrl}")
	private String getMaterialStatusUrl;

	@Value("${toutiao.dspid}")
	private String dspid;

	/**
	 * 定时获取今日头条广告状态。
	 */
	public void getStatusDetail() {
		LOGGER.info("++++++++++Toutiao get material status begin+++++++++++");
		LOGGER.info("++++++++++Toutiao get material status end+++++++++++");
	}
}
