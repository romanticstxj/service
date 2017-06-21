package com.madhouse.platform.smartexchange.dto;

import java.io.Serializable;
import java.util.List;

public class SupplierDemandDto implements Serializable {

    private static final long serialVersionUID = -208424536439833205L;

    private Integer id;
    private Integer supplierAdspaceId;
    private Integer demandAdspaceId;
    private Integer demandId;
    private Integer weight;
    private Boolean delFlg;
    private List<TargetingDto> targetings;
    private List<ScheduleDto> schedules;

    private String demandAdspaceName;
    private String demandName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSupplierAdspaceId() {
        return supplierAdspaceId;
    }

    public void setSupplierAdspaceId(Integer supplierAdspaceId) {
        this.supplierAdspaceId = supplierAdspaceId;
    }

    public Integer getDemandAdspaceId() {
        return demandAdspaceId;
    }

    public void setDemandAdspaceId(Integer demandAdspaceId) {
        this.demandAdspaceId = demandAdspaceId;
    }

    public Integer getDemandId() {
        return demandId;
    }

    public void setDemandId(Integer demandId) {
        this.demandId = demandId;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Boolean getDelFlg() {
        return delFlg;
    }

    public void setDelFlg(Boolean delFlg) {
        this.delFlg = delFlg;
    }

    public String getDemandAdspaceName() {
        return demandAdspaceName;
    }

    public void setDemandAdspaceName(String demandAdspaceName) {
        this.demandAdspaceName = demandAdspaceName;
    }

    public String getDemandName() {
        return demandName;
    }

    public void setDemandName(String demandName) {
        this.demandName = demandName;
    }

    public List<TargetingDto> getTargetings() {
        return targetings;
    }

    public void setTargetings(List<TargetingDto> targetings) {
        this.targetings = targetings;
    }

	public List<ScheduleDto> getSchedules() {
		return schedules;
	}

	public void setSchedules(List<ScheduleDto> schedules) {
		this.schedules = schedules;
	}
    
}
