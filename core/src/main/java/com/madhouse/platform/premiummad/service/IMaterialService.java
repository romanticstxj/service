package com.madhouse.platform.premiummad.service;

import java.util.List;
import java.util.Map;

import com.madhouse.platform.premiummad.model.MaterialAuditResultModel;
import com.madhouse.platform.premiummad.model.MaterialModel;

public interface IMaterialService {

	/**
	 * DSP 端查询素材审核状态
	 * 
	 * @param ids
	 *            DSP平台定义的素材 ID
	 * @param dspId
	 * @return
	 */
	List<MaterialAuditResultModel> getMaterialAuditResult(String ids, String dspId);

	/**
	 * DSP端上传素材
	 * 
	 * @param entity
	 */
	void upload(MaterialModel entity);

	/**
	 * 根据媒体返回的结果更新状态
	 * 
	 * @param auditResults
	 */
	void updateStatusToMedia(List<MaterialAuditResultModel> auditResults);

	/**
	 * 素材提交媒体后更改状态为审核中
	 * 
	 * @param materialIdKeys
	 *            我方的素材ID
	 */
	void updateStatusAfterUpload(Map<Integer, String> materialIdKeys);
}
