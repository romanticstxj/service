package com.madhouse.platform.premiummad.media.model;

public class WeiboClientStatusDetail {
	private String client_id;// 客户ID
	private String verify_status;// 审核状态描述
	private String verify_info;// 原因描述

	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public String getVerify_status() {
		return verify_status;
	}

	public void setVerify_status(String verify_status) {
		this.verify_status = verify_status;
	}

	public String getVerify_info() {
		return verify_info;
	}

	public void setVerify_info(String verify_info) {
		this.verify_info = verify_info;
	}
}
