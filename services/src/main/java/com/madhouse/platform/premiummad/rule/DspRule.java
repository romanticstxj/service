package com.madhouse.platform.premiummad.rule;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dto.DspDto;
import com.madhouse.platform.premiummad.entity.Dsp;
import com.madhouse.platform.premiummad.entity.DspAuth;
import com.madhouse.platform.premiummad.exception.BusinessException;
import com.madhouse.platform.premiummad.util.BeanUtils;
import com.madhouse.platform.premiummad.util.StringUtils;

public class DspRule extends BaseRule{
	
	public static Dsp convertToModel(DspDto dto, Dsp entity, boolean isCreate){
		BeanUtils.copyProperties(dto, entity);
        BeanUtils.setCommonParam(entity, isCreate);
        
        //把页面上的部分多选项转化为数据库的单值
        int deliveryType = StringUtils.convertMultiChoiceToSingleChoice(dto.getDeliveryType());
        entity.setDeliveryType((byte) deliveryType);
		
		return entity;
	}
	
	public static List<DspDto> convertToDto(Dsp entity, DspDto dto) {
        BeanUtils.copyProperties(entity,dto);
        List<DspDto> dtos = new ArrayList<>();
        dtos.add(dto);
        
        String deliveryTypeStr = StringUtils.convertSingleChoiceToMultiChoice((int)entity.getDeliveryType());
        dto.setDeliveryType(deliveryTypeStr);
        
		return dtos;
	}
	
	public static List<DspDto> convertToDtoList(List<Dsp> entities, ArrayList<DspDto> dtos) {
        //copy entity to dto
		BeanUtils.copyList(entities,dtos,DspDto.class);
        for(int i=0; i<dtos.size(); i++){
    		String deliveryTypeStr = StringUtils.convertSingleChoiceToMultiChoice((int)entities.get(i).getDeliveryType());
    		dtos.get(i).setDeliveryType(deliveryTypeStr);
        }
        
        return dtos;
	}

	public static List<DspAuth> convertToDspAuthModelList(List<DspAuth> dspAuthDtos, boolean b) {
		List<DspAuth> dspAuths = JSONArray.parseArray(dspAuthDtos.toString(), DspAuth.class);
		if(BeanUtils.isEmpty(dspAuths)){
			return null;
		}
		checkIntegrity(dspAuths);
		for(int i=0;i<dspAuths.size();i++){
			BaseRule.validateAndProcessDto(dspAuths.get(i));
			BeanUtils.setCommonParam(dspAuths.get(i), true);
		}
		return dspAuths;
	}

	private static void checkIntegrity(List<DspAuth> dspAuths) {
		DspAuth singleDspAuth = dspAuths.get(0);
		int adspaceId = singleDspAuth.getAdspaceId();
		int mediaId = singleDspAuth.getMediaId();
		if(dspAuths.size() > 1){ //如果数据不止一条，不能有所有的概念
			for(DspAuth da: dspAuths){
				if(StringUtils.intEquals(da.getAdspaceId(), SystemConstant.DB.DSP_MEDIA_AUTH_ALL)){
					throw new BusinessException(StatusCode.SC31006);
				}
			}
		} else if(adspaceId == SystemConstant.DB.DSP_MEDIA_AUTH_ALL){ //如果数据只有一条，广告位id是-1，媒体id也须-1
			if(mediaId != SystemConstant.DB.DSP_MEDIA_AUTH_ALL){
				singleDspAuth.setMediaId(SystemConstant.DB.DSP_MEDIA_AUTH_ALL);
			}
		}
	}
}
