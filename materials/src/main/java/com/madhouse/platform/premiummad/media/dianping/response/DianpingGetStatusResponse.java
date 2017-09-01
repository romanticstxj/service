package com.madhouse.platform.premiummad.media.dianping.response;

import java.util.List;

public class DianpingGetStatusResponse {

	/**
	 * ret : 0
	 * msg :
	 * data : {"resultList":[{"creativeId":212212,"status":1,"reason":""},{"creativeId":212213,"status":0,"reason":""},{"creativeId":212214,"status":-1,"reason":"禁投行业"}]}
	 */
	private int ret;
	private String msg;
	private DataBean data;

	public int getRet() {
		return ret;
	}

	public void setRet(int ret) {
		this.ret = ret;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public DataBean getData() {
		return data;
	}

	public void setData(DataBean data) {
		this.data = data;
	}

	public static class DataBean {
		/**
		 * creativeId : 212212 status : 1 reason :
		 */
		private List<ResultListBean> resultList;

		public List<ResultListBean> getResultList() {
			return resultList;
		}

		public void setResultList(List<ResultListBean> resultList) {
			this.resultList = resultList;
		}

		public static class ResultListBean {
			private int creativeId;
			private int status;
			private String reason;

			public int getCreativeId() {
				return creativeId;
			}

			public void setCreativeId(int creativeId) {
				this.creativeId = creativeId;
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
}
