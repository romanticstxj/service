package com.madhouse.platform.premiummad.rule;

import java.util.ArrayList;
import java.util.List;

import com.madhouse.platform.premiummad.constant.FieldType;
import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.entity.Advertiser;
import com.madhouse.platform.premiummad.exception.BusinessException;
import com.madhouse.platform.premiummad.util.BeanUtils;

public class AdvertiserAuditRule extends BaseRule{
	
	public static List<Advertiser> convertToDto(Advertiser advertiser) {
        List<Advertiser> advertisers = new ArrayList<Advertiser>();
        advertisers.add(advertiser);
        if(advertiser != null){
        	convertAuditMode(advertiser);
        }
		return advertisers;
	}
	
	public static void convertToDtoList(List<Advertiser> advertisers) {
		if(advertisers != null && advertisers.size() > 0){
			for(int i=0; i< advertisers.size(); i++){
				convertAuditMode(advertisers.get(i));
			}
		}
	}
	
	private static void convertAuditMode(Advertiser entity) {
		//left blank
	}
	
	/**
	 * 验证dto中非空字段，对于非空字段，则把Null转成数据库的默认值
	 * @param dto
	 */
	public static void validateDto(Object dto){
		String fieldName = BeanUtils.hasEmptyField1(dto);
        if (fieldName != null)
        	throw new BusinessException(StatusCode.SC20002, FieldType.getChineseMessage(fieldName) + "不能为空");
	}
}
