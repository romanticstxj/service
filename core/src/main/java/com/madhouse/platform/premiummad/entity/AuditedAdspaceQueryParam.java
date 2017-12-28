package com.madhouse.platform.premiummad.entity;

import java.util.List;

public class AuditedAdspaceQueryParam {
	// DSP ID
	private String dspId;

	// 媒体ID列表
	private List<Integer> mediaIds;

	// 广告位ID列表
	private List<Integer> adspaceIds;

	public String getDspId() {
		return dspId;
	}

	public void setDspId(String dspId) {
		this.dspId = dspId;
	}

	public List<Integer> getMediaIds() {
		return mediaIds;
	}

	public void setMediaIds(List<Integer> mediaIds) {
		this.mediaIds = mediaIds;
	}

	public List<Integer> getAdspaceIds() {
		return adspaceIds;
	}

	public void setAdspaceIds(List<Integer> adspaceIds) {
		this.adspaceIds = adspaceIds;
	}

}
