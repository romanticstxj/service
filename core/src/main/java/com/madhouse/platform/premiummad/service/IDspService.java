package com.madhouse.platform.premiummad.service;

public interface IDspService {

	/**
	 * DSP 权限校验（在 mad_sys_dsp 存在且 已启用）
	 * 
	 * @param dspId
	 * @param token
	 */
	void checkDspPermission(String dspId, String token);
}
