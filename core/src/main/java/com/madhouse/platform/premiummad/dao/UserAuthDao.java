package com.madhouse.platform.premiummad.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface UserAuthDao {

	List<Integer> queryMediaIdList(@Param("userId") Integer userId, @Param("mediaIds") String[] mediaIds);

	List<Integer> queryAdspaceIdList(@Param("userId") Integer userId, @Param("adspaceIds") String[] adspaceIds);
	
}
