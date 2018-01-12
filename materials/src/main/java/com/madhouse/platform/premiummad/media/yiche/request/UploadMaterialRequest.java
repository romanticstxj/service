package com.madhouse.platform.premiummad.media.yiche.request;


public class UploadMaterialRequest extends BaseRequest {
	// 物料失效时间的时间戳
	private long expireTime;

	// 素材信息对象
	private CreativeData materialList;

	public long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}

	public CreativeData getMaterialList() {
		return materialList;
	}

	public void setMaterialList(CreativeData materialList) {
		this.materialList = materialList;
	}
}
