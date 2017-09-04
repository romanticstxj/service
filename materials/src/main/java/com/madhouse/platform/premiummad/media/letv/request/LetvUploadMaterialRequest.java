package com.madhouse.platform.premiummad.media.letv.request;

import java.util.List;

public class LetvUploadMaterialRequest extends LetvTokenRequest {

	private List<LetvUploadMaterialDetailRequest> ad;

	public List<LetvUploadMaterialDetailRequest> getAd() {
		return ad;
	}

	public void setAd(List<LetvUploadMaterialDetailRequest> ad) {
		this.ad = ad;
	}
}
