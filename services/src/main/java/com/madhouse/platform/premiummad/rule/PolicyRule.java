package com.madhouse.platform.premiummad.rule;

import java.util.List;

import com.madhouse.platform.premiummad.dto.PolicyDto;
import com.madhouse.platform.premiummad.entity.Policy;
import com.madhouse.platform.premiummad.util.BeanUtils;

public class PolicyRule {
	
	public static Policy convertToModel(PolicyDto dto, Policy entity){
		
		BeanUtils.copyProperties(dto, entity);
        BeanUtils.setCreateParam(entity);
        
//		//设置投放时段
//		Integer isTimeTargeting = dto.getIsTimeTargeting();
//		if(isTimeTargeting != null){
//			if(isTimeTargeting.intValue() == 1){ //指定时段
//				entity.setTimeTargeting(timeTargeting);
//			}
//		}
		
        return entity;
	}

	public static List<PolicyDto> convertToDto(Policy policy, PolicyDto policyDto) {
		return null;
	}
}
