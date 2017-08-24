package com.madhouse.platform.premiummad.media.model;

import java.util.List;

public class LetvStatusRequest extends LetvTokenRequest {

	private List<String> adurl;

	public List<String> getAdurl() {
		return adurl;
	}

	public void setAdurl(List<String> adurl) {
		this.adurl = adurl;
	}

}
