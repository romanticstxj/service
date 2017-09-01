package com.madhouse.platform.premiummad.media.momo.response;

import java.util.List;

public class MomoGetStatusResponse {

	/**
	 * ec : 200 em : 查询成功 data : [{"crid":"123","status":1,"reason":""},{}]
	 */
	private int ec;
	
	private String em;
	
	/**
	 * crid : 123 status : 1 reason :
	 */

	private List<DataBean> data;

	public int getEc() {
		return ec;
	}

	public void setEc(int ec) {
		this.ec = ec;
	}

	public String getEm() {
		return em;
	}

	public void setEm(String em) {
		this.em = em;
	}

	public List<DataBean> getData() {
		return data;
	}

	public void setData(List<DataBean> data) {
		this.data = data;
	}

	public static class DataBean {
		private String crid;
		private int status;
		private String reason;

		public String getCrid() {
			return crid;
		}

		public void setCrid(String crid) {
			this.crid = crid;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public String getReason() {
			return reason;
		}

		public void setReason(String reason) {
			this.reason = reason;
		}
	}
}
