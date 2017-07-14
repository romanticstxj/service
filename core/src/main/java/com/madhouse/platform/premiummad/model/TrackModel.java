package com.madhouse.platform.premiummad.model;

import com.madhouse.platform.premiummad.annotation.NotNullAndBlank;

public class TrackModel {
	/**
	 * 监测时间： 0：开始、-1：中点、-2：结束、>0：实际秒数
	 * default : 0
	 */
	private Integer startDelay = Integer.valueOf(0);
	
	/**
	 * 监测URL
	 */
	@NotNullAndBlank
	private String url;

	public Integer getStartDelay() {
		return startDelay;
	}

	public void setStartDelay(Integer startDelay) {
		this.startDelay = startDelay;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
