package com.madhouse.platform.premiummad.media.model;

import java.util.List;

public class ToutiaoMaterialUploadRequest {
	private String adid;// dsp 方的广告id
	private int height;// 素材的高
	private int width;// 素材的宽
	private String img_url;// 素材的地址
	private String click_through_url;// 素材的落地页
	private String title;// 素材的标题
	private String source;// 素材的来源
	private int is_inapp;
	private int ad_type;
	private String app_type;// 应用下载素材才有，取值为'ios' or'android
	private String nurl;// 获胜的url
	private List<String> click_url;// 点击监测url， 可以为多个， 必须数组表示
	private List<String> show_url;// 展示监测url， 可以为多个， 必须数组表示

	public String getAdid() {
		return adid;
	}

	public void setAdid(String adid) {
		this.adid = adid;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getImg_url() {
		return img_url;
	}

	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}

	public String getClick_through_url() {
		return click_through_url;
	}

	public void setClick_through_url(String click_through_url) {
		this.click_through_url = click_through_url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public int getIs_inapp() {
		return is_inapp;
	}

	public void setIs_inapp(int is_inapp) {
		this.is_inapp = is_inapp;
	}

	public int getAd_type() {
		return ad_type;
	}

	public void setAd_type(int ad_type) {
		this.ad_type = ad_type;
	}

	public String getApp_type() {
		return app_type;
	}

	public void setApp_type(String app_type) {
		this.app_type = app_type;
	}

	public String getNurl() {
		return nurl;
	}

	public void setNurl(String nurl) {
		this.nurl = nurl;
	}

	public List<String> getClick_url() {
		return click_url;
	}

	public void setClick_url(List<String> click_url) {
		this.click_url = click_url;
	}

	public List<String> getShow_url() {
		return show_url;
	}

	public void setShow_url(List<String> show_url) {
		this.show_url = show_url;
	}

}
