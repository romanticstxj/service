package com.madhouse.platform.premiummad.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.dao.PolicyDao;
import com.madhouse.platform.premiummad.entity.Policy;
import com.madhouse.platform.premiummad.exception.BusinessException;
import com.madhouse.platform.premiummad.service.IPolicyService;

public class PolicyServiceImpl implements IPolicyService {
	
	@Autowired
	private PolicyDao policyDao;

	@Override
	public int insert(Policy policy) {
		int count = checkName(policy.getName().trim());
        if (count > 0) //检查名称
            throw new BusinessException(StatusCode.SC20101);
        
        return policyDao.insertSelective(policy);
	}

	public int checkName(String name) {
		return 0;
	}

}
