package com.madhouse.platform.premiummad.media.sohu.response;

import java.util.List;

public class SohuContentMaterialResponse extends SohuContentBaseResponse {

	private List<SohuStatusDetailResponse> items;

	public List<SohuStatusDetailResponse> getItems() {
		return items;
	}

	public void setItems(List<SohuStatusDetailResponse> items) {
		this.items = items;
	}

}
