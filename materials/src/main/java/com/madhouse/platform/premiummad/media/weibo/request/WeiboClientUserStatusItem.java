package com.madhouse.platform.premiummad.media.weibo.request;

public class WeiboClientUserStatusItem {
	// 审核通过的广告主ID required
	private String client_id;

	// 发布博文所用的uid
	private String uid;

	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
}
