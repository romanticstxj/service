package com.madhouse.platform.premiummad.media.tencent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.madhouse.platform.premiummad.constant.MediaMapping;
import com.madhouse.platform.premiummad.media.util.TencentHttpUtil;

/**
 * 获取有效资源预定（deal Id）信息
 */
@Component
public class ValidResourceDealIdTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(ValidResourceDealIdTask.class);

	@Value("${tencent.validresource}")
	private String customerPassUrl;

	@Autowired
	private TencentHttpUtil tencentHttpUtil;

	public void getValidResourceDealId() {
		LOGGER.info(MediaMapping.TENCENT + " CustomerPassApiTask-getValidResourceDealId start");
		LOGGER.info(MediaMapping.TENCENT + " CustomerPassApiTask-getValidResourceDealId end");
	}
}
