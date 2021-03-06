package com.madhouse.platform.premiummad.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

public class MaterialDto implements Serializable {

	private static final long serialVersionUID = -4527909581700251071L;
	
	/**
	 * DSP 定义的素材ID
	 */
	@NotNull(message = "素材ID必须[id]")
	private String id;
	
	/**
	 * 素材名称
	 */
	@NotNull(message = "素材名称必须[name]")
	private String name;
	
	/**
	 * 广告形式
	 */
	@NotNull(message = "广告形式必须[layout]")
	private Integer layout;
	
	/**
	 * DSP 平台定义的广告主ID
	 */
	@NotNull(message = "广告主ID必须[advertiserId]")
	private String advertiserId;
	
	/**
	 * 投放类型
	 * 1: PDB、2:PD、4:PMP、8:RTB
	 */
	@NotNull(message = "投放类型必须[deliveryType]")
	private Integer deliveryType;
	
	/**
	 * 广告品牌名称
	 */
	private String brand;
	
	/**
	 * 代理商名称
	 */
	private String agency;
	
	/**
	 * 用户ID
	 */
	private String userId;
	
	/**
	 * PremiumMAD 平台定义的Deal ID
	 */
	private String dealId;
	
	/**
	 * 有效日期(yyyy-MM-dd)
	 */
	private String startDate;
	
	/**
	 * 失效日期(yyyy-MM-dd)
	 */
	@NotNull(message = "失效日期必须[endDate]")
	private String endDate;
	
	/**
	 * PremiumMAD 平台定义的媒体ID
	 */
	@NotNull(message = "媒体ID必须[mediaId]")
	private Integer mediaId;
	
	/**
	 * 广告位，可同时指定多个
	 */
	private List<Integer> adspaceId;
	
	/**
	 * 广告素材宽度（单位:pixel)
	 */
	@NotNull(message = "素材宽必须[w]")
	private Integer w;
	
	/**
	 * 广告素材高度（单位:pixel)
	 */
	@NotNull(message = "素材高必须[h]")
	private Integer h;
	
	/**
	 * 信息流广告图标URL
	 */
	private String icon;
	
	/**
	 * 信息流广告标题
	 */
	private String title;
	
	/**
	 * 信息流广告描述
	 */
	private String desc;
	
	/**
	 * 视频信息流广告封面URL
	 */
	private String cover;
	
	/**
	 * 信息流广告正文
	 */
	private String content;
	
	/**
	 * 广告素材URL
	 */
	private List<String> adm;
	
	/**
	 * 视频广告时长（单位：秒）
	 */
	private Integer duration;
	
	/**
	 * 广告落地页URL
	 */
	private String lpgUrl;
	
	/**
	 * 广告点击行为
	 */
	private Integer actType;
	
	/**
	 * 广告监测信息
	 */
	private MonitorDto monitor;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getAdvertiserId() {
		return advertiserId;
	}

	public void setAdvertiserId(String advertiserId) {
		this.advertiserId = advertiserId;
	}

	public Integer getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(Integer deliveryType) {
		this.deliveryType = deliveryType;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getAgency() {
		return agency;
	}

	public void setAgency(String agency) {
		this.agency = agency;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDealId() {
		return dealId;
	}

	public void setDealId(String dealId) {
		this.dealId = dealId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Integer getLayout() {
		return layout;
	}

	public void setLayout(Integer layout) {
		this.layout = layout;
	}

	public Integer getMediaId() {
		return mediaId;
	}

	public void setMediaId(Integer mediaId) {
		this.mediaId = mediaId;
	}

	public List<Integer> getAdspaceId() {
		return adspaceId;
	}

	public void setAdspaceId(List<Integer> adspaceId) {
		this.adspaceId = adspaceId;
	}

	public Integer getW() {
		return w;
	}

	public void setW(Integer w) {
		this.w = w;
	}

	public Integer getH() {
		return h;
	}

	public void setH(Integer h) {
		this.h = h;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public List<String> getAdm() {
		return adm;
	}

	public void setAdm(List<String> adm) {
		this.adm = adm;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getLpgUrl() {
		return lpgUrl;
	}

	public void setLpgUrl(String lpgUrl) {
		this.lpgUrl = lpgUrl;
	}

	public Integer getActType() {
		return actType;
	}

	public void setActType(Integer actType) {
		this.actType = actType;
	}

	public MonitorDto getMonitor() {
		return monitor;
	}

	public void setMonitor(MonitorDto monitor) {
		this.monitor = monitor;
	}
}

