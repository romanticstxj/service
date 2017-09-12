package com.madhouse.platform.premiummad.media.funadx.request;

public class FunadxPMRequest {
	private Integer point;// 曝光监测时点, 必须填写。-1为终点监播
	private String url;// 必须填写

	public Integer getPoint() {
		return point;
	}

	public void setPoint(Integer point) {
		this.point = point;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
