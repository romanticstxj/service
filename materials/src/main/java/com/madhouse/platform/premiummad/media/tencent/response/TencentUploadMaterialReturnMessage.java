package com.madhouse.platform.premiummad.media.tencent.response;

public class TencentUploadMaterialReturnMessage {
	private String dsp_order_id;
	private String err_code;
	private String err_msg;
	
	public String getDsp_order_id() {
		return dsp_order_id;
	}
	public void setDsp_order_id(String dsp_order_id) {
		this.dsp_order_id = dsp_order_id;
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
