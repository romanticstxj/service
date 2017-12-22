package com.madhouse.platform.premiummad.media.autohome.request;

import java.util.List;

public class CreativeUploadRequest<T> extends BaseRequest {
	private List<T> creative;

	public List<T> getCreative() {
		return creative;
	}

	public void setCreative(List<T> creative) {
		this.creative = creative;
	}
}
