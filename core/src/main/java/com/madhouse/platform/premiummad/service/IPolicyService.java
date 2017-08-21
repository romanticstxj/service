package com.madhouse.platform.premiummad.service;

import java.util.List;

import com.madhouse.platform.premiummad.entity.Policy;

public interface IPolicyService extends IBaseService<Policy>{
	
	Policy queryPolicyById(Integer id, Integer type);

	int update(Policy policy);

	int updateStatus(Policy policy);
	
	/**
	 * 根据SSP的dealID和媒体ID获取媒体的DealId
	 * 
	 * @param dealId
	 * @param mediaId
	 * @return 
	 */
	String getMediaDealId(Integer dealId, Integer mediaId);

	List<Policy> queryAllByParams(List<Integer> policyIdList, Integer status, Integer type);
}
