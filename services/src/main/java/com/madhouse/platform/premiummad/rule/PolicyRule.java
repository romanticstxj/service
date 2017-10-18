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
	
	public static Policy convertToModel(PolicyDto dto, Policy entity, boolean isCreate){
		
		//copy PolicyDto
		BeanUtils.copyProperties(dto, entity, "policyAdspaces", "policyDsp");
        BeanUtils.setCommonParam(entity, isCreate);
        
        setFlagForStorage(dto, entity);
        
        //copy PolicyAdspaceDto
        List<PolicyAdspace> policyAdspaces = new ArrayList<PolicyAdspace>();
        BeanUtils.copyList(dto.getPolicyAdspaces(), policyAdspaces, PolicyAdspace.class, "bidFloor");
        entity.setPolicyAdspaces(policyAdspaces);
        
        //copy PolicyDspDto
        List<PolicyDsp> policyDsps = new ArrayList<PolicyDsp>();
        BeanUtils.copyList(dto.getPolicyDsps(), policyDsps, PolicyDsp.class);
        entity.setPolicyDsps(policyDsps);
        
        Integer policyId = entity.getId();
        List<PolicyAdspaceDto> policyAdspaceDtos = dto.getPolicyAdspaces();
        if(policyAdspaces != null){
        	for(int i=0; i< policyAdspaces.size(); i++){
        		policyAdspaces.get(i).setPolicyId(policyId);
        		//转换广告售卖价格从单位元到分
        		Integer bidFloorUnitFen = StringUtils.convertCurrencyYuanToFen(policyAdspaceDtos.get(i).getBidFloor());
        		policyAdspaces.get(i).setBidFloor(bidFloorUnitFen);
        		//设置策略中广告位的日期和创建者信息
        		BeanUtils.setCommonParam(policyAdspaces.get(i), true);
        	}
        }
        
        if(policyDsps != null){
        	for(int i=0; i< policyDsps.size(); i++){
        		policyDsps.get(i).setPolicyId(policyId);
        		//设置策略中dsp的日期和创建者信息
        		BeanUtils.setCommonParam(policyDsps.get(i), true);
        	}
        }
        
        return entity;
	}

	/**
	 * 根据前端传的是否限制的标志，来决定是否存值到数据库
	 * @param dto
	 * @param entity
	 */
	private static void setFlagForStorage(PolicyDto dto, Policy entity) {
		entity.setLocationTargeting((dto.getIsLocationTargeting() != null 
        		&& dto.getIsLocationTargeting().intValue() == SystemConstant.DB.IS_LIMIT) 
				? dto.getLocationTargeting(): null);
        entity.setTimeTargeting((dto.getIsTimeTargeting() != null 
        		&& dto.getIsTimeTargeting().intValue() == SystemConstant.DB.IS_LIMIT) 
				? dto.getTimeTargeting(): null);
        if(!StringUtils.isEmpty(dto.getIsQuantityLimit()) && dto.getIsQuantityLimit().intValue() == SystemConstant.DB.IS_NOT_LIMIT){
        	entity.setLimitType((byte) 0);
		} else{
			entity.setLimitType(dto.getLimitType());
		}
        entity.setEndDate((dto.getIsEndDate() != null 
        		&& dto.getIsEndDate().intValue() == SystemConstant.DB.IS_LIMIT) 
				? dto.getEndDate(): null);
        entity.setOsTargeting((dto.getIsOsTargeting() != null 
        		&& dto.getIsOsTargeting().intValue() == SystemConstant.DB.IS_LIMIT)
        		? dto.getOsTargeting() : null);
        entity.setConnTargeting((dto.getIsConnTargeting() != null 
        		&& dto.getIsConnTargeting().intValue() == SystemConstant.DB.IS_LIMIT)
        		? dto.getConnTargeting() : null);
	}

	public static List<PolicyDto> convertToDto(Policy policy, PolicyDto policyDto) {
		
		//copy Policy
		BeanUtils.copyProperties(policy, policyDto, "policyAdspaces", "policyDsp");
		
		setFlagForDisplay(policy, policyDto);
		
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
	
	/**
	 * 根据后端数据库里的实际值，来决定这个值所对应的标志复选框如何显示
	 * @param policy
	 * @param policyDto
	 */
	private static void setFlagForDisplay(Policy policy, PolicyDto policyDto) {
		policyDto.setIsLocationTargeting(StringUtils.isEmpty(policy.getLocationTargeting()) ? 
				SystemConstant.DB.IS_NOT_LIMIT : SystemConstant.DB.IS_LIMIT);
		policyDto.setIsTimeTargeting(StringUtils.isEmpty(policy.getTimeTargeting()) ? 
				SystemConstant.DB.IS_NOT_LIMIT : SystemConstant.DB.IS_LIMIT);
		policyDto.setIsQuantityLimit((!StringUtils.isEmpty(policy.getLimitType()) && policy.getLimitType().intValue() == 0) 
				? SystemConstant.DB.IS_NOT_LIMIT : SystemConstant.DB.IS_LIMIT);
		policyDto.setIsEndDate((!StringUtils.isEmpty(policy.getEndDate()) && policy.getEndDate() == null) 
				? SystemConstant.DB.IS_NOT_LIMIT : SystemConstant.DB.IS_LIMIT);
		policyDto.setIsOsTargeting(StringUtils.isEmpty(policy.getOsTargeting()) ? 
				SystemConstant.DB.IS_NOT_LIMIT : SystemConstant.DB.IS_LIMIT);
		policyDto.setIsConnTargeting(StringUtils.isEmpty(policy.getConnTargeting()) ? 
				SystemConstant.DB.IS_NOT_LIMIT : SystemConstant.DB.IS_LIMIT);
	}

	public static void validateDto(PolicyDto policyDto){
		BaseRule.validateDto(policyDto);
        List<PolicyAdspaceDto> policyAdspaceDtos = policyDto.getPolicyAdspaces();
        if(policyAdspaceDtos == null || policyAdspaceDtos.size() == 0){
        	throw new BusinessException(StatusCode.SC20404);
        }
        
        for(int i=0; i<policyAdspaceDtos.size(); i++){
        	BaseRule.validateDto(policyAdspaceDtos.get(i));
        }
        
        List<PolicyDspDto> policyDspDtos = policyDto.getPolicyDsps();
        if(policyDspDtos == null || policyDspDtos.size() == 0){
        	throw new BusinessException(StatusCode.SC20405);
        } 

        if(policyDto.getType().intValue() != SystemConstant.OtherConstant.POLICY_TYPE_RTB){
    		//如果策略类型是pd或者pdb，则此策略的dsp数量只能是一个
    		if(policyDspDtos.size() > 1){
    			throw new BusinessException(StatusCode.SC20406);
    		}
    	}
        
        for(int i=0; i<policyDspDtos.size(); i++){
        	BaseRule.validateDto(policyDspDtos.get(i));
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
