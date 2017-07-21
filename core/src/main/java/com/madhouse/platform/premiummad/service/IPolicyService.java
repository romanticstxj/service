package com.madhouse.platform.premiummad.service;

import com.madhouse.platform.premiummad.entity.Policy;

public interface IPolicyService {
	
	int insert(Policy policy);

	Policy queryPolicyById(Integer id);
}
