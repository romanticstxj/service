package com.madhouse.platform.premiummad.media.tencent.response;

public class TencentUploadAdvertiserRetMsg {
	/**
	 * 广告主ID
	 */
	private Integer id;
	
	/**
	 * 广告主名称
	 */
	private String name;
	
	/**
	 * 成功不返回，失败返回错误编码
	 */
	private String err_code;
	
	/**
	 * 成功不返回，失败返回错误编码
	 */
	private String err_msg;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getErr_code() {
		return err_code;
	}

	public void setErr_code(String err_code) {
		this.err_code = err_code;
	}

	public String getErr_msg() {
		return err_msg;
	}

	public void setErr_msg(String err_msg) {
		this.err_msg = err_msg;
	}
}
