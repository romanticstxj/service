package com.madhouse.platform.premiummad.rule;

import java.util.ArrayList;
import java.util.List;

import com.madhouse.platform.premiummad.dto.DspDto;
import com.madhouse.platform.premiummad.entity.Dsp;
import com.madhouse.platform.premiummad.util.BeanUtils;
import com.madhouse.platform.premiummad.util.StringUtils;

public class DspRule extends BaseRule{
	
	public static Dsp convertToModel(DspDto dto, Dsp entity){
		BeanUtils.copyProperties(dto, entity);
        BeanUtils.setCreateParam(entity);
        
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
}
