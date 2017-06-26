package com.madhouse.platform.premiummad.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madhouse.platform.premiummad.dao.BusinessMasterDao;
import com.madhouse.platform.premiummad.entity.BusinessMaster;
import com.madhouse.platform.premiummad.service.IBusinessMasterService;

@Service
@Transactional(rollbackFor=RuntimeException.class)
public class BusinessMasterServiceImpl implements IBusinessMasterService{
	@Autowired
	private BusinessMasterDao businessMasterDao;
	
	public BusinessMaster queryByUrl(String url){
		return businessMasterDao.queryByUrl(url);
	}

	@Override
	public List<BusinessMaster> queryAll() {
		return businessMasterDao.queryAll();
	}

}	
