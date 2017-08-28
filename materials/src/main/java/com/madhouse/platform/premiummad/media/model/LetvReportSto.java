package com.madhouse.platform.premiummad.media.model;

import java.util.List;
import java.util.Map;

public class LetvReportSto {
	Map<String, List<LetvReportDetailResponse>> records;

	public Map<String, List<LetvReportDetailResponse>> getRecords() {
		return records;
	}

	public void setRecords(Map<String, List<LetvReportDetailResponse>> records) {
		this.records = records;
	}

	@Override
	public String toString() {
		return "LetvReportSto{" + "records=" + records + '}';
	}
}
