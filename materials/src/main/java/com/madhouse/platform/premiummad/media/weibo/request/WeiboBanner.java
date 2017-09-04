package com.madhouse.platform.premiummad.media.weibo.request;

import java.util.List;

public class WeiboBanner {
	private String creative_id;// 创意ID,即我们平台的物料ID
	private String ad_url;// 广告素材URL
	private String content_category;// 行业编码
	private String landingpage_url;// 落地页
	private String client_id;// 广告主ID
	private String client_name;// 广告主名称
	private List<String> monitor_url;// 曝光监控url数组
	private List<String> clk_url;// 点击监控url数组

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

	public String getContent_category() {
		return content_category;
	}

	public void setContent_category(String content_category) {
		this.content_category = content_category;
	}

	public String getLandingpage_url() {
		return landingpage_url;
	}

	public void setLandingpage_url(String landingpage_url) {
		this.landingpage_url = landingpage_url;
	}

	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public String getClient_name() {
		return client_name;
	}

	public void setClient_name(String client_name) {
		this.client_name = client_name;
	}

	public List<String> getMonitor_url() {
		return monitor_url;
	}

	public void setMonitor_url(List<String> monitor_url) {
		this.monitor_url = monitor_url;
	}

	public List<String> getClk_url() {
		return clk_url;
	}

	public void setClk_url(List<String> clk_url) {
		this.clk_url = clk_url;
	}
}
