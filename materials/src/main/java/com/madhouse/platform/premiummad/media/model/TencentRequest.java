package com.madhouse.platform.premiummad.media.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TencentRequest {

	@Value("${tencent.dspId}")
	private String dspId;

	@Value("${tencent.token}")
	private String token;

	public String getDspId() {
		return dspId;
	}

	public void setDspId(String dspId) {
		this.dspId = dspId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
