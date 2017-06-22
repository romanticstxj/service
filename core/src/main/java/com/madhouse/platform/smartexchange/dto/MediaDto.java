package com.madhouse.platform.smartexchange.dto;

import java.io.Serializable;
import java.util.Date;

import com.madhouse.platform.smartexchange.annotation.NotNull;

public class MediaDto implements Serializable{
	
	private static final long serialVersionUID = 1634655943775249685L;

	private Integer id;
	
	private String delFlg;
	
	private Integer adCount;
	@NotNull
	private String name;
	@NotNull
	private String type;
	@NotNull
	private String attribute;
	@NotNull
	private String interactionType;
	@NotNull
	private String advertiserVerifyType;
	@NotNull
	private String materialVerifyType;
	@NotNull
	private Integer timeoutPermitted;
	
	private Date createTime;
	
	private String remark;
	
	private String updateType;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDelFlg() {
		return delFlg;
	}

	public void setDelFlg(String delFlg) {
		this.delFlg = delFlg;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getInteractionType() {
		return interactionType;
	}

	public void setInteractionType(String interactionType) {
		this.interactionType = interactionType;
	}

	public String getAdvertiserVerifyType() {
		return advertiserVerifyType;
	}

	public void setAdvertiserVerifyType(String advertiserVerifyType) {
		this.advertiserVerifyType = advertiserVerifyType;
	}

	public String getMaterialVerifyType() {
		return materialVerifyType;
	}

	public void setMaterialVerifyType(String materialVerifyType) {
		this.materialVerifyType = materialVerifyType;
	}

	public Integer getTimeoutPermitted() {
		return timeoutPermitted;
	}

	public void setTimeoutPermitted(Integer timeoutPermitted) {
		this.timeoutPermitted = timeoutPermitted;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUpdateType() {
		return updateType;
	}

	public void setUpdateType(String updateType) {
		this.updateType = updateType;
	}
	
}
