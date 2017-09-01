package com.madhouse.platform.premiummad.media.iqiyi.response;

public class IQiyiCustomerStatusDetail {

	private String ad_id;
	/* 分别为：WAIT PASS UNPASS */
	private String status;
	/* 仅当status为unpass时候返回 */
	private String reason;

	public String getAd_id() {
		return ad_id;
	}

	public void setAd_id(String ad_id) {
		this.ad_id = ad_id;
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
}
