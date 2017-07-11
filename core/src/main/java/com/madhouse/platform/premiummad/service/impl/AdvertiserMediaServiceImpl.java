package com.madhouse.platform.premiummad.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madhouse.platform.premiummad.constant.AdevertiserIndustry;
import com.madhouse.platform.premiummad.constant.AdvertiserAuditMode;
import com.madhouse.platform.premiummad.constant.MaterialMediaStatusCode;
import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.dao.AdvertiserMapper;
import com.madhouse.platform.premiummad.dao.AdvertiserMediaMapper;
import com.madhouse.platform.premiummad.dao.SysMediaMapper;
import com.madhouse.platform.premiummad.entity.Advertiser;
import com.madhouse.platform.premiummad.entity.AdvertiserMedia;
import com.madhouse.platform.premiummad.entity.AdvertiserMediaUnion;
import com.madhouse.platform.premiummad.entity.SysMedia;
import com.madhouse.platform.premiummad.exception.BusinessException;
import com.madhouse.platform.premiummad.model.AdvertiserMediaAuditResultModel;
import com.madhouse.platform.premiummad.model.AdvertiserMediaModel;
import com.madhouse.platform.premiummad.rule.AdvertiserMediaRule;
import com.madhouse.platform.premiummad.rule.MediaRule;
import com.madhouse.platform.premiummad.service.IAdvertiserMediaService;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AdvertiserMediaServiceImpl implements IAdvertiserMediaService {
	
	@Autowired
	private AdvertiserMediaMapper advertiserMediaDao;
	
	@Autowired
	private SysMediaMapper mediaDao;
	
	@Autowired
	private AdvertiserMapper advertiserDao;
	
	/**
	 * 根据 DSP 广告主Key 查询广告主是否被媒体审核通过 
	 * @param ids
	 * @param dspId
	 * @return
	 */
	@Override
	public List<AdvertiserMediaAuditResultModel> getAdvertiserMediaAuditResult(String ids, String dspId) {
		// 解析传入的广告主Key
		String[] idStrs = AdvertiserMediaRule.parseStringToDistinctArray(ids);

		// 查询广告主的审核结果
		List<AdvertiserMediaAuditResultModel> results = new ArrayList<AdvertiserMediaAuditResultModel>();
		if (idStrs != null && idStrs.length > 1) {
			List<AdvertiserMediaUnion> selectAdvertiserMedias = advertiserMediaDao.selectAdvertiserMedias(idStrs, dspId);
			AdvertiserMediaRule.convert(selectAdvertiserMedias, results);
		}

		return results;
	}
	
	/**
	 * DSP端上传广告主
	 * @param entity
	 */
	@Transactional
	@Override
	public void upload(AdvertiserMediaModel entity) {
		// 校验参数合法性
		if (AdevertiserIndustry.getDescrip(entity.getIndustry().intValue()) == null) {
			throw new BusinessException(StatusCode.SC400, "广告主所属工业编码不存在");
		}
		if (entity.getMediaId() == null || entity.getMediaId().isEmpty()) {
			throw new BusinessException(StatusCode.SC400, "广告主关联的媒体ID必须");
		}

		// 查询关联的媒体是否存在且有效
		String[] distinctMediaIds = AdvertiserMediaRule.parseListToDistinctArray(entity.getMediaId());
		List<SysMedia> uploadedMedias = mediaDao.selectMedias(distinctMediaIds);
		MediaRule.checkMedias(distinctMediaIds, uploadedMedias);

		// 查询广告主是否存在,不存在构建，否则更新
		Advertiser advertiser = advertiserDao.selectByAdvertiserKeyAndDspId(entity.getId(), entity.getDspId());
		advertiser = AdvertiserMediaRule.buildAdvertiser(advertiser, entity);
		entity.setAdvertiserId(advertiser.getId());
		
		// 判断广告主与媒体是否已存在
		List<AdvertiserMediaUnion> advertiserMedias = advertiserMediaDao.selectByAdvertiserKeyAndMediaIds(entity);
		List<Map<Integer, AdvertiserMediaUnion>> classfiedMaps = new ArrayList<Map<Integer, AdvertiserMediaUnion>>();
		AdvertiserMediaRule.classifyAdvertiserAndMedias(advertiserMedias, entity, classfiedMaps);

		// 存在待审核、审核中，审核通过的不允许推送，提示信息
		String errorMsg = AdvertiserMediaRule.validateAdvertiserAndMedias(classfiedMaps, entity.getId());
		if (errorMsg != null) {
			throw new BusinessException(StatusCode.SC411, errorMsg);
		}
		
		// 数据存储
		// 广告主不存在插入一条新纪录
		if (advertiser.getId() == null) {
			// 数据插入 
			int effortRows = advertiserDao.insertAdvertiser(advertiser);
			if (effortRows != 1) {
				throw new BusinessException(StatusCode.SC500);
			}
			
			// 广告主ID回写
			AdvertiserMediaRule.relatedAdvertiserId(classfiedMaps, advertiser.getId());
		} else { // 对于驳回再次提交的，广告主进行更新
			int effortRows = advertiserDao.updateByPrimaryKeySelective(advertiser);
			if (effortRows != 1) {
				throw new BusinessException(StatusCode.SC500);
			}
		}
		
		// 保存未提交的广告主和媒体关系
		if (classfiedMaps.get(4) != null && classfiedMaps.get(4).size() > 0) {
			List<AdvertiserMedia> insertedRecords = AdvertiserMediaRule.convert(classfiedMaps.get(4));
			int effortRows = advertiserMediaDao.insertByBatch(insertedRecords);
			if (effortRows != insertedRecords.size()) {
				throw new BusinessException(StatusCode.SC500);
			}
		}

		// 更新驳回的广告主和媒体关系
		if (classfiedMaps.get(3) != null && classfiedMaps.get(3).size() > 0) {
			List<AdvertiserMedia> insertedRecords = AdvertiserMediaRule.convert(classfiedMaps.get(3));
			int effortRows = advertiserMediaDao.updateByBath(insertedRecords);
			if (effortRows != insertedRecords.size()) {
				throw new BusinessException(StatusCode.SC500);
			}
		}
		
		// 根据媒体审核模式处理
		audit(uploadedMedias, classfiedMaps.get(4), classfiedMaps.get(3));
	}
	
	private void audit(List<SysMedia> uploadedMedias, Map<Integer, AdvertiserMediaUnion> unUploadedAdvertiserMedias, Map<Integer, AdvertiserMediaUnion> rejectedAdvertiserMedias) {
		for (SysMedia media : uploadedMedias) {
			// 平台审核不处理
			if (AdvertiserAuditMode.AAM10002.getValue() == media.getAdvertiserAuditMode().intValue()) {
				continue;
			}
			
			AdvertiserMediaUnion item = unUploadedAdvertiserMedias.get(media.getId());
			if (item == null) {
				item = rejectedAdvertiserMedias.get(media.getId());
			}
			if (item != null) {
				AdvertiserMedia updateItem = new AdvertiserMedia();
				// 如果模式是不审核，审核状态修改为审核通过
				if (AdvertiserAuditMode.AAM10001.getValue() == media.getAdvertiserAuditMode().intValue()) {
					updateItem.setAuditedTime(new Date());
					updateItem.setStatus(Byte.valueOf(String.valueOf(MaterialMediaStatusCode.MMSC10004.getValue())));

					updateItem.setAdvertiserId(item.getAdvertiserId());
					updateItem.setMediaId(item.getMediaId());
				}

				// 如果模式是媒体审核，推送给媒体，状态修改为审核中
				if (AdvertiserAuditMode.AAM10003.getValue() == media.getAdvertiserAuditMode().intValue()) {
					// 推送给媒体 TODO

					// 状态修改为审核中
					updateItem.setStatus(Byte.valueOf(String.valueOf(MaterialMediaStatusCode.MMSC10003.getValue())));
					updateItem.setAuditedTime(new Date());
					
					updateItem.setAdvertiserId(item.getAdvertiserId());
					updateItem.setMediaId(item.getMediaId());
				}

				advertiserMediaDao.updateForAudit(updateItem);
			}
		}
	}
}
