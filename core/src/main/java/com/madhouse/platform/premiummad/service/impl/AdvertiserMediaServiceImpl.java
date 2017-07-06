package com.madhouse.platform.premiummad.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.madhouse.platform.premiummad.constant.AdvertiserAuditMode;
import com.madhouse.platform.premiummad.constant.AuditeeCode;
import com.madhouse.platform.premiummad.constant.MaterialMediaStatusCode;
import com.madhouse.platform.premiummad.dao.AdvertiserMapper;
import com.madhouse.platform.premiummad.dao.AdvertiserMediaMapper;
import com.madhouse.platform.premiummad.dao.MediaDao;
import com.madhouse.platform.premiummad.entity.Advertiser;
import com.madhouse.platform.premiummad.entity.AdvertiserMedia;
import com.madhouse.platform.premiummad.entity.AdvertiserMediaUnion;
import com.madhouse.platform.premiummad.entity.Media;
import com.madhouse.platform.premiummad.model.AdvertiserMediaAuditResultModel;
import com.madhouse.platform.premiummad.model.AdvertiserMediaModel;
import com.madhouse.platform.premiummad.model.OperationResultModel;
import com.madhouse.platform.premiummad.rule.AdvertiserMediaRule;
import com.madhouse.platform.premiummad.rule.MediaRule;
import com.madhouse.platform.premiummad.service.IAdvertiserMediaService;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AdvertiserMediaServiceImpl implements IAdvertiserMediaService {
	
	@Autowired
	private AdvertiserMediaMapper advertiserMediaDao;
	
	@Autowired
	private MediaDao mediaDao;
	
	@Autowired
	private AdvertiserMapper advertiserDao;
	
	/**
	 * 根据 DSP 广告主Key 查询广告主是否被媒体审核通过 
	 * @param ids
	 * @return
	 */
	@Override
	public List<AdvertiserMediaAuditResultModel> getAdvertiserMediaAuditResult(String ids) {
		// 解析传入的广告主Key
		String[] idStrs = AdvertiserMediaRule.parseStringToArray(ids);

		// 查询广告主的审核结果
		List<AdvertiserMediaAuditResultModel> results = new ArrayList<AdvertiserMediaAuditResultModel>();
		if (idStrs != null && idStrs.length > 1) {
			List<AdvertiserMediaUnion> selectAdvertiserMedias = advertiserMediaDao.selectAdvertiserMedias(idStrs);
			AdvertiserMediaRule.convert(selectAdvertiserMedias, results);
		}

		return results;
	}
	
	/**
	 * DSP端上传广告主
	 * @param entity
	 * @return operationResultModel
	 */
	@Override
	public OperationResultModel upload(AdvertiserMediaModel entity) {
		OperationResultModel operationResult = new OperationResultModel();

		// 查询关联的媒体是否存在且有效
		List<Media> uploadedMedias = mediaDao.queryAll((String[])entity.getMediaId().toArray());
		MediaRule.checkMedias(entity.getMediaId(), uploadedMedias, operationResult);
		if (!operationResult.isSuccessful()) {
			return operationResult;
		}

		// 查询广告主是否存在,不存在构建
		Advertiser advertiser = advertiserDao.selectByAdvertiserKey(entity.getId());
		if (advertiser == null) {
			advertiser = AdvertiserMediaRule.buildAdvertiser(entity);
		}
		entity.setAdvertiserId(advertiser.getId());
		
		// 判断广告主与媒体是否已存在
		List<AdvertiserMediaUnion> advertiserMedias = advertiserMediaDao.selectByAdvertiserKeyAndMediaIds(entity);
		List<Map<Integer, AdvertiserMediaUnion>> classfiedMaps = new ArrayList<Map<Integer, AdvertiserMediaUnion>>();
		AdvertiserMediaRule.classifyAdvertiserAndMedias(advertiserMedias, entity, classfiedMaps);

		// 存在待审核、审核中，审核通过的不允许推送，提示信息
		String errorMsg = AdvertiserMediaRule.validateAdvertiserAndMedias(classfiedMaps, entity.getId());
		if (errorMsg != null) {
			operationResult.setSuccessful(Boolean.FALSE);
			operationResult.setErrorMessage(errorMsg);
			return operationResult;
		}
		
		// 数据存储
		// 广告主不存在插入一条新纪录
		if (advertiser.getId() == null) {
			// 数据插入 
			int effortRows = advertiserDao.insertAdvertiser(advertiser);
			if (effortRows != 1) {
				operationResult.setSuccessful(Boolean.FALSE);
				operationResult.setErrorMessage("系统异常");
				return operationResult;
			}
			
			// 广告主ID回写
			AdvertiserMediaRule.relatedAdvertiserId(classfiedMaps, advertiser.getId());
		}
		
		// 保存未提交的广告主和媒体关系
		if (classfiedMaps.get(4) != null && classfiedMaps.get(4).size() > 0) {
			List<AdvertiserMedia> insertedRecords = AdvertiserMediaRule.convert(classfiedMaps.get(4));
			int effortRows = advertiserMediaDao.insertByBatch(insertedRecords);
			if (effortRows != insertedRecords.size()) {
				operationResult.setSuccessful(Boolean.FALSE);
				operationResult.setErrorMessage("系统异常");
				return operationResult;
			}
		}

		// 更新驳回的广告主和媒体关系
		if (classfiedMaps.get(3) != null && classfiedMaps.get(3).size() > 0) {
			List<AdvertiserMedia> insertedRecords = AdvertiserMediaRule.convert(classfiedMaps.get(3));
			int effortRows = advertiserMediaDao.updateByBath(insertedRecords);
			if (effortRows != insertedRecords.size()) {
				operationResult.setSuccessful(Boolean.FALSE);
				operationResult.setErrorMessage("系统异常");
				return operationResult;
			}
		}
		
		// 根据媒体审核模式处理
		audit(uploadedMedias, classfiedMaps.get(4), classfiedMaps.get(3));

		return operationResult;
	}
	
	private void audit(List<Media> uploadedMedias, Map<Integer, AdvertiserMediaUnion> unUploadedAdvertiserMedias, Map<Integer, AdvertiserMediaUnion> rejectedAdvertiserMedias) {
		for (Media media : uploadedMedias) {
			AdvertiserMediaUnion item = unUploadedAdvertiserMedias.get(media.getId());
			if (item == null) {
				item = rejectedAdvertiserMedias.get(media.getId());
			}
			if (item != null) {
				AdvertiserMedia updateItem = new AdvertiserMedia();
				// 如果模式是不审核，审核状态修改为审核通过
				if (AdvertiserAuditMode.AAM10001.getValue() == media.getMaterialAuditMode().intValue()) {
					updateItem.setAuditedTime(new Date());
					updateItem.setAuditedUser(0); // TODO
					updateItem.setAuditee(AuditeeCode.AC10001.getValue()); // TODO
					updateItem.setStatus(Byte.valueOf(String.valueOf(MaterialMediaStatusCode.MMSC10004.getValue())));

					updateItem.setAdvertiserId(item.getAdvertiserId());
					updateItem.setMediaId(item.getMediaId());
				}

				// 平台审核， 审核方修改为我方
				if (AdvertiserAuditMode.AAM10002.getValue() == media.getMaterialAuditMode().intValue()) {
					updateItem.setAuditee(AuditeeCode.AC10001.getValue());
					updateItem.setAdvertiserId(item.getAdvertiserId());
					updateItem.setMediaId(item.getMediaId());
				}

				// 如果模式是媒体审核，推送给媒体，状态修改为审核中
				if (AdvertiserAuditMode.AAM10003.getValue() == media.getMaterialAuditMode().intValue()) {
					// 推送给媒体 TODO

					// 状态修改为审核中
					updateItem.setAuditee(AuditeeCode.AC10002.getValue());
					updateItem.setStatus(Byte.valueOf(String.valueOf(MaterialMediaStatusCode.MMSC10003.getValue())));

					updateItem.setAdvertiserId(item.getAdvertiserId());
					updateItem.setMediaId(item.getMediaId());
				}

				advertiserMediaDao.updateForAudit(updateItem);
			}
		}
	}
}
