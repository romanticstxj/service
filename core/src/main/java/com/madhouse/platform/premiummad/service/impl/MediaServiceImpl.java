package com.madhouse.platform.premiummad.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madhouse.platform.premiummad.dao.MediaDao;
import com.madhouse.platform.premiummad.entity.Media;
import com.madhouse.platform.premiummad.service.IMediaService;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class MediaServiceImpl implements IMediaService {
	
	@Autowired
	private MediaDao mediaDao;

	@Override
	public List<Media> queryAll(String ids) {
		String[] idStrs = null;
		if(ids != null){
			idStrs = ids.split(",");
		}
		return mediaDao.queryAll(idStrs);
	}

	@Override
	public Integer insert(Media media) {
		return mediaDao.insert(media);
	}

	@Override
	public Media queryMediaById(Integer mediaId) {
		return mediaDao.queryMediaById(mediaId);
	}

	@Override
	public Integer update(Media media) {
		return mediaDao.update(media);
	}

	@Override
	public Integer checkName(String mediaName) {
		return mediaDao.checkName(mediaName);
	}

	@Override
	public Integer updateStatus(Media media) {
		return mediaDao.updateStatus(media);
	}

}
