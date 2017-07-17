package com.madhouse.platform.premiummad.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.madhouse.platform.premiummad.annotation.NotNullAndBlank;

public class MaterialModel implements Serializable {

	private static final long serialVersionUID = -4527909581700251071L;
	
	/**
	 * DSP ID
	 */
	private String dspId;
	
	/**
	 * DSP 定义的素材ID
	 */
	@NotNullAndBlank
	private String id;
	
	/**
	 * 素材名称
	 */
	@NotNullAndBlank
	private String name;
	
	/**
	 * 广告类型
	 */
	@NotNullAndBlank
	private Integer adType;
	
	/**
	 * DSP 平台定义的广告主ID
	 */
	@NotNullAndBlank
	private String advertiserId;
	
	/**
	 * 投放类型
	 * 1: PDB、2:PD、4:PMP、8:RTB
	 */
	@NotNullAndBlank
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
	 * PremiumMAD 平台定义的Deal ID
	 */
	private String dealId;
	
	/**
	 * 有效日期(yyyy-MM-dd)
	 */
	private Date startDate;
	
	/**
	 * 失效日期(yyyy-MM-dd)
	 */
	@NotNullAndBlank
	private Date endDate;
	
	/**
	 * PremiumMAD 平台定义的媒体ID，可同时制定多个媒体
	 */
	@NotNullAndBlank
	private List<Integer> mediaId;
	
	/**
	 * 广告素材宽度（单位:pixel)
	 */
	@NotNullAndBlank
	private Integer weight;
	
	/**
	 * 广告素材高度（单位:pixel)
	 */
	@NotNullAndBlank
	private Integer height;
	
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
	 * 广告素材URL
	 */
	@NotNullAndBlank
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
	private MonitorModel monitor;

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

	public Integer getAdType() {
		return adType;
	}

	public void setAdType(Integer adType) {
		this.adType = adType;
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

	public String getDealId() {
		return dealId;
	}

	public void setDealId(String dealId) {
		this.dealId = dealId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<Integer> getMediaId() {
		return mediaId;
	}

	public void setMediaId(List<Integer> mediaId) {
		this.mediaId = mediaId;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
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

	public MonitorModel getMonitor() {
		return monitor;
	}

	public void setMonitor(MonitorModel monitor) {
		this.monitor = monitor;
	}

	public String getDspId() {
		return dspId;
	}

	public void setDspId(String dspId) {
		this.dspId = dspId;
	}
}
