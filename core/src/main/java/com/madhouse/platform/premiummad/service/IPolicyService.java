package com.madhouse.platform.premiummad.service;

import java.util.List;

import com.madhouse.platform.premiummad.entity.Policy;

public interface IPolicyService extends IBaseService<Policy>{
	
	Policy queryPolicyById(Integer id, Integer type);

	List<Policy> queryAllByParams(List<Integer> policyIdList, Integer status, Integer type);
	
}
