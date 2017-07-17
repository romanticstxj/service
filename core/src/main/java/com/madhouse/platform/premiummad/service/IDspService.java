package com.madhouse.platform.premiummad.service;

import com.madhouse.platform.premiummad.entity.Dsp;

public interface IDspService {

	/**
	 * DSP 权限校验（在 mad_sys_dsp 存在且 已启用）
	 * @param dspId
	 * @param token
	 */
	void checkDspPermission(String dspId, String token);
	
    /**
     * 新建dsp
     * @param Dsp dsp
     */
	int insertWithParamsProcess(Dsp dsp, String xFrom);

    /**
     * 检查名称重复
     * @param dspName
     * @return
     */
	int checkName(String dspName);

    /**
     * 更新dsp
     * @param dsp
     * @return
     */
    int update(Dsp dsp);

    /**
     * 根据id查询dsp
     * @param id
     * @return
     */
	Dsp queryById(Integer id);

	/**
	 * 更新dsp状态
	 * @param dsp
	 * @return
	 */
	int updateStatus(Dsp dsp);
}
