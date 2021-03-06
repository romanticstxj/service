package com.madhouse.platform.premiummad.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.madhouse.platform.premiummad.annotation.NotNullAndBlank;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.validator.Insert;
import com.madhouse.platform.premiummad.validator.Update;
import com.madhouse.platform.premiummad.validator.UpdateStatus;

public class PolicyDto implements Serializable{

	private static final long serialVersionUID = -3532246711555090831L;
	
	@NotNull(message=SystemConstant.ErrorMessage.NO_UPDATE_ID, groups={Update.class, UpdateStatus.class})
    private Integer id;
	
	private Integer dealId;
    @NotNullAndBlank
    private String name;
    @NotNull(message=SystemConstant.ErrorMessage.NO_UPDATE_TYPE, groups={Update.class, UpdateStatus.class, Insert.class})
    private Integer type; //策略类型(1: PDB, 2: PD, 4: PMP, 8: RTB)
    @NotNullAndBlank
    @Min(message=SystemConstant.ErrorMessage.ERROR_WEIGHT_FORMAT, value=1, groups={Update.class, UpdateStatus.class})
    private Integer weight; //权重
    @NotNullAndBlank
//    @NotPast(groups={Update.class, Insert.class})
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
    
    private Integer isOsTargeting;
    @NotNullAndBlank
    private String osTargeting; //OS定向(多个用半角逗号分隔, 1:Android, 2:iOS, 3: 其它)
    
    private Integer isConnTargeting;
    @NotNullAndBlank
    private String connTargeting; //连网方式定向(多个用半角逗号分隔, 1:WIFI, 2:2G, 3:3G, 4:4G)
    @NotNullAndBlank
    private Integer isQuantityLimit; //0：不限投放量，1：限制投放量

    private Byte limitType; //总量控制(1: 每天, 2: 整单)

    private Integer limitReqs;

    private Byte limitSpeed; //投放速度(1:加速投放, 2:匀速投放)
    
    private Integer pushRatio = 100; //推送流量比（大于100的整数，为返还流量点数 + 购买流量）

    @NotNull(message=SystemConstant.ErrorMessage.NO_UPDATE_STATUS, groups=UpdateStatus.class)
    private Byte status;
    @Length(max=SystemConstant.DB.DESC_LENGTH, groups={Update.class, Insert.class})
    private String description;
    
    private Integer overdue = SystemConstant.DB.POLICY_STATUS_NO_OVERDUE;
    
    private List<PolicyAdspaceDto> policyAdspaces;
    
    private List<PolicyDspDto> policyDsps;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDealId() {
		return dealId;
	}

	public void setDealId(Integer dealId) {
		this.dealId = dealId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
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

	public Integer getIsQuantityLimit() {
		return isQuantityLimit;
	}

	public void setIsQuantityLimit(Integer isQuantityLimit) {
		this.isQuantityLimit = isQuantityLimit;
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

	public Byte getLimitSpeed() {
		return limitSpeed;
	}

	public void setLimitSpeed(Byte limitSpeed) {
		this.limitSpeed = limitSpeed;
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

	public List<PolicyAdspaceDto> getPolicyAdspaces() {
		return policyAdspaces;
	}

	public void setPolicyAdspaces(List<PolicyAdspaceDto> policyAdspaces) {
		this.policyAdspaces = policyAdspaces;
	}

	public List<PolicyDspDto> getPolicyDsps() {
		return policyDsps;
	}

	public void setPolicyDsps(List<PolicyDspDto> policyDsps) {
		this.policyDsps = policyDsps;
	}

	public Integer getIsOsTargeting() {
		return isOsTargeting;
	}

	public void setIsOsTargeting(Integer isOsTargeting) {
		this.isOsTargeting = isOsTargeting;
	}

	public Integer getIsConnTargeting() {
		return isConnTargeting;
	}

	public void setIsConnTargeting(Integer isConnTargeting) {
		this.isConnTargeting = isConnTargeting;
	}

	public Integer getPushRatio() {
		return pushRatio;
	}

	public void setPushRatio(Integer pushRatio) {
		this.pushRatio = pushRatio;
	}

	public Integer getOverdue() {
		return overdue;
	}

	public void setOverdue(Integer overdue) {
		this.overdue = overdue;
	}

}
