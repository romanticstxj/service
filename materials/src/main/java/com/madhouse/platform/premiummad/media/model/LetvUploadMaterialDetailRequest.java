package com.madhouse.platform.premiummad.media.model;

import java.util.List;

public class LetvUploadMaterialDetailRequest {

	private String url;
	private List<String> landingpage;
	private String advertiser;
	private String startdate;
	private String enddate;
	private String type;
	private Integer duration;
	private List<Integer> media;
	private List<Integer> display;
	private String industry;

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public List<Integer> getDisplay() {
		return display;
	}

	public void setDisplay(List<Integer> display) {
		this.display = display;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<String> getLandingpage() {
		return landingpage;
	}

	public void setLandingpage(List<String> landingpage) {
		this.landingpage = landingpage;
	}

	public String getAdvertiser() {
		return advertiser;
	}

	public void setAdvertiser(String advertiser) {
		this.advertiser = advertiser;
	}

	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public List<Integer> getMedia() {
		return media;
	}

	public void setMedia(List<Integer> media) {
		this.media = media;
	}
}
