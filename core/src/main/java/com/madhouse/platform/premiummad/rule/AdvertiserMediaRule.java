package com.madhouse.platform.premiummad.rule;

import java.util.ArrayList;
import java.util.List;

import com.madhouse.platform.premiummad.entity.Advertiser;
import com.madhouse.platform.premiummad.entity.AdvertiserMedia;
import com.madhouse.platform.premiummad.entity.AdvertiserMediaUnion;
import com.madhouse.platform.premiummad.model.AdvertiserMediaAuditResultModel;
import com.madhouse.platform.premiummad.model.AdvertiserMediaModel;

public class AdvertiserMediaRule extends BaseRule {
	public static void convert(List<AdvertiserMediaUnion> source, List<AdvertiserMediaAuditResultModel> destination) {
		if (source == null) {
			return;
		}
		
		destination = new ArrayList<AdvertiserMediaAuditResultModel>();
		if (source.isEmpty()) {
			return;
		}
		
		for (AdvertiserMediaUnion sourceItem : source) {
			AdvertiserMediaAuditResultModel destinationItem = new AdvertiserMediaAuditResultModel();
			destinationItem.setId(sourceItem.getAdvertiserKey()); //DSP 传过来的广告主ID TODO
			destinationItem.setMediaId(String.valueOf(sourceItem.getMediaId()));
			destinationItem.setStatus(Integer.valueOf(sourceItem.getStatus()));
			destinationItem.setErrorMessage(sourceItem.getReason());
			destination.add(destinationItem);
		}
	}
	
	public static void buildEnties(AdvertiserMediaModel source, Advertiser advertiser, AdvertiserMedia advertiserMedia) {
		
	}
}
