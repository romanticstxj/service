package com.madhouse.platform.premiummad.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.madhouse.platform.premiummad.annotation.NotNullAndBlank;
import com.madhouse.platform.premiummad.constant.SystemCommonMsg;
import com.madhouse.platform.premiummad.validator.Update;
import com.madhouse.platform.premiummad.validator.UpdateStatus;

public class DspDto {
	
	@NotNull(message=SystemCommonMsg.NO_UPDATE_ID, groups={Update.class, UpdateStatus.class})
	private Integer id;
	@NotNullAndBlank
	private String name;
	@NotNullAndBlank
	private String bidUrl; //dsp url
	@NotNullAndBlank
	private Byte deliveryType; //合作模式(1: PDB, 2: PD, 4: PMP, 8: RTB)
	
	private Integer maxQPS; //最大QPS
	
	private Integer bidPercent; //加价百分比
	
	private String token;
	@NotNull(message=SystemCommonMsg.NO_UPDATE_STATUS, groups=UpdateStatus.class)
	@Size(message=SystemCommonMsg.ERROR_UPDATE_STATUS, min=0, max=2, groups=UpdateStatus.class)
	private Integer status;

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

	public String getBidUrl() {
		return bidUrl;
	}

	public void setBidUrl(String bidUrl) {
		this.bidUrl = bidUrl;
	}

	public Byte getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(Byte deliveryType) {
		this.deliveryType = deliveryType;
	}

	public Integer getMaxQPS() {
		return maxQPS;
	}

	public void setMaxQPS(Integer maxQPS) {
		this.maxQPS = maxQPS;
	}

	public Integer getBidPercent() {
		return bidPercent;
	}

	public void setBidPercent(Integer bidPercent) {
		this.bidPercent = bidPercent;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
