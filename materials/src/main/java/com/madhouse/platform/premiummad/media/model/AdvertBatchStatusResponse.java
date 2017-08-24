package com.madhouse.platform.premiummad.media.model;

import java.util.List;

public class AdvertBatchStatusResponse extends TencentResponse {

	private List<RetMsg> ret_msg;

	public List<RetMsg> getRet_msg() {
		return ret_msg;
	}

	public void setRet_msg(List<RetMsg> ret_msg) {
		this.ret_msg = ret_msg;
	}
}
