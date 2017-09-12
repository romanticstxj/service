package com.madhouse.platform.premiummad.media.weibo.response;

public class WeiboMaterialUploadResponse {
	private Integer ret_code;
	private Integer err_code;
	private String ret_msg;
	
	public String getRet_msg() {
		return ret_msg;
	}

	public void setRet_msg(String ret_msg) {
		this.ret_msg = ret_msg;
	}

	public Integer getRet_code() {
		return ret_code;
	}

	public void setRet_code(Integer ret_code) {
		this.ret_code = ret_code;
	}

	public Integer getErr_code() {
		return err_code;
	}

	public void setErr_code(Integer err_code) {
		this.err_code = err_code;
	}
}
