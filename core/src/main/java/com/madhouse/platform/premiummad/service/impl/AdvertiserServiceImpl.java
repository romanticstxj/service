package com.madhouse.platform.premiummad.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.madhouse.platform.premiummad.constant.AdvertiserAuditMode;
import com.madhouse.platform.premiummad.constant.AdvertiserStatusCode;
import com.madhouse.platform.premiummad.constant.MaterialStatusCode;
import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.dao.AdvertiserMapper;
import com.madhouse.platform.premiummad.dao.SysMediaMapper;
import com.madhouse.platform.premiummad.entity.Advertiser;
import com.madhouse.platform.premiummad.entity.SysMedia;
import com.madhouse.platform.premiummad.exception.BusinessException;
import com.madhouse.platform.premiummad.model.AdvertiserAuditResultModel;
import com.madhouse.platform.premiummad.model.AdvertiserModel;
import com.madhouse.platform.premiummad.rule.AdvertiserRule;
import com.madhouse.platform.premiummad.rule.MediaRule;
import com.madhouse.platform.premiummad.service.IAdvertiserService;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AdvertiserServiceImpl implements IAdvertiserService {
	
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
	public List<AdvertiserAuditResultModel> getAdvertiserAuditResult(String ids, String dspId) {
		// 解析传入的广告主Key
		String[] idStrs = AdvertiserRule.parseStringToDistinctArray(ids);

		// 查询广告主的审核结果
		List<AdvertiserAuditResultModel> results = new ArrayList<AdvertiserAuditResultModel>();
		if (idStrs != null && idStrs.length > 1) {
			List<Advertiser> selectAdvertisers = advertiserDao.selectByAdvertiserKeysAndDspId(idStrs, dspId);
			AdvertiserRule.convert(selectAdvertisers, results);
		}

		return results;
	}
	
	/**
	 * DSP端上传广告主
	 * @param entity
	 */
	@Transactional
	@Override
	public void upload(AdvertiserModel entity) {
		// 校验参数合法性
		AdvertiserRule.paramterValidate(entity);

		// 查询关联的媒体是否存在且有效
		String[] distinctMediaIds = AdvertiserRule.parseListToDistinctArray(entity.getMediaId());
		List<SysMedia> uploadedMedias = mediaDao.selectMedias(distinctMediaIds);
		MediaRule.checkMedias(distinctMediaIds, uploadedMedias);

		// 判断广告主是否已存在
		List<Advertiser> advertisers = advertiserDao.selectByAdvertiserKeyAndMediaIds(entity);
		List<Map<Integer, Advertiser>> classfiedMaps = new ArrayList<Map<Integer, Advertiser>>();
		AdvertiserRule.classifyAdvertisers(advertisers, entity, classfiedMaps);

		// 存在待审核、审核中，审核通过的不允许推送，提示信息
		String errorMsg = AdvertiserRule.validateAdvertisers(classfiedMaps, entity.getId());
		if (errorMsg != null) {
			throw new BusinessException(StatusCode.SC411, errorMsg);
		}
		
		// 数据存储
		// 保存未提交的广告主和媒体关系
		if (classfiedMaps.get(4) != null && classfiedMaps.get(4).size() > 0) {
			List<Advertiser> insertedRecords = (List<Advertiser>) classfiedMaps.get(4).values();
			for (Advertiser item : insertedRecords) {
				int effortRows = advertiserDao.insertAdvertiser(item);
				if (effortRows != 1) {
					throw new BusinessException(StatusCode.SC500);
				}
			}
		}

		// 更新驳回的广告主和媒体关系
		if (classfiedMaps.get(3) != null && classfiedMaps.get(3).size() > 0) {
			List<Advertiser> insertedRecords = (List<Advertiser>) classfiedMaps.get(4).values();
			int effortRows = advertiserDao.updateByBath(insertedRecords);
			if (effortRows != insertedRecords.size()) {
				throw new BusinessException(StatusCode.SC500);
			}
		}
		
		// 根据媒体审核模式处理
		audit(uploadedMedias, classfiedMaps.get(4), classfiedMaps.get(3));
	}
	
	private void audit(List<SysMedia> uploadedMedias, Map<Integer, Advertiser> unUploadedAdvertisers, Map<Integer, Advertiser> rejectedAdvertisers) {
		for (SysMedia media : uploadedMedias) {
			// 平台审核不处理
			if (AdvertiserAuditMode.AAM10002.getValue() == media.getAdvertiserAuditMode().intValue()) {
				continue;
			}

			Advertiser item = unUploadedAdvertisers.get(media.getId());
			if (item == null) {
				item = rejectedAdvertisers.get(media.getId());
			}
			if (item != null) {
				Advertiser updateItem = new Advertiser();
				// 如果模式是不审核，审核状态修改为审核通过
				if (AdvertiserAuditMode.AAM10001.getValue() == media.getAdvertiserAuditMode().intValue()) {
					updateItem.setStatus(Byte.valueOf(String.valueOf(MaterialStatusCode.MSC10004.getValue())));
					updateItem.setUpdatedTime(new Date());

					updateItem.setId(item.getId());
				}

				// 如果模式是媒体审核，推送给媒体，状态修改为审核中
				if (AdvertiserAuditMode.AAM10003.getValue() == media.getAdvertiserAuditMode().intValue()) {
					// 推送给媒体 TODO

					// 状态修改为审核中
					updateItem.setStatus(Byte.valueOf(String.valueOf(MaterialStatusCode.MSC10003.getValue())));
					updateItem.setUpdatedTime(new Date());

					updateItem.setId(item.getId());
				}

				int effortRows = advertiserDao.updateByPrimaryKeySelective(updateItem);
				if (effortRows != 1) {
					throw new BusinessException(StatusCode.SC500);
				}
			}
		}
	}
	
	/**
	 * 校验广告主和指定的媒体是否已审核通过
	 * @param uploadedMedias
	 * @param dspId
	 * @param advertiserKey
	 * @return
	 */
	@Override
	public String validateAdKeyAndMedias(List<SysMedia> uploadedMedias, String dspId, String advertiserKey) {
		// 获取媒体ID
		List<Integer> mediaIds = new ArrayList<Integer>();
		for (SysMedia media : uploadedMedias) {
			mediaIds.add(media.getId());
		}

		// 获取广告主和媒体的关系
		AdvertiserModel param = new AdvertiserModel();
		param.setId(advertiserKey);
		param.setDspId(dspId);
		param.setMediaId(mediaIds);
		List<Advertiser> advetisers = advertiserDao.selectByAdvertiserKeyAndMediaIds(param);

		// 校验需要审核的媒体是否关联的广告主都已审核通过
		StringBuilder errorMsg = new StringBuilder();
		for (SysMedia media : uploadedMedias) {
			// 广告主不需要审核的不校验
			if (AdvertiserAuditMode.AAM10001.getValue() == media.getAdvertiserAuditMode().intValue()) {
				continue;
			}
			boolean audited = false;
			String auditedStatus = "未上传";
			for (Advertiser advertiser : advetisers) {
				// 是该媒体下，且是审核通过的
				if (advertiser.getMediaId().intValue() == media.getId().intValue()) {
					if (AdvertiserStatusCode.ASC10004.getValue() == advertiser.getStatus().intValue()) {
						audited = true;
					} else {
						auditedStatus = AdvertiserStatusCode.getDescrip(advertiser.getStatus().intValue());
					}
				}
			}
			if (audited) {
				errorMsg.append(";");
				errorMsg.append("广告主[" + advertiserKey + "],媒体[" + media.getId().intValue() + "]" + auditedStatus);
			}
		}
		return errorMsg.substring(1);
	}
}
