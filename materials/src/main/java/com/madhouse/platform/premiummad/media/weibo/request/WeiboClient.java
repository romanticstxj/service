package com.madhouse.platform.premiummad.media.weibo.request;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class WeiboClient {
	private String client_id;// 客户代码（我们定义的广告主ID）
	private String client_name;// 客户营业执照全称
	private String content_category;// 客户行业
	private String url;// 客户的URL
	private List<WeiboQualificationFile> qualification_files;// 资质文件信息
	private String memo;// 备注信息

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

	public String getContent_category() {
		return content_category;
	}

	public void setContent_category(String content_category) {
		this.content_category = content_category;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<WeiboQualificationFile> getQualification_files() {
		return qualification_files;
	}

	public void setQualification_files(List<WeiboQualificationFile> qualification_files) {
		this.qualification_files = qualification_files;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
}
