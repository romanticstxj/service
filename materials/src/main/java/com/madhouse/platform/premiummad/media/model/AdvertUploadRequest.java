package com.madhouse.platform.premiummad.media.model;

import java.util.List;
import java.util.Map;

/**
 * 广告位上传请求,对应的字段名称order_info,对应的是json格式
 */
public class AdvertUploadRequest {

	private String dsp_order_id;
	private String client_name;
	private String targeting_url;
	private List<String> monitor_url;// 展示监播
	private List<String> click_monitor_url;// 点击监播
	private int display_id;// 广告形式
	private String landing_page;// 静态落地页(供审核查看) 腾讯用
	private List<Map<String, String>> ad_content; // 创意素材内容

	public List<String> getClick_monitor_url() {
		return click_monitor_url;
	}

	public void setClick_monitor_url(List<String> click_monitor_url) {
		this.click_monitor_url = click_monitor_url;
	}

	public String getDsp_order_id() {
		return dsp_order_id;
	}

	public void setDsp_order_id(String dsp_order_id) {
		this.dsp_order_id = dsp_order_id;
	}

	public String getClient_name() {
		return client_name;
	}

	public void setClient_name(String client_name) {
		this.client_name = client_name;
	}

	public String getTargeting_url() {
		return targeting_url;
	}

	public void setTargeting_url(String targeting_url) {
		this.targeting_url = targeting_url;
	}

	public List<String> getMonitor_url() {
		return monitor_url;
	}

	public void setMonitor_url(List<String> monitor_url) {
		this.monitor_url = monitor_url;
	}

	public int getDisplay_id() {
		return display_id;
	}

	public void setDisplay_id(int display_id) {
		this.display_id = display_id;
	}

	public String getLanding_page() {
		return landing_page;
	}

	public void setLanding_page(String landing_page) {
		this.landing_page = landing_page;
	}

	public List<Map<String, String>> getAd_content() {
		return ad_content;
	}

	public void setAd_content(List<Map<String, String>> ad_content) {
		this.ad_content = ad_content;
	}
}
