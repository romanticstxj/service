package com.madhouse.platform.premiummad.media.model;

import java.util.List;

public class FunadxUploadRequest extends FunadxTokenRequest {
	private List<FunadxMaterialRequest> material;

	public List<FunadxMaterialRequest> getMaterial() {
		return material;
	}

	public void setMaterial(List<FunadxMaterialRequest> material) {
		this.material = material;
	}

}
