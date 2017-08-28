package com.madhouse.platform.premiummad.media.model;

import java.util.List;

public class FunadxStatusSuccessDetailResponse {
	private Integer total;
	private List<FunadxDetailResponse> records;

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public List<FunadxDetailResponse> getRecords() {
		return records;
	}

	public void setRecords(List<FunadxDetailResponse> records) {
		this.records = records;
	}
}
