package com.madhouse.platform.premiummad.task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.entity.DSPMappingMetaData;
import com.madhouse.platform.premiummad.entity.DSPMetaData;
import com.madhouse.platform.premiummad.service.IDSPService;
import com.madhouse.platform.premiummad.util.ResourceManager;

@Component
public class DspTask {
    
    
    private static final Logger LOGGER = LoggerFactory.getLogger("metadata");
    
    
    @Autowired
    private IDSPService service;
    
    @Autowired
    private ResourceManager rm;
    
    @Value("${ALL_DSP}")
    private String ALL_DSP;
    
    @Value("${DSP_META_DATA}")
    private String DSP_META_DATA;
    
    @Value("${DSP_MAPPING_DATA}")
    private String DSP_MAPPING_DATA;
    
    @Value("${EXPIRATION_TIME}")
    private Integer EXPIRATION_TIME;
    
    public void loadDSPMetaData() {
        Jedis redisMaster = rm.getJedisPoolMaster().getResource();
        try {
            LOGGER.debug("------------DSPTask-----loadDSPMetaData------start--");
            final List<DSPMetaData> lists = service.queryAll();
            long begin = System.currentTimeMillis();
            for (DSPMetaData metaData : lists) {
                redisMaster.setex(String.format(this.DSP_META_DATA, String.valueOf(metaData.getId())), EXPIRATION_TIME, JSON.toJSONString(metaData));
                redisMaster.sadd(this.ALL_DSP, String.valueOf(metaData.getId()));
            }
            redisMaster.expire(this.ALL_DSP, EXPIRATION_TIME);
            LOGGER.info("op dsp_task_info :{} ms", System.currentTimeMillis() - begin);//op不能修改,是关键字,在运维那里有监控
            LOGGER.debug("------------DSPTask-----loadDSPMetaData------  End--");
        } catch (Exception e) {
            LOGGER.error("------------DSPTask-----loadDSPMetaData------error:{}",e.toString());
        } finally {
            if(null != redisMaster){
                redisMaster.close();
            }
        }
    }
    public void loadDSPMappingData() {
        Jedis redisMaster = rm.getJedisPoolMaster().getResource();
        try {
            LOGGER.debug("------------DSPTask------plcmtMappingDsp-----start--");
            final List<DSPMappingMetaData> lists = service.queryAdspaceMappingDsp();
            long begin = System.currentTimeMillis();
            for (DSPMappingMetaData mappingMetaData : lists) {
                redisMaster.setex(String.format(this.DSP_MAPPING_DATA, String.valueOf(mappingMetaData.getAdspaceId()), String.valueOf(mappingMetaData.getDspId())), EXPIRATION_TIME, JSON.toJSONString(mappingMetaData));
            }
            LOGGER.info("op dsp_task_info :{} ms", System.currentTimeMillis() - begin);//op不能修改,是关键字,在运维那里有监控
            LOGGER.debug("------------DSPTask-----plcmtMappingDsp------End--");
        } catch (Exception e) {
            LOGGER.error("------------DSPTask-----plcmtMappingDsp------error:{}",e.toString());
        } finally {
            if(null != redisMaster){
                redisMaster.close();
            }
        }
    }
}
