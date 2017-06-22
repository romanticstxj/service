package com.madhouse.platform.smartexchange.dto;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class UserMediaAdspaceDto implements Serializable {
	private static final long serialVersionUID = -5819840240091444689L;

	private Integer id;
	private Integer userId;
	
	private Integer mediaId;
	@JSONField(serialzeFeatures = { SerializerFeature.WriteNullStringAsEmpty })
	private String mediaName;
	private String mediaType;//媒体类型
	

	private Integer adspaceId;
	@JSONField(serialzeFeatures = { SerializerFeature.WriteNullStringAsEmpty })
	private String adspaceName;
	private String adspaceKey;
	
	private Integer[] userIds;
	private Integer[] mediaIds;
	private Integer[] adspaceIds;
	
	private List<UserMediaAdspaceDto> userMediaAdspaces;

	private Boolean isSuper=false; //如果为true就是超级用户
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getAdspaceId() {
		return adspaceId;
	}

	public void setAdspaceId(Integer adspaceId) {
		this.adspaceId = adspaceId;
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

	public String getAdspaceName() {
		return adspaceName;
	}

	public void setAdspaceName(String adspaceName) {
		this.adspaceName = adspaceName;
	}

	public Integer[] getUserIds() {
		return userIds;
	}

	public void setUserIds(Integer[] userIds) {
		this.userIds = userIds;
	}

	public Integer[] getMediaIds() {
		return mediaIds;
	}

	public void setMediaIds(Integer[] mediaIds) {
		this.mediaIds = mediaIds;
	}

	public Integer[] getAdspaceIds() {
		return adspaceIds;
	}

	public void setAdspaceIds(Integer[] adspaceIds) {
		this.adspaceIds = adspaceIds;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public Boolean getIsSuper() {
		return isSuper;
	}

	public void setIsSuper(Boolean isSuper) {
		this.isSuper = isSuper;
	}

	public String getAdspaceKey() {
		return adspaceKey;
	}

	public void setAdspaceKey(String adspaceKey) {
		this.adspaceKey = adspaceKey;
	}

	public List<UserMediaAdspaceDto> getUserMediaAdspaces() {
		return userMediaAdspaces;
	}

	public void setUserMediaAdspaces(List<UserMediaAdspaceDto> userMediaAdspaces) {
		this.userMediaAdspaces = userMediaAdspaces;
	}

	@Override
	public String toString() {
		return "UserMediaAdspaceDto [id=" + id + ", userId=" + userId + ", mediaId=" + mediaId + ", mediaName="
				+ mediaName + ", mediaType=" + mediaType + ", adspaceId=" + adspaceId + ", adspaceName=" + adspaceName
				+ ", adspaceKey=" + adspaceKey + ", userIds=" + Arrays.toString(userIds) + ", mediaIds="
				+ Arrays.toString(mediaIds) + ", adspaceIds=" + Arrays.toString(adspaceIds) + ", userMediaAdspaces="
				+ userMediaAdspaces + ", isSuper=" + isSuper + "]";
	}

}
