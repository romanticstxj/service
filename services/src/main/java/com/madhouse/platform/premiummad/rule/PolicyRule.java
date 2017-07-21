package com.madhouse.platform.premiummad.rule;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dto.PolicyAdspaceDto;
import com.madhouse.platform.premiummad.dto.PolicyDspDto;
import com.madhouse.platform.premiummad.dto.PolicyDto;
import com.madhouse.platform.premiummad.entity.Policy;
import com.madhouse.platform.premiummad.entity.PolicyAdspace;
import com.madhouse.platform.premiummad.entity.PolicyDsp;
import com.madhouse.platform.premiummad.exception.BusinessException;
import com.madhouse.platform.premiummad.util.BeanUtils;
import com.madhouse.platform.premiummad.util.StringUtils;

public class PolicyRule {
	
	public static Policy convertToModel(PolicyDto dto, Policy entity){
		
		//copy PolicyDto
		BeanUtils.copyProperties(dto, entity, "policyAdspaces", "policyDsp");
        BeanUtils.setCreateParam(entity);
        
        //copy PolicyAdspaceDto
        List<PolicyAdspace> policyAdspaces = new ArrayList<PolicyAdspace>();
        BeanUtils.copyList(dto.getPolicyAdspaces(), policyAdspaces, PolicyAdspace.class, "bidFloor");
        entity.setPolicyAdspaces(policyAdspaces);
        
        //copy PolicyDspDto
        PolicyDsp policyDsp = new PolicyDsp();
        BeanUtils.copyProperties(dto.getPolicyDsp(), policyDsp);
        BeanUtils.setCreateParam(policyDsp);
        entity.setPolicyDsp(policyDsp);
        
//		//设置投放时段
//		Integer isTimeTargeting = dto.getIsTimeTargeting();
//		if(isTimeTargeting != null){
//			if(isTimeTargeting.intValue() == 1){ //指定时段
//				entity.setTimeTargeting(timeTargeting);
//			}
//		}
        
        List<PolicyAdspaceDto> policyAdspaceDtos = dto.getPolicyAdspaces();
        if(policyAdspaces != null){
        	for(int i=0; i< policyAdspaces.size(); i++){
        		//转换广告售卖价格从单位元到分
        		Integer bidFloorUnitFen = StringUtils.convertCurrencyYuanToFen(policyAdspaceDtos.get(i).getBidFloor());
        		policyAdspaces.get(i).setBidFloor(bidFloorUnitFen);
        		//设置策略中广告位的日期和创建者信息
        		BeanUtils.setCreateParam(policyAdspaces.get(i));
        	}
        }
        
//        Integer isQuantityLimit = dto.getIsQuantityLimit();
//        if(isQuantityLimit != null){
//        	if(isQuantityLimit.intValue() == 1){ //有总量限制
//        		
//        	}
//        }
        
        return entity;
	}

	public static List<PolicyDto> convertToDto(Policy policy, PolicyDto policyDto) {
		
		//copy Policy
		BeanUtils.copyProperties(policy, policyDto, "policyAdspaces", "policyDsp");
		
		List<PolicyAdspaceDto> policyAdspaceDtos = new ArrayList<PolicyAdspaceDto>();
		BeanUtils.copyList(policy.getPolicyAdspaces(), policyAdspaceDtos, PolicyAdspaceDto.class, "bidFloor");
		policyDto.setPolicyAdspaces(policyAdspaceDtos);
		
		PolicyDspDto policyDspDto = new PolicyDspDto();
		BeanUtils.copyProperties(policy.getPolicyDsp(), policyDspDto);
		policyDto.setPolicyDsp(policyDspDto);
		
		//设置前端结束时间限制与否的开关
		Integer isEndDate = policyDto.getEndDate() == null ? 0 : 1;
		policyDto.setIsEndDate(isEndDate);
		
		
		
		if(policyAdspaceDtos != null){
			for(int i=0; i<policyAdspaceDtos.size(); i++){
				Double bidFloor = StringUtils.convertCurrencyFentoYuan(policy.getPolicyAdspaces().get(i).getBidFloor());
				policyAdspaceDtos.get(i).setBidFloor(bidFloor);
			}
		}
		        
		return null;
	}
	
	public static void validateDto(PolicyDto policyDto){
		String fieldName = BeanUtils.hasEmptyField(policyDto);
        if (fieldName != null)
        	throw new BusinessException(StatusCode.SC20001, fieldName + " cannot be null");
	}
}
