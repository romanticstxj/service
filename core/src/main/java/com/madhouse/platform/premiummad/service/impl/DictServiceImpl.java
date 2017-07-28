package com.madhouse.platform.premiummad.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madhouse.platform.premiummad.dao.DictDao;
import com.madhouse.platform.premiummad.entity.Dict;
import com.madhouse.platform.premiummad.entity.Location;
import com.madhouse.platform.premiummad.service.IDictService;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class DictServiceImpl implements IDictService {

	@Autowired
	private DictDao dictDao;
	
	@Override
	public List<Dict> queryAllMediaCategories() {
		return dictDao.queryAllMediaCategories();
	}

	@Override
	public List<Dict> queryAllAdspaceLayout(Dict dict) {
		return dictDao.queryAllAdspaceLayout(dict);
	}
	
	@Override
	public List<Location> queryAllLocations() {
		return dictDao.queryAllLocations();
	}


}
