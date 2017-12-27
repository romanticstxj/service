package com.madhouse.platform.premiummad.entity;

public class AdvertiserUserUnion {

	private Integer id;

	private Integer advertiserId;

	private String qualificationFile;
	
	private String userId;

	private Byte status;

	private String reason;

	// 关联广告主相关信息
	private Byte advertiserStatus;

	private String advertiserName;
	
	private String mediaAdvertiserKey;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAdvertiserId() {
		return advertiserId;
	}

	public void setAdvertiserId(Integer advertiserId) {
		this.advertiserId = advertiserId;
	}

	public String getQualificationFile() {
		return qualificationFile;
	}

	public void setQualificationFile(String qualificationFile) {
		this.qualificationFile = qualificationFile;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Byte getAdvertiserStatus() {
		return advertiserStatus;
	}

	public void setAdvertiserStatus(Byte advertiserStatus) {
		this.advertiserStatus = advertiserStatus;
	}

	public String getAdvertiserName() {
		return advertiserName;
	}

	public void setAdvertiserName(String advertiserName) {
		this.advertiserName = advertiserName;
	}

	public String getMediaAdvertiserKey() {
		return mediaAdvertiserKey;
	}

	public void setMediaAdvertiserKey(String mediaAdvertiserKey) {
		this.mediaAdvertiserKey = mediaAdvertiserKey;
	}
}