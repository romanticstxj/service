package com.madhouse.platform.premiummad.dto;

import java.util.List;

import com.madhouse.platform.premiummad.annotation.NotNull;

public class AdspaceMappingDto {
	
	@NotNull
	private Integer adspaceId; //我方广告位id
	
	private String mediaAdspaceKey; //媒体方广告位key
	
	private List<DspMappingDto> dspMappingDtos; //dsp方的映射信息，可能多个

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

	public List<DspMappingDto> getDspMappingDtos() {
		return dspMappingDtos;
	}

	public void setDspMappingDtos(List<DspMappingDto> dspMappingDtos) {
		this.dspMappingDtos = dspMappingDtos;
	}

}
