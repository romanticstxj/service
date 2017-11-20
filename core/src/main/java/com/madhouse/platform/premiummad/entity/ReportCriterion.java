package com.madhouse.platform.premiummad.entity;

import java.util.Date;
import java.util.List;

import com.madhouse.platform.premiummad.constant.SystemConstant;

public class ReportCriterion{
	
	public static final int DIM_SORTING_POLICY = SystemConstant.DB.DIM_SORTING_POLICY;
	public static final int DIM_SORTING_DSP = SystemConstant.DB.DIM_SORTING_DSP;
	public static final int DIM_SORTING_MEDIA = SystemConstant.DB.DIM_SORTING_MEDIA;
	public static final int DIM_SORTING_ADSPACE = SystemConstant.DB.DIM_SORTING_ADSPACE;
	public static final int DIM_SORTING_LOCATION = SystemConstant.DB.DIM_SORTING_LOCATION;
	public static final int DIM_SORTING_DATE = SystemConstant.DB.DIM_SORTING_DATE;
	public static final int DIM_SORTING_HOUR = SystemConstant.DB.DIM_SORTING_HOUR;
	
	private Integer type; //查找的报表类型，后台实现其实是关联到哪个表(1:默认,2:运营商,4:设备类型,8:联网方式,16:地域)

	private Integer dims; //查询的维度(1:日期,2:时间,4:媒体,8:广告位,16:策略,32:需求方)

	private Integer realtime; //1:实时,0:离线
	
	private Integer mediaId; //媒体filter条件
	
	private Integer dspId; //Dsp filter条件
	
	private Integer policyId; //策略filter条件
	private Integer policyType; //策略filter条件
	
	private Date startDate; //开始日期
	
	private Date endDate; //结束日期
	
	private Boolean hasMedia;
	
	private Boolean hasAdspace;
	
	private Boolean hasDate;
	
	private Boolean hasHour;
	
	private Boolean hasDsp;
	
	private Boolean hasPolicy;
	
	private Boolean hasCarrier;
	
	private Boolean hasDevice;
	
	private Boolean hasConn;
	
	private Boolean hasLocation;
	
	private int lastOrderPosition;
	
	private List<Integer> mediaIds;
	
	private List<Integer> policyIds;

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

	public List<Integer> getMediaIds() {
		return mediaIds;
	}

	public void setMediaIds(List<Integer> mediaIds) {
		this.mediaIds = mediaIds;
	}

	public List<Integer> getPolicyIds() {
		return policyIds;
	}

	public void setPolicyIds(List<Integer> policyIds) {
		this.policyIds = policyIds;
	}

	public Boolean getHasCarrier() {
		return hasCarrier;
	}

	public void setHasCarrier(Boolean hasCarrier) {
		this.hasCarrier = hasCarrier;
	}

	public Boolean getHasDevice() {
		return hasDevice;
	}

	public void setHasDevice(Boolean hasDevice) {
		this.hasDevice = hasDevice;
	}

	public Boolean getHasConn() {
		return hasConn;
	}

	public void setHasConn(Boolean hasConn) {
		this.hasConn = hasConn;
	}

	public Boolean getHasLocation() {
		return hasLocation;
	}

	public void setHasLocation(Boolean hasLocation) {
		this.hasLocation = hasLocation;
	}

	public int getLastOrderPosition() {
		return lastOrderPosition;
	}

	public void setLastOrderPosition(int lastOrderPosition) {
		this.lastOrderPosition = lastOrderPosition;
	}

	public Integer getDspId() {
		return dspId;
	}

	public void setDspId(Integer dspId) {
		this.dspId = dspId;
	}

	public Integer getPolicyId() {
		return policyId;
	}

	public void setPolicyId(Integer policyId) {
		this.policyId = policyId;
	}

	public Integer getPolicyType() {
		return policyType;
	}

	public void setPolicyType(Integer policyType) {
		this.policyType = policyType;
	}
}
