package com.madhouse.platform.premiummad.media.weibo.request;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class WeiboClientUploadRequest extends WeiboTokenRequest {
	private List<WeiboClient> clients;// 客户列表

	public List<WeiboClient> getClients() {
		return clients;
	}

	public void setClients(List<WeiboClient> clients) {
		this.clients = clients;
	}

}
