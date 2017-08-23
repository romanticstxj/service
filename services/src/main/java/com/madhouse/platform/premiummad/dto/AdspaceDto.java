package com.madhouse.platform.premiummad.dto;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.madhouse.platform.premiummad.annotation.NotNullAndBlank;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.validator.Insert;
import com.madhouse.platform.premiummad.validator.Update;
import com.madhouse.platform.premiummad.validator.UpdateStatus;

public class AdspaceDto implements Serializable{

	private static final long serialVersionUID = -1387509375255091486L;

	@NotNull(message=SystemConstant.ErrorMessage.NO_UPDATE_ID, groups={Update.class, UpdateStatus.class})
	private Integer id;
	@NotNull(message=SystemConstant.ErrorMessage.NO_UPDATE_STATUS, groups=UpdateStatus.class)
	private Integer status;
	@NotNullAndBlank
	private String name;
	
	private String adspaceKey;
	@NotNullAndBlank
	private Integer mediaId;
	private String mediaName;
	private Integer mediaStatus;
	@NotNullAndBlank
	private Integer terminalType; 
	@NotNullAndBlank
	private Integer terminalOs;
	@NotNullAndBlank
	private Integer supportHttps;
	@NotNullAndBlank
	private Integer bidType;
	@NotNullAndBlank
	private Double bidFloor;
	@NotNullAndBlank
	private Integer adType;	//广告类型
	@NotNullAndBlank
	private Integer layout;	//广告形式
	private String layoutName;
	
	private String materialType; //物料格式
	
	private String materialSize;
	
	private Integer materialMaxKbyte;
	
	private Integer materialCount;
	
	private String materialDuration;
	
	private String logoType;
	
	private String logoSize;
	
	private Integer logoMaxKbyte;
	
	private Integer titleMaxLength;
	
	private Integer descMaxLength;

	private String coverType;
	
	private String coverSize;
	
	private Integer coverMaxKbyte;
	
	@Length(max=SystemConstant.DB.DESC_LENGTH, groups={Update.class, Insert.class})
	private String description;
	
	private Date createdTime;
	
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

	public String getAdspaceKey() {
		return adspaceKey;
	}

	public void setAdspaceKey(String adspaceKey) {
		this.adspaceKey = adspaceKey;
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

	public void setSupportHttps(Integer supportHttps) {
		this.supportHttps = supportHttps;
	}

	public Integer getBidType() {
		return bidType;
	}

	public void setBidType(Integer bidType) {
		this.bidType = bidType;
	}

	public Double getBidFloor() {
		return bidFloor;
	}

	public void setBidFloor(Double bidFloor) {
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

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMaterialType() {
		return materialType;
	}

	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}

	public String getLogoType() {
		return logoType;
	}

	public void setLogoType(String logoType) {
		this.logoType = logoType;
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
		this.materialDuration = materialDuration;
	}

	public String getCoverType() {
		return coverType;
	}

	public void setCoverType(String coverType) {
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

	public Integer getMediaStatus() {
		return mediaStatus;
	}

	public void setMediaStatus(Integer mediaStatus) {
		this.mediaStatus = mediaStatus;
	}

	@Override
	public String toString() {
		return "AdspaceDto [id=" + id + ", status=" + status + ", name=" + name + ", adspaceKey=" + adspaceKey
				+ ", mediaId=" + mediaId + ", mediaName=" + mediaName + ", mediaStatus=" + mediaStatus
				+ ", terminalType=" + terminalType + ", terminalOs=" + terminalOs + ", supportHttps=" + supportHttps
				+ ", bidType=" + bidType + ", bidFloor=" + bidFloor + ", adType=" + adType + ", layout=" + layout
				+ ", layoutName=" + layoutName + ", materialType=" + materialType + ", materialSize=" + materialSize
				+ ", materialMaxKbyte=" + materialMaxKbyte + ", materialCount=" + materialCount + ", materialDuration="
				+ materialDuration + ", logoType=" + logoType + ", logoSize=" + logoSize + ", logoMaxKbyte="
				+ logoMaxKbyte + ", titleMaxLength=" + titleMaxLength + ", descMaxLength=" + descMaxLength
				+ ", coverType=" + coverType + ", coverSize=" + coverSize + ", coverMaxKbyte=" + coverMaxKbyte
				+ ", description=" + description + ", createdTime=" + createdTime + "]";
	}

}
