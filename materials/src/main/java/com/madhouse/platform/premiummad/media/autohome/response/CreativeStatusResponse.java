package com.madhouse.platform.premiummad.media.autohome.response;

public class CreativeStatusResponse {
	/**
	 * 具体传输数据
	 */
	private CreativeStatusData data;
	
	/**
	 * 为1表示成功
	 */
	private int status;
	
	/**
	 * 错误数据
	 */
	private StatusInfo statusInfo;

	public CreativeStatusData getData() {
		return data;
	}

	public void setData(CreativeStatusData data) {
		this.data = data;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public StatusInfo getStatusInfo() {
		return statusInfo;
	}

	public void setStatusInfo(StatusInfo statusInfo) {
		this.statusInfo = statusInfo;
	}
}
