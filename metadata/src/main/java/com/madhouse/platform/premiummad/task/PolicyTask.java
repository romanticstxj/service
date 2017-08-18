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
import com.madhouse.platform.premiummad.entity.PolicyMetaData.WeekdayHours;
import com.madhouse.platform.premiummad.service.IPolicyService;
import com.madhouse.platform.premiummad.util.ResourceManager;

@Component
public class PolicyTask {
private static final Logger LOGGER = LoggerFactory.getLogger("metadata");

    @Value("${ALL_POLICY}")
    private String ALL_POLICY;
    
    @Value("${POLICY_META_DATA}")
    private String POLICY_META_DATA;
    
    @Value("${EXPIRATION_TIME}")
    private Integer EXPIRATION_TIME;
    
    @Autowired
    private IPolicyService policyService;
    
    @Autowired
    private ResourceManager rm;
    
    public void loadPolicyMetaData() {
        Jedis redisMaster = rm.getJedisPoolMaster().getResource();
        try {
            LOGGER.debug("------------PolicyTask-----------start--");
            final List<Policy> listPolicys = policyService.queryAll();
            long begin = System.currentTimeMillis();
            redisMaster.del(ALL_POLICY);
            for (Policy  policy: listPolicys) {
                if(policy.getPolicyAdspaces().size()>0 && policy.getPolicyDsp().size()>0){
                    PolicyMetaData metaData = new PolicyMetaData();
                    try {
                        if(null != policy){
                            BeanUtils.copyProperties(policy, metaData);
                            List<PolicyMetaData.WeekdayHours> weekdayHoursList =new ArrayList<PolicyMetaData.WeekdayHours>();
                            String[] weekDay= StringUtils.tokenizeToStringArray(policy.getTimeTargeting(), "&");
                            if(!StringUtils.isEmpty(weekDay)){
                                for (String days : weekDay) {
                                    //0:0,1,2,3&1:19,20,21,22,23
                                    PolicyMetaData.WeekdayHours weekdayHours  = metaData.new WeekdayHours();
                                    String[] day = StringUtils.tokenizeToStringArray(days, ":");
                                    if(!StringUtils.isEmpty(day[0])){
                                        weekdayHours.setWeekDay(Integer.parseInt(day[0]));
                                        weekdayHours.setHours(splitIdsToInt(day[1]));
                                        weekdayHoursList.add(weekdayHours);
                                    }
                                }
                            }
                            
                            metaData.setWeekdayHoursList(weekdayHoursList);
                            String [] location = StringUtils.tokenizeToStringArray(policy.getLocationTargeting(), ",");
                            metaData.setLocation(!StringUtils.isEmpty(location) ? Arrays.asList(location) : null);
                            metaData.setOs(splitIdsToInt(policy.getOsTargeting()));
                            metaData.setConnectionType(splitIdsToInt(policy.getConnTargeting()));
                            
                            List<DSPInfo> dspInfo=new ArrayList<PolicyMetaData.DSPInfo>();
                            for (com.madhouse.platform.premiummad.entity.DSPInfo dsp : policy.getPolicyDsp()) {
                                PolicyMetaData.DSPInfo info= metaData.new DSPInfo();
                                BeanUtils.copyProperties(dsp, info);
                                dspInfo.add(info);
                            }
                            metaData.setDspInfoList(dspInfo);
                            
                            List<AdspaceInfo> adspaceInfo=new ArrayList<PolicyMetaData.AdspaceInfo>();
                            for (com.madhouse.platform.premiummad.entity.AdspaceInfo dsp : policy.getPolicyAdspaces()) {
                                PolicyMetaData.AdspaceInfo info= metaData.new AdspaceInfo();
                                BeanUtils.copyProperties(dsp, info);
                                adspaceInfo.add(info);
                            }
                            metaData.setAdspaceInfoList(adspaceInfo);
                        }
                        redisMaster.setex(String.format(this.POLICY_META_DATA, String.valueOf(policy.getId())), EXPIRATION_TIME, JSON.toJSONString(metaData));
                        redisMaster.sadd(this.ALL_POLICY, String.valueOf(policy.getId()));
                    } catch (Exception e) {
                        e.printStackTrace();
                        LOGGER.error("------------PolicyTask-----is_for------error:{}",e.toString());
                    }
                }
            }
            redisMaster.expire(this.ALL_POLICY, EXPIRATION_TIME);
            LOGGER.info("op loadPolicyMetaData :{} ms", System.currentTimeMillis() - begin);//op不能修改,是关键字,在运维那里有监控
            LOGGER.debug("------------PolicyTask-----------  End--");
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("------------PolicyTask-----------error:{}",e.toString());
        }finally{
            if(null != redisMaster){
                redisMaster.close();
            }
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