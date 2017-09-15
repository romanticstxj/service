package com.madhouse.platform.premiummad.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import redis.clients.jedis.Jedis;

import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.entity.MaterialMetaData;
import com.madhouse.platform.premiummad.entity.MaterialMetaData.Monitor;
import com.madhouse.platform.premiummad.service.IMaterialService;
import com.madhouse.platform.premiummad.util.ResourceManager;

@Component
public class MaterialTask {
 private static final Logger LOGGER = LoggerFactory.getLogger("metadata");
    
    @Autowired
    private IMaterialService iMaterialService;
    
    @Autowired
    private ResourceManager rm;
    
    @Value("${ALL_MATERIAL}")
    private String ALL_MATERIAL;
    
    @Value("${MATERIAL_META_DATA}")
    private String MATERIAL_META_DATA;
    
    @Value("${EXPIRATION_TIME}")
    private Integer EXPIRATION_TIME;
    
    public void loadMaterialMetaData() {
        Jedis redisMaster = rm.getJedisPoolMaster().getResource();
        try {
            LOGGER.debug("------------MaterialTask-----loadMaterialMetaData------start--");
            final List<Material> lists = iMaterialService.queryAll();
            long begin = System.currentTimeMillis();
            Set<String> allMaterialIds = redisMaster.smembers(ALL_MATERIAL);
            for (Material material : lists) {
                MaterialMetaData metaData = new MaterialMetaData();
                if(null != material){
                    BeanUtils.copyProperties(material, metaData);
                    MaterialMetaData.Monitor monitor = metaData.new Monitor();
                    monitor.setClkUrls(Arrays.asList(StringUtils.tokenizeToStringArray(material.getClkUrls(),"|")));
                    monitor.setSecUrls(Arrays.asList(StringUtils.tokenizeToStringArray(material.getSecUrls(),"|")));
                    monitor.setImpUrls(getTrack(monitor,material.getImpUrls()));
                    metaData.setMonitor(monitor);
                    metaData.setAdm(Arrays.asList(StringUtils.tokenizeToStringArray(material.getAdMaterials(),"|")));
                    String[] size = StringUtils.tokenizeToStringArray(material.getSize(),"*");
                    metaData.setW(!StringUtils.isEmpty(size[0])?Integer.parseInt(size[0]) : 0);
                    metaData.setH(!StringUtils.isEmpty(size[1])?Integer.parseInt(size[1]) : 0);
                    redisMaster.setex(String.format(this.MATERIAL_META_DATA, String.valueOf(metaData.getId())), EXPIRATION_TIME, JSON.toJSONString(metaData));
                    redisMaster.sadd(this.ALL_MATERIAL, String.valueOf(metaData.getId()));

                    if (allMaterialIds != null && allMaterialIds.size() > 0) {
                        allMaterialIds.remove(String.valueOf(metaData.getId()));
                    }
                }
            }

            if (allMaterialIds != null && allMaterialIds.size() > 0) {
                for (String materialId : allMaterialIds) {
                    redisMaster.srem(this.ALL_MATERIAL, materialId);
                }
            }

            redisMaster.expire(this.ALL_MATERIAL, EXPIRATION_TIME);

            LOGGER.info("op loadMaterialMetaData :{} ms", System.currentTimeMillis() - begin);//op不能修改,是关键字,在运维那里有监控
            LOGGER.debug("------------MaterialTask-----loadMaterialMetaData------  End--");
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("------------MaterialTask-----loadMaterialMetaData------error:{}",e.toString());
        } finally {
            if(null != redisMaster){
                redisMaster.close();
            }
        }
    }
    
    private List<MaterialMetaData.Monitor.Track> getTrack(Monitor monitor,String impUrls) {
        List<MaterialMetaData.Monitor.Track> listTracks= new ArrayList<MaterialMetaData.Monitor.Track>();
        String[] imps = StringUtils.tokenizeToStringArray(impUrls,"|");
        for (int i = 0; i < imps.length; i++) {
            if(!StringUtils.isEmpty(imps[i])){
                MaterialMetaData.Monitor.Track track= monitor.new Track();
                String[] imp= imps[i].split("`");
                track.setStartDelay(Integer.parseInt(imp[0]));
                track.setUrl(imp[1]);
                listTracks.add(track);
            }
        }
        return listTracks;   
        
    }
}
