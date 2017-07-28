package com.madhouse.platform.premiummad.task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.entity.Media;
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
    
    @Autowired
    private IMediaService mediaService;
    
    @Autowired
    private ResourceManager rm;
    
    public void run() {
        LOGGER.debug("------------MediaTask-----------start--");
        this.redisMaster = rm.getJedisPoolMaster().getResource();
        final List<Media> listMedias = mediaService.queryAll("");
        long begin = System.currentTimeMillis();
        for (Media media : listMedias) {
            redisMaster.set(String.format(this.MEDIA_META_DATA, media.getId()), JSON.toJSONString(media), "NX", "EX", 500);
            redisMaster.sadd(this.ALL_MEDIA, String.valueOf(media.getId()));
        }
        long end = System.currentTimeMillis();
        LOGGER.info("op " + (end - begin) + " ms");//op不能修改,是关键字,在运维那里有监控
        LOGGER.debug("------------MediaTask-----------  End--");
    }
    
}
