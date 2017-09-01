package com.madhouse.platform.premiummad.media.tencent.response;

import com.madhouse.platform.premiummad.media.tencent.request.TencentAdvertiserData;

public class TencentAdvertiserStatusData extends TencentAdvertiserData {
	/**
	 * 审核状态 (1-审核通过，3-审核不通过，8-待审核)
	 */
	private Integer status;

	/**
	 * 审核意见
	 */
	private String vinfo;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getVinfo() {
		return vinfo;
	}

	public void setVinfo(String vinfo) {
		this.vinfo = vinfo;
	}
}
