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

	public static void validateDto(AdspaceDto adspaceDto){
		String fieldName = BeanUtils.hasEmptyField(adspaceDto);
        if (fieldName != null)
        	throw new BusinessException(StatusCode.SC20002, fieldName + " cannot be null");
	}
}
