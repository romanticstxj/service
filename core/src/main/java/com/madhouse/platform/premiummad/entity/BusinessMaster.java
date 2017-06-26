package com.madhouse.platform.premiummad.entity;

import java.io.Serializable;

public class BusinessMaster implements Serializable {

	private static final long serialVersionUID = 1191421974650764413L;

	private Integer id;
	private String systemId;
	private String businessId;
	private String url;
	private String dataSourceBeanId;
	private String currencyCode;
	private Integer timeZoneOffset;
	private String languageCode;
	private String impUrlPrefix;
	private String clkUrlPrefix;
	private String creativeUrlPrefix;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDataSourceBeanId() {
		return dataSourceBeanId;
	}

	public void setDataSourceBeanId(String dataSourceBeanId) {
		this.dataSourceBeanId = dataSourceBeanId;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public Integer getTimeZoneOffset() {
		return timeZoneOffset;
	}

	public void setTimeZoneOffset(Integer timeZoneOffset) {
		this.timeZoneOffset = timeZoneOffset;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public String getImpUrlPrefix() {
		return impUrlPrefix;
	}

	public void setImpUrlPrefix(String impUrlPrefix) {
		this.impUrlPrefix = impUrlPrefix;
	}

	public String getClkUrlPrefix() {
		return clkUrlPrefix;
	}

	public void setClkUrlPrefix(String clkUrlPrefix) {
		this.clkUrlPrefix = clkUrlPrefix;
	}

	public String getCreativeUrlPrefix() {
		return creativeUrlPrefix;
	}

	public void setCreativeUrlPrefix(String creativeUrlPrefix) {
		this.creativeUrlPrefix = creativeUrlPrefix;
	}


}
