package com.madhouse.platform.premiummad.rule;

import java.util.ArrayList;
import java.util.List;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dto.AdspaceDto;
import com.madhouse.platform.premiummad.entity.Adspace;
import com.madhouse.platform.premiummad.exception.BusinessException;
import com.madhouse.platform.premiummad.util.BeanUtils;
import com.madhouse.platform.premiummad.util.StringUtils;

public class AdspaceRule extends BaseRule{

	public static Adspace convertToModel(AdspaceDto adspaceDto, Adspace adspace){
		BeanUtils.copyProperties(adspaceDto, adspace, SystemConstant.OtherConstant.ADSPACE_BID_FLOOR);
        BeanUtils.setCreateParam(adspace);
        
        //把页面上的底价（元）转换成数据库需要的底价（分）
        Integer bidFloorUnitFen = StringUtils.convertCurrencyYuanToFen(adspaceDto.getBidFloor());
        adspace.setBidFloor(bidFloorUnitFen);
        
        //把页面上的部分多选项转化为数据库的单值
        int materialType = StringUtils.convertMultiChoiceToSingleChoice(adspaceDto.getMaterialType());
        int logoType = StringUtils.convertMultiChoiceToSingleChoice(adspaceDto.getLogoType());
        int videoType = StringUtils.convertMultiChoiceToSingleChoice(adspaceDto.getVideoType());
        adspace.setMaterialType(materialType);
        adspace.setLogoType(logoType);
        adspace.setVideoType(videoType);
		
		return adspace;
	}
	
	public static List<AdspaceDto> convertToDto(Adspace adspace, AdspaceDto adspaceDto) {
		
        BeanUtils.copyProperties(adspace,adspaceDto,"bidFloor");
        List<AdspaceDto> adspaceDtos = new ArrayList<>();
        adspaceDtos.add(adspaceDto);
        
        Double bidFloor = StringUtils.convertCurrencyFentoYuan(adspace.getBidFloor());
        adspaceDto.setBidFloor(bidFloor);
        
        String materialType = StringUtils.convertSingleChoiceToMultiChoice(adspace.getMaterialType());
        String logoType = StringUtils.convertSingleChoiceToMultiChoice(adspace.getLogoType());
        String videoType = StringUtils.convertSingleChoiceToMultiChoice(adspace.getVideoType());
        adspaceDto.setMaterialType(materialType);
        adspaceDto.setLogoType(logoType);
        adspaceDto.setVideoType(videoType);
        
		return adspaceDtos;
	}
	
	public static void validateDto(AdspaceDto adspaceDto){
		String fieldName = BeanUtils.hasEmptyField(adspaceDto);
        if (fieldName != null)
        	throw new BusinessException(StatusCode.SC20001, fieldName + " cannot be null");
	}
}
