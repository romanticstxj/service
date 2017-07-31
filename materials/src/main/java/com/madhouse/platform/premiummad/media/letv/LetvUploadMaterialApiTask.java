package com.madhouse.platform.premiummad.media.letv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LetvUploadMaterialApiTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(LetvUploadMaterialApiTask.class);

    @Value("${letv.materialUrl}")
    private String uploadMaterialApiUrl;

    @Value("${letv.isDebug}")
    private Boolean isDebug;

    /**
     * 上传广告物料
     */
    public void uploadMaterial() {
    	LOGGER.info("++++++++++Letv upload material begin+++++++++++");
		LOGGER.info("++++++++++Letv upload material end+++++++++++");
    }
}
