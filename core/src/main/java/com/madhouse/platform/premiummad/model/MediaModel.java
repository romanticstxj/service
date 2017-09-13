package com.madhouse.platform.premiummad.model;

public class MediaModel {
	private Integer id;
	private String name;
	private Byte category;
	// app, site
	private Byte type;
	private Byte advertiserAuditMode;
	private Byte materialAuditMode;

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Byte getCategory() {
		return category;
	}

	public void setCategory(Byte category) {
		this.category = category;
	}

	public Byte getAdvertiserAuditMode() {
		return advertiserAuditMode;
	}

	public void setAdvertiserAuditMode(Byte advertiserAuditMode) {
		this.advertiserAuditMode = advertiserAuditMode;
	}

	public Byte getMaterialAuditMode() {
		return materialAuditMode;
	}

	public void setMaterialAuditMode(Byte materialAuditMode) {
		this.materialAuditMode = materialAuditMode;
	}
}
