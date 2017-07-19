package com.madhouse.platform.premiummad.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.madhouse.platform.premiummad.dao.PolicyDao;
import com.madhouse.platform.premiummad.entity.Policy;
import com.madhouse.platform.premiummad.service.IPolicyService;

public class PolicyServiceImpl implements IPolicyService {
	
	@Autowired
	private PolicyDao policyDao;

	@Override
	public int insert(Policy policy) {
		policyDao.insertSelective(policy);
		return 0;
	}

}
