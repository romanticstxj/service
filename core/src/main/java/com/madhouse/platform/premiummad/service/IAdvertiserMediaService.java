package com.madhouse.platform.premiummad.service;

import java.util.List;

import com.madhouse.platform.premiummad.model.AdvertiserMediaAuditResultModel;
import com.madhouse.platform.premiummad.model.AdvertiserMediaModel;
import com.madhouse.platform.premiummad.model.OperationResultModel;

public interface IAdvertiserMediaService {

	/**
	 * 根据 DSP 广告主Key 查询广告主是否被媒体审核通过
	 * @param ids
	 * @param dspId
	 * @return
	 */
	List<AdvertiserMediaAuditResultModel> getAdvertiserMediaAuditResult(String ids, String dspId);

	/**
	 * DSP端上传广告主
	 * @param entity
	 * @return operationResultModel
	 */
	OperationResultModel upload(AdvertiserMediaModel entity);
}
