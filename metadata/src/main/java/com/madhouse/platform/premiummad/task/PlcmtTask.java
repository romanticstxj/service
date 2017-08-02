package com.madhouse.platform.premiummad.task;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import redis.clients.jedis.Jedis;

import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.entity.Adspace;
import com.madhouse.platform.premiummad.entity.PlcmtMetaData;
import com.madhouse.platform.premiummad.entity.PlcmtMetaData.Image;
import com.madhouse.platform.premiummad.service.IPlcmtService;
import com.madhouse.platform.premiummad.util.Constant;
import com.madhouse.platform.premiummad.util.ResourceManager;

@Component
public class PlcmtTask {
private static final Logger LOGGER = LoggerFactory.getLogger("metadata");
    
    private Jedis redisMaster = null;
    
    @Value("${ALL_PLACEMENT}")
    private String ALL_PLACEMENT;
    
    @Value("${PLACEMENT_META_DATA}")
    private String PLACEMENT_META_DATA;
    
    @Value("${EXPIRATION_DATE}")
    private Integer EXPIRATION_DATE;
    
    @Autowired
    private IPlcmtService plcmtService;
    
    @Autowired
    private ResourceManager rm;
    
    public void run() {
        try {
            LOGGER.debug("------------PlcmtTask-----------start--");
            this.redisMaster = rm.getJedisPoolMaster().getResource();
            final List<Adspace> listAdspaces = plcmtService.queryAll();
            long begin = System.currentTimeMillis();
            for (Adspace adspace : listAdspaces) {
                PlcmtMetaData metaData = new PlcmtMetaData();
                try {
                    if(null != adspace){
                        BeanUtils.copyProperties(adspace, metaData);
                        //广告类型(1: 普通硬广, 2: 视频, 3: 原生)
                        switch (adspace.getAdType()) {
                            case Constant.PlcmtType.BANNER:
                                metaData.setBanner(getImage(adspace, metaData));
                                break;
                            case Constant.PlcmtType.VIDEO:
                                PlcmtMetaData.Video video = getVideo(adspace, metaData);
                                switch (adspace.getLayout()) {
                                    case Constant.Layout.VIDEO_201 :
                                        video.setStartDelay(Constant.StartDelay.BEFORE_COMMENCEMENT);
                                        video.setLinearity(Constant.Linearity.PATCH_VIDEO);
                                        break;
                                    case Constant.Layout.VIDEO_202 :
                                        video.setStartDelay(Constant.StartDelay.IN_THE_POST);
                                        video.setLinearity(Constant.Linearity.PATCH_VIDEO);
                                        break;
                                    case Constant.Layout.VIDEO_203 :
                                        video.setStartDelay(Constant.StartDelay.POST);
                                        video.setLinearity(Constant.Linearity.PATCH_VIDEO);
                                        break;
                                    case Constant.Layout.VIDEO_211:
                                        String[] videoMaterialSize = StringUtils.tokenizeToStringArray(adspace.getMaterialSize(), "*");
                                        video.setW(Integer.parseInt(videoMaterialSize[0]));
                                        video.setH(Integer.parseInt(videoMaterialSize[1]));
                                        video.setMimes(queryMimesByType(adspace.getMaterialType()));
                                        video.setLinearity(Constant.Linearity.SUSPENSION_PAUSE);
                                        break;
                                    case Constant.Layout.VIDEO_221:
                                        video.setLinearity(Constant.Linearity.BOOT_VIDEO);
                                        break;
                                    case Constant.Layout.VIDEO_222:
                                        video.setLinearity(Constant.Linearity.SHUTDOWN_VIDEO);
                                        break;
                                    case Constant.Layout.VIDEO_231:
                                        video.setLinearity(Constant.Linearity.SCREEN_VIDEO);
                                        break;
                                }
                                
                                metaData.setVideo(video);
                                break;
                            case Constant.PlcmtType.NATIVE:
                                PlcmtMetaData.Native natives = metaData.new Native();
                                if(adspace.getLayout().equals(Constant.Layout.NATIVE_311)){
                                    PlcmtMetaData.Image nativesImage = metaData.new Image();
                                    String[] videoSize = StringUtils.tokenizeToStringArray(adspace.getVideoSize(), "*");
                                    nativesImage.setW(Integer.parseInt(videoSize[0]));
                                    nativesImage.setH(Integer.parseInt(videoSize[1]));
                                    nativesImage.setMimes(queryMimesByType(adspace.getMaterialType()));
                                    natives.setCover(nativesImage);
                                    natives.setVideo(getVideo(adspace, metaData));
                                } else {
                                    metaData.setLayout(adspace.getLayout()+adspace.getMainPicNumber());
                                    natives.setCover(getImage(adspace, metaData));
                                }
                                PlcmtMetaData.Image nativesIcon = metaData.new Image();
                                if(!org.apache.commons.lang3.StringUtils.isEmpty(adspace.getLogoSize())){
                                    String[] nativesIconSize = StringUtils.tokenizeToStringArray(adspace.getLogoSize(), "*");
                                    nativesIcon.setW(Integer.parseInt(nativesIconSize[0]));
                                    nativesIcon.setH(Integer.parseInt(nativesIconSize[1]));
                                }
                                if(!org.apache.commons.lang3.StringUtils.isEmpty(adspace.getLogoType()+"")){
                                    nativesIcon.setMimes(queryMimesByType(adspace.getLogoType()));
                                }
                                natives.setIcon(nativesIcon);
                                natives.setTitle(adspace.getTitleMaxLength() > 0 ? adspace.getTitleMaxLength() : 0);
                                natives.setDesc(adspace.getDescMaxLength() > 0 ? adspace.getDescMaxLength() : 0);
                                metaData.setNatives(natives);
                                break;
                        }
                        
                    }
                    redisMaster.set(String.format(this.PLACEMENT_META_DATA, String.valueOf(adspace.getId())), JSON.toJSONString(metaData), "NX", "EX", EXPIRATION_DATE);
                    redisMaster.sadd(this.ALL_PLACEMENT, String.valueOf(adspace.getId()));
                } catch (Exception e) {
                    e.printStackTrace();
                    LOGGER.error("------------PlcmtTask-----is_for------error:{}",e.toString());
                }
            }
            LOGGER.info("op plcmt_task_info :{} ms", System.currentTimeMillis() - begin);//op不能修改,是关键字,在运维那里有监控
            LOGGER.debug("------------PlcmtTask-----------  End--");
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("------------PlcmtTask-----------error:{}",e.toString());
        }
    }
    public List<String> queryMimesByType(Integer type) {
        if(type == null || type.intValue() <= 0){
            return null;
        }
        List<Integer> result = new ArrayList<Integer>();
        Integer not = 1;
        while(not < type){
            if((not & type) > 0){
                result.add(not);
            }
            not *= 2;
        }
        return plcmtService.queryMimesById(result);
    }
    public PlcmtMetaData.Image getImage(Adspace adspace ,PlcmtMetaData metaData) {
        PlcmtMetaData.Image img = metaData.new Image();
        String[] str = StringUtils.tokenizeToStringArray(adspace.getMaterialSize(), "*");
        img.setW(Integer.parseInt(str[0]));
        img.setH(Integer.parseInt(str[1]));
        
        metaData.setW(Integer.parseInt(str[0]));
        metaData.setH(Integer.parseInt(str[1]));
        
        
        img.setMimes(queryMimesByType(adspace.getMaterialType()));
        return img;
    }
    public PlcmtMetaData.Video getVideo(Adspace adspace ,PlcmtMetaData metaData) {
        PlcmtMetaData.Video video = metaData.new Video();
        if(!adspace.getLayout().equals(Constant.Layout.VIDEO_211)){
            String[] videoSize = StringUtils.tokenizeToStringArray(adspace.getVideoSize(), "*");
            video.setW(Integer.parseInt(videoSize[0]));
            video.setH(Integer.parseInt(videoSize[1]));
            
            metaData.setW(Integer.parseInt(videoSize[0]));
            metaData.setH(Integer.parseInt(videoSize[1]));
            
            String[] nativesdescription = StringUtils.tokenizeToStringArray(adspace.getVideoDuration(), ",");
            video.setMinDuraion(Integer.parseInt(nativesdescription[0]));
            video.setMaxDuration(Integer.parseInt(nativesdescription[1]));
            video.setMimes(queryMimesByType(adspace.getVideoType()));
        }
        return video;
    }
    
    
   
}
