package com.madhouse.platform.premiummad.model;

import java.util.List;

public class MonitorModel {
	/**
	 * 展示监测URL
	 */
	private List<TrackModel> impUrls;
	
	/**
	 * 点击监测URL
	 */
	private List<String> clkUrls;
	
	/**
	 * 品牌安全监测URL
	 */
	private List<String> secUrls;

	public List<TrackModel> getImpUrls() {
		return impUrls;
	}

	public void setImpUrls(List<TrackModel> impUrls) {
		this.impUrls = impUrls;
	}

	public List<String> getClkUrls() {
		return clkUrls;
	}

	public void setClkUrls(List<String> clkUrls) {
		this.clkUrls = clkUrls;
	}

	public List<String> getSecUrls() {
		return secUrls;
	}

	public void setSecUrls(List<String> secUrls) {
		this.secUrls = secUrls;
	}
}
