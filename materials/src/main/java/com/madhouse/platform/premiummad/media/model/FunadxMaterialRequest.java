package com.madhouse.platform.premiummad.media.model;

import java.util.List;

public class FunadxMaterialRequest {
	private String crid;// 素材ID，必须填写(对应物料的materialId)
	private String advertiser;// “广告主的中文名称”, //必须填写
	private String adm;// 物料地址，必须填写
	private String startdate;// 物料生效时间，格式要求：YYYY-mm-dd，必须填写
	private String enddate;// 物料失效时间，格式要求：YYYY-mm-dd，必须填写
	private Integer duration;// 单位为秒。素材用于贴片的话此栏位必须有值，否则无法投放
	private List<FunadxPMRequest> pm;// 曝光监测对象
	private List<String> cm;// 点击监测地址
	private String type;// 物料类型：必填，图片（image）、视频（video）、Flash（flash）
	private String landingpage;// 落地页地址(对应物料的targetUrl)

	public String getLandingpage() {
		return landingpage;
	}

	public void setLandingpage(String landingpage) {
		this.landingpage = landingpage;
	}

	public String getCrid() {
		return crid;
	}

	public void setCrid(String crid) {
		this.crid = crid;
	}

	public String getAdvertiser() {
		return advertiser;
	}

	public void setAdvertiser(String advertiser) {
		this.advertiser = advertiser;
	}

	public String getAdm() {
		return adm;
	}

	public void setAdm(String adm) {
		this.adm = adm;
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

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public List<FunadxPMRequest> getPm() {
		return pm;
	}

	public void setPm(List<FunadxPMRequest> pm) {
		this.pm = pm;
	}

	public List<String> getCm() {
		return cm;
	}

	public void setCm(List<String> cm) {
		this.cm = cm;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
