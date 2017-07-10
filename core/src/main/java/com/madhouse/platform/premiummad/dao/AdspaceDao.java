package com.madhouse.platform.premiummad.dao;

import com.madhouse.platform.premiummad.entity.Adspace;

public interface AdspaceDao {
	
	public Integer insert(Adspace adspace);
	
	public Integer checkName(String adspaceName);

	public Integer update(Adspace adspace);

	
}
