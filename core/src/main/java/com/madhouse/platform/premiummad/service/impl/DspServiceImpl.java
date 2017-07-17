package com.madhouse.platform.premiummad.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.dao.DspMapper;
import com.madhouse.platform.premiummad.entity.Dsp;
import com.madhouse.platform.premiummad.exception.BusinessException;
import com.madhouse.platform.premiummad.model.OperationResultModel;
import com.madhouse.platform.premiummad.service.IDspService;
import com.madhouse.platform.premiummad.util.StringUtils;

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
	public OperationResultModel checkDspPermission(String dspId, String token) {
		OperationResultModel result = new OperationResultModel();
		
		// 传入参数校验
		if (StringUtils.isBlank(dspId) || !dspId.matches("[0-9]+") || StringUtils.isBlank(token)) {
			result.setErrorMessage("dspId 或 token 无效");
			return result;
		}
		
		// 查询dsp是否存在
		Dsp param = new Dsp();
		param.setId(Integer.valueOf(dspId));
		param.setToken(token);
		Dsp dsp = dspDao.selectByIdAndToken(param);
		
		if (dsp == null) {
			result.setErrorMessage("该 DSP 无权限[dspId=" + dspId + ",token=" + token + "]");
			return result;
		}
		
		if (dsp.getStatus() < 1) {
			result.setErrorMessage("该 DSP权限未启用");
			return result;
		}
		
		result.setSuccessful(Boolean.TRUE);
		return result;
	}

	@Override
	public int insertWithParamsProcess(Dsp dsp, String xFrom) {
		dspDao.insertSelective(dsp);
		postprocessDspParams(dsp, xFrom);
        return updateDspToken(dsp);
	}
	
	private void postprocessDspParams(Dsp dsp, String xFrom) {
		//生成dsp token，更新入到数据库e
		Integer id = dsp.getId();
		String name = dsp.getName();
		String combinedStr = new StringBuffer(id.toString()).append(name).append(xFrom).toString();
		String token = StringUtils.getMD5(combinedStr); //生成32位md5码
		dsp.setToken(token);
	}
	
	public int updateDspToken(Dsp dsp) {
		Dsp queryParam = new Dsp();
		queryParam.setToken(dsp.getToken()); 
		Dsp queryResult = dspDao.selectByIdAndToken(queryParam); //查询dsp token是否已存在
		if(queryResult != null){
			throw new BusinessException(StatusCode.SC20301);
		}
		return dspDao.updateByPrimaryKeySelective(dsp);
	}

	@Override
	public int checkName(String dspName) {
		return dspDao.checkName(dspName);
	}

	@Override
	public int update(Dsp dsp) {
		Dsp queryResult = dspDao.selectByPrimaryKey(dsp.getId());
        if (queryResult == null)
        	throw new BusinessException(StatusCode.SC20002);
        if (!queryResult.getName().equals(dsp.getName())) { //名称不相等,检查名称
            Integer count = dspDao.checkName(dsp.getName().trim());
            if (count > 0)
            throw new BusinessException(StatusCode.SC20302);
        }
        return dspDao.updateByPrimaryKeySelective(dsp);
	}

	@Override
	public Dsp queryById(Integer id) {
		return dspDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateStatus(Dsp dsp) {
		Dsp queryResult = dspDao.selectByPrimaryKey(dsp.getId());
        if (queryResult == null)
        	throw new BusinessException(StatusCode.SC20002);
        return dspDao.updateStatus(dsp);
	}
}
