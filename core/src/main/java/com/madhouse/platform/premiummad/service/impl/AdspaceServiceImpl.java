package com.madhouse.platform.premiummad.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.dao.AdspaceDao;
import com.madhouse.platform.premiummad.entity.Adspace;
import com.madhouse.platform.premiummad.entity.AdspaceMapping;
import com.madhouse.platform.premiummad.entity.DspMapping;
import com.madhouse.platform.premiummad.service.IAdspaceService;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AdspaceServiceImpl implements IAdspaceService {
	
	@Autowired
	private AdspaceDao adspaceDao;
	
	@Override
	public List<Adspace> queryAll(String ids) {
		String[] idStrs = null;
		if(ids != null){
			idStrs = ids.split(",");
		}
		return adspaceDao.queryAll(idStrs);
	}

	@Override
	public Integer insert(Adspace adspace) {
		return adspaceDao.insert(adspace);
	}

	@Override
	public Adspace queryAdspaceById(Integer adspaceId) {
		return adspaceDao.queryAdspaceById(adspaceId);
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
		return adspaceDao.updateStatus(adspace);
	}

	@Override
	public StatusCode addAdspaceMediaMapping(AdspaceMapping adspaceMapping) {
		int queryResult = queryAdspaceMediaMapping(adspaceMapping);
		if(queryResult > 0){ //我方广告位ID不可重复
			return StatusCode.SC22011;
		}
		
		adspaceDao.insertAdspaceMediaMapping(adspaceMapping);
		return StatusCode.SC20000;
	}

	private int queryAdspaceMediaMapping(AdspaceMapping queryParam) {
		return adspaceDao.queryAdspaceMediaMapping(queryParam);
	}

	@Override
	public StatusCode addAdspaceDspMapping(List<DspMapping> dspMappings) {
//		int queryResult = queryAdspaceMediaMapping(adspaceMapping);
//		if(queryResult > 0){ //我方广告位ID不可重复
//			return StatusCode.SC22012;
//		}
		
		adspaceDao.insertAdspaceDspMapping(dspMappings);
		return StatusCode.SC20000;
	}

}
