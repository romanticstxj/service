package com.madhouse.platform.premiummad.media.iqiyi.request;

import java.util.Date;

public class IQiyiMaterialStatusRequest {
	private String dsp_token;// 字符串类型，用来唯一标识合作方
	private String m_id;// 上传成功时返回的素材id，查询单个素材时使用，必填
	private String batch;// 多个m_id，用逗号分隔，批量查询素材时使用，必填
	private String status;// 素材状态，按素材状态查询素材列表时使用，必填
	private Date start_date;// 状态修改的起始时间，按素材状态查询素材列表时使用，格式为yyyy-MM-dd，选填
	private Date end_date;// 状态修改的截止时间，按素材状态查询素材列表时使用，格式为yyyy-MM-dd，选填

	public String getDsp_token() {
		return dsp_token;
	}

	public void setDsp_token(String dsp_token) {
		this.dsp_token = dsp_token;
	}

	public String getM_id() {
		return m_id;
	}

	public void setM_id(String m_id) {
		this.m_id = m_id;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

}
