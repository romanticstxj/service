package com.madhouse.platform.premiummad.media.weibo.request;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WeiboTokenRequest {
	@Value("${weibo.dspid}")
	private String dspid;
	@Value("${weibo.token}")
	private String token;

	public String getDspid() {
		return dspid;
	}

	public void setDspid(String dspid) {
		this.dspid = dspid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
