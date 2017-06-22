package com.madhouse.platform.smartexchange.dto;

import com.madhouse.platform.smartexchange.annotation.NotNull;

import java.io.Serializable;

public class DemandAdspaceDto implements Serializable {

	private static final long serialVersionUID = -3350197657943720775L;

	private Integer id;
	@NotNull
	private String name;
	@NotNull
	private Integer demandId;
	@NotNull
	private String adspaceKey;
	private String secretKey;
	private String osType;
	private String remark;
	private Boolean delFlg;
	private String pkgName;
	private String appName;
	
	
	private String demandName;
	private String osTypeName;
	private Integer relatedCount;

	private Integer adType; //广告类型
	private String adTypeName;
	
	private Double price;
	private String priceType;
	private String mimes;
	
	private Integer maxDuration; //视频的最大长度
	private Integer minDuration; //视频的最小长度
	
	public String getMimes() {
		return mimes;
	}

	public void setMimes(String mimes) {
		this.mimes = mimes;
	}

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

	public Integer getDemandId() {
		return demandId;
	}

	public void setDemandId(Integer demandId) {
		this.demandId = demandId;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getAdspaceKey() {
		return adspaceKey;
	}

	public void setAdspaceKey(String adspaceKey) {
		this.adspaceKey = adspaceKey;
	}

	public String getOsType() {
		return osType;
	}

	public void setOsType(String osType) {
		this.osType = osType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Boolean getDelFlg() {
		return delFlg;
	}

	public void setDelFlg(Boolean delFlg) {
		this.delFlg = delFlg;
	}

	public String getDemandName() {
		return demandName;
	}

	public void setDemandName(String demandName) {
		this.demandName = demandName;
	}

	public String getOsTypeName() {
		return osTypeName;
	}

	public void setOsTypeName(String osTypeName) {
		this.osTypeName = osTypeName;
	}

	public Integer getRelatedCount() {
		return relatedCount;
	}

	public void setRelatedCount(Integer relatedCount) {
		this.relatedCount = relatedCount;
	}

	public Integer getAdType() {
		return adType;
	}

	public void setAdType(Integer adType) {
		this.adType = adType;
	}

	public String getAdTypeName() {
		return adTypeName;
	}

	public void setAdTypeName(String adTypeName) {
		this.adTypeName = adTypeName;
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

	public String getPkgName() {
		return pkgName;
	}

	public void setPkgName(String pkgName) {
		this.pkgName = pkgName;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public Integer getMaxDuration() {
		return maxDuration;
	}

	public void setMaxDuration(Integer maxDuration) {
		this.maxDuration = maxDuration;
	}

	public Integer getMinDuration() {
		return minDuration;
	}

	public void setMinDuration(Integer minDuration) {
		this.minDuration = minDuration;
	}

}
