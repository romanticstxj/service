package com.madhouse.platform.premiummad.entity;

import com.madhouse.platform.premiummad.annotation.NotNullAndBlank;

public class DspAuth {

	private Integer id;
	
	@NotNullAndBlank
	private Integer dspId;
	@NotNullAndBlank
	private Adspace[] adspaces;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDspId() {
		return dspId;
	}

	public void setDspId(Integer dspId) {
		this.dspId = dspId;
	}

	public Adspace[] getAdspaces() {
		return adspaces;
	}

	public void setAdspaces(Adspace[] adspaces) {
		this.adspaces = adspaces;
	}
	
}
