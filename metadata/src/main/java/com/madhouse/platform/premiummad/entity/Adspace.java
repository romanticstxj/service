package com.madhouse.platform.premiummad.entity;

public class Adspace {
	
	private long id;
	
	private String name;
	
	private String adspaceKey;
	private long mediaId;
	private String mediaName;
	private int terminalType; 
	private int terminalOs;
	private Boolean enableHttps;
	private int bidType;
	private int bidFloor;
	private int adType;	//广告类型
	private Integer layout;	//广告形式
	private String layoutName;
	private int materialType;
	private String materialSize;
	private int materialMaxKbyte;
	private String materialDuration;

	private int logoType;
	
	private String logoSize;
	
	private int logoMaxKbyte;
	
	private int titleMaxLength;
	
	private int descMaxLength;
	
	private int materialCount;

	private int coverType;
	private String coverSize;
	private int coverMaxKbyte;

	private String description;
	
	private int updateType;
	
	private int status;
	
	private int content;

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

	public int getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(int terminalType) {
		this.terminalType = terminalType;
	}

	public int getTerminalOs() {
		return terminalOs;
	}

	public void setTerminalOs(int terminalOs) {
		this.terminalOs = terminalOs;
	}
	public Boolean getEnableHttps() {
        return enableHttps;
    }

    public void setEnableHttps(Boolean enableHttps) {
        this.enableHttps = enableHttps;
    }

    public int getBidType() {
		return bidType;
	}

	public void setBidType(int bidType) {
		this.bidType = bidType;
	}

	public int getBidFloor() {
		return bidFloor;
	}

	public void setBidFloor(int bidFloor) {
		this.bidFloor = bidFloor;
	}

	public int getAdType() {
		return adType;
	}

	public void setAdType(int adType) {
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

	public int getMaterialType() {
		return materialType;
	}

	public void setMaterialType(int materialType) {
		this.materialType = materialType;
	}

	public String getMaterialSize() {
		return materialSize;
	}

	public void setMaterialSize(String materialSize) {
		this.materialSize = materialSize;
	}

	public int getMaterialMaxKbyte() {
		return materialMaxKbyte;
	}

	public void setMaterialMaxKbyte(int materialMaxKbyte) {
		this.materialMaxKbyte = materialMaxKbyte;
	}

	public int getLogoType() {
		return logoType;
	}

	public void setLogoType(int logoType) {
		this.logoType = logoType;
	}

	public String getLogoSize() {
		return logoSize;
	}

	public void setLogoSize(String logoSize) {
		this.logoSize = logoSize;
	}

	public int getLogoMaxKbyte() {
		return logoMaxKbyte;
	}

	public void setLogoMaxKbyte(int logoMaxKbyte) {
		this.logoMaxKbyte = logoMaxKbyte;
	}

	public int getTitleMaxLength() {
		return titleMaxLength;
	}

	public void setTitleMaxLength(int titleMaxLength) {
		this.titleMaxLength = titleMaxLength;
	}

	public int getDescMaxLength() {
		return descMaxLength;
	}

	public void setDescMaxLength(int descMaxLength) {
		this.descMaxLength = descMaxLength;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getUpdateType() {
		return updateType;
	}

	public void setUpdateType(int updateType) {
		this.updateType = updateType;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMaterialDuration() {
		return materialDuration;
	}

	public void setMaterialDuration(String materialDuration) {
		this.materialDuration = materialDuration;
	}

	public int getMaterialCount() {
		return materialCount;
	}

	public void setMaterialCount(int materialCount) {
		this.materialCount = materialCount;
	}

	public int getCoverType() {
		return coverType;
	}

	public void setCoverType(int coverType) {
		this.coverType = coverType;
	}

	public String getCoverSize() {
		return coverSize;
	}

	public void setCoverSize(String coverSize) {
		this.coverSize = coverSize;
	}

	public int getCoverMaxKbyte() {
		return coverMaxKbyte;
	}

	public void setCoverMaxKbyte(int coverMaxKbyte) {
		this.coverMaxKbyte = coverMaxKbyte;
	}
    public int getContent() {
        return content;
    }
    public void setContent(int content) {
        this.content = content;
    }
	
}
