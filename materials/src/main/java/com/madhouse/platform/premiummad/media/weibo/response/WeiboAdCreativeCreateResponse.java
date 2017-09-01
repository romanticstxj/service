package com.madhouse.platform.premiummad.media.weibo.response;

import java.util.List;

public class WeiboAdCreativeCreateResponse {
	private Integer ret_code; // 返回代码
	private Integer err_code; // 参考错误代码
	private List<WeiboAdCreativeDetail> ret_msg; // 结果信息

	public Integer getRet_code() {
		return ret_code;
	}

	public void setRet_code(Integer ret_code) {
		this.ret_code = ret_code;
	}

	public List<WeiboAdCreativeDetail> getRet_msg() {
		return ret_msg;
	}

	public void setRet_msg(List<WeiboAdCreativeDetail> ret_msg) {
		this.ret_msg = ret_msg;
	}

	public Integer getErr_code() {
		return err_code;
	}

	public void setErr_code(Integer err_code) {
		this.err_code = err_code;
	}
}
