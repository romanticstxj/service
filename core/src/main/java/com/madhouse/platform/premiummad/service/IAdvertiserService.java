package com.madhouse.platform.premiummad.service;

import java.util.List;

import com.madhouse.platform.premiummad.entity.SysMedia;
import com.madhouse.platform.premiummad.model.AdvertiserAuditResultModel;
import com.madhouse.platform.premiummad.model.AdvertiserModel;

public interface IAdvertiserService {

	/**
	 * 根据 DSP 广告主Key 查询广告主是否被媒体审核通过
	 * @param ids
	 * @param dspId
	 * @return
	 */
	List<AdvertiserAuditResultModel> getAdvertiserAuditResult(String ids, String dspId);

	/**
	 * DSP端上传广告主
	 * @param entity
	 */
	void upload(AdvertiserModel entity);

	/**
	 * 校验广告主和指定的媒体是否已审核通过
	 * @param uploadedMedias
	 * @param dspId
	 * @param advertiserKey
	 * @param mediaIds
	 * @return
	 */
	String validateAdKeyAndMedias(List<SysMedia> uploadedMedias, String dspId, String advertiserKey);
}
