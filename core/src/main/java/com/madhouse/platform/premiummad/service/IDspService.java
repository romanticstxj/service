package com.madhouse.platform.premiummad.service;

import com.madhouse.platform.premiummad.entity.Dsp;

public interface IDspService extends IBaseService<Dsp>{

	/**
	 * DSP 权限校验（在 mad_sys_dsp 存在且 已启用）
	 * 
	 * @param dspId
	 * @param token
	 */
	void checkDspPermission(String dspId, String token);
	
    /**
     * 新建dsp
     * @param Dsp dsp
     */
	int insertWithParamsProcess(Dsp dsp, String xFrom);

}
