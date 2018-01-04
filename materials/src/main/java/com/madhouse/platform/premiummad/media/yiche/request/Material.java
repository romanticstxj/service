package com.madhouse.platform.premiummad.media.yiche.request;

public class Material {
	// 订单编号
	private String orderCode;
	// 广告主ID
	private int advertiserId;
	// 素材类型
	private int materialType;
	// 模板ID
	private int templateId;
	// 素材名称
	private String materialName;
	// 素材宽
	private int width;
	// 素材高
	private int height;
	// 图片、flash的url地址（先上传OSS文件服务器，查询url)
	private String imgUrl;
	// 标题
	private String title;
	// 跳转链接地址
	private String linkUrl;
	// 视频地址（先上传OSS文件服务器，查询url)
	private String viewUrl;
	// 品牌ID
	private int brandId;
	// 车型ID
	private int modelId;
	// 新闻简介
	private String summary;
	// 文字内容
	private String content;
	// 投放平台
	private int platform;
	// H5类型时，先写html代码
	private String htmlContent;
	// H5类型时，填写js代码
	private String jsContent;

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public int getAdvertiserId() {
		return advertiserId;
	}

	public void setAdvertiserId(int advertiserId) {
		this.advertiserId = advertiserId;
	}

	public int getMaterialType() {
		return materialType;
	}

	public void setMaterialType(int materialType) {
		this.materialType = materialType;
	}

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
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

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getViewUrl() {
		return viewUrl;
	}

	public void setViewUrl(String viewUrl) {
		this.viewUrl = viewUrl;
	}

	public int getBrandId() {
		return brandId;
	}

	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}

	public int getModelId() {
		return modelId;
	}

	public void setModelId(int modelId) {
		this.modelId = modelId;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getPlatform() {
		return platform;
	}

	public void setPlatform(int platform) {
		this.platform = platform;
	}

	public String getHtmlContent() {
		return htmlContent;
	}

	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}

	public String getJsContent() {
		return jsContent;
	}

	public void setJsContent(String jsContent) {
		this.jsContent = jsContent;
	}
}
