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

	List<Adspace> queryAllByParams(@Param("idStrs") String[] idStrs, @Param("status") Integer status);

	Integer insertAdspaceMediaMapping(AdspaceMapping adspaceMapping);

	Integer insertAdspaceDspMapping(@Param("dspMappings") List<DspMapping> dspMappings);

	int queryAdspaceMediaMapping(@Param("queryParam") AdspaceMapping queryParam);

	int queryAdspaceDspMapping(Integer adspaceId);

	List<AdspaceMapping> queryAdspaceMappingById(Integer id);

	Integer removeAdspaceMediaMapping(@Param("adspaceId") Integer adspaceId);
	
	Integer removeAdspaceDspMapping(@Param("adspaceId") Integer adspaceId);

	int queryByAdspaceKey(String adspaceKey);

	Integer updateAdspaceKey(Adspace adspace);

	
}
