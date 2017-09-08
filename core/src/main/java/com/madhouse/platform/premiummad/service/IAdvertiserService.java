package com.madhouse.platform.premiummad.service;

import java.util.List;
import java.util.Map;

import com.madhouse.platform.premiummad.entity.Advertiser;
import com.madhouse.platform.premiummad.entity.SysMedia;
import com.madhouse.platform.premiummad.model.AdvertiserAuditResultModel;
import com.madhouse.platform.premiummad.model.AdvertiserModel;

public interface IAdvertiserService {

	/**
	 * 根据 DSP 广告主Key 查询广告主是否被媒体审核通过
	 * 
	 * @param ids
	 * @param dspId
	 * @return
	 */
	List<AdvertiserAuditResultModel> getAdvertiserAuditResult(String ids, String dspId);

	/**
	 * DSP端上传广告主
	 * 
	 * @param entity
	 */
	void upload(AdvertiserModel entity);

	/**
	 * 校验广告主和指定的媒体是否已审核通过
	 * 
	 * @param uploadedMedias
	 * @param dspId
	 * @param advertiserKey
	 * @param mediaIds
	 * @return
	 */
	String validateAdKeyAndMedias(List<SysMedia> uploadedMedias, String dspId, String advertiserKey);

	/**
	 * 广告主提交媒体后更改状态为审核中
	 * 
	 * @param advertiserIds
	 *            我方的广告主ID
	 */
	void updateStatusAfterUpload(Map<Integer, String> advertiserIdKeys);
	
	/**
	 * 根据媒体返回的结果更新状态
	 * 
	 * @param auditResults
	 */
	void updateStatusToMedia(List<AdvertiserAuditResultModel> auditResults);

	List<Advertiser> queryAll(List<Integer> mediaIds);

	Advertiser queryById(Integer id);

	void auditAdvertiser(String[] ids, Integer status, String reason, Integer userId);
}
