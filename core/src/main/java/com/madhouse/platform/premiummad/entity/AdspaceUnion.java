package com.madhouse.platform.premiummad.entity;

public class AdspaceUnion extends BaseEntity {

	private Integer id;
	private String name;
	private Integer mediaId;
	private Integer terminalType;
	private Integer terminalOs;
	private Integer blockId;
	private Integer supportHttps;
	private Integer bidType;
	private Integer bidFloor;
	private Integer adType; // 广告类型
	private Integer layout; // 广告形式
	private Integer materialType;
	private String materialSize;
	private Integer materialMaxKbyte;
	private Integer materialCount;
	private String materialDuration;
	private Integer logoType;
	private String logoSize;
	private Integer logoMaxKbyte;
	private Integer titleMaxLength;
	private Integer descMaxLength;
	private Integer contentMaxLength; // 正文最大长度
	private Integer coverType;
	private String coverSize;
	private Integer coverMaxKbyte;
	private String description;
	private Integer status;
	// 媒体表相关字段
	private String mediaName;
	private Byte advertiserAuditMode;
	private Byte materialAuditMode;
	private Integer mediaStatus;

	// DSP ADSPACE相关字段
	private String adspaceKey;

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
		this.name = name == null ? null : name.trim();
	}

	public String getAdspaceKey() {
		return adspaceKey;
	}

	public void setAdspaceKey(String adspaceKey) {
		this.adspaceKey = adspaceKey == null ? null : adspaceKey.trim();
	}

	public Integer getMediaId() {
		return mediaId;
	}

	public void setMediaId(Integer mediaId) {
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

	public Integer getSupportHttps() {
		return supportHttps;
	}

	public Integer getBlockId() {
		return blockId;
	}

	public void setBlockId(Integer blockId) {
		this.blockId = blockId;
	}

	public void setSupportHttps(Integer supportHttps) {
		this.supportHttps = supportHttps;
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
	
	public Integer getLayout() {
		return layout;
	}

	public void setLayout(Integer layout) {
		this.layout = layout;
	}

	public Integer getAdType() {
		return adType;
	}

	public void setAdType(Integer adType) {
		this.adType = adType;
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
		this.materialSize = materialSize == null ? null : materialSize.trim();
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
		this.logoSize = logoSize == null ? null : logoSize.trim();
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
		this.description = description == null ? null : description.trim();
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getMaterialCount() {
		return materialCount;
	}

	public void setMaterialCount(Integer materialCount) {
		this.materialCount = materialCount;
	}

	public String getMaterialDuration() {
		return materialDuration;
	}

	public void setMaterialDuration(String materialDuration) {
		this.materialDuration = materialDuration == null ? null : materialDuration.trim();
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
		this.coverSize = coverSize == null ? null : coverSize.trim();
	}

	public Integer getCoverMaxKbyte() {
		return coverMaxKbyte;
	}

	public void setCoverMaxKbyte(Integer coverMaxKbyte) {
		this.coverMaxKbyte = coverMaxKbyte;
	}

	public Integer getMediaStatus() {
		return mediaStatus;
	}

	public void setMediaStatus(Integer mediaStatus) {
		this.mediaStatus = mediaStatus;
	}

	public Integer getContentMaxLength() {
		return contentMaxLength;
	}

	public void setContentMaxLength(Integer contentMaxLength) {
		this.contentMaxLength = contentMaxLength;
	}

	public Byte getAdvertiserAuditMode() {
		return advertiserAuditMode;
	}

	public void setAdvertiserAuditMode(Byte advertiserAuditMode) {
		this.advertiserAuditMode = advertiserAuditMode;
	}

	public Byte getMaterialAuditMode() {
		return materialAuditMode;
	}

	public void setMaterialAuditMode(Byte materialAuditMode) {
		this.materialAuditMode = materialAuditMode;
	}
}
