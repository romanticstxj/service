package com.madhouse.platform.premiummad.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madhouse.platform.premiummad.dao.PolicyDao;
import com.madhouse.platform.premiummad.entity.Policy;
import com.madhouse.platform.premiummad.service.IPolicyService;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class PolicyServiceImpl implements IPolicyService {
	
	@Autowired
	private PolicyDao policyDao;

    @Override
    public List<Policy> queryAll() {
        return policyDao.queryAll();
    }

   


	
}
