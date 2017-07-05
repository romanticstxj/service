package com.madhouse.platform.premiummad.service;

import com.madhouse.platform.premiummad.model.OperationResultModel;

public interface IDspService {

	/**
	 * DSP 权限校验（在 mad_sys_dsp 存在且 已启用）
	 * @param dspId
	 * @param token
	 * @return
	 */
	OperationResultModel checkDspPermission(String dspId, String token);

}
