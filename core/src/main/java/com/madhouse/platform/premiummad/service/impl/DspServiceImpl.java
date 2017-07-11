package com.madhouse.platform.premiummad.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.dao.DspMapper;
import com.madhouse.platform.premiummad.entity.Dsp;
import com.madhouse.platform.premiummad.exception.BusinessException;
import com.madhouse.platform.premiummad.service.IDspService;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class DspServiceImpl implements IDspService {
	
	@Autowired
	private DspMapper dspDao;
	
	/**
	 * DSP 权限校验（在 mad_sys_dsp 存在且 已启用）
	 * @param dspId
	 * @param token
	 * @return
	 */
	@Override
	public void checkDspPermission(String dspId, String token) {
		// 传入参数校验
		if (StringUtils.isBlank(dspId) || !dspId.matches("[0-9]+") || StringUtils.isBlank(token)) {
			throw new BusinessException(StatusCode.SC400, "dspId 或 token 未提供");
		}
		
		// 查询dsp是否存在
		Dsp param = new Dsp();
		param.setId(Integer.valueOf(dspId));
		param.setToken(token);
		Dsp dsp = dspDao.selectByIdAndToken(param);
		
		if (dsp == null) {
			throw new BusinessException(StatusCode.SC405, "该 DSP 无权限[dspId=" + dspId + ",token=" + token + "]");
		}
		
		if (dsp.getStatus() < 1) {
			throw new BusinessException(StatusCode.SC405, "该 DSP权限未启用");
		}
	}
}
