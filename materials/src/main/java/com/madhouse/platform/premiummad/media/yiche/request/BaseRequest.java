package com.madhouse.platform.premiummad.media.yiche.request;

public class BaseRequest {
	/**
	 * DSP 注册 ADX 所得值
	 */
	private String dspId;

	/**
	 * 验签串
	 */
	private String sign;

	/**
	 * 当前请求时间戳
	 */
	private String timestamp;

	public String getDspId() {
		return dspId;
	}

	public void setDspId(String dspId) {
		this.dspId = dspId;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}
