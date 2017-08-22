package com.madhouse.platform.premiummad.media.model;

import org.springframework.stereotype.Component;

@Component
public class WeiboMaterialUploadRequest extends WeiboTokenRequest {
	private WeiboAdInfo ad_info;

	public WeiboAdInfo getAd_info() {
		return ad_info;
	}

	public void setAd_info(WeiboAdInfo ad_info) {
		this.ad_info = ad_info;
	}
}
