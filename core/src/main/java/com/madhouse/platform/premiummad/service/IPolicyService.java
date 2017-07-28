package com.madhouse.platform.premiummad.service;

import java.util.List;

import com.madhouse.platform.premiummad.entity.Policy;

public interface IPolicyService {
	
	int insert(Policy policy);

	Policy queryPolicyById(Integer id, Integer type);

	int update(Policy policy);

	List<Policy> queryAllByParams(String policyIds, Integer status, Integer type);

	int updateStatus(Policy policy);
}
