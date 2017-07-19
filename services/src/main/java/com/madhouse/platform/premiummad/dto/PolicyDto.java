package com.madhouse.platform.premiummad.dto;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.madhouse.platform.premiummad.annotation.NotNullAndBlank;
import com.madhouse.platform.premiummad.constant.SystemCommonMsg;
import com.madhouse.platform.premiummad.validator.Update;
import com.madhouse.platform.premiummad.validator.UpdateStatus;

public class PolicyDto implements Serializable{

	private static final long serialVersionUID = -3532246711555090831L;
	
	@NotNull(message=SystemCommonMsg.NO_UPDATE_ID, groups={Update.class, UpdateStatus.class})
    private Integer id;
	
	private Integer dealId;
    @NotNullAndBlank
    private String name;
    @NotNullAndBlank
    private Byte type; //策略类型(1: PDB, 2: PD, 4: PMP, 8: RTB)
    @NotNullAndBlank
    private Integer weight; //权重
    
    private Date startDate;
    @NotNullAndBlank
    private Integer isEndDate; //0:不限结束时间，1:限制结束时间
    
    private Date endDate;
    @NotNullAndBlank
    private Integer isTimeTargeting; //0：全时段，1：指定时段
    
    private String timeTargeting; //时间定向(格式:0:0,1,2,3&1:19,20,21,22,23)
    @NotNullAndBlank
    private Integer isLocationTargeting; //0:不限地域，1:指定地域

    private String locationTargeting; //地域定向
    @NotNullAndBlank
    private String osTargeting; //OS定向(多个用半角逗号分隔, 1:Android, 2:iOS, 3: 其它)
    @NotNullAndBlank
    private String connTargeting; //连网方式定向(多个用半角逗号分隔, 1:WIFI, 2:2G, 3:3G, 4:4G)
    @NotNullAndBlank
    private Integer isQuantityLimit; //0：不限投放量，1：限制投放量

    private Byte limitType; //总量控制(1: 每天, 2: 整单)

    private Integer limitReqs;

    private Integer limitQps;
    
    private Byte limitSpeed; //投放速度(1:加速投放, 2:匀速投放)

    private Integer bidType;

    private Integer bidFloor;
    @NotNull(message=SystemCommonMsg.NO_UPDATE_STATUS, groups=UpdateStatus.class)
    private Byte status;

    private String description;

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

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Integer getIsEndDate() {
		return isEndDate;
	}

	public void setIsEndDate(Integer isEndDate) {
		this.isEndDate = isEndDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getIsTimeTargeting() {
		return isTimeTargeting;
	}

	public void setIsTimeTargeting(Integer isTimeTargeting) {
		this.isTimeTargeting = isTimeTargeting;
	}

	public String getTimeTargeting() {
		return timeTargeting;
	}

	public void setTimeTargeting(String timeTargeting) {
		this.timeTargeting = timeTargeting;
	}

	public Integer getIsLocationTargeting() {
		return isLocationTargeting;
	}

	public void setIsLocationTargeting(Integer isLocationTargeting) {
		this.isLocationTargeting = isLocationTargeting;
	}

	public String getLocationTargeting() {
		return locationTargeting;
	}

	public void setLocationTargeting(String locationTargeting) {
		this.locationTargeting = locationTargeting;
	}

	public String getOsTargeting() {
		return osTargeting;
	}

	public void setOsTargeting(String osTargeting) {
		this.osTargeting = osTargeting;
	}

	public String getConnTargeting() {
		return connTargeting;
	}

	public void setConnTargeting(String connTargeting) {
		this.connTargeting = connTargeting;
	}

	public Byte getLimitType() {
		return limitType;
	}

	public void setLimitType(Byte limitType) {
		this.limitType = limitType;
	}

	public Integer getLimitReqs() {
		return limitReqs;
	}

	public void setLimitReqs(Integer limitReqs) {
		this.limitReqs = limitReqs;
	}

	public Integer getLimitQps() {
		return limitQps;
	}

	public void setLimitQps(Integer limitQps) {
		this.limitQps = limitQps;
	}

	public Byte getLimitSpeed() {
		return limitSpeed;
	}

	public void setLimitSpeed(Byte limitSpeed) {
		this.limitSpeed = limitSpeed;
	}

	public Integer getBidType() {
		return bidType;
	}

	public void setBidType(Integer bidType) {
		this.bidType = bidType;
	}

	public Integer getBidFloor() {
		return bidFloor;
	}

	public void setBidFloor(Integer bidFloor) {
		this.bidFloor = bidFloor;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getIsQuantityLimit() {
		return isQuantityLimit;
	}

	public void setIsQuantityLimit(Integer isQuantityLimit) {
		this.isQuantityLimit = isQuantityLimit;
	}

}
