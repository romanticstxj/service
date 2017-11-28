package com.madhouse.platform.premiummad.rule;

import java.util.ArrayList;
import java.util.List;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dto.AdspaceDto;
import com.madhouse.platform.premiummad.dto.PolicyAdspaceDto;
import com.madhouse.platform.premiummad.dto.PolicyDto;
import com.madhouse.platform.premiummad.entity.Adspace;
import com.madhouse.platform.premiummad.exception.BusinessException;
import com.madhouse.platform.premiummad.util.BeanUtils;
import com.madhouse.platform.premiummad.util.StringUtils;

public class AdspaceRule extends BaseRule{
	
	public static void validateDto(Object dto){
		BaseRule.validateDto(dto);
		if(dto instanceof AdspaceDto){
			AdspaceDto adspaceDto = (AdspaceDto) dto;
			if(!allConformed(adspaceDto.getLogoType(), adspaceDto.getLogoSize(), adspaceDto.getLogoMaxKbyte())){
				throw new BusinessException(StatusCode.SC20208);
			}
			
			//只判断封面类型和封面最大k数，因为封面尺寸总是由其主素材尺寸确定
			if(!allConformed(adspaceDto.getCoverType(), adspaceDto.getCoverMaxKbyte())){
				throw new BusinessException(StatusCode.SC20209);
			}
		}
	}
	
	/**
	 * 判断所有的对象，要么都非空，要么都空
	 * @param objs
	 * @return
	 */
	private static boolean allConformed(Object... objs){
		boolean allPopulated = true;
		boolean allNotPopulated = true;
		for(Object obj : objs){
			allPopulated = allPopulated && !StringUtils.isEmpty(obj);
			allNotPopulated = allNotPopulated && StringUtils.isEmpty(obj);
		}
		
		return allPopulated || allNotPopulated;
	}

	public static Adspace convertToModel(AdspaceDto adspaceDto, Adspace adspace, boolean isCreate){
		BeanUtils.copyProperties(adspaceDto, adspace, SystemConstant.OtherConstant.ADSPACE_BID_FLOOR);
        BeanUtils.setCommonParam(adspace, isCreate);
        
        //把页面上的底价（元）转换成数据库需要的底价（分）
        Integer bidFloorUnitFen = StringUtils.convertCurrencyYuanToFen(adspaceDto.getBidFloor());
        adspace.setBidFloor(bidFloorUnitFen);
        
        //把页面上的部分多选项转化为数据库的单值
        int materialType = StringUtils.convertMultiChoiceToSingleChoice(adspaceDto.getMaterialType());
        int logoType = StringUtils.convertMultiChoiceToSingleChoice(adspaceDto.getLogoType());
        int coverType = StringUtils.convertMultiChoiceToSingleChoice(adspaceDto.getCoverType());
        adspace.setMaterialType(materialType);
        adspace.setLogoType(logoType);
        adspace.setCoverType(coverType);
		
		return adspace;
	}
	
	public static List<AdspaceDto> convertToDto(Adspace adspace, AdspaceDto adspaceDto) {
        BeanUtils.copyProperties(adspace,adspaceDto);
        List<AdspaceDto> adspaceDtos = new ArrayList<>();
        adspaceDtos.add(adspaceDto);
        
        convertToDtoSpecifically(adspace, adspaceDto);
		return adspaceDtos;
	}
	
	public static List<PolicyAdspaceDto> convertToDtoOnlyWithPolicies(Adspace adspace, List<PolicyAdspaceDto> adspacePolicyDtos) {
    	//复制广告位里的广告位策略关联信息
    	BeanUtils.copyList(adspace.getAdspacePolicies(), adspacePolicyDtos, PolicyAdspaceDto.class);
    	if(adspacePolicyDtos != null){
    		for(int i=0; i<adspacePolicyDtos.size(); i++){
				//设置广告位关联的策略列表里的每个具体策略的信息
				PolicyDto policyDto = new PolicyDto();
				BeanUtils.copyProperties(adspace.getAdspacePolicies().get(i).getPolicy(), policyDto);
				adspacePolicyDtos.get(i).setPolicy(policyDto);
			}
    	}
        
		return adspacePolicyDtos;
	}
	
	public static List<AdspaceDto> convertToDtoList(List<Adspace> entities, ArrayList<AdspaceDto> dtos) {
        //copy entity to dto
		BeanUtils.copyList(entities,dtos,AdspaceDto.class);
        for(int i=0; i<dtos.size(); i++){
        	convertToDtoSpecifically(entities.get(i), dtos.get(i));
        }
        
        return dtos;
	}
	
	private static void convertToDtoSpecifically(Adspace adspace, AdspaceDto adspaceDto) {
		Double bidFloor = StringUtils.convertCurrencyFentoYuan(adspace.getBidFloor());
        adspaceDto.setBidFloor(bidFloor);
        
        String materialType = StringUtils.convertSingleChoiceToMultiChoice(adspace.getMaterialType());
        String logoType = StringUtils.convertSingleChoiceToMultiChoice(adspace.getLogoType());
        String coverType = StringUtils.convertSingleChoiceToMultiChoice(adspace.getCoverType());
        adspaceDto.setMaterialType(materialType);
        adspaceDto.setLogoType(logoType);
        adspaceDto.setCoverType(coverType);
	}

}
