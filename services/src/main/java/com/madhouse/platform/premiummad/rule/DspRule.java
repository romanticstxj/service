package com.madhouse.platform.premiummad.rule;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONArray;
import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dto.DspDto;
import com.madhouse.platform.premiummad.dto.DspDto.RequestUrl;
import com.madhouse.platform.premiummad.entity.Dsp;
import com.madhouse.platform.premiummad.entity.DspMedia;
import com.madhouse.platform.premiummad.exception.BusinessException;
import com.madhouse.platform.premiummad.util.BeanUtils;
import com.madhouse.platform.premiummad.util.ObjectUtils;
import com.madhouse.platform.premiummad.util.StringUtils;

public class DspRule extends BaseRule{
	
	public static void validateDto(Object dto){
		BaseRule.validateDto(dto);
		
		DspDto dspDto = (DspDto) dto;
		String bidUrl = dspDto.getBidUrl();
		RequestUrl[] requestUrl = dspDto.getRequestUrl();
		//判断如果bidUrl，requestUrl有且只能有一个有值
		if(!(StringUtils.isEmpty(bidUrl) ^ ObjectUtils.isEmpty(requestUrl))){
			throw new BusinessException(StatusCode.SC20304);
		}
		
		//验证url pattern是否正确
		boolean matchesUrlPattern = StringUtils.matchesUrlPattern(bidUrl);
		if(!matchesUrlPattern){
			throw new BusinessException(StatusCode.SC20303);
		}
		if(!ObjectUtils.isEmpty(requestUrl)){
			//如果requestUrl填了信息，那么要满足url正则表达式
			for(RequestUrl url: requestUrl){
				Integer deliveryType = url.getDeliveryType();
				String innerBidUrl = url.getBidUrl();
				if(StringUtils.isEmpty(deliveryType) || StringUtils.isEmpty(innerBidUrl)){
					throw new BusinessException(StatusCode.SC20304);
				}
				matchesUrlPattern = StringUtils.matchesUrlPattern(innerBidUrl);
				if(!matchesUrlPattern){
					throw new BusinessException(StatusCode.SC20303);
				}
			}
		}
	}
	
	public static Dsp convertToModel(DspDto dto, Dsp entity, boolean isCreate){
		BeanUtils.copyProperties(dto, entity);
        BeanUtils.setCommonParam(entity, isCreate);
        
        //把页面上的部分多选项转化为数据库的单值
        int deliveryType = StringUtils.convertMultiChoiceToSingleChoice(dto.getDeliveryType());
        entity.setDeliveryType((byte) deliveryType);
        
        //把dspUrls转化成requestUrl
        if(dto.getRequestUrl() != null && dto.getRequestUrl().length > 0){
        	String requestUrl = JSONArray.toJSONString(dto.getRequestUrl());
        	entity.setRequestUrl(requestUrl);
        }
        
		return entity;
	}
	
	public static List<DspDto> convertToDto(Dsp entity, DspDto dto) {
        BeanUtils.copyProperties(entity,dto);
        List<DspDto> dtos = new ArrayList<>();
        dtos.add(dto);
        
        String deliveryTypeStr = StringUtils.convertSingleChoiceToMultiChoice((int)entity.getDeliveryType());
        dto.setDeliveryType(deliveryTypeStr);
        
        //把requestUrl转换成dspUrls
        String requestUrl = entity.getRequestUrl();
        if(requestUrl != null && requestUrl.length() > 0){
        	List<DspDto.RequestUrl> list = JSONArray.parseArray(requestUrl, DspDto.RequestUrl.class);
        	if(list != null && list.size() > 0){
        		DspDto.RequestUrl[] arr = list.toArray(new DspDto.RequestUrl[list.size()]);
        		dto.setRequestUrl(arr);
        	}
        }
        
		return dtos;
	}
	
	public static List<DspDto> convertToDtoList(List<Dsp> entities, ArrayList<DspDto> dtos) {
        //copy entity to dto
		BeanUtils.copyList(entities,dtos,DspDto.class);
        for(int i=0; i<dtos.size(); i++){
    		String deliveryTypeStr = StringUtils.convertSingleChoiceToMultiChoice((int)entities.get(i).getDeliveryType());
    		
    		//把requestUrl转换成dspUrls
            String requestUrl = entities.get(i).getRequestUrl();
            if(requestUrl != null && requestUrl.length() > 0){
            	List<DspDto.RequestUrl> list = JSONArray.parseArray(requestUrl, DspDto.RequestUrl.class);
            	if(list != null && list.size() > 0){
            		DspDto.RequestUrl[] arr = list.toArray(new DspDto.RequestUrl[list.size()]);
            		dtos.get(i).setRequestUrl(arr);
            	}
            }
    		
    		dtos.get(i).setDeliveryType(deliveryTypeStr);
        }
        
        return dtos;
	}

	public static List<DspMedia> convertToDspAuthModelList(Set<DspMedia> dspAuthDtos, boolean isCreate) {
		List<DspMedia> dspAuths = JSONArray.parseArray(dspAuthDtos.toString(), DspMedia.class);
		if(BeanUtils.isEmpty(dspAuths)){
			return null;
		}
		checkIntegrity(dspAuths);
		for(int i=0;i<dspAuths.size();i++){
			BaseRule.validateAndProcessDto(dspAuths.get(i));
			BeanUtils.setCommonParam(dspAuths.get(i), isCreate);
		}
		return dspAuths;
	}

	private static void checkIntegrity(List<DspMedia> dspAuths) {
		DspMedia singleDspAuth = dspAuths.get(0);
		Integer adspaceId = singleDspAuth.getAdspaceId();
		Integer mediaId = singleDspAuth.getMediaId();
		if(dspAuths.size() > 1){ //如果数据不止一条，不能有所有的概念
			for(DspMedia da: dspAuths){
				if(StringUtils.intEquals(da.getAdspaceId(), SystemConstant.DB.DSP_MEDIA_AUTH_ALL)){
					throw new BusinessException(StatusCode.SC31006);
				}
			}
		} else if(StringUtils.intEquals(adspaceId, SystemConstant.DB.DSP_MEDIA_AUTH_ALL)){ //如果数据只有一条，广告位id是-1，媒体id也须-1
			if(StringUtils.intNotEquals(mediaId, SystemConstant.DB.DSP_MEDIA_AUTH_ALL)){
				singleDspAuth.setMediaId(SystemConstant.DB.DSP_MEDIA_AUTH_ALL);
			}
		}
	}
}
