package com.madhouse.platform.premiummad.dto;

public class AdspaceMappingDto {
	
	private Integer adspaceId; //我方广告位id
	
	private String mediaAdspaceKey; //媒体方广告位key
	
	private DspMappingDto[] dspMappingDtos; //dsp方的映射信息，可能多个

	public Integer getAdspaceId() {
		return adspaceId;
	}

	public void setAdspaceId(Integer adspaceId) {
		this.adspaceId = adspaceId;
	}

	public String getMediaAdspaceKey() {
		return mediaAdspaceKey;
	}

	public void setMediaAdspaceKey(String mediaAdspaceKey) {
		this.mediaAdspaceKey = mediaAdspaceKey;
	}

	public DspMappingDto[] getDspMappingDtos() {
		return dspMappingDtos;
	}

	public void setDspMappingDtos(DspMappingDto[] dspMappingDtos) {
		this.dspMappingDtos = dspMappingDtos;
	}
}
