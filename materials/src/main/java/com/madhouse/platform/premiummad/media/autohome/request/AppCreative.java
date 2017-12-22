package com.madhouse.platform.premiummad.media.autohome.request;

public class AppCreative {
	/**
	 * 所属广告主ID
	 */
	private int advertiserId;

	/**
	 * 所属广告主名称
	 */
	private String advertiserName;

	/**
	 * 所属行业ID
	 */
	private int industryId;

	/**
	 * 所属行业名称
	 */
	private String industryName;

	/**
	 * 素材类型
	 */
	private int creativeTypeId;

	/**
	 * 广告位宽
	 */
	private int width;

	/**
	 * 广告位高
	 */
	private int height;

	/**
	 * 模板ID
	 */
	private int templateId;

	/**
	 * 去重码
	 */
	private String repeatedCode;

	/**
	 * 广告内容
	 */
	private AppAdsnippet adsnippet;

	/**
	 * PC/M-1, App=2
	 */
	private int platform;

	public int getAdvertiserId() {
		return advertiserId;
	}

	public void setAdvertiserId(int advertiserId) {
		this.advertiserId = advertiserId;
	}

	public String getAdvertiserName() {
		return advertiserName;
	}

	public void setAdvertiserName(String advertiserName) {
		this.advertiserName = advertiserName;
	}

	public int getIndustryId() {
		return industryId;
	}

	public void setIndustryId(int industryId) {
		this.industryId = industryId;
	}

	public String getIndustryName() {
		return industryName;
	}

	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}

	public int getCreativeTypeId() {
		return creativeTypeId;
	}

	public void setCreativeTypeId(int creativeTypeId) {
		this.creativeTypeId = creativeTypeId;
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

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public String getRepeatedCode() {
		return repeatedCode;
	}

	public void setRepeatedCode(String repeatedCode) {
		this.repeatedCode = repeatedCode;
	}

	public AppAdsnippet getAdsnippet() {
		return adsnippet;
	}

	public void setAdsnippet(AppAdsnippet adsnippet) {
		this.adsnippet = adsnippet;
	}

	public int getPlatform() {
		return platform;
	}

	public void setPlatform(int platform) {
		this.platform = platform;
	}

}
