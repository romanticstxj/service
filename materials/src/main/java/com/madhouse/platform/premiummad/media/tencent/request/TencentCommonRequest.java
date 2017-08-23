package com.madhouse.platform.premiummad.media.tencent.request;

/**
 * 素材状态查询请求,对应的字段名称data,对应的是json格式
 */
public class TencentCommonRequest<T> {

	private String token;
	private String dsp_id;
	private String time;
	private String sig;
	private T data;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getDsp_id() {
		return dsp_id;
	}

	public void setDsp_id(String dsp_id) {
		this.dsp_id = dsp_id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getSig() {
		return sig;
	}

	public void setSig(String sig) {
		this.sig = sig;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
