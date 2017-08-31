package com.madhouse.platform.premiummad.media.tencent.response;

import java.util.List;

public class TencentMaterailStatusRetMsg {

	private String dsp_order_id;
	private int advertiser_id;
	private String advertiser_name;
	private int display_id;
	private String targeting_url;
	private String landing_page;
	private List<String> monitor_url;
	private List<String> click_monitor_url;
	private int status;
	private String vinfo;
	private String ad_content;
	private String ad_ext;

	public void setDsp_order_id(String dsp_order_id) {
		this.dsp_order_id = dsp_order_id;
	}

	public String getDsp_order_id() {
		return dsp_order_id;
	}

	public void setAdvertiser_id(int advertiser_id) {
		this.advertiser_id = advertiser_id;
	}

	public int getAdvertiser_id() {
		return advertiser_id;
	}

	public void setAdvertiser_name(String advertiser_name) {
		this.advertiser_name = advertiser_name;
	}

	public String getAdvertiser_name() {
		return advertiser_name;
	}

	public void setDisplay_id(int display_id) {
		this.display_id = display_id;
	}

	public int getDisplay_id() {
		return display_id;
	}

	public void setTargeting_url(String targeting_url) {
		this.targeting_url = targeting_url;
	}

	public String getTargeting_url() {
		return targeting_url;
	}

	public void setLanding_page(String landing_page) {
		this.landing_page = landing_page;
	}

	public String getLanding_page() {
		return landing_page;
	}

	public void setMonitor_url(List<String> monitor_url) {
		this.monitor_url = monitor_url;
	}

	public List<String> getMonitor_url() {
		return monitor_url;
	}

	public void setClick_monitor_url(List<String> click_monitor_url) {
		this.click_monitor_url = click_monitor_url;
	}

	public List<String> getClick_monitor_url() {
		return click_monitor_url;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public void setVinfo(String vinfo) {
		this.vinfo = vinfo;
	}

	public String getVinfo() {
		return vinfo;
	}

	public String getAd_content() {
		return ad_content;
	}

	public void setAd_content(String ad_content) {
		this.ad_content = ad_content;
	}

	public String getAd_ext() {
		return ad_ext;
	}

	public void setAd_ext(String ad_ext) {
		this.ad_ext = ad_ext;
	}
}