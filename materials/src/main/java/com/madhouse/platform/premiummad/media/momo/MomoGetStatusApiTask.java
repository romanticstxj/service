package com.madhouse.platform.premiummad.media.momo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * desc : 获取创意状态 1-通过,0待审核,-1-驳回
 */
@Component
public class MomoGetStatusApiTask {

    private Logger LOGGER = LoggerFactory.getLogger(MomoGetStatusApiTask.class);
    
    @Value("${momo_status_url}")
    private String statusUrl;

    @Value("${momo_upload_dspid}")
    private String dspId;

    public void getStatusResponse() throws Exception{
    	LOGGER.info("++++++++++Mono get material status begin+++++++++++");
		LOGGER.info("++++++++++Mono get material status end+++++++++++");
    }
}
