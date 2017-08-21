package com.madhouse.platform.premiummad.entity;

public class Media extends BaseEntity{
	
	private Integer id;
	
	private Integer status;
	
	private Integer adCount;
	
	private String name;
	
	private Integer category;
	
	private String categoryName;
	
	private Integer type;
	
	private Integer accessType;
	
	private Integer advertiserAuditMode;
	
	private Integer materialAuditMode;
	
	private Integer timeout;
	
	private String description;
	
	private Integer updateType;
	
	private Integer apiType;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getAdCount() {
		return adCount;
	}

	public void setAdCount(Integer adCount) {
		this.adCount = adCount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getAccessType() {
		return accessType;
	}

	public void setAccessType(Integer accessType) {
		this.accessType = accessType;
	}

	public Integer getAdvertiserAuditMode() {
		return advertiserAuditMode;
	}

	public void setAdvertiserAuditMode(Integer advertiserAuditMode) {
		this.advertiserAuditMode = advertiserAuditMode;
	}

	public Integer getMaterialAuditMode() {
		return materialAuditMode;
	}

	public void setMaterialAuditMode(Integer materialAuditMode) {
		this.materialAuditMode = materialAuditMode;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description == null ? null : description.trim();
	}

	public Integer getUpdateType() {
		return updateType;
	}

	public void setUpdateType(Integer updateType) {
		this.updateType = updateType;
	}

	public Integer getApiType() {
		return apiType;
	}

	public void setApiType(Integer apiType) {
		this.apiType = apiType;
	}
	
	
}
