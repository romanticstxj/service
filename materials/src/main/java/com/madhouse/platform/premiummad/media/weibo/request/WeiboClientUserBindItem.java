package com.madhouse.platform.premiummad.media.weibo.request;

import java.util.List;

public class WeiboClientUserBindItem {
	// 审核通过的广告主ID required
	private String client_id;

	// 客户营业执照全称
	private String client_name;

	// 发布博文所用的uid
	private String uid;

	// 与绑定uid相关的资质文件信息
	private List<String> qualification_files;

	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public String getClient_name() {
		return client_name;
	}

	public void setClient_name(String client_name) {
		this.client_name = client_name;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public List<String> getQualification_files() {
		return qualification_files;
	}

	public void setQualification_files(List<String> qualification_files) {
		this.qualification_files = qualification_files;
	}
}
