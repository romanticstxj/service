package com.madhouse.platform.premiummad.media.model;

import java.util.Map;

public class AdvertBatchStatusResponse extends TencentResponse {

	private Msg ret_msg;

	public Msg getRet_msg() {
		return ret_msg;
	}

	public void setRet_msg(Msg ret_msg) {
		this.ret_msg = ret_msg;
	}

	public class Msg {

		private Integer total;
		private Integer count;
		private Map<String, Object> records;

		public Integer getTotal() {
			return total;
		}

		public void setTotal(Integer total) {
			this.total = total;
		}

		public Integer getCount() {
			return count;
		}

		public void setCount(Integer count) {
			this.count = count;
		}

		public Map<String, Object> getRecords() {
			return records;
		}

		public void setRecords(Map<String, Object> records) {
			this.records = records;
		}

	}
}
