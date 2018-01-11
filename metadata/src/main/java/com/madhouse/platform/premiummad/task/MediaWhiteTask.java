package com.madhouse.platform.premiummad.task;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.entity.MediaMetaData;
import com.madhouse.platform.premiummad.service.IMediaService;
import com.madhouse.platform.premiummad.service.IMediaWhiteService;
import com.madhouse.platform.premiummad.util.ResourceManager;

@Component
public class MediaWhiteTask {
    private static final Logger LOGGER = LoggerFactory.getLogger("metadata");
    
    @Value("${ALL_MEDIA_WHITELIST}")
    private String ALL_MEDIA_WHITELIST;
    
    @Value("${EXPIRATION_TIME}")
    private Integer EXPIRATION_TIME;
    
    @Autowired
    private IMediaWhiteService mediaWhiteService;
    
    @Autowired
    private ResourceManager rm;
    
    public void loadMediaWhiteMetaData() {
        Jedis redisMaster = rm.getJedisPoolMaster().getResource();
        try {
            LOGGER.debug("------------MediaWhiteTask-----------start--");
            final List<Integer> listMedias = mediaWhiteService.queryAll();
            long begin = System.currentTimeMillis();
            Set<String> allMediaIds = redisMaster.smembers(ALL_MEDIA_WHITELIST);
            for (Integer mediaId : listMedias) {
                redisMaster.sadd(this.ALL_MEDIA_WHITELIST, String.valueOf(mediaId));
                if (allMediaIds != null && allMediaIds.size() > 0) {
                    allMediaIds.remove(String.valueOf(mediaId));
                }
            }

            if (allMediaIds != null && allMediaIds.size() > 0) {
                for (String mediaId : allMediaIds) {
                    redisMaster.srem(this.ALL_MEDIA_WHITELIST, mediaId);
                }
            }

            redisMaster.expire(this.ALL_MEDIA_WHITELIST, EXPIRATION_TIME);
            LOGGER.info("op loadMediaWhiteMetaData :{} ms", System.currentTimeMillis() - begin);//op不能修改,是关键字,在运维那里有监控
            LOGGER.debug("------------MediaWhiteTask-----------  End--");
        } catch (Exception e) {
            LOGGER.error("------------MediaWhiteTask-----------error:{}",e.toString());
        } finally {
            if(null != redisMaster){
                redisMaster.close();
            }
        }
    }
    
}
