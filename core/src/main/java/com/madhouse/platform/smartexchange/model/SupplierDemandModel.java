package com.madhouse.platform.smartexchange.model;

import java.util.List;

public class SupplierDemandModel {

	private Integer id;
	private String adspaceKey;
	private Integer width;
	private Integer height;
	private Integer adType;
	private Double price;
	private String priceType;
	private Boolean supportHttps;
	
	private String requiresMaterialVerifying; //物料是否需要校验

	private List<DemandAdspaceModel> demandAdspaces;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAdspaceKey() {
		return adspaceKey;
	}

	public void setAdspaceKey(String adspaceKey) {
		this.adspaceKey = adspaceKey;
	}

	public List<DemandAdspaceModel> getDemandAdspaces() {
		return demandAdspaces;
	}

	public void setDemandAdspaces(List<DemandAdspaceModel> demandAdspaces) {
		this.demandAdspaces = demandAdspaces;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getAdType() {
		return adType;
	}

	public void setAdType(Integer adType) {
		this.adType = adType;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}

	public String getRequiresMaterialVerifying() {
		return requiresMaterialVerifying;
	}

	public void setRequiresMaterialVerifying(String requiresMaterialVerifying) {
		this.requiresMaterialVerifying = requiresMaterialVerifying;
	}

	public Boolean getSupportHttps() {
		return supportHttps;
	}

	public void setSupportHttps(Boolean supportHttps) {
		this.supportHttps = supportHttps;
	}
}
