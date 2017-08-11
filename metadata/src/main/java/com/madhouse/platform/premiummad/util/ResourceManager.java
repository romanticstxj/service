package com.madhouse.platform.premiummad.util;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import redis.clients.jedis.JedisPool;

@Component
public class ResourceManager {
    
    @Value("${redis.key}")
    private String redisKey;

    @Value("${redis.host}")
    private String redisHost;
    
    @Value("${redis.port}")
    private Integer port;

    @Value("${redis.timeout}")
    private Integer timeout;
    
    @Value("${redis.maxActive}")
    private Integer maxActive;
    
    @Value("${redis.maxIdle}")
    private Integer maxIdle;
    
    @Value("${redis.maxWait}")
    private Integer maxWait;
    
    @Value("${redis.minIdle}")
    private Integer minIdle;

    private JedisPool jedisPoolMaster;

    public  JedisPool getJedisPoolMaster() {
        synchronized (this) {
            if (jedisPoolMaster == null) {
                GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
                poolConfig.setMinIdle(minIdle);
                poolConfig.setMaxIdle(maxIdle);
                poolConfig.setMaxTotal(timeout);

                jedisPoolMaster = new JedisPool(poolConfig,redisHost, port);
            }
        }

        return jedisPoolMaster;
    }
    

}
