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
    
    private Jedis redisMaster = null;
    
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
        try {
            LOGGER.debug("------------DSPTask-----run------start--");
            this.redisMaster = rm.getJedisPoolMaster().getResource();
            final List<DSPMetaData> lists = service.queryAll();
            long begin = System.currentTimeMillis();
            for (DSPMetaData metaData : lists) {
                redisMaster.setex(String.format(this.DSP_META_DATA, String.valueOf(metaData.getId())), EXPIRATION_TIME, JSON.toJSONString(metaData));
                redisMaster.sadd(this.ALL_DSP, String.valueOf(metaData.getId()));
            }
            redisMaster.expire(this.ALL_DSP, EXPIRATION_TIME);
            LOGGER.info("op dsp_task_info :{} ms", System.currentTimeMillis() - begin);//op不能修改,是关键字,在运维那里有监控
            LOGGER.debug("------------DSPTask-----run------  End--");
        } catch (Exception e) {
            LOGGER.error("------------DSPTask-----run------error:{}",e.toString());
        }
    }
    public void loadDSPMappingData() {
        try {
            LOGGER.debug("------------DSPTask------plcmtMappingDsp-----start--");
            this.redisMaster = rm.getJedisPoolMaster().getResource();
            final List<DSPMappingMetaData> lists = service.queryAdspaceMappingDsp();
            long begin = System.currentTimeMillis();
            redisMaster.setex(this.DSP_MAPPING_DATA, EXPIRATION_TIME ,JSON.toJSONString(lists));
            LOGGER.info("op dsp_task_info :{} ms", System.currentTimeMillis() - begin);//op不能修改,是关键字,在运维那里有监控
            LOGGER.debug("------------DSPTask-----plcmtMappingDsp------End--");
        } catch (Exception e) {
            LOGGER.error("------------DSPTask-----plcmtMappingDsp------error:{}",e.toString());
        }
    }
}
