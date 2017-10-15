package com.madhouse.platform.premiummad.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dao.PolicyAdspaceDao;
import com.madhouse.platform.premiummad.dao.PolicyDao;
import com.madhouse.platform.premiummad.dao.PolicyDspDao;
import com.madhouse.platform.premiummad.entity.Policy;
import com.madhouse.platform.premiummad.entity.PolicyAdspace;
import com.madhouse.platform.premiummad.entity.PolicyDsp;
import com.madhouse.platform.premiummad.exception.BusinessException;
import com.madhouse.platform.premiummad.service.IPolicyService;

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
            throw new BusinessException(StatusCode.SC20401);
        
        policyDao.insertSelective(policy);
        
        Integer policyId = policy.getId();
        List<PolicyAdspace> policyAdspaces = policy.getPolicyAdspaces();
        if(policyAdspaces != null && policyAdspaces.size() > 0){
        	for(int i=0; i< policyAdspaces.size(); i++){
        		//设置policyId到策略广告位关联表
        		policyAdspaces.get(i).setPolicyId(policyId);
        	}
        } 
        
        List<PolicyDsp> policyDsps = policy.getPolicyDsps();
        if(policyDsps != null && policyDsps.size() > 0){
        	for(int i=0; i< policyDsps.size(); i++){
        		//设置policyId到策略dsp关联表
        		policyDsps.get(i).setPolicyId(policyId);
        	}
        } 
        
        policyAdspaceDao.batchInsert(policyAdspaces);
        policyDspDao.batchInsert(policyDsps);
        return 0;
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
        	throw new BusinessException(StatusCode.SC20003);
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
		policyAdspaceDao.batchInsert(policyAdspaces);
		
		//只有rtb模式下才可以修改dsp
		if(policy.getType().intValue() == SystemConstant.OtherConstant.POLICY_TYPE_RTB){
			List<PolicyDsp> policyDsps = policy.getPolicyDsps();
	        if(policyDsps != null){
	        	for(int i=0; i< policyDsps.size(); i++){
	        		//设置policyId到策略Dsp关联表
	        		policyDsps.get(i).setPolicyId(policyId);
	        	}
	        }
			policyDspDao.deleteByPolicyId(policyId);
			policyDspDao.batchInsert(policyDsps);
		}
		
		return 0;
	}

	@Override
	public List<Policy> queryAllByParams(List<Integer> policyIdList, Integer status, Integer type) {
		return policyDao.queryAllByParams(policyIdList, status, type);
	}

	@Override
	public int updateStatus(Policy policy) {
		Policy queryResult = queryPolicyById(policy.getId(), policy.getType());
        if (queryResult == null)
        	throw new BusinessException(StatusCode.SC20003);
		return policyDao.updateStatus(policy);
	}

	@Override
	public String getMediaDealId(String dealId, Integer mediaId) {
		if (StringUtils.isBlank(dealId) || mediaId == null) {
			return null;
		}
		List<PolicyAdspace> policyAdspaces = policyAdspaceDao.selectByPolicyIdAndMediaId(Integer.valueOf(dealId), mediaId);
		if (policyAdspaces == null || policyAdspaces.isEmpty()) {
			return null;
		}
		
		return policyAdspaces.get(0).getMediaDealId();
	}

	public Policy queryById(Integer id) {
		return null;
	}

	@Override
	public List<Policy> queryAll(List<Integer> ids) {
		return null;
	}
}
