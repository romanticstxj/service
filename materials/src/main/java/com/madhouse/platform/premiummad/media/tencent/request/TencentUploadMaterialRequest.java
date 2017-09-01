package com.madhouse.platform.premiummad.media.tencent.request;

import java.util.List;

/**
 * 广告位上传请求,对应的字段名称data,对应的是json格式
 */
public class TencentUploadMaterialRequest {

	private String token;
	private String dsp_id;
	private String time;
	private String sig;
	private List<TencentUploadMaterialData> data;

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

	public List<TencentUploadMaterialData> getData() {
		return data;
	}

	public void setData(List<TencentUploadMaterialData> data) {
		this.data = data;
	}
}
