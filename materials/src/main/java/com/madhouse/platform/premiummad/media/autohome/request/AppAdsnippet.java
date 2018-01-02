package com.madhouse.platform.premiummad.media.autohome.request;

import java.util.List;
import java.util.Map;

public class AppAdsnippet {
	// 图片内容
	private List<Map<String, String>> content;

	// 曝光监控地址
	private List<String> pv;

	// 点击地址（302跳转）
	private String link;

	public List<Map<String, String>> getContent() {
		return content;
	}

	public void setContent(List<Map<String, String>> content) {
		this.content = content;
	}

	public List<String> getPv() {
		return pv;
	}

	public void setPv(List<String> pv) {
		this.pv = pv;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

}
