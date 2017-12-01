package com.madhouse.platform.premiummad.rule;

import java.util.ArrayList;
import java.util.List;

import com.madhouse.platform.premiummad.dto.RequestBlockDto;
import com.madhouse.platform.premiummad.entity.RequestBlock;
import com.madhouse.platform.premiummad.util.BeanUtils;

public class RequestBlockRule extends BaseRule{
	
	public static List<RequestBlockDto> convertToDto(RequestBlock entity, RequestBlockDto dto) {
		//copy Policy
		BeanUtils.copyProperties(entity, dto);
		List<RequestBlockDto> result = new ArrayList<RequestBlockDto>();
		result.add(dto);
		return result;
	}
	
	public static RequestBlock convertToModel(RequestBlockDto dto, RequestBlock entity, boolean isCreate){
		//copy RequestBlockDto
		BeanUtils.copyProperties(dto, entity);
        BeanUtils.setCommonParam(entity, isCreate);
        return entity;
	}
	
	public static List<RequestBlockDto> convertToDtoList(List<RequestBlock> source, List<RequestBlockDto> target){
		//copy entity to dto
		BeanUtils.copyList(source, target, RequestBlockDto.class);
		return target;
	}
}
