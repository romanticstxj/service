package com.madhouse.platform.premiummad.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.madhouse.platform.premiummad.entity.Adspace;
import com.madhouse.platform.premiummad.entity.Media;

public interface AdspaceDao {
	
	public Integer insert(Adspace adspace);
	
	public Integer checkName(String adspaceName);

	public Integer update(Adspace adspace);

	
}
