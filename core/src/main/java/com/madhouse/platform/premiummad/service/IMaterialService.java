package com.madhouse.platform.premiummad.service;

import java.util.List;

import com.madhouse.platform.premiummad.model.MaterialAuditResultModel;
import com.madhouse.platform.premiummad.model.MaterialModel;

public interface IMaterialService {

	/**
	 * DSP 端查询素材审核状态
	 * @param ids DSP平台定义的素材 ID
	 * @param dspId
	 * @return
	 */
	List<MaterialAuditResultModel> getMaterialAuditResult(String ids, String dspId);

	/**
	 * DSP端上传素材
	 * @param entity
	 */
	void upload(MaterialModel entity);
}
