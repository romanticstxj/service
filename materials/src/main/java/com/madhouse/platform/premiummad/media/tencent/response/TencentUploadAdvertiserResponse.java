package com.madhouse.platform.premiummad.media.tencent.response;

import java.util.List;

public class TencentUploadAdvertiserResponse extends TencentResponse {
	/**
	 * 返回内容
	 */
	private List<TencentUploadAdvertiserRetMsg> ret_msg;

	public List<TencentUploadAdvertiserRetMsg> getRet_msg() {
		return ret_msg;
	}

	public void setRet_msg(List<TencentUploadAdvertiserRetMsg> ret_msg) {
		this.ret_msg = ret_msg;
	}

}
