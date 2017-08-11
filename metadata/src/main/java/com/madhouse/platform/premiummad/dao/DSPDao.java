package com.madhouse.platform.premiummad.dao;

import java.util.List;

import com.madhouse.platform.premiummad.entity.DSPMappingMetaData;
import com.madhouse.platform.premiummad.entity.DSPMetaData;

public interface DSPDao {

	public List<DSPMetaData> queryAll();

	public List<DSPMappingMetaData> queryAdspaceMappingDsp();
}
