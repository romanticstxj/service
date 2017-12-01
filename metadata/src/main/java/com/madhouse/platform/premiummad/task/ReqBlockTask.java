package com.madhouse.platform.premiummad.task;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

import com.madhouse.platform.premiummad.entity.ReqBlock;
import com.madhouse.platform.premiummad.service.IReqBlockService;
import com.madhouse.platform.premiummad.util.Constant;
import com.madhouse.platform.premiummad.util.ResourceManager;
import com.madhouse.platform.premiummad.util.StringUtil;
@Component
public class ReqBlockTask {
	private static final Logger LOGGER = LoggerFactory.getLogger("metadata");

	@Autowired
    private ResourceManager rm;
	
	
	@Value("${ALL_BLOCKED_DEVICE_IP}")
	private String ALL_BLOCKED_DEVICE_IP;
	
	@Value("${ALL_BLOCKED_DEVICE_IFA}")
	private String ALL_BLOCKED_DEVICE_IFA;
	
	@Value("${ALL_BLOCKED_DEVICE_DIDMD5}")
	private String ALL_BLOCKED_DEVICE_DIDMD5;
	
	@Value("${ALL_BLOCKED_DEVICE_DPIDMD5}")
	private String ALL_BLOCKED_DEVICE_DPIDMD5;
	
	@Value("${EXPIRATION_TIME}")
    private Integer EXPIRATION_TIME;
	
	@Autowired
    private IReqBlockService blockService;
	
	public void loadReqBlockMetaData() {
		Jedis redisMaster = rm.getJedisPoolMaster().getResource();
		try {
			LOGGER.debug("------------ReqBlockTask-----loadReqBlockMetaData------start--");
			List<ReqBlock> list = blockService.queryAll();
			long begin = System.currentTimeMillis();
	        Set<String> blockIps = redisMaster.smembers(ALL_BLOCKED_DEVICE_IP);
	        Set<String> blockIfas = redisMaster.smembers(ALL_BLOCKED_DEVICE_IFA);
	        Set<String> blockDidmad5s = redisMaster.smembers(ALL_BLOCKED_DEVICE_DIDMD5);
	        Set<String> blockDpidmad5s = redisMaster.smembers(ALL_BLOCKED_DEVICE_DPIDMD5);
	        for (ReqBlock reqBlock : list) {
	        	try {
		        	if(Constant.BlockType.IP == reqBlock.getType()){
		        		redisMaster.sadd(this.ALL_BLOCKED_DEVICE_IP, reqBlock.getCode());
	                    if (blockIps != null && blockIps.size() > 0) {
	                    	blockIps.remove(reqBlock.getCode());
	                    }
		        	} else if(Constant.BlockType.IFA == reqBlock.getType()){
		        		redisMaster.sadd(this.ALL_BLOCKED_DEVICE_IFA, reqBlock.getCode());
		        		if (blockIfas != null && blockIfas.size() > 0) {
		        			blockIfas.remove(reqBlock.getCode());
	                    }
		        	} else if (Constant.BlockType.DID == reqBlock.getType()){
		        		String didMd5 = reqBlock.getCode().toLowerCase();
		        		if(didMd5.length() <= 20) {
		        			didMd5 = StringUtil.getMD5(didMd5);
		        		}
		        		redisMaster.sadd(this.ALL_BLOCKED_DEVICE_DIDMD5, didMd5);
		        		if (blockDidmad5s != null && blockDidmad5s.size() > 0) {
		        			blockDidmad5s.remove(didMd5);
	                    }
		        	} else if (Constant.BlockType.DPID == reqBlock.getType()){
		        		String dpidMd5 = reqBlock.getCode().toLowerCase();
		        		if(dpidMd5.length() <= 20) {
		        			dpidMd5 = StringUtil.getMD5(dpidMd5);
		        		}
		        		redisMaster.sadd(this.ALL_BLOCKED_DEVICE_DPIDMD5, dpidMd5);
		        		if (blockDpidmad5s != null && blockDpidmad5s.size() > 0) {
		        			blockDpidmad5s.remove(dpidMd5);
	                    }
		        	}
	        	} catch (Exception e) {
                    LOGGER.error("[{}]-----ReqBlockTask-----is_for------error:{}",reqBlock.getId(),e.toString());
                }
			}
	        if (blockIps != null && blockIps.size() > 0) {
                for (String ip : blockIps) {
                    redisMaster.srem(this.ALL_BLOCKED_DEVICE_IP, ip);
                }
            }
        	if (blockIfas != null && blockIfas.size() > 0) {
                for (String ifa : blockIfas) {
                    redisMaster.srem(this.ALL_BLOCKED_DEVICE_IFA, ifa);
                }
            }
        	if (blockDidmad5s != null && blockDidmad5s.size() > 0) {
                for (String didmad5 : blockDidmad5s) {
                    redisMaster.srem(this.ALL_BLOCKED_DEVICE_DIDMD5, didmad5);
                }
            }
        	if (blockDpidmad5s != null && blockDpidmad5s.size() > 0) {
                for (String dpidmad5 : blockDpidmad5s) {
                    redisMaster.srem(this.ALL_BLOCKED_DEVICE_DPIDMD5, dpidmad5);
                }
            }
        	redisMaster.expire(this.ALL_BLOCKED_DEVICE_IP, EXPIRATION_TIME);
        	redisMaster.expire(this.ALL_BLOCKED_DEVICE_IFA, EXPIRATION_TIME);
        	redisMaster.expire(this.ALL_BLOCKED_DEVICE_DIDMD5, EXPIRATION_TIME);
        	redisMaster.expire(this.ALL_BLOCKED_DEVICE_DPIDMD5, EXPIRATION_TIME);
        	
            LOGGER.info("op loadMaterialMetaData :{} ms", System.currentTimeMillis() - begin);//op不能修改,是关键字,在运维那里有监控
            LOGGER.debug("------------MaterialTask-----loadMaterialMetaData------  End--");
        } catch (Exception e) {
        	LOGGER.error("------------ReqBlockTask-----loadReqBlockMetaData------error:{}",e.toString());
        } finally {
            if(null != redisMaster){
                redisMaster.close();
            }
        }
	}
	
	
	
	
}
