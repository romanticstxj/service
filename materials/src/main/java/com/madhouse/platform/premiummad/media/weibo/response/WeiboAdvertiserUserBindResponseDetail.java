package com.madhouse.platform.premiummad.media.weibo.response;

public class WeiboAdvertiserUserBindResponseDetail {
	private Integer err_code;// 参考错误代码
	private String client_id;// 出错的广告主ID

	public Integer getErr_code() {
		return err_code;
	}

	public void setErr_code(Integer err_code) {
		this.err_code = err_code;
	}

	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}
}
