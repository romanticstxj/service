package com.madhouse.platform.premiummad.media.model;

public class LetvReportDetailResponse {

	private Integer adzoneid;
	private Long bid;
	private Long winbid;
	private Long pv;
	private Long click;
	private Double expend;

	public Integer getAdzoneid() {
		return adzoneid;
	}

	public void setAdzoneid(Integer adzoneid) {
		this.adzoneid = adzoneid;
	}

	public Long getBid() {
		return bid;
	}

	public void setBid(Long bid) {
		this.bid = bid;
	}

	public Long getWinbid() {
		return winbid;
	}

	public void setWinbid(Long winbid) {
		this.winbid = winbid;
	}

	public Long getPv() {
		return pv;
	}

	public void setPv(Long pv) {
		this.pv = pv;
	}

	public Long getClick() {
		return click;
	}

	public void setClick(Long click) {
		this.click = click;
	}

	public Double getExpend() {
		return expend;
	}

	public void setExpend(Double expend) {
		this.expend = expend;
	}

	@Override
	public String toString() {
		return "LetvReportDetailResponse{" + "adzoneid=" + adzoneid + ", bid=" + bid + ", winbid=" + winbid + ", pv=" + pv + ", click=" + click + ", expend=" + expend + '}';
	}
}
