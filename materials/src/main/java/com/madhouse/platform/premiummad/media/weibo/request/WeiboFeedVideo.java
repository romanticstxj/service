package com.madhouse.platform.premiummad.media.weibo.request;

import java.util.List;

public class WeiboFeedVideo {
	private String creative_id;// 创意ID,即我们平台的物料ID
	private String ad_url;// 封面URL
	private String video_url;// 视频URL
	private String content_category;// 行业编码
	private String landingpage_url;// 落地页
	private String client_id;// 广告主ID
	private String client_name;// 广告主名称
	private List<String> monitor_url;// 曝光监控url数组
	private List<String> clk_url;// 点击监控url数组
	private String uid;// 使用当前素材发布微博所有uid
	private String mblog_text;// 微博正文
	private String title;// 微博内视频显示标题
	private String desc;// 微博内视频描述
	private String button_type;// 微博视频card按钮类型
	private String button_url;// card按钮跳转地址

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getMblog_text() {
		return mblog_text;
	}

	public void setMblog_text(String mblog_text) {
		this.mblog_text = mblog_text;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getButton_type() {
		return button_type;
	}

	public void setButton_type(String button_type) {
		this.button_type = button_type;
	}

	public String getButton_url() {
		return button_url;
	}

	public void setButton_url(String button_url) {
		this.button_url = button_url;
	}

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

	public String getVideo_url() {
		return video_url;
	}

	public void setVideo_url(String video_url) {
		this.video_url = video_url;
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
