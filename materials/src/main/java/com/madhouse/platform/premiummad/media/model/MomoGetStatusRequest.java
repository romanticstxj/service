package com.madhouse.platform.premiummad.media.model;

import java.util.List;

public class MomoGetStatusRequest {

	/**
	 * dspid : 123 
	 * crids : ["123","234"]
	 */
	private String dspid;
	private List<String> crids;

	public String getDspid() {
		return dspid;
	}

	public void setDspid(String dspid) {
		this.dspid = dspid;
	}

	public List<String> getCrids() {
		return crids;
	}

	public void setCrids(List<String> crids) {
		this.crids = crids;
	}
}
