package com.madhouse.platform.premiummad.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madhouse.platform.premiummad.dao.MediaWhiteDao;
import com.madhouse.platform.premiummad.service.IMediaWhiteService;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class MediaWhiteServiceImpl implements IMediaWhiteService {
	
	@Autowired
	private MediaWhiteDao mediaDao;

	@Override
	public List<Integer> queryAll() {
		return mediaDao.queryAll();
	}

	


	
}
