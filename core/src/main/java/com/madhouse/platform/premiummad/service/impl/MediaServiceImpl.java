package com.madhouse.platform.premiummad.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madhouse.platform.premiummad.constant.AdvertiserAuditMode;
import com.madhouse.platform.premiummad.constant.MaterialAuditMode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dao.MediaDao;
import com.madhouse.platform.premiummad.dao.SysMediaMapper;
import com.madhouse.platform.premiummad.entity.Media;
import com.madhouse.platform.premiummad.entity.SysMedia;
import com.madhouse.platform.premiummad.model.MediaModel;
import com.madhouse.platform.premiummad.service.IMediaService;
import com.madhouse.platform.premiummad.util.BeanUtils;
import com.madhouse.platform.premiummad.util.StringUtils;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class MediaServiceImpl implements IMediaService {
	
	@Autowired
	private MediaDao mediaDao;
	
	@Autowired
	private SysMediaMapper sysMediaMapper;

	@Override
	public List<Media> queryAll(List<Integer> ids, Integer category) {
		return mediaDao.queryAll(ids, category);
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
	
	/**
	 * 通过媒体组ID列表及审核模式（素材/广告主）获取媒体ID
	 * 
	 * @param meidaGroups
	 * @param materialMeidaAuditMode
	 * @return
	 */
	@Override
	public int[] getMeidaIds(String meidaGroups, Byte meidaAuditMode) {
		// 解析媒体组
		int[] apiTypes = StringUtils.splitToIntArray(meidaGroups);
		if (apiTypes == null || apiTypes.length < 1) {
			return new int[0];
		}

		// 获取广告主需要媒体审核的媒体
		List<SysMedia> medias = new ArrayList<>();
		if (SystemConstant.MediaAuditObject.ADVERTISER.intValue() == meidaAuditMode.intValue()) {
			medias = sysMediaMapper.selectByApiTypeAndAdAuditMode(apiTypes, AdvertiserAuditMode.AAM10003.getValue());
		}

		// 获取素材需要媒体审核的媒体
		if (SystemConstant.MediaAuditObject.MATERIAL.intValue() == meidaAuditMode.intValue()) {
			medias = sysMediaMapper.selectByApiTypeAndMaterialAuditMode(apiTypes, MaterialAuditMode.MAM10003.getValue());
		}

		// 返回媒体ID列表
		int[] mediaIds = new int[medias.size()];
		for (int i = 0; i < medias.size(); i++) {
			mediaIds[i] = medias.get(i).getId();
		}
		return mediaIds;
	}

	@Override
	public List<Media> queryAll(List<Integer> ids) {
		// TODO Auto-generated method stub
		return null;
	}
}
