package com.madhouse.platform.premiummad.dto;

import java.io.Serializable;
import java.util.Date;

import com.madhouse.platform.premiummad.annotation.NotNull;

public class MediaDto implements Serializable{
	
	private static final long serialVersionUID = 1634655943775249685L;

	private Integer id;
	
	private Integer status;
	
	private Integer adCount;
	@NotNull
	private String name;
	@NotNull
	private Integer category;
	private String categoryName;
	@NotNull
	private Integer type;
	@NotNull
	private Integer accessType;
	@NotNull
	private Integer advertiserAuditMode;
	@NotNull
	private Integer materialAuditMode;
	@NotNull
	private Integer timeout;
	
	private Date createdTime;
	
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
		this.name = name;
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

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
