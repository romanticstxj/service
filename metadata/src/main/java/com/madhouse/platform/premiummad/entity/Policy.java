package com.madhouse.platform.premiummad.entity;

import java.util.List;

public class Policy {
	
	private long id;
	
	private String dealId;
    
    private Integer deliveryType; //策略类型(1: PDB, 2: PD, 4: PMP, 8: RTB)
    
    private Integer weight; //权重
    
    private String startDate;
    
    private String endDate;
    
    private String timeTargeting; //时间定向(格式:0:0,1,2,3&1:19,20,21,22,23)

    private String locationTargeting; //地域定向

    private String osTargeting; //OS定向(多个用半角逗号分隔, 1:Android, 2:iOS, 3: 其它)

    private String connTargeting; //连网方式定向(多个用半角逗号分隔, 1:WIFI, 2:2G, 3:3G, 4:4G)

    private int controlType; //总量控制(0: 不限, 1: 每天, 2: 整单)

    private Integer maxCount;
    
    private Integer controlMethod; //投放速度(1:加速投放, 2:匀速投放)

    private Integer status;

    private List<AdspaceInfo> policyAdspaces;
    
    private List<DSPInfo> policyDsp;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDealId() {
        return dealId;
    }

    public void setDealId(String dealId) {
        this.dealId = dealId;
    }

    public Integer getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(Integer deliveryType) {
        this.deliveryType = deliveryType;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getTimeTargeting() {
        return timeTargeting;
    }

    public void setTimeTargeting(String timeTargeting) {
        this.timeTargeting = timeTargeting;
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

    public int getControlType() {
        return controlType;
    }

    public void setControlType(int controlType) {
        this.controlType = controlType;
    }

    public Integer getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(Integer maxCount) {
        this.maxCount = maxCount;
    }

    public Integer getControlMethod() {
        return controlMethod;
    }

    public void setControlMethod(Integer controlMethod) {
        this.controlMethod = controlMethod;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<AdspaceInfo> getPolicyAdspaces() {
        return policyAdspaces;
    }

    public void setPolicyAdspaces(List<AdspaceInfo> policyAdspaces) {
        this.policyAdspaces = policyAdspaces;
    }

    public List<DSPInfo> getPolicyDsp() {
        return policyDsp;
    }

    public void setPolicyDsp(List<DSPInfo> policyDsp) {
        this.policyDsp = policyDsp;
    }

    

    
}