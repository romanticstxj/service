package com.madhouse.platform.premiummad.media.model;

public class WeiboMaterialUploadResponseDetail {
	private Integer err_code;// 参考错误代码
	private String ad_url;// 出错的广告URL

	public Integer getErr_code() {
		return err_code;
	}

	public void setErr_code(Integer err_code) {
		this.err_code = err_code;
	}

	public String getAd_url() {
		return ad_url;
	}

	public void setAd_url(String ad_url) {
		this.ad_url = ad_url;
	}
}
