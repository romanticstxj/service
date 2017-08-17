package com.madhouse.platform.premiummad.rule;

import java.util.ArrayList;
import java.util.List;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dto.MaterialDto;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.exception.BusinessException;
import com.madhouse.platform.premiummad.util.BeanUtils;

public class MaterialAuditRule extends BaseRule{
	
	public static void validateDto(MaterialDto dto){
		String fieldName = BeanUtils.hasEmptyField(dto);
        if (fieldName != null)
        	throw new BusinessException(StatusCode.SC20002, fieldName + " cannot be null");
	}
	
	public static List<Material> convertToDto(Material entity) {
        List<Material> materials = new ArrayList<>();
        materials.add(entity);
        byte status = entity.getStatus();
        int materialAuditMode = entity.getMaterialAuditMode();
        //根据不同的审核方式，来设置返回给前端的审核状态
        switch(materialAuditMode){
	        case SystemConstant.DB.NO_AUDIT:
	        	//若无需审核，则始终返回审核通过给前端
	        	entity.setStatus((byte)SystemConstant.DB.AUDIT_PASS);
	        	break;
	        case SystemConstant.DB.AUDIT_BY_SSP:
	        	//若由平台审核
	        	break;
	        case SystemConstant.DB.AUDIT_BY_MEDIA:
	        	
	        	break;
	        default:
        	
        }
        
		return materials;
	}
}
