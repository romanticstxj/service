package com.madhouse.platform.premiummad.media.model;

public class FunadxDetailResponse {
	private String crid;
	private String adm;
	private Integer result;
	private String reason;

	public String getCrid() {
		return crid;
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public void setCrid(String crid) {
		this.crid = crid;
	}

	public String getAdm() {
		return adm;
	}

	public void setAdm(String adm) {
		this.adm = adm;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
