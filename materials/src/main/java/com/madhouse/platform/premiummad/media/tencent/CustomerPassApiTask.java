package com.madhouse.platform.premiummad.media.tencent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.madhouse.platform.premiummad.constant.MediaMapping;
import com.madhouse.platform.premiummad.media.util.TencentHttpUtil;

/**
 * 获取审核通过客户的信息
 */
@Component
public class CustomerPassApiTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerPassApiTask.class);

	@Value("${tencent.customerPass}")
	private String customerPassUrl;

	@Autowired
	private TencentHttpUtil tencentHttpUtil;

	public void customerPassToDb() {
		LOGGER.info(MediaMapping.TENCENT + " ValidResourceDealIdTask-customerPassToDb start");
		LOGGER.info(MediaMapping.TENCENT + " ValidResourceDealIdTask-customerPassToDb end");
	}
}
