package com.madhouse.platform.premiummad.model;

import java.util.Date;

public class MediaAuditAdvertiserModel {

	private Integer id;

	private Integer dspId;

	private String advertiserKey;

	private String advertiserName;

	private Integer mediaId;

	private Integer advertiserAuditMode;

	private Short industry;

	private String website;

	private String contact;

	private String address;

	private String phone;

	private String license;

	private Byte status;

	private String reason;

	private String mediaAdvertiserKey;

	private Integer auditedUser;

	private Date updatedTime;
	
	/**
	 * 同一家媒体的具体媒体
	 */
	private int[] mediaIds;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDspId() {
		return dspId;
	}

	public void setDspId(Integer dspId) {
		this.dspId = dspId;
	}

	public String getAdvertiserKey() {
		return advertiserKey;
	}

	public void setAdvertiserKey(String advertiserKey) {
		this.advertiserKey = advertiserKey;
	}

	public String getAdvertiserName() {
		return advertiserName;
	}

	public void setAdvertiserName(String advertiserName) {
		this.advertiserName = advertiserName;
	}

	public Integer getMediaId() {
		return mediaId;
	}

	public void setMediaId(Integer mediaId) {
		this.mediaId = mediaId;
	}

	public Integer getAdvertiserAuditMode() {
		return advertiserAuditMode;
	}

	public void setAdvertiserAuditMode(Integer advertiserAuditMode) {
		this.advertiserAuditMode = advertiserAuditMode;
	}

	public Short getIndustry() {
		return industry;
	}

	public void setIndustry(Short industry) {
		this.industry = industry;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
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

	public String getMediaAdvertiserKey() {
		return mediaAdvertiserKey;
	}

	public void setMediaAdvertiserKey(String mediaAdvertiserKey) {
		this.mediaAdvertiserKey = mediaAdvertiserKey;
	}

	public Integer getAuditedUser() {
		return auditedUser;
	}

	public void setAuditedUser(Integer auditedUser) {
		this.auditedUser = auditedUser;
	}

	public int[] getMediaIds() {
		return mediaIds;
	}

	public void setMediaIds(int[] mediaIds) {
		this.mediaIds = mediaIds;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
}