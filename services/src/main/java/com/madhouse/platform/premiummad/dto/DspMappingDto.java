package com.madhouse.platform.premiummad.dto;

import com.madhouse.platform.premiummad.annotation.NotNullAndBlank;

public class DspMappingDto {
	
	private Integer adspaceId; //我方广告位id，设置为了表操作
	@NotNullAndBlank
	private Integer dspId; //DSP在我们系统的ID
	@NotNullAndBlank
	private Integer dspMediaId; //DSP方的媒体id
	@NotNullAndBlank
	private String dspAdspaceKey; //DSP方的广告位Key

	public Integer getAdspaceId() {
		return adspaceId;
	}

	public void setAdspaceId(Integer adspaceId) {
		this.adspaceId = adspaceId;
	}

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
