package com.madhouse.platform.premiummad.media.tencent.response;

import java.util.List;

/**
 * 广告信息上传
 */
public class TencentUploadMaterialResponse extends TencentResponse {

	private List<TencentUploadMaterialReturnMessage> ret_msg;

	public List<TencentUploadMaterialReturnMessage> getRet_msg() {
		return ret_msg;
	}

	public void setRet_msg(List<TencentUploadMaterialReturnMessage> ret_msg) {
		this.ret_msg = ret_msg;
	}
}
