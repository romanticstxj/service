package com.madhouse.platform.premiummad.task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.entity.MediaMetaData;
import com.madhouse.platform.premiummad.service.IMediaService;
import com.madhouse.platform.premiummad.util.ResourceManager;

@Component
public class MediaTask {
    private static final Logger LOGGER = LoggerFactory.getLogger("metadata");
    
    private Jedis redisMaster = null;
    
    @Value("${ALL_MEDIA}")
    private String ALL_MEDIA;
    
    @Value("${MEDIA_META_DATA}")
    private String MEDIA_META_DATA;
    
    @Value("${EXPIRATION_DATE}")
    private Integer EXPIRATION_DATE;
    
    @Autowired
    private IMediaService mediaService;
    
    @Autowired
    private ResourceManager rm;
    
    public void run() {
        try {
            LOGGER.debug("------------MediaTask-----------start--");
            this.redisMaster = rm.getJedisPoolMaster().getResource();
            final List<MediaMetaData> listMedias = mediaService.queryAll();
            long begin = System.currentTimeMillis();
            for (MediaMetaData media : listMedias) {
                redisMaster.set(String.format(this.MEDIA_META_DATA, String.valueOf(media.getId())), JSON.toJSONString(media), "NX", "EX", EXPIRATION_DATE);
                redisMaster.sadd(this.ALL_MEDIA, String.valueOf(media.getId()));
            }
            LOGGER.info("op media_task_info :{} ms", System.currentTimeMillis() - begin);//op不能修改,是关键字,在运维那里有监控
            LOGGER.debug("------------MediaTask-----------  End--");
        } catch (Exception e) {
            LOGGER.error("------------MediaTask-----------error:{}",e.toString());
        }
    }
    
}
