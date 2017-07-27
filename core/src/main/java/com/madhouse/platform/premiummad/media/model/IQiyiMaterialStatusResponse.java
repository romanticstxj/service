package com.madhouse.platform.premiummad.media.model;

import java.util.List;

public class IQiyiMaterialStatusResponse {
	private String code;
	private List<IQiyiMaterialStatusDetailResponse> results;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<IQiyiMaterialStatusDetailResponse> getResults() {
		return results;
	}

	public void setResults(List<IQiyiMaterialStatusDetailResponse> results) {
		this.results = results;
	}

}
