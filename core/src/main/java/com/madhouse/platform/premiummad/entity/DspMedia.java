package com.madhouse.platform.premiummad.entity;

import com.madhouse.platform.premiummad.annotation.NotNullAndBlank;

public class DspMedia extends BaseEntity{

	private Integer id;
	@NotNullAndBlank
	private Integer dspId;
	@NotNullAndBlank
	private Integer mediaId;
	@NotNullAndBlank
	private Integer adspaceId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDspId() {
		return dspId;
	}

	public void setDspId(Integer dspId) {
		this.dspId = dspId;
	}

	public Integer getMediaId() {
		return mediaId;
	}

	public void setMediaId(Integer mediaId) {
		this.mediaId = mediaId;
	}

	public Integer getAdspaceId() {
		return adspaceId;
	}

	public void setAdspaceId(Integer adspaceId) {
		this.adspaceId = adspaceId;
	}

}
