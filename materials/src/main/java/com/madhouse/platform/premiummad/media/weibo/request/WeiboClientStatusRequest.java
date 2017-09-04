package com.madhouse.platform.premiummad.media.weibo.request;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class WeiboClientStatusRequest extends WeiboTokenRequest {
	private List<String> client_ids;

	public List<String> getClient_ids() {
		return client_ids;
	}

	public void setClient_ids(List<String> client_ids) {
		this.client_ids = client_ids;
	}
}
