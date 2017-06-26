package com.madhouse.platform.premiummad.entity;

import java.util.Date;

public class BaseEntity {
	
	private Integer createdUser;
	
	private Date createdTime;
	
	private Integer modifiedUser;
	
	private Date modifiedTime;

	public Integer getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(Integer createdUser) {
		this.createdUser = createdUser;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Integer getModifiedUser() {
		return modifiedUser;
	}

	public void setModifiedUser(Integer modifiedUser) {
		this.modifiedUser = modifiedUser;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	
	
}
