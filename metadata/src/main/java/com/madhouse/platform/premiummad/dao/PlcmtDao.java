package com.madhouse.platform.premiummad.dao;

import java.util.List;

import com.madhouse.platform.premiummad.entity.Adspace;
import com.madhouse.platform.premiummad.entity.MediaMappingMetaData;

public interface PlcmtDao {

	public List<Adspace> queryAll();

	public List<MediaMappingMetaData> queryMediaMappingMetaData();
}
