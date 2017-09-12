package com.madhouse.platform.premiummad.media.letv.request;

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
