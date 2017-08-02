package com.madhouse.platform.premiummad.task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

import com.alibaba.fastjson.JSON;
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
    
    @Value("${EXPIRATION_DATE}")
    private Integer EXPIRATION_DATE;
    
    public void run() {
        try {
            LOGGER.debug("------------DSPTask-----------start--");
            this.redisMaster = rm.getJedisPoolMaster().getResource();
            final List<DSPMetaData> listMedias = service.queryAll();
            long begin = System.currentTimeMillis();
            for (DSPMetaData metaData : listMedias) {
                redisMaster.set(String.format(this.DSP_META_DATA, String.valueOf(metaData.getId())), JSON.toJSONString(metaData), "NX", "EX", EXPIRATION_DATE);
                redisMaster.sadd(this.ALL_DSP, String.valueOf(metaData.getId()));
            }
            LOGGER.info("op dsp_task_info :{} ms", System.currentTimeMillis() - begin);//op不能修改,是关键字,在运维那里有监控
            LOGGER.debug("------------DSPTask-----------  End--");
        } catch (Exception e) {
            LOGGER.error("------------DSPTask-----------error:{}",e.toString());
        }
    }
}
