package com.madhouse.platform.premiummad.model;

public class AdvertiserAuditResultModel {
	/**
	 * 广告主 ID(DSN 或  我方系统的id)
	 */
	private String id;
	
	/**
	 * remiumMAD平台定义的媒体ID
	 */
	private String mediaId;
	
	/**
	 * 在同一个组的媒体ID
	 */
	private int[] mediaIds;
	
	/**
	 * 审核状态
	 * -1：审核未通过；0：待审核；1：第三方媒体审核中；2：审核通过
	 */
	private Integer status;
	
	/**
	 * 审核未通过原因
	 */
	private String errorMessage;
	
	/**
	 * 媒体方存储的广告主key
	 */
	private String mediaAdvertiserKey;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public int[] getMediaIds() {
		return mediaIds;
	}

	public void setMediaIds(int[] mediaIds) {
		this.mediaIds = mediaIds;
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

	public String getMediaAdvertiserKey() {
		return mediaAdvertiserKey;
	}

	public void setMediaAdvertiserKey(String mediaAdvertiserKey) {
		this.mediaAdvertiserKey = mediaAdvertiserKey;
	}
}
