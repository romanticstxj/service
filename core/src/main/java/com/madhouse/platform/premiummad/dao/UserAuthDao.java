package com.madhouse.platform.premiummad.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.madhouse.platform.premiummad.entity.UserAuth;

public interface UserAuthDao {

	List<Integer> queryMediaIdList(@Param("userId") Integer userId, @Param("mediaIds") String[] mediaIds,
			@Param("isAdmin") Integer isAdmin);
	
	List<Integer> queryAdspaceIdList(@Param("userId") Integer userId, @Param("adspaceIds") String[] adspaceIds,
			@Param("isAdmin") Integer isAdmin);
	
	List<Integer> queryPolicyIdList(@Param("userId") Integer userId, @Param("policyIds") String[] policyIds, 
			@Param("isAdmin") Integer isAdmin);

	int checkAdminForMedia(Integer userId);

	int checkAdminForPolicy(Integer userId);

	void addUserMediaAuth(UserAuth userAuth);

	void removeUserMediaAuth(Integer userId);

	void removeUserPolicyAuth(Integer userId);

	void addUserPolicyAuth(UserAuth userAuth);
}
