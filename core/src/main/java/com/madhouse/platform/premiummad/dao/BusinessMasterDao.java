package com.madhouse.platform.premiummad.dao;

import java.util.List;

import com.madhouse.platform.premiummad.entity.BusinessMaster;

public interface BusinessMasterDao {
	
	BusinessMaster queryByUrl(String url);

	List<BusinessMaster> queryAll();
	
}	
