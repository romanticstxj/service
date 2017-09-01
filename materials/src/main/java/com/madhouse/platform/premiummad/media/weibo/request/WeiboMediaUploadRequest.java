package com.madhouse.platform.premiummad.media.weibo.request;

import org.springframework.stereotype.Component;

@Component
public class WeiboMediaUploadRequest extends WeiboTokenRequest {
	/**
	 * 初始化接口获取到的uploadId
	 */
	private String uploadId;

	/**
	 * 分块编号，从0开始
	 */
	private int partNumber;

	/**
	 * 分块数据md5
	 */
	private String sliceCheck;

	/**
	 * 分块数据（由于json无法直接传输二进制数据，需要先对分块数据进行base64编码） 分块长度对打位 init 接口中返回的 length值
	 */
	private String content;

	public String getUploadId() {
		return uploadId;
	}

	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}

	public int getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(int partNumber) {
		this.partNumber = partNumber;
	}

	public String getSliceCheck() {
		return sliceCheck;
	}

	public void setSliceCheck(String sliceCheck) {
		this.sliceCheck = sliceCheck;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
