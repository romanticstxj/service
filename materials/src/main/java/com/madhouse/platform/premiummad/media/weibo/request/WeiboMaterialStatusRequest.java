package com.madhouse.platform.premiummad.media.weibo.request;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class WeiboMaterialStatusRequest extends WeiboTokenRequest {
	private List<String> creative_ids;

	public List<String> getCreative_ids() {
		return creative_ids;
	}

	public void setCreative_ids(List<String> creative_ids) {
		this.creative_ids = creative_ids;
	}

}
