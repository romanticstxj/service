package com.madhouse.platform.premiummad.media.sohu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.madhouse.platform.premiummad.constant.SystemConstant;

@Component
public class SohuNewsUploadMaterialApiTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(SystemConstant.LOG_TASK_OTV);

	/**
	 * 上传广告物料
	 */
	public void uploadSohuMaterial() {
		LOGGER.info("++++++++++Sohu News upload material begin+++++++++++");
		System.err.println("sohu upload material");
	}
}
