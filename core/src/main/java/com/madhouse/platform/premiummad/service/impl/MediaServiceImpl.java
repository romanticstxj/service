package com.madhouse.platform.premiummad.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madhouse.platform.premiummad.dao.MediaDao;
import com.madhouse.platform.premiummad.dao.SysMediaMapper;
import com.madhouse.platform.premiummad.entity.Media;
import com.madhouse.platform.premiummad.entity.SysMedia;
import com.madhouse.platform.premiummad.model.MediaModel;
import com.madhouse.platform.premiummad.service.IMediaService;
import com.madhouse.platform.premiummad.util.BeanUtils;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class MediaServiceImpl implements IMediaService {
	
	@Autowired
	private MediaDao mediaDao;
	
	@Autowired
	private SysMediaMapper sysMediaMapper;

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

	/**
	 * 获取所有已启用的媒体列表
	 * 
	 * @return
	 */
	@Override
	public List<MediaModel> getAuditedMedia() {
		List<MediaModel> medias = new ArrayList<MediaModel>();

		// 获取所有已审核的媒体
		List<SysMedia> auditedMedias = sysMediaMapper.selectAuditedMedias();
		if (auditedMedias == null || auditedMedias.isEmpty()) {
			return medias;
		}

		// 转换成model类型
		for (SysMedia item : auditedMedias) {
			MediaModel meida = new MediaModel();
			BeanUtils.copyProperties(item, meida);
			medias.add(meida);
		}
		return medias;
	}
}
