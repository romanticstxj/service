package com.madhouse.platform.premiummad.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.validator.Insert;
import com.madhouse.platform.premiummad.validator.Update;

public class MediaWhiteListDto {
	@NotNull(message=SystemConstant.ErrorMessage.NO_UPDATE_ID, groups=Update.class)
	private Integer id;
	@NotNull(message=SystemConstant.ErrorMessage.NO_CREATE_COMPULSORY_PARAMS, groups=Insert.class)
    private Integer mediaId;
	
	private String mediaName;
    
    private String description;
    
    private Integer mediaCategory;
    
    private String mediaCategoryName;

    private Integer status;
    
    private Date createdTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMediaId() {
		return mediaId;
	}

	public void setMediaId(Integer mediaId) {
		this.mediaId = mediaId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getMediaCategory() {
		return mediaCategory;
	}

	public void setMediaCategory(Integer mediaCategory) {
		this.mediaCategory = mediaCategory;
	}

	public String getMediaCategoryName() {
		return mediaCategoryName;
	}

	public void setMediaCategoryName(String mediaCategoryName) {
		this.mediaCategoryName = mediaCategoryName;
	}

	public String getMediaName() {
		return mediaName;
	}

	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
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
}
