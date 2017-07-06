package com.madhouse.platform.premiummad.service;

import java.util.List;
import com.madhouse.platform.premiummad.model.MaterialMediaAuditResultModel;

public interface IMaterialMediaService {

	/**
	 * DSP 端查询素材审核状态
	 * @param ids DSP平台定义的素材 ID
	 * @return
	 */
	List<MaterialMediaAuditResultModel> getMaterialMediaAuditResult(String ids);
}
