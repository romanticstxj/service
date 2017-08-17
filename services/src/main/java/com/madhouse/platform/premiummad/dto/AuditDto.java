package com.madhouse.platform.premiummad.dto;

import com.madhouse.platform.premiummad.annotation.NotNullAndBlank;

public class AuditDto {
	
	@NotNullAndBlank
	private String ids;
	@NotNullAndBlank
	private Integer status;

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
