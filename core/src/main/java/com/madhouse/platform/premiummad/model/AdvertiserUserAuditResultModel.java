package com.madhouse.platform.premiummad.model;

public class AdvertiserUserAuditResultModel {
	/**
	 * PremiumMAD平台定义的广告主ID
	 */
	private Integer advertiserId;

	/**
	 * 媒体方存储的广告主key
	 */
	private String mediaAdvertiserKey;

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 审核状态 -1：审核未通过；0：待审核；1：第三方媒体审核中；2：审核通过
	 */
	private Integer status;

	/**
	 * 审核未通过原因
	 */
	private String errorMessage;

	public Integer getAdvertiserId() {
		return advertiserId;
	}

	public void setAdvertiserId(Integer advertiserId) {
		this.advertiserId = advertiserId;
	}

	public String getMediaAdvertiserKey() {
		return mediaAdvertiserKey;
	}

	public void setMediaAdvertiserKey(String mediaAdvertiserKey) {
		this.mediaAdvertiserKey = mediaAdvertiserKey;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
