package com.madhouse.platform.premiummad.entity;

import com.madhouse.platform.premiummad.annotation.NotNullAndBlank;

public class UserAuth extends BaseEntity{
	
	private Integer id;
	
	@NotNullAndBlank
	private Integer specifiedUserId;
	
	private Integer[] mediaIds;
	
	private Integer[] policyIds;
	
	private Integer isAdmin;

	public Integer getSpecifiedUserId() {
		return specifiedUserId;
	}

	public void setSpecifiedUserId(Integer specifiedUserId) {
		this.specifiedUserId = specifiedUserId;
	}

	public Integer[] getMediaIds() {
		return mediaIds;
	}

	public void setMediaIds(Integer[] mediaIds) {
		this.mediaIds = mediaIds;
	}

	public Integer[] getPolicyIds() {
		return policyIds;
	}

	public void setPolicyIds(Integer[] policyIds) {
		this.policyIds = policyIds;
	}

	public Integer getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Integer isAdmin) {
		this.isAdmin = isAdmin;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
