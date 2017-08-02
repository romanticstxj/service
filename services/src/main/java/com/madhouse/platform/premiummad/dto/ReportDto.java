package com.madhouse.platform.premiummad.dto;

import com.madhouse.platform.premiummad.annotation.NotNullAndBlank;

public class ReportDto {
	
	@NotNullAndBlank
	private Integer type; //查找的报表类型，后台实现其实是关联到哪个表(1:默认,2:运营商,4:设备类型,8:联网方式,16:地域)
	@NotNullAndBlank
	private Integer dims; //查询的维度(1:日期,2:时间,4:媒体,8:广告位,16:策略,32:需求方)
	@NotNullAndBlank
	private Integer realtime; //1:实时,0:离线
	
	private Integer mediaId; //媒体filter条件
	@NotNullAndBlank
	private String startDate; //开始日期
	@NotNullAndBlank
	private String endDate; //结束日期
	
	public ReportDto(Integer type, Integer dims, Integer realtime, Integer mediaId, String startDate, String endDate) {
		super();
		this.type = type;
		this.dims = dims;
		this.realtime = realtime;
		this.mediaId = mediaId;
		this.startDate = startDate;
		this.endDate = endDate;
	}

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
	
}
