package com.madhouse.platform.premiummad.media.tencent.response;

import java.util.List;

/**
 * 广告信息上传
 */
public class AdvertUploadResponse extends TencentResponse {

	private List<AdvertUploadReturnMessage> ret_msg;

	public List<AdvertUploadReturnMessage> getRet_msg() {
		return ret_msg;
	}

	public void setRet_msg(List<AdvertUploadReturnMessage> ret_msg) {
		this.ret_msg = ret_msg;
	}
}
