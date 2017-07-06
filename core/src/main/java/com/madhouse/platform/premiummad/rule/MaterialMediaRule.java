package com.madhouse.platform.premiummad.rule;

import java.util.ArrayList;
import java.util.List;

import com.madhouse.platform.premiummad.entity.MaterialMediaUnion;
import com.madhouse.platform.premiummad.model.MaterialMediaAuditResultModel;

public class MaterialMediaRule extends BaseRule {
	public static void convert(List<MaterialMediaUnion> source, List<MaterialMediaAuditResultModel> destination) {
		if (source == null) {
			return;
		}
		
		destination = new ArrayList<MaterialMediaAuditResultModel>();
		if (source.isEmpty()) {
			return;
		}
		
		for (MaterialMediaUnion sourceItem : source) {
			MaterialMediaAuditResultModel destinationItem = new MaterialMediaAuditResultModel();
			destinationItem.setId(sourceItem.getMaterialKey()); //DSP 传过来的素材ID TODO
			destinationItem.setMediaId(String.valueOf(sourceItem.getMediaId()));
			destinationItem.setStatus(Integer.valueOf(sourceItem.getStatus()));
			destinationItem.setErrorMessage(sourceItem.getReason());
			destination.add(destinationItem);
		}
	}
}
