package com.madhouse.platform.premiummad.model;

public class MediaModel {
	private long id;
	private String name;
	private int category;
	// app, site
	private int type;
	private int advertiserAuditMode;
	private int materialAuditMode;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public int getAdvertiserAuditMode() {
		return advertiserAuditMode;
	}

	public void setAdvertiserAuditMode(int advertiserAuditMode) {
		this.advertiserAuditMode = advertiserAuditMode;
	}

	public int getMaterialAuditMode() {
		return materialAuditMode;
	}

	public void setMaterialAuditMode(int materialAuditMode) {
		this.materialAuditMode = materialAuditMode;
	}
}
