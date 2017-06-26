package com.madhouse.platform.premiummad.service;

import java.util.List;

import com.madhouse.platform.premiummad.entity.BusinessMaster;

public interface IBusinessMasterService {
	
	BusinessMaster queryByUrl(String url);
	
	List<BusinessMaster> queryAll();
}	
