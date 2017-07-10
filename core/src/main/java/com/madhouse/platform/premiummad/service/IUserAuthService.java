package com.madhouse.platform.premiummad.service;

import java.util.List;

public interface IUserAuthService {
	
	/**
	 * 根据用户id查询其所有权限的媒体id
	 * @return
	 */
	List<Integer> queryMediaIdListByUserId(Integer userId);
}
