package com.madhouse.platform.premiummad.media.model;

import java.util.List;

public class SohuContentMaterialResponse extends SohuContentBaseResponse {

	private List<SohutvStatusDetailResponse> items;

	public List<SohutvStatusDetailResponse> getItems() {
		return items;
	}

	public void setItems(List<SohutvStatusDetailResponse> items) {
		this.items = items;
	}

}
