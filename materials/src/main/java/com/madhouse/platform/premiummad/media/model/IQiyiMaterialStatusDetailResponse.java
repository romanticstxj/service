package com.madhouse.platform.premiummad.media.model;

public class IQiyiMaterialStatusDetailResponse {
	private String status;
	private String reason;
	private String tv_id;
	private String m_id;

	public String getM_id() {
		return m_id;
	}

	public void setM_id(String m_id) {
		this.m_id = m_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getTv_id() {
		return tv_id;
	}

	public void setTv_id(String tv_id) {
		this.tv_id = tv_id;
	}

}
