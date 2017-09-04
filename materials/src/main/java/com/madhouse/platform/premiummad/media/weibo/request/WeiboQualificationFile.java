package com.madhouse.platform.premiummad.media.weibo.request;

import org.springframework.stereotype.Component;

@Component
public class WeiboQualificationFile {
	private String file_name;// 资质文件名称
	private String file_url;// 资质文件URL

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public String getFile_url() {
		return file_url;
	}

	public void setFile_url(String file_url) {
		this.file_url = file_url;
	}
}
