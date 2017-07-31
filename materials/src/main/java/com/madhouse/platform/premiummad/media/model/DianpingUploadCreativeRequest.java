package com.madhouse.platform.premiummad.media.model;

import java.util.List;

public class DianpingUploadCreativeRequest {
	private int slotId;// 创意对应广告位id
	private DianpingCreativeInfoRequest creativeInfo;// 本次新增广告物料信息（json格式）
	private String landingPage;// 落地页地址
	private List<String> impressionMonitor;// 曝光打点检测地址（数组，json格式）
	private List<String> clickMonitor;// 点击打点检测地址（数组，json格式）

	public int getSlotId() {
		return slotId;
	}

	public void setSlotId(int slotId) {
		this.slotId = slotId;
	}

	public DianpingCreativeInfoRequest getCreativeInfo() {
		return creativeInfo;
	}

	public void setCreativeInfo(DianpingCreativeInfoRequest creativeInfo) {
		this.creativeInfo = creativeInfo;
	}

	public String getLandingPage() {
		return landingPage;
	}

	public void setLandingPage(String landingPage) {
		this.landingPage = landingPage;
	}

	public List<String> getImpressionMonitor() {
		return impressionMonitor;
	}

	public void setImpressionMonitor(List<String> impressionMonitor) {
		this.impressionMonitor = impressionMonitor;
	}

	public List<String> getClickMonitor() {
		return clickMonitor;
	}

	public void setClickMonitor(List<String> clickMonitor) {
		this.clickMonitor = clickMonitor;
	}

}
