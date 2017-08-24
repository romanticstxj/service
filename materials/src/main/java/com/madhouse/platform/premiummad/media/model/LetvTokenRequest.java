package com.madhouse.platform.premiummad.media.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LetvTokenRequest {

	@Value("${letv.dspid}")
	private Integer dspid;

	@Value("${letv.token}")
	private String token;

	public Integer getDspid() {
		return dspid;
	}

	public void setDspid(Integer dspid) {
		this.dspid = dspid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
