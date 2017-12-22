package com.madhouse.platform.premiummad.media.weibo.request;

import java.util.List;

public class WeiboClientUserStatusRequest extends WeiboTokenRequest {
	// 关联账号信息数组
	private List<WeiboClientUserStatusItem> ids;

	public List<WeiboClientUserStatusItem> getIds() {
		return ids;
	}

	public void setIds(List<WeiboClientUserStatusItem> ids) {
		this.ids = ids;
	}
}
