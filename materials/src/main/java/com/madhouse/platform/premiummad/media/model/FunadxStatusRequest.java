package com.madhouse.platform.premiummad.media.model;

import java.util.List;

public class FunadxStatusRequest extends FunadxTokenRequest {
	private List<String> crid;

	public List<String> getCrid() {
		return crid;
	}

	public void setCrid(List<String> crid) {
		this.crid = crid;
	}
}
