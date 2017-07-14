package com.madhouse.platform.premiummad.dto;

import java.util.List;

public class MonitorDto {
	/**
	 * 展示监测URL
	 */
	private List<TrackDto> impUrls;
	
	/**
	 * 点击监测URL
	 */
	private List<String> clkUrls;
	
	/**
	 * 品牌安全监测URL
	 */
	private List<String> secUrls;

	public List<TrackDto> getImpUrls() {
		return impUrls;
	}

	public void setImpUrls(List<TrackDto> impUrls) {
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
