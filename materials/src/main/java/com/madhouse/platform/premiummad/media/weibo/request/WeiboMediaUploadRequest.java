package com.madhouse.platform.premiummad.media.weibo.request;

import org.springframework.stereotype.Component;

@Component
public class WeiboMediaUploadRequest extends WeiboTokenRequest {
	/**
	 * 初始化接口获取到的uploadId
	 */
	private String upload_id;

	/**
	 * 分块编号，从0开始
	 */
	private int part_number;

	/**
	 * 分块数据md5
	 */
	private String slice_check;

	/**
	 * 分块数据（由于json无法直接传输二进制数据，需要先对分块数据进行base64编码） 分块长度对打位 init 接口中返回的 length值
	 */
	private String content;
	
	/**
	 * 文件大小（字节）
	 */
	private int file_length;
	
	/**
	 * 文件MD5值
	 */
	private String check;
	
	/**
	 * 初始化返回的length
	 */
	private int length;

	public String getUpload_id() {
		return upload_id;
	}

	public void setUpload_id(String upload_id) {
		this.upload_id = upload_id;
	}

	public int getPart_number() {
		return part_number;
	}

	public void setPart_number(int part_number) {
		this.part_number = part_number;
	}

	public String getSlice_check() {
		return slice_check;
	}

	public void setSlice_check(String slice_check) {
		this.slice_check = slice_check;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getFile_length() {
		return file_length;
	}

	public void setFile_length(int file_length) {
		this.file_length = file_length;
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
