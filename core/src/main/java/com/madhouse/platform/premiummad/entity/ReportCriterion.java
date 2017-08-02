package com.madhouse.platform.premiummad.entity;

import java.util.Date;

public class ReportCriterion extends BaseEntity{
	
	private Integer type; //查找的报表类型，后台实现其实是关联到哪个表(1:默认,2:运营商,4:设备类型,8:联网方式,16:地域)

	private Integer dims; //查询的维度(1:日期,2:时间,4:媒体,8:广告位,16:策略,32:需求方)

	private Integer realtime; //1:实时,0:离线
	
	private Integer mediaId; //媒体filter条件
	
	private Date startDate; //开始日期
	
	private Date endDate; //结束日期
	
	private Boolean hasMedia;
	
	private Boolean hasAdspace;
	
	private Boolean hasDate;
	
	private Boolean hasHour;
	
	private Boolean hasDsp;
	
	private Boolean hasPolicy;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getDims() {
		return dims;
	}

	public void setDims(Integer dims) {
		this.dims = dims;
	}

	public Integer getRealtime() {
		return realtime;
	}

	public void setRealtime(Integer realtime) {
		this.realtime = realtime;
	}

	public Integer getMediaId() {
		return mediaId;
	}

	public void setMediaId(Integer mediaId) {
		this.mediaId = mediaId;
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

	public Boolean getHasMedia() {
		return hasMedia;
	}

	public void setHasMedia(Boolean hasMedia) {
		this.hasMedia = hasMedia;
	}

	public Boolean getHasAdspace() {
		return hasAdspace;
	}

	public void setHasAdspace(Boolean hasAdspace) {
		this.hasAdspace = hasAdspace;
	}

	public Boolean getHasDate() {
		return hasDate;
	}

	public void setHasDate(Boolean hasDate) {
		this.hasDate = hasDate;
	}

	public Boolean getHasHour() {
		return hasHour;
	}

	public void setHasHour(Boolean hasHour) {
		this.hasHour = hasHour;
	}

	public Boolean getHasDsp() {
		return hasDsp;
	}

	public void setHasDsp(Boolean hasDsp) {
		this.hasDsp = hasDsp;
	}

	public Boolean getHasPolicy() {
		return hasPolicy;
	}

	public void setHasPolicy(Boolean hasPolicy) {
		this.hasPolicy = hasPolicy;
	}
}
