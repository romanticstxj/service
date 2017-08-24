package com.madhouse.platform.premiummad.rule;

import java.util.ArrayList;
import java.util.List;

import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.entity.Advertiser;

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
		Integer advertiserAuditMode = entity.getAdvertiserAuditMode();
        //根据不同的审核方式，来设置返回给前端的审核状态
        switch(advertiserAuditMode){
	        case SystemConstant.DB.NO_AUDIT:
	        	//若无需审核，则始终返回审核通过给前端
	        	entity.setStatus((byte)SystemConstant.DB.AUDIT_PASS);
	        	break;
	        case SystemConstant.DB.AUDIT_BY_SSP:
	        	//若由平台审核，则后端的待审核和前端要显示的待审核意义相同，所以直接返回
	        	break;
	        case SystemConstant.DB.AUDIT_BY_MEDIA:
	        	//若媒体审核，则媒体审核前待审核即为前端的待提交;媒体审核后的审核中即为前端的待审核;其余不变
//	        	if(entity.getStatus().intValue() == SystemConstant.DB.TO_BE_AUDIT){
//	        		entity.setStatus((byte) SystemConstant.DB.TO_BE_SUBMIT);
//	        	} else if(entity.getStatus().intValue() == SystemConstant.DB.IN_AUDIT){
//	        		entity.setStatus((byte) SystemConstant.DB.TO_BE_AUDIT);
//	        	}
	        	break;
	        default:
        }
	}
}
