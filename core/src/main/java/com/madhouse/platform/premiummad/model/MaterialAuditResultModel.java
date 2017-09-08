package com.madhouse.platform.premiummad.model;

public class MaterialAuditResultModel {
	/**
	 * 素材ID(DSN 或 我方系统的id)
	 */
	private String id;

	/**
	 * remiumMAD平台定义的媒体ID
	 */
	private String mediaId;

	/**
	 * 审核状态 -1：审核未通过；0：待审核；1：第三方媒体审核中；2：审核通过
	 */
	private Integer status;

	/**
	 * 广告位ID
	 */
	private Integer adspaceId;
	
	/**
	 * 审核未通过原因
	 */
	private String errorMessage;

	/**
	 * 媒体分配的key
	 */
	private String mediaMaterialKey;
	
	/**
	 * 查询媒体状态
	 */
	private String mediaQueryKey;
	
	/**
	 * 媒体上传的url
	 */
	private String mediaMaterialUrl;

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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getAdspaceId() {
		return adspaceId;
	}

	public void setAdspaceId(Integer adspaceId) {
		this.adspaceId = adspaceId;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getMediaMaterialKey() {
		return mediaMaterialKey;
	}

	public void setMediaMaterialKey(String mediaMaterialKey) {
		this.mediaMaterialKey = mediaMaterialKey;
	}

	public String getMediaQueryKey() {
		return mediaQueryKey;
	}

	public void setMediaQueryKey(String mediaQueryKey) {
		this.mediaQueryKey = mediaQueryKey;
	}

	public String getMediaMaterialUrl() {
		return mediaMaterialUrl;
	}

	public void setMediaMaterialUrl(String mediaMaterialUrl) {
		this.mediaMaterialUrl = mediaMaterialUrl;
	}
}