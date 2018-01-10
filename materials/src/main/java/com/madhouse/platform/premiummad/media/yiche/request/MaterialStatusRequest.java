package com.madhouse.platform.premiummad.media.yiche.request;

public class MaterialStatusRequest extends BaseRequest {
	// 素材托管 ID
	private String depositId;

	public String getDepositId() {
		return depositId;
	}

	public void setDepositId(String depositId) {
		this.depositId = depositId;
	}
}
