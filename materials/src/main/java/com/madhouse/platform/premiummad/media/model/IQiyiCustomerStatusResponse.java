package com.madhouse.platform.premiummad.media.model;

import java.util.List;

public class IQiyiCustomerStatusResponse {

	private String code;
	private List<IQiyiCustomerStatusDetail> results;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<IQiyiCustomerStatusDetail> getResults() {
		return results;
	}

	public void setResults(List<IQiyiCustomerStatusDetail> results) {
		this.results = results;
	}

	@Override
	public String toString() {
		return "IQiyiCustomerStatusResponse{" + "code='" + code + '\'' + ", results=" + results + '}';
	}
}
