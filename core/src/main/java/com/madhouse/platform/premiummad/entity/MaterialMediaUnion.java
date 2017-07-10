package com.madhouse.platform.premiummad.entity;

import java.util.Date;

public class MaterialMediaUnion {

	private Integer id;

	private Integer materialId;

	private String materialKey;

	private Integer mediaId;

	private Byte status;

	private String reason;

	private Date createdTime;

	private Integer auditedUser;

	private Date auditedTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMaterialId() {
		return materialId;
	}

	public void setMaterialId(Integer materialId) {
		this.materialId = materialId;
	}

	public String getMaterialKey() {
		return materialKey;
	}

	public void setMaterialKey(String materialKey) {
		this.materialKey = materialKey;
	}

	public Integer getMediaId() {
		return mediaId;
	}

	public void setMediaId(Integer mediaId) {
		this.mediaId = mediaId;
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

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Integer getAuditedUser() {
		return auditedUser;
	}

	public void setAuditedUser(Integer auditedUser) {
		this.auditedUser = auditedUser;
	}

	public Date getAuditedTime() {
		return auditedTime;
	}

	public void setAuditedTime(Date auditedTime) {
		this.auditedTime = auditedTime;
	}
}