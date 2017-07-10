package com.madhouse.platform.premiummad.dao;

import java.util.List;

public interface UserAuthDao {

	List<Integer> queryMediaIdListByUserId(Integer userId);
	
}
