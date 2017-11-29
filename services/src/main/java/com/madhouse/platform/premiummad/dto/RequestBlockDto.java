package com.madhouse.platform.premiummad.dto;

import java.io.Serializable;
import java.util.Date;

import com.madhouse.platform.premiummad.annotation.NotNullAndBlank;

public class RequestBlockDto implements Serializable{
	
	private static final long serialVersionUID = -2456563813935310911L;

	private Integer id;
	@NotNullAndBlank
	private String code;
	@NotNullAndBlank
	private Integer type;
	
	private String typeName;
	
	private String description;
	
	private Integer status;
	
	private Date createdTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
}
