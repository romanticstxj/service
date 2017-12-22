package com.madhouse.platform.premiummad.media.weibo.request;

import java.util.List;

public class WeiboClientUserBindRequest extends WeiboTokenRequest {
	// 同步关联账号信息数组
	private List<WeiboClientUserBindItem> client_user;

	public List<WeiboClientUserBindItem> getClient_user() {
		return client_user;
	}

	public void setClient_user(List<WeiboClientUserBindItem> client_user) {
		this.client_user = client_user;
	}
}
