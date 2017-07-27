package com.madhouse.platform.premiummad.media.iqiyi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.madhouse.platform.premiummad.media.util.IQiYiHttpUtils;

@Component("iQiyiMaterialStatusApiTask")
public class IQiyiMaterialStatusApiTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(IQiyiMaterialStatusApiTask.class);

	@Value("${iqiyi.material.list}")
	private String materialListUrl;

	@Autowired
	private IQiYiHttpUtils iQiYiHttpUtils;

	public void getStatusDetail() {
		LOGGER.info("++++++++++iqiyi get material status begin+++++++++++");
		// TODO
		LOGGER.info("++++++++++iqiyi get material status end+++++++++++");
	}
}
