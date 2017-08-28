package com.madhouse.platform.premiummad.media.weibo.response;

public class WeiboMaterialUploadResponseDetail {
	private Integer err_code;// 参考错误代码
	private String creative_id;// 出错的素材ID

	public Integer getErr_code() {
		return err_code;
	}

	public void setErr_code(Integer err_code) {
		this.err_code = err_code;
	}

	public String getCreative_id() {
		return creative_id;
	}

	public void setCreative_id(String creative_id) {
		this.creative_id = creative_id;
	}
}
