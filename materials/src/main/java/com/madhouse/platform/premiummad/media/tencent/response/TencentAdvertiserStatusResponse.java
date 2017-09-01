package com.madhouse.platform.premiummad.media.tencent.response;

import java.util.List;

public class TencentAdvertiserStatusResponse extends TencentResponse {

	private List<TencentAdvertiserStatusData> ret_msg;

	public List<TencentAdvertiserStatusData> getRet_msg() {
		return ret_msg;
	}

	public void setRet_msg(List<TencentAdvertiserStatusData> ret_msg) {
		this.ret_msg = ret_msg;
	}
}
