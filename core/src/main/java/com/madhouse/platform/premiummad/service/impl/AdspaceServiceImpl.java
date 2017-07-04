package com.madhouse.platform.premiummad.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madhouse.platform.premiummad.dao.AdspaceDao;
import com.madhouse.platform.premiummad.entity.Adspace;
import com.madhouse.platform.premiummad.service.IAdspaceService;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AdspaceServiceImpl implements IAdspaceService {
	
	@Autowired
	private AdspaceDao adspaceDao;
	
	@Value("${jdbc.premiummad.schema}")
	private String jdbcSchema;
	
	@Override
	public List<Adspace> queryAll(String ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer insert(Adspace adspace) {
		return adspaceDao.insert(adspace);
	}

	@Override
	public Adspace queryAdspaceById(Integer adspaceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer update(Adspace adspace) {
		return adspaceDao.update(adspace);
	}

	@Override
	public Integer checkName(String adspaceName) {
		return adspaceDao.checkName(adspaceName);
	}

	@Override
	public Integer updateStatus(Adspace adspace) {
		// TODO Auto-generated method stub
		return null;
	}


}
