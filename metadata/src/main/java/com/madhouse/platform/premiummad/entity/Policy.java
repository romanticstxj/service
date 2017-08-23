package com.madhouse.platform.premiummad.entity;

import java.util.List;

public class Policy {
	
	private long id;
	
	private String dealId;
    
    private int deliveryType; //策略类型(1: PDB, 2: PD, 4: PMP, 8: RTB)
    
    private int weight; //权重
    
    private String startDate;
    
    private String endDate;
    
    private String timeTargeting; //时间定向(格式:0:0,1,2,3&1:19,20,21,22,23)

    private String locationTargeting; //地域定向

    private String osTargeting; //OS定向(多个用半角逗号分隔, 1:Android, 2:iOS, 3: 其它)

    private String connTargeting; //连网方式定向(多个用半角逗号分隔, 1:WIFI, 2:2G, 3:3G, 4:4G)

    private int controlType; //总量控制(0: 不限, 1: 每天, 2: 整单)

    private int maxCount;
    
    private int controlMethod; //投放速度(1:加速投放, 2:匀速投放)

    private int status;

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

    public int getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(int deliveryType) {
        this.deliveryType = deliveryType;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
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

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public int getControlMethod() {
        return controlMethod;
    }

    public void setControlMethod(int controlMethod) {
        this.controlMethod = controlMethod;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
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