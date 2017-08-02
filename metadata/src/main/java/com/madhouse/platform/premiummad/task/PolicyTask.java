package com.madhouse.platform.premiummad.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import redis.clients.jedis.Jedis;

import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.entity.Policy;
import com.madhouse.platform.premiummad.entity.PolicyMetaData;
import com.madhouse.platform.premiummad.entity.PolicyMetaData.AdspaceInfo;
import com.madhouse.platform.premiummad.entity.PolicyMetaData.DSPInfo;
import com.madhouse.platform.premiummad.service.IPolicyService;
import com.madhouse.platform.premiummad.util.ResourceManager;

@Component
public class PolicyTask {
private static final Logger LOGGER = LoggerFactory.getLogger("metadata");
    
    private Jedis redisMaster = null;
    
    @Value("${ALL_POLICY}")
    private String ALL_POLICY;
    
    @Value("${POLICY_META_DATA}")
    private String POLICY_META_DATA;
    
    @Value("${EXPIRATION_DATE}")
    private Integer EXPIRATION_DATE;
    
    @Autowired
    private IPolicyService policyService;
    
    @Autowired
    private ResourceManager rm;
    
    public void run() {
        try {
            LOGGER.debug("------------PolicyTask-----------start--");
            this.redisMaster = rm.getJedisPoolMaster().getResource();
            final List<Policy> listPolicys = policyService.queryAll();
            long begin = System.currentTimeMillis();
            for (Policy  policy: listPolicys) {
                if(policy.getPolicyAdspaces().size()>0 && policy.getPolicyDsp().size()>0){
                    PolicyMetaData metaData = new PolicyMetaData();
                    try {
                        if(null != policy){
                            BeanUtils.copyProperties(policy, metaData);
                            Map<Integer, List<Integer>> weekDayHours = new HashMap<Integer, List<Integer>>();
                            String[] weekDay= StringUtils.tokenizeToStringArray(policy.getTimeTargeting(), "&");
                            if(!StringUtils.isEmpty(weekDay)){
                                for (String days : weekDay) {
                                    //0:0,1,2,3&1:19,20,21,22,23
                                    String[] day = StringUtils.tokenizeToStringArray(days, ":");
                                    if(!StringUtils.isEmpty(day[0])){
                                        weekDayHours.put(Integer.parseInt(day[0]),splitIdsToInt(day[1]));
                                    }
                                }
                            }
                            metaData.setWeekDayHours(weekDayHours);
                            String [] location = StringUtils.tokenizeToStringArray(policy.getLocationTargeting(), ",");
                            metaData.setLocation(!StringUtils.isEmpty(location) ? Arrays.asList(location) : null);
                            metaData.setOs(splitIdsToInt(policy.getOsTargeting()));
                            metaData.setConnectionType(splitIdsToInt(policy.getConnTargeting()));
                            Map<Long, DSPInfo> dspInfo=new HashMap<Long, PolicyMetaData.DSPInfo>();
                            for (com.madhouse.platform.premiummad.entity.DSPInfo dsp : policy.getPolicyDsp()) {
                                PolicyMetaData.DSPInfo info= metaData.new DSPInfo();
                                BeanUtils.copyProperties(dsp, info);
                                dspInfo.put(dsp.getId(),info);
                            }
                            metaData.setDspInfoMap(dspInfo);
                            Map<Long, AdspaceInfo> adspaceInfo=new HashMap<Long, PolicyMetaData.AdspaceInfo>();
                            for (com.madhouse.platform.premiummad.entity.AdspaceInfo dsp : policy.getPolicyAdspaces()) {
                                PolicyMetaData.AdspaceInfo info= metaData.new AdspaceInfo();
                                BeanUtils.copyProperties(dsp, info);
                                adspaceInfo.put(dsp.getId(),info);
                            }
                            metaData.setAdspaceInfoMap(adspaceInfo);
                        }
                        redisMaster.set(String.format(this.POLICY_META_DATA, String.valueOf(policy.getId())), JSON.toJSONString(metaData), "NX", "EX", EXPIRATION_DATE);
                        redisMaster.sadd(this.ALL_POLICY, String.valueOf(policy.getId()));
                    } catch (Exception e) {
                        e.printStackTrace();
                        LOGGER.error("------------PolicyTask-----is_for------error:{}",e.toString());
                    }
                }
            }
            LOGGER.info("op policy_task_info :{} ms", System.currentTimeMillis() - begin);//op不能修改,是关键字,在运维那里有监控
            LOGGER.debug("------------PolicyTask-----------  End--");
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("------------PolicyTask-----------error:{}",e.toString());
        }
    }
    
    public  List<Integer> splitIdsToInt(String ids) {
        List<Integer> list= new ArrayList<Integer>();
        if (ids == null) {
            return null;
        }
        String[] str = StringUtils.tokenizeToStringArray(ids, ",");
        for (String string : str) {
            list.add(Integer.parseInt(string));
        }
        return list;

    }
}