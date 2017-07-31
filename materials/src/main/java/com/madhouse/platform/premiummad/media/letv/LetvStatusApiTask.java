package com.madhouse.platform.premiummad.media.letv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LetvStatusApiTask {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LetvStatusApiTask.class);

	@Value("${letv.statusUrl}")
	private String statusUrl;

	@Value("${letv.isDebug}")
	private Boolean isDebug;

	public void getStatusDetail() {
		LOGGER.info("++++++++++Letv get material status begin+++++++++++");
		LOGGER.info("++++++++++Letv get material status end+++++++++++");
	}
}
