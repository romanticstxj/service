package com.madhouse.platform.premiummad.media.weibo.request;

import java.util.List;

public class WeiboFeedGrid {
	private String creative_id;
	private String content_category;
	private String client_id;
	private String client_name;
	private List<String> monitor_ur;
	private List<String> click_url;
	private String uid;
	private String mblog_text;
	private WeiboGrid grid;

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

	public List<String> getMonitor_ur() {
		return monitor_ur;
	}

	public void setMonitor_ur(List<String> monitor_ur) {
		this.monitor_ur = monitor_ur;
	}

	public List<String> getClick_url() {
		return click_url;
	}

	public void setClick_url(List<String> click_url) {
		this.click_url = click_url;
	}

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

	public WeiboGrid getGrid() {
		return grid;
	}

	public void setGrid(WeiboGrid grid) {
		this.grid = grid;
	}
}
