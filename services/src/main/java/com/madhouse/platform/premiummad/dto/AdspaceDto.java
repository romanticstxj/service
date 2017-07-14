package com.madhouse.platform.premiummad.dto;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.madhouse.platform.premiummad.annotation.NotNullAndBlank;
import com.madhouse.platform.premiummad.constant.SystemCommonMsg;
import com.madhouse.platform.premiummad.validator.Update;
import com.madhouse.platform.premiummad.validator.UpdateStatus;

public class AdspaceDto implements Serializable{

	private static final long serialVersionUID = -1387509375255091486L;

	@NotNull(message=SystemCommonMsg.NO_UPDATE_ID, groups={Update.class, UpdateStatus.class})
	private Integer id;
	@NotNull(message=SystemCommonMsg.NO_UPDATE_STATUS, groups=UpdateStatus.class)
	private Integer status;
	@NotNullAndBlank
	private String name;
	
	private String adspaceKey;
	@NotNullAndBlank
	private Integer mediaId;
	private String mediaName;
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
	@NotNullAndBlank
	private Integer materialType;
	@NotNullAndBlank
	private String materialSize;
	@NotNullAndBlank
	private Integer materialMaxKbyte;
	
	private Integer logoType;
	
	private String logoSize;
	
	private Integer logoMaxKbyte;
	
	private Integer titleMaxLength;
	
	private Integer descMaxLength;
	
	private Integer mainPicNumber;

	private Integer videoType;
	
	private Integer videoSize;
	
	private Integer videoMaxKbyte;
	
	private String videoDuration;
	
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

	public Integer getMainPicNumber() {
		return mainPicNumber;
	}

	public void setMainPicNumber(Integer mainPicNumber) {
		this.mainPicNumber = mainPicNumber;
	}

	public Integer getVideoType() {
		return videoType;
	}

	public void setVideoType(Integer videoType) {
		this.videoType = videoType;
	}

	public Integer getVideoSize() {
		return videoSize;
	}

	public void setVideoSize(Integer videoSize) {
		this.videoSize = videoSize;
	}

	public Integer getVideoMaxKbyte() {
		return videoMaxKbyte;
	}

	public void setVideoMaxKbyte(Integer videoMaxKbyte) {
		this.videoMaxKbyte = videoMaxKbyte;
	}

	public String getVideoDuration() {
		return videoDuration;
	}

	public void setVideoDuration(String videoDuration) {
		this.videoDuration = videoDuration;
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

	@Override
	public String toString() {
		return "AdspaceDto [id=" + id + ", status=" + status + ", name=" + name + ", adspaceKey=" + adspaceKey
				+ ", mediaId=" + mediaId + ", mediaName=" + mediaName + ", terminalType=" + terminalType
				+ ", terminalOs=" + terminalOs + ", supportHttps=" + supportHttps + ", bidType=" + bidType
				+ ", bidFloor=" + bidFloor + ", adType=" + adType + ", layout=" + layout + ", layoutName=" + layoutName
				+ ", materialType=" + materialType + ", materialSize=" + materialSize + ", materialMaxKbyte="
				+ materialMaxKbyte + ", logoType=" + logoType + ", logoSize=" + logoSize + ", logoMaxKbyte="
				+ logoMaxKbyte + ", titleMaxLength=" + titleMaxLength + ", descMaxLength=" + descMaxLength
				+ ", mainPicNumber=" + mainPicNumber + ", videoType=" + videoType + ", videoSize=" + videoSize
				+ ", videoMaxKbyte=" + videoMaxKbyte + ", videoDuration=" + videoDuration + ", description="
				+ description + ", createdTime=" + createdTime + "]";
	}

}
