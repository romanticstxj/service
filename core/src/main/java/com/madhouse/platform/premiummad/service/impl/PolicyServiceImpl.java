package com.madhouse.platform.premiummad.service.impl;

import java.util.ArrayList;
import java.util.Date;
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
import com.madhouse.platform.premiummad.entity.Adspace;
import com.madhouse.platform.premiummad.entity.Policy;
import com.madhouse.platform.premiummad.entity.PolicyAdspace;
import com.madhouse.platform.premiummad.entity.PolicyDsp;
import com.madhouse.platform.premiummad.exception.BusinessException;
import com.madhouse.platform.premiummad.service.IPolicyService;
import com.madhouse.platform.premiummad.service.IUserAuthService;
import com.madhouse.platform.premiummad.util.DateUtils;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class PolicyServiceImpl implements IPolicyService {
	
	@Autowired
	private PolicyDao policyDao;
	
	@Autowired
	private PolicyAdspaceDao policyAdspaceDao;
	
	@Autowired
	private PolicyDspDao policyDspDao;
	
	@Autowired
	private IUserAuthService userAuthService;
	
	@Override
	public int insert(Policy policy) {
		int count = checkName(policy.getName().trim());
        if (count > 0) //检查名称
            throw new BusinessException(StatusCode.SC20401);
        if(judgeOverdue(policy)){ //如果策略过期，返回错误信息
        	throw new BusinessException(StatusCode.SC20402);
        }
        
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
	public Policy queryPolicyById(Integer id, Integer userId) {
		List<Integer> adspaceIds = userAuthService.queryAdspaceIdList(userId, null);
		Policy policy = policyDao.selectCascadedlyByPrimaryKey(id);
		updateStatusForOverdue(policy);
		List<PolicyAdspace> policyAdspaces = policy.getPolicyAdspaces();
		List<PolicyAdspace> newPolicyAdspaces = new ArrayList<PolicyAdspace>();
		//显示此用户有权限的策略中的广告位
		for(int i=0; i<policyAdspaces.size(); i++){
			Adspace adspace = policyAdspaces.get(i).getAdspace();
			if(adspace != null){
				int adspaceId = adspace.getId();
				if (adspaceIds.contains(adspaceId)) {
					newPolicyAdspaces.add(policyAdspaces.get(i));
				}
			}
		}
		policy.setPolicyAdspaces(newPolicyAdspaces);
		return policy;
	}
	
	/**
	 * 如果策略的结束日期小于今天，那么返回状态码2，表示策略过期
	 * @param policy
	 */
	private void updateStatusForOverdue(Policy policy) {
		boolean overdue = judgeOverdue(policy);
		if(overdue){
			policy.setStatus((byte) SystemConstant.DB.POLICY_STATUS_OVERDUE); 
		}
	}
	
	/**
	 * 判断策略是否过期
	 * @param policy
	 * @return
	 */
	private boolean judgeOverdue(Policy policy) {
		Date endDate = policy.getEndDate();
		long overdueDays = DateUtils.getDateSubtract(endDate, new Date());
		return overdueDays > 0;
	}

	@Override
	public int update(Policy policy, Integer userId) {
		Policy queryResult = policyDao.selectByPrimaryKey(policy.getId());
        if (queryResult == null)
        	throw new BusinessException(StatusCode.SC20003);
        if (!queryResult.getName().equals(policy.getName())) { //名称不相等,检查名称
            Integer count = checkName(policy.getName().trim());
            if (count > 0)
            	throw new BusinessException(StatusCode.SC20401);
        }
        if(judgeOverdue(policy)){ //如果策略过期，返回错误信息
        	throw new BusinessException(StatusCode.SC20402);
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
		
		updatePolicyAdspaces(policyId, userId, policyAdspaces);
		
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
	
	/**
	 * 更新这个用户有权限的策略下的广告位
	 * @param policyId
	 * @param userId
	 */
	private void updatePolicyAdspaces(Integer policyId, Integer userId, List<PolicyAdspace> policyAdspaces){
		List<Integer> adspaceIds = userAuthService.queryAdspaceIdList(userId, null);
		policyAdspaceDao.deleteByPolicyId(policyId, adspaceIds);
		policyAdspaceDao.batchInsert(policyAdspaces);
	}

	@Override
	public List<Policy> queryAllByParams(List<Integer> policyIdList, Integer status, Integer type) {
		List<Policy> result= policyDao.queryAllByParams(policyIdList, status, type);
		for(int i=0; i<result.size(); i++){
			Policy policy = result.get(i);
			updateStatusForOverdue(policy);
		}
		return result;
	}

	@Override
	public int updateStatus(Policy policy) {
		Policy queryResult = policyDao.selectByPrimaryKey(policy.getId());
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

	@Override
	public List<Policy> queryAll(List<Integer> ids) {
		return null;
	}

	@Override
	public int update(Policy t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Policy queryById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

}
