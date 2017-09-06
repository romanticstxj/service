package com.madhouse.platform.premiummad.rule;

import java.util.ArrayList;
import java.util.List;

import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.entity.Material;
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
//		int materialAuditMode = entity.getMaterialAuditMode();
//        //根据不同的审核方式，来设置返回给前端的审核状态
//        switch(materialAuditMode){
//	        case SystemConstant.DB.NO_AUDIT:
//	        	//若无需审核，则始终返回审核通过给前端
//	        	entity.setStatus((byte)SystemConstant.DB.AUDIT_PASS);
//	        	break;
//	        case SystemConstant.DB.AUDIT_BY_SSP:
//	        	//若由平台审核，则后端的待审核与前端的待审核一致
//	        	if(entity.getStatus().intValue() == SystemConstant.DB.TO_BE_AUDIT_BE){
//	        		entity.setStatus((byte) SystemConstant.DB.TO_BE_AUDIT_FE);
//	        	}
//	        	break;
//	        case SystemConstant.DB.AUDIT_BY_MEDIA:
//	        	break;
//	        default:
//        }
		
		String impUrls = entity.getImpUrls();
		String formalizedImpUrls = StringUtils.formalizeUrls(impUrls);
		entity.setImpUrls(formalizedImpUrls);
	}
}
