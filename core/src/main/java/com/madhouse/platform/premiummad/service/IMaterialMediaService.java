package com.madhouse.platform.premiummad.service;

import java.util.List;

import com.madhouse.platform.premiummad.model.MaterialMediaAuditResultModel;
import com.madhouse.platform.premiummad.model.MaterialMediaModel;
import com.madhouse.platform.premiummad.model.OperationResultModel;

public interface IMaterialMediaService {

	/**
	 * DSP 端查询素材审核状态
	 * @param ids DSP平台定义的素材 ID
	 * @return
	 */
	List<MaterialMediaAuditResultModel> getMaterialMediaAuditResult(String ids);

	/**
	 * DSP端上传素材
	 * @param entity
	 * @return operationResultModel
	 */
	OperationResultModel upload(MaterialMediaModel entity);
}
