package com.madhouse.platform.premiummad.rule;

import java.util.ArrayList;
import java.util.List;

import com.madhouse.platform.premiummad.constant.FieldType;
import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.exception.BusinessException;
import com.madhouse.platform.premiummad.util.BeanUtils;
import com.madhouse.platform.premiummad.util.StringUtils;

public class MaterialAuditRule extends BaseRule{
	
	public static List<Material> convertToDto(Material entity) {
        List<Material> materials = new ArrayList<>();
        materials.add(entity);
        convertMaterialAuditMode(entity);
		return materials;
	}
	
	public static void convertToDtoList(List<Material> materials) {
		if(materials != null && materials.size() > 0){
			for(int i=0; i< materials.size(); i++){
				convertMaterialAuditMode(materials.get(i));
			}
		}
	}
	
	/**
	 * 后台数据库状态(-1: 未通过, 0: 待审核(若平台审核，表示平台还未审核；若媒体审核，表示未提交给媒体的状态), 1: 审核中(若媒体审核，表示已提交给媒体后的状态), 2: 通过审核)
	          前端状态(-1: 未通过, 0: 待提交(若媒体审核，表示还未提交给媒体), 1: 待审核(若平台审核，表示平台还未审核；若媒体审核，表示已提交给媒体并有待于他们审核的状态), 2: 通过审核)
	 * @param entity
	 */
	private static void convertMaterialAuditMode(Material entity) {
		if(entity != null){
			String impUrls = entity.getImpUrls();
			String formalizedImpUrls = StringUtils.formalizeUrls(impUrls);
			entity.setImpUrls(formalizedImpUrls);
		}
	}
	
	/**
	 * 验证dto中非空字段，对于非空字段，则把Null转成数据库的默认值
	 * @param dto
	 */
	public static void validateDto(Object dto){
		String fieldName = BeanUtils.processEmptyField(dto);
        if (fieldName != null)
        	throw new BusinessException(StatusCode.SC20002, FieldType.getChineseMessage(fieldName) + "不能为空");
	}
}
