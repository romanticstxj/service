package com.madhouse.platform.premiummad.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.dao.PolicyAdspaceDao;
import com.madhouse.platform.premiummad.dao.PolicyDao;
import com.madhouse.platform.premiummad.dao.PolicyDspDao;
import com.madhouse.platform.premiummad.entity.Policy;
import com.madhouse.platform.premiummad.entity.PolicyAdspace;
import com.madhouse.platform.premiummad.exception.BusinessException;
import com.madhouse.platform.premiummad.service.IPolicyService;
import com.madhouse.platform.premiummad.util.StringUtils;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class PolicyServiceImpl implements IPolicyService {
	
	@Autowired
	private PolicyDao policyDao;
	
	@Autowired
	private PolicyAdspaceDao policyAdspaceDao;
	
	@Autowired
	private PolicyDspDao policyDspDao;

	@Override
	public int insert(Policy policy) {
		int count = checkName(policy.getName().trim());
        if (count > 0) //检查名称
            throw new BusinessException(StatusCode.SC20101);
        
        policyDao.insertSelective(policy);
        
        Integer policyId = policy.getId();
        List<PolicyAdspace> policyAdspaces = policy.getPolicyAdspaces();
        if(policyAdspaces != null){
        	for(int i=0; i< policyAdspaces.size(); i++){
        		//设置policyId到策略广告位关联表
        		policyAdspaces.get(i).setPolicyId(policyId);
        	}
        }
        
        policy.getPolicyDsp().setPolicyId(policyId); //设置policyId到策略dsp关联表
        
        policyAdspaceDao.batchInsert(policyAdspaces);
        
        return policyDspDao.insertSelective(policy.getPolicyDsp());
        
	}

	public int checkName(String name) {
		return policyDao.checkName(name);
	}
	
	@Override
	public Policy queryPolicyById(Integer id, Integer type) {
		return policyDao.selectCascadedlyByPrimaryKey(id, type);
	}
	
	@Override
	public int update(Policy policy) {
		Policy queryResult = queryPolicyById(policy.getId(), policy.getType());
        if (queryResult == null)
        	throw new BusinessException(StatusCode.SC20002);
        if (!queryResult.getName().equals(policy.getName())) { //名称不相等,检查名称
            Integer count = checkName(policy.getName().trim());
            if (count > 0)
            	throw new BusinessException(StatusCode.SC20401);
        }
        
        Integer policyId = policy.getId();
        List<PolicyAdspace> policyAdspaces = policy.getPolicyAdspaces();
        if(policyAdspaces != null){
        	for(int i=0; i< policyAdspaces.size(); i++){
        		//设置policyId到策略广告位关联表
        		policyAdspaces.get(i).setPolicyId(policyId);
        	}
        }
        
		policyDao.update(policy);
		
		policyAdspaceDao.deleteByPolicyId(policyId);
		return policyAdspaceDao.batchInsert(policyAdspaces);
	}

	@Override
	public List<Policy> queryAllByParams(String policyIds, Integer status, Integer type) {
		String[] idStrs = StringUtils.splitIds(policyIds);
		return policyDao.queryAllByParams(idStrs, status, type);
	}

	@Override
	public int updateStatus(Policy policy) {
		Policy queryResult = queryPolicyById(policy.getId(), policy.getType());
        if (queryResult == null)
        	throw new BusinessException(StatusCode.SC20002);
        
		return policyDao.updateStatus(policy);
	}

	@Override
	public String getMediaDealId(Integer dealId, Integer mediaId) {
		if (dealId == null || mediaId == null) {
			return null;
		}
		List<PolicyAdspace> policyAdspaces = policyAdspaceDao.selectByPolicyIdAndMediaId(dealId, mediaId);
		if (policyAdspaces == null || policyAdspaces.isEmpty()) {
			return null;
		}
		
		return policyAdspaces.get(0).getMediaDealId();
	}
}
