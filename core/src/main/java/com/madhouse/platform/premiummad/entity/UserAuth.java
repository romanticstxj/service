package com.madhouse.platform.premiummad.entity;

public class UserAuth extends BaseEntity{
	
	private Integer userId;
	
	private Integer[] mediaIds;
	
	private Integer[] policyIds;
	
	private Integer isAdmin;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
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
	
	
}
