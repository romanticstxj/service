package com.madhouse.platform.premiummad.dto;

public class DspMappingDto {
	
	private Integer dspId; //DSP在我们系统的ID
	
	private Integer dspMediaId; //DSP方的媒体id
	
	private String dspAdspaceKey; //DSP方的广告位Key

	public Integer getDspId() {
		return dspId;
	}

	public void setDspId(Integer dspId) {
		this.dspId = dspId;
	}

	public Integer getDspMediaId() {
		return dspMediaId;
	}

	public void setDspMediaId(Integer dspMediaId) {
		this.dspMediaId = dspMediaId;
	}

	public String getDspAdspaceKey() {
		return dspAdspaceKey;
	}

	public void setDspAdspaceKey(String dspAdspaceKey) {
		this.dspAdspaceKey = dspAdspaceKey;
	}
	
	
}
