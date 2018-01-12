package com.madhouse.platform.premiummad.rule;

import java.util.ArrayList;
import java.util.List;

import com.madhouse.platform.premiummad.dto.MediaWhiteListDto;
import com.madhouse.platform.premiummad.entity.MediaWhiteList;
import com.madhouse.platform.premiummad.util.BeanUtils;

public class MediaWhiteListRule extends BaseRule{
	
	public static List<MediaWhiteListDto> convertToDto(MediaWhiteList entity, MediaWhiteListDto dto) {
		BeanUtils.copyProperties(entity, dto);
		List<MediaWhiteListDto> result = new ArrayList<>();
		result.add(dto);
		return result;
	}
	
	public static MediaWhiteList convertToModel(MediaWhiteListDto dto, MediaWhiteList entity, boolean isCreate){
		//copy RequestBlockDto
		BeanUtils.copyProperties(dto, entity);
        BeanUtils.setCommonParam(entity, isCreate);
        return entity;
	}
	
	public static List<MediaWhiteListDto> convertToDtoList(List<MediaWhiteList> source, List<MediaWhiteListDto> target){
		//copy entity to dto
		BeanUtils.copyList(source, target, MediaWhiteListDto.class);
		return target;
	}
}
