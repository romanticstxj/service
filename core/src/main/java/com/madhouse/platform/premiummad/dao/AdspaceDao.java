package com.madhouse.platform.premiummad.dao;

import com.madhouse.platform.premiummad.entity.Adspace;
<<<<<<< HEAD
import com.madhouse.platform.premiummad.entity.AdspaceMapping;
import com.madhouse.platform.premiummad.entity.DspMapping;
=======
>>>>>>> materialDev

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

	int queryAdspaceDspMapping(Integer adspaceId);

	AdspaceMapping queryAdspaceMappingById(Integer id);

	Integer removeAdspaceMediaMapping(@Param("adspaceId") Integer adspaceId);
	
	Integer removeAdspaceDspMapping(@Param("adspaceId") Integer adspaceId);

	int queryByAdspaceKey(String adspaceKey);

	
}
