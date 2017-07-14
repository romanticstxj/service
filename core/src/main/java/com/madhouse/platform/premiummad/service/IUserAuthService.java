package com.madhouse.platform.premiummad.service;

import java.util.List;

public interface IUserAuthService {
	
	/**
	 * 查询此用户所有权限的媒体id
	 * @return
	 */
	List<Integer> queryMediaIdList(Integer userId, String mediaIds);

	List<Integer> queryAdspaceIdList(Integer userId, String valueOf);
}
