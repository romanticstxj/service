package com.madhouse.platform.premiummad.media.tencent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.madhouse.platform.premiummad.constant.MediaMapping;
import com.madhouse.platform.premiummad.media.util.TencentHttpUtil;

/**
 * 广告位信息同步 ,涉及到的中间表md_adspace_mapping
 */
@Component
public class AdspaceSynchApiTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(AdspaceSynchApiTask.class);

	@Value("${tencent.adspaceSynch}")
	
	
	private String adspaceSynchUrl;

	@Autowired
	private TencentHttpUtil tencentHttpUtil;
	
	public void synchToDB() {
		LOGGER.info(MediaMapping.TENCENT + " AdspaceSynchApiTask-synchToDB start");
		LOGGER.info(MediaMapping.TENCENT + " AdspaceSynchApiTask-synchToDB end");
	}
}
