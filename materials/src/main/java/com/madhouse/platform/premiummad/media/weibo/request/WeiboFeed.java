package com.madhouse.platform.premiummad.media.weibo.request;

import java.util.List;

public class WeiboFeed {
	private String creative_id;// 创意ID,即我们平台的物料ID
	private String content_category;// 行业编码
	private String client_id;// 广告主ID
	private String client_name;// 广告主名称
	private List<String> monitor_url;// 曝光监控url数组
	private List<String> clk_url;// 点击监控url数组
	private String obj_id;// 微博mid
	private String uid;// 使用当前素材发布微博所有uid
	private String mblog_text;// 微博正文
	private List<String> pics;// 微博图片url

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

	public String getCreative_id() {
		return creative_id;
	}

	public void setCreative_id(String creative_id) {
		this.creative_id = creative_id;
	}

	public String getContent_category() {
		return content_category;
	}

	public void setContent_category(String content_category) {
		this.content_category = content_category;
	}

	public String getObj_id() {
		return obj_id;
	}

	public void setObj_id(String obj_id) {
		this.obj_id = obj_id;
	}

	public List<String> getPics() {
		return pics;
	}

	public void setPics(List<String> pics) {
		this.pics = pics;
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
