package com.madhouse.platform.premiummad.entity;

public class Adspace {
	
	private long id;
	
	private String name;
	
	private String adspaceKey;
	private long mediaId;
	private String mediaName;
	private Integer terminalType; 
	private Integer terminalOs;
	private Integer enableHttps;
	private Integer bidType;
	private Integer bidFloor;
	private Integer adType;	//广告类型
	private Integer layout;	//广告形式
	private String layoutName;
	private Integer materialType;
	private String materialSize;
	private Integer materialMaxKbyte;
	private String materialDuration;

	private Integer logoType;
	
	private String logoSize;
	
	private Integer logoMaxKbyte;
	
	private Integer titleMaxLength;
	
	private Integer descMaxLength;
	
	private Integer materialCount;

	private Integer coverType;
	private String coverSize;
	private Integer coverMaxKbyte;

	private String description;
	
	private Integer updateType;
	
	private Integer status;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAdspaceKey() {
		return adspaceKey;
	}

	public void setAdspaceKey(String adspaceKey) {
		this.adspaceKey = adspaceKey;
	}

	public long getMediaId() {
		return mediaId;
	}

	public void setMediaId(long mediaId) {
		this.mediaId = mediaId;
	}

	public String getMediaName() {
		return mediaName;
	}

	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}

	public Integer getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(Integer terminalType) {
		this.terminalType = terminalType;
	}

	public Integer getTerminalOs() {
		return terminalOs;
	}

	public void setTerminalOs(Integer terminalOs) {
		this.terminalOs = terminalOs;
	}

	public Integer getEnableHttps() {
		return enableHttps;
	}

	public void setEnableHttps(Integer enableHttps) {
		this.enableHttps = enableHttps;
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

	public Integer getAdType() {
		return adType;
	}

	public void setAdType(Integer adType) {
		this.adType = adType;
	}

	public Integer getLayout() {
		return layout;
	}

	public void setLayout(Integer layout) {
		this.layout = layout;
	}

	public String getLayoutName() {
		return layoutName;
	}

	public void setLayoutName(String layoutName) {
		this.layoutName = layoutName;
	}

	public Integer getMaterialType() {
		return materialType;
	}

	public void setMaterialType(Integer materialType) {
		this.materialType = materialType;
	}

	public String getMaterialSize() {
		return materialSize;
	}

	public void setMaterialSize(String materialSize) {
		this.materialSize = materialSize;
	}

	public Integer getMaterialMaxKbyte() {
		return materialMaxKbyte;
	}

	public void setMaterialMaxKbyte(Integer materialMaxKbyte) {
		this.materialMaxKbyte = materialMaxKbyte;
	}

	public Integer getLogoType() {
		return logoType;
	}

	public void setLogoType(Integer logoType) {
		this.logoType = logoType;
	}

	public String getLogoSize() {
		return logoSize;
	}

	public void setLogoSize(String logoSize) {
		this.logoSize = logoSize;
	}

	public Integer getLogoMaxKbyte() {
		return logoMaxKbyte;
	}

	public void setLogoMaxKbyte(Integer logoMaxKbyte) {
		this.logoMaxKbyte = logoMaxKbyte;
	}

	public Integer getTitleMaxLength() {
		return titleMaxLength;
	}

	public void setTitleMaxLength(Integer titleMaxLength) {
		this.titleMaxLength = titleMaxLength;
	}

	public Integer getDescMaxLength() {
		return descMaxLength;
	}

	public void setDescMaxLength(Integer descMaxLength) {
		this.descMaxLength = descMaxLength;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getUpdateType() {
		return updateType;
	}

	public void setUpdateType(Integer updateType) {
		this.updateType = updateType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMaterialDuration() {
		return materialDuration;
	}

	public void setMaterialDuration(String materialDuration) {
		this.materialDuration = materialDuration;
	}

	public Integer getMaterialCount() {
		return materialCount;
	}

	public void setMaterialCount(Integer materialCount) {
		this.materialCount = materialCount;
	}

	public Integer getCoverType() {
		return coverType;
	}

	public void setCoverType(Integer coverType) {
		this.coverType = coverType;
	}

	public String getCoverSize() {
		return coverSize;
	}

	public void setCoverSize(String coverSize) {
		this.coverSize = coverSize;
	}

	public Integer getCoverMaxKbyte() {
		return coverMaxKbyte;
	}

	public void setCoverMaxKbyte(Integer coverMaxKbyte) {
		this.coverMaxKbyte = coverMaxKbyte;
	}
}
