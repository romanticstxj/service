package com.madhouse.platform.premiummad.media.model;

import java.util.List;

public class SohuCustomerListResponse extends SohuContentBaseResponse {

	List<SohuCustomerListDetail> items;

	public List<SohuCustomerListDetail> getItems() {
		return items;
	}

	public void setItems(List<SohuCustomerListDetail> items) {
		this.items = items;
	}
}
