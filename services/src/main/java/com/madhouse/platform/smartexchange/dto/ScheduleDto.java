package com.madhouse.platform.smartexchange.dto;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class ScheduleDto implements Serializable{

	private static final long serialVersionUID = -5492677703400167977L;
	
	private Integer id;
	private Integer supplierAdspaceId;
	private Integer demandAdspaceId;
    private Integer demandId;
    @JSONField(serialzeFeatures = { SerializerFeature.WriteDateUseDateFormat }, format = "yyyy-MM-dd")
	private Date date;
	private Long imp;//为null就是不限制量

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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getImp() {
		return imp;
	}

	public void setImp(Long imp) {
		this.imp = imp;
	}

	public Integer getDemandId() {
		return demandId;
	}

	public void setDemandId(Integer demandId) {
		this.demandId = demandId;
	}
	

}
