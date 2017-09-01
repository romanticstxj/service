package com.madhouse.platform.premiummad.media.weibo.request;

import org.springframework.stereotype.Component;

@Component
public class WeiboMediaInitRequest extends WeiboTokenRequest {
	/**
	 * 视频名称，必须带有后缀（仅支持 .mp4）
	 */
	private String name;

	/**
	 * 文件 md5
	 */
	private String check;

	/**
	 * 文件大小（字节）
	 */
	private int length;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCheck() {
		return check;
	}

	public void setCheck(String check) {
		this.check = check;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
}
