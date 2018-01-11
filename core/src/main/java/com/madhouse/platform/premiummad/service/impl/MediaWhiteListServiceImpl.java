package com.madhouse.platform.premiummad.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.dao.MediaWhiteListDao;
import com.madhouse.platform.premiummad.entity.MediaWhiteList;
import com.madhouse.platform.premiummad.exception.BusinessException;
import com.madhouse.platform.premiummad.service.IMediaWhiteListService;

@Service
@Transactional(rollbackFor=RuntimeException.class)
public class MediaWhiteListServiceImpl implements IMediaWhiteListService {
	
	@Autowired
	private MediaWhiteListDao mediaWhiteListDao;
	
	@Override
	public void insert(MediaWhiteList mediaWhiteList) {
		int count = mediaWhiteListDao.checkName(mediaWhiteList.getMediaId());
		if(count > 0){ //媒体白名单不能重复
			throw new BusinessException(StatusCode.SC20602);
		}
		mediaWhiteListDao.insertSelective(mediaWhiteList);
	}

	@Override
	public void delete(Integer id) {
		MediaWhiteList queryResult = mediaWhiteListDao.selectByPrimaryKey(id);
		if(queryResult == null){ //查无此数据
			throw new BusinessException(StatusCode.SC20003);
		}
		mediaWhiteListDao.delete(id);
	}

	@Override
	public List<MediaWhiteList> list(Integer mediaCategory) {
		return mediaWhiteListDao.list(mediaCategory);
	}

}
