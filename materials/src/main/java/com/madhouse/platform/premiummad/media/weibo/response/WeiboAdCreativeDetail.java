package com.madhouse.platform.premiummad.media.weibo.response;

public class WeiboAdCreativeDetail {
	private String creative_id;// 广告创意ID，即物料ID
	private String obj_id;// 创意对应的对象id(mid)
	private String err_msg;// 错误信息

	public String getCreative_id() {
		return creative_id;
	}

	public void setCreative_id(String creative_id) {
		this.creative_id = creative_id;
	}

	public String getObj_id() {
		return obj_id;
	}

	public void setObj_id(String obj_id) {
		this.obj_id = obj_id;
	}

	public String getErr_msg() {
		return err_msg;
	}

	public void setErr_msg(String err_msg) {
		this.err_msg = err_msg;
	}
}
