package com.madhouse.platform.premiummad.media.model;

public class WeiboMaterialStatusResponse {
	private Integer ret_code;// 返回代码
	private Integer err_code;// 参考错误代码
	private WeiboMaterialStatusMessage ret_msg;

	public WeiboMaterialStatusMessage getRet_msg() {
		return ret_msg;
	}

	public void setRet_msg(WeiboMaterialStatusMessage ret_msg) {
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
