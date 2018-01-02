package com.madhouse.platform.premiummad.media.autohome.request;

public class BaseRequest {
	/**
	 * 所属DSP ID
	 */
	private int dspId;

	/**
	 * 所属DSP 名称
	 */
	private String dspName;

	/**
	 * 验签串
	 */
	private String sign;

	/**
	 * 签名时间戳
	 */
	private long timestamp;

	public int getDspId() {
		return dspId;
	}

	public void setDspId(int dspId) {
		this.dspId = dspId;
	}

	public String getDspName() {
		return dspName;
	}

	public void setDspName(String dspName) {
		this.dspName = dspName;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

}
