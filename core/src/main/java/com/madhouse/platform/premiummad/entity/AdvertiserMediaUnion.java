package com.madhouse.platform.premiummad.entity;

import java.util.Date;

public class AdvertiserMediaUnion {

	private Integer id;

	private Integer advertiserId;
	
	private String advertiserKey;

	private Integer mediaId;

	private Boolean auditee;

	private Byte status;

	private String reason;

	private Integer auditedUser;

	private Date createdTime;

	private Date auditedTime;

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

	public String getAdvertiserKey() {
		return advertiserKey;
	}

	public void setAdvertiserKey(String advertiserKey) {
		this.advertiserKey = advertiserKey;
	}

	public Integer getMediaId() {
		return mediaId;
	}

	public void setMediaId(Integer mediaId) {
		this.mediaId = mediaId;
	}

	public Boolean getAuditee() {
		return auditee;
	}

	public void setAuditee(Boolean auditee) {
		this.auditee = auditee;
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

	public Integer getAuditedUser() {
		return auditedUser;
	}

	public void setAuditedUser(Integer auditedUser) {
		this.auditedUser = auditedUser;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getAuditedTime() {
		return auditedTime;
	}

	public void setAuditedTime(Date auditedTime) {
		this.auditedTime = auditedTime;
	}
}