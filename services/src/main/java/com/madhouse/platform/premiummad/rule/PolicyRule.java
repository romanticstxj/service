package com.madhouse.platform.premiummad.rule;

import java.util.ArrayList;
import java.util.List;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dto.AdspaceDto;
import com.madhouse.platform.premiummad.dto.PolicyAdspaceDto;
import com.madhouse.platform.premiummad.dto.PolicyDspDto;
import com.madhouse.platform.premiummad.dto.PolicyDto;
import com.madhouse.platform.premiummad.entity.Policy;
import com.madhouse.platform.premiummad.entity.PolicyAdspace;
import com.madhouse.platform.premiummad.entity.PolicyDsp;
import com.madhouse.platform.premiummad.exception.BusinessException;
import com.madhouse.platform.premiummad.util.BeanUtils;
import com.madhouse.platform.premiummad.util.StringUtils;

public class PolicyRule extends BaseRule{
	
	public static Policy convertToModel(PolicyDto dto, Policy entity){
		
		//copy PolicyDto
		BeanUtils.copyProperties(dto, entity, "policyAdspaces", "policyDsp");
        BeanUtils.setCreateParam(entity);
        
        //copy PolicyAdspaceDto
        List<PolicyAdspace> policyAdspaces = new ArrayList<PolicyAdspace>();
        BeanUtils.copyList(dto.getPolicyAdspaces(), policyAdspaces, PolicyAdspace.class, "bidFloor");
        entity.setPolicyAdspaces(policyAdspaces);
        
        //copy PolicyDspDto
        List<PolicyDsp> policyDsps = new ArrayList<PolicyDsp>();
        BeanUtils.copyList(dto.getPolicyDsps(), policyDsps, PolicyDsp.class);
        entity.setPolicyDsps(policyDsps);
        
//		//设置投放时段
//		Integer isTimeTargeting = dto.getIsTimeTargeting();
//		if(isTimeTargeting != null){
//			if(isTimeTargeting.intValue() == 1){ //指定时段
//				entity.setTimeTargeting(timeTargeting);
//			}
//		}
        
        Integer policyId = entity.getId();
        List<PolicyAdspaceDto> policyAdspaceDtos = dto.getPolicyAdspaces();
        if(policyAdspaces != null){
        	for(int i=0; i< policyAdspaces.size(); i++){
        		policyAdspaces.get(i).setPolicyId(policyId);
        		
        		//转换广告售卖价格从单位元到分
        		Integer bidFloorUnitFen = StringUtils.convertCurrencyYuanToFen(policyAdspaceDtos.get(i).getBidFloor());
        		policyAdspaces.get(i).setBidFloor(bidFloorUnitFen);
        		//设置策略中广告位的日期和创建者信息
        		BeanUtils.setCreateParam(policyAdspaces.get(i));
        	}
        }
        
        if(policyDsps != null){
        	for(int i=0; i< policyDsps.size(); i++){
        		policyDsps.get(i).setPolicyId(policyId);
        		//设置策略中dsp的日期和创建者信息
        		BeanUtils.setCreateParam(policyDsps.get(i));
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
		
		List<PolicyDspDto> policyDspDtos = new ArrayList<PolicyDspDto>();
        BeanUtils.copyList(policy.getPolicyDsps(), policyDspDtos, PolicyDspDto.class);
        policyDto.setPolicyDsps(policyDspDtos);
		
		//设置前端结束时间限制与否的开关
		Integer isEndDate = policyDto.getEndDate() == null ? 0 : 1;
		policyDto.setIsEndDate(isEndDate);
		
		if(policyAdspaceDtos != null){
			for(int i=0; i<policyAdspaceDtos.size(); i++){
				//设置policy广告位列表里的每个具体广告位信息
				AdspaceDto adspaceDto = new AdspaceDto();
				BeanUtils.copyProperties(policy.getPolicyAdspaces().get(i).getAdspace(), adspaceDto);
				policyAdspaceDtos.get(i).setAdspace(adspaceDto);
				
				//convert bidfloor in policyadspace
				Double bidFloor = StringUtils.convertCurrencyFentoYuan(policy.getPolicyAdspaces().get(i).getBidFloor());
				policyAdspaceDtos.get(i).setBidFloor(bidFloor);
			}
		}
		
		List<PolicyDto> result = new ArrayList<PolicyDto>();
		result.add(policyDto);
		return result;
	}
	
	public static void validateDto(PolicyDto policyDto){
		String fieldName = BeanUtils.hasEmptyField(policyDto);
        if (fieldName != null)
        	throw new BusinessException(StatusCode.SC20001, fieldName + " cannot be null");
        
        List<PolicyAdspaceDto> policyAdspaceDtos = policyDto.getPolicyAdspaces();
        if(policyAdspaceDtos == null || policyAdspaceDtos.size() == 0){
        	throw new BusinessException(StatusCode.SC20404);
        }
        
        for(PolicyAdspaceDto policyAdspaceDto: policyAdspaceDtos){
        	double bidFloor = policyAdspaceDto.getBidFloor();
        	AdspaceDto adspaceDto = policyAdspaceDto.getAdspace();
        	if(adspaceDto == null){ //广告位底价信息未提供
        		throw new BusinessException(StatusCode.SC20408);
        	}
        	double baseBidFloor = adspaceDto.getBidFloor();
        	if(bidFloor < baseBidFloor){ //广告位售卖单价不能低于其底价
        		throw new BusinessException(StatusCode.SC20407);
        	}
        }
        
        List<PolicyDspDto> policyDspDtos = policyDto.getPolicyDsps();
        if(policyDspDtos == null || policyDspDtos.size() == 0){
        	throw new BusinessException(StatusCode.SC20405);
        } else{
        	if(policyDto.getType().intValue() != SystemConstant.OtherConstant.POLICY_TYPE_RTB){
        		//如果策略类型是pd或者pdb，则此策略的dsp数量只能是一个
        		if(policyDspDtos.size() > 1){
        			throw new BusinessException(StatusCode.SC20406);
        		}
        	}
        }
	}
	
	public static List<PolicyDto> convertToDtoList(List<Policy> policies, ArrayList<PolicyDto> policyDtos) {
        //copy entity to dto
		BeanUtils.copyList(policies,policyDtos,PolicyDto.class);
        for(int i=0; i<policyDtos.size(); i++){
        	List<PolicyDspDto> policyDspDtos = new ArrayList<PolicyDspDto>();
            BeanUtils.copyList(policies.get(i).getPolicyDsps(), policyDspDtos, PolicyDspDto.class);
            policyDtos.get(i).setPolicyDsps(policyDspDtos);
            
        	//设置前端结束时间限制与否的开关
    		Integer isEndDate = policyDtos.get(i).getEndDate() == null ? 0 : 1;
    		policyDtos.get(i).setIsEndDate(isEndDate);
        }
        
        return policyDtos;
	}
}
