package com.madhouse.platform.premiummad.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madhouse.platform.premiummad.dao.MediaDao;
import com.madhouse.platform.premiummad.entity.MediaMetaData;
import com.madhouse.platform.premiummad.service.IMediaService;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class MediaServiceImpl implements IMediaService {
	
	@Autowired
	private MediaDao mediaDao;

	@Override
	public List<MediaMetaData> queryAll() {
		return mediaDao.queryAll();
	}


	
}
