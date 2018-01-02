package com.madhouse.platform.premiummad.media.autohome.response;

public class CreativeUploadResponse {
	/**
	 * 具体传输数据
	 */
	private CreativeUploadData data;
	
	/**
	 * 为1表示成功
	 */
	private int status;
	
	/**
	 * 错误数据
	 */
	private StatusInfo statusInfo;

	public CreativeUploadData getData() {
		return data;
	}

	public void setData(CreativeUploadData data) {
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
