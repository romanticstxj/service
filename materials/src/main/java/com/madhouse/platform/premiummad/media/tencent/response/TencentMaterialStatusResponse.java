package com.madhouse.platform.premiummad.media.tencent.response;

import java.util.List;

public class TencentMaterialStatusResponse extends TencentResponse {

	private List<TencentMaterailStatusRetMsg> ret_msg;

	public List<TencentMaterailStatusRetMsg> getRet_msg() {
		return ret_msg;
	}

	public void setRet_msg(List<TencentMaterailStatusRetMsg> ret_msg) {
		this.ret_msg = ret_msg;
	}
}
