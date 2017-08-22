package com.madhouse.platform.premiummad.media.model;

public class WeiboMaterialStatusMessageDetail {
	private String creative_id;// 广告创意ID，即物料ID
	private String ad_url;// 广告url
	private String landingpage_url;// 广告跳转url
	private String status;// 审核状态
	private String reason;// 审核未通过原因

	public String getCreative_id() {
		return creative_id;
	}

	public void setCreative_id(String creative_id) {
		this.creative_id = creative_id;
	}

	public String getAd_url() {
		return ad_url;
	}

	public void setAd_url(String ad_url) {
		this.ad_url = ad_url;
	}

	public String getLandingpage_url() {
		return landingpage_url;
	}

	public void setLandingpage_url(String landingpage_url) {
		this.landingpage_url = landingpage_url;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
