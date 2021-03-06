package com.madhouse.platform.premiummad.entity;

import java.util.List;

public class AdspaceMapping extends BaseEntity{
	
	private Integer adspaceId; //我方广告位id
	
	private String mediaAdspaceKey; //媒体方广告位key
	
	private List<DspMapping> dspMappings; //dsp方的映射信息，可能多个

	public Integer getAdspaceId() {
		return adspaceId;
	}

	public void setAdspaceId(Integer adspaceId) {
		this.adspaceId = adspaceId;
	}

	public String getMediaAdspaceKey() {
		return this.mediaAdspaceKey;
	}

	public void setMediaAdspaceKey(String mediaAdspaceKey) {
		this.mediaAdspaceKey = mediaAdspaceKey == null ? null : mediaAdspaceKey.trim();
	}

	public List<DspMapping> getDspMappings() {
		return dspMappings;
	}

	public void setDspMappings(List<DspMapping> dspMappings) {
		this.dspMappings = dspMappings;
	}

}
