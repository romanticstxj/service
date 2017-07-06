package com.madhouse.platform.premiummad.dao;

import com.madhouse.platform.premiummad.entity.Adspace;

public interface AdspaceDao {
	
	Integer insert(Adspace adspace);
	
	Integer checkName(String adspaceName);

	Integer update(Adspace adspace);

	Adspace queryAdspaceById(Integer adspaceId);

	Integer updateStatus(Adspace adspace);

	
}
