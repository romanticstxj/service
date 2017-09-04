package com.madhouse.platform.premiummad.media.tencent.request;

/**
 * 腾讯基本资质内容
 */
public class TencentQualidata {
	/**
	 * 资质名称
	 */
	private String name;

	/**
	 * 资质文件
	 */
	private String fileUrl;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
}
