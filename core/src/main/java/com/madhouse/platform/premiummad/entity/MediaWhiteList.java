package com.madhouse.platform.premiummad.entity;

public class MediaWhiteList extends BaseEntity{

	private Integer id;
	
    private Integer mediaId;
    
    private String mediaName;
    
    private String description;
    
    private Integer mediaCategory;
    
    private String mediaCategoryName;

    private Integer status;

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

}