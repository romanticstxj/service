package com.madhouse.platform.premiummad.media.model;

import java.util.List;

public class ValueMakerMaterialUploadRequest {
	private String id;// dsp系统中的id
	private String landingpage;// 落地页地址，包括宏
	private int width;
	private int height;
	private int adtype;// 1-Banner广告，2-开屏广告，3-插屏广告，4-信息流广告
	private int format;// 展示格式：1-STATIC_PIC,2-DYNAMIC_PIC,4-TXT
	private int category;// 行业id
	private List<String> adomain_list;// 点击跳转目标地址主域名
	private List<String> pic_urls;// 素材链接列表
	private String title;// 标题
	private String text;// 文字

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLandingpage() {
		return landingpage;
	}

	public void setLandingpage(String landingpage) {
		this.landingpage = landingpage;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getAdtype() {
		return adtype;
	}

	public void setAdtype(int adtype) {
		this.adtype = adtype;
	}

	public int getFormat() {
		return format;
	}

	public void setFormat(int format) {
		this.format = format;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public List<String> getAdomain_list() {
		return adomain_list;
	}

	public void setAdomain_list(List<String> adomain_list) {
		this.adomain_list = adomain_list;
	}

	public List<String> getPic_urls() {
		return pic_urls;
	}

	public void setPic_urls(List<String> pic_urls) {
		this.pic_urls = pic_urls;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
