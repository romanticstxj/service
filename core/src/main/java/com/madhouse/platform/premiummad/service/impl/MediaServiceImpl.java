package com.madhouse.platform.premiummad.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.madhouse.platform.premiummad.dao.MediaDao;
import com.madhouse.platform.premiummad.entity.Media;
import com.madhouse.platform.premiummad.model.MediaModel;
import com.madhouse.platform.premiummad.service.IMediaService;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class MediaServiceImpl implements IMediaService {
	
	@Autowired
	private MediaDao mediaDao;

	@Override
	public List<Media> queryAll(List<Integer> ids) {
		return mediaDao.queryAll(ids);
	}

	@Override
	public int insert(Media media) {
		return mediaDao.insert(media);
	}

	@Override
	public Media queryById(Integer mediaId) {
		return mediaDao.queryMediaById(mediaId);
	}

	@Override
	public int update(Media media) {
		return mediaDao.update(media);
	}

	@Override
	public int checkName(String mediaName) {
		return mediaDao.checkName(mediaName);
	}

	@Override
	public int updateStatus(Media media) {
		return mediaDao.updateStatus(media);
	}

	@Override
	public List<MediaModel> getAuditedMedia(String dspId) {
		// TODO Auto-generated method stub
		return null;
	}

}
