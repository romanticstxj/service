package com.madhouse.platform.premiummad.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.madhouse.platform.premiummad.entity.Adspace;
import com.madhouse.platform.premiummad.entity.AdspaceMapping;
import com.madhouse.platform.premiummad.entity.DspMapping;

public interface AdspaceDao {
	
	Integer insert(Adspace adspace);
	
	Integer checkName(String adspaceName);

	Integer update(Adspace adspace);

	Adspace queryAdspaceById(Integer adspaceId);

	Integer updateStatus(Adspace adspace);

	List<Adspace> queryAll(@Param("idStrs") String[] idStrs);

	Integer insertAdspaceMediaMapping(AdspaceMapping adspaceMapping);

	Integer insertAdspaceDspMapping(@Param("dspMappings") List<DspMapping> dspMappings);

	int queryAdspaceMediaMapping(@Param("queryParam") AdspaceMapping queryParam);
	
}
