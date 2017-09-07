package com.madhouse.platform.premiummad.service;

import java.util.List;

import com.madhouse.platform.premiummad.entity.UserAuth;

public interface IUserAuthService {
	
	/**
	 * 查询此用户所有权限的媒体id
	 * @return
	 */
	List<Integer> queryMediaIdList(Integer userId, String mediaIds);

	List<Integer> queryAdspaceIdList(Integer userId, String adspaceIds);

	List<Integer> queryPolicyIdList(Integer userId, String policyIds);

	void updateUserMediaAuth(UserAuth userAuth);

	void updateUserPolicyAuth(UserAuth userAuth);
	
	List<Integer> queryAuthorizedMediaIdList(Integer userId);
	
	List<Integer> queryAuthorizedPolicyIdList(Integer userId);

}
