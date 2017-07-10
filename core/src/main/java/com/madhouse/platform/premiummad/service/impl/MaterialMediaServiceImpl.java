package com.madhouse.platform.premiummad.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madhouse.platform.premiummad.constant.MaterialAuditMode;
import com.madhouse.platform.premiummad.constant.MaterialMediaStatusCode;
import com.madhouse.platform.premiummad.dao.MaterialMapper;
import com.madhouse.platform.premiummad.dao.MaterialMediaMapper;
import com.madhouse.platform.premiummad.dao.SysMediaMapper;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.entity.MaterialMedia;
import com.madhouse.platform.premiummad.entity.MaterialMediaUnion;
import com.madhouse.platform.premiummad.entity.SysMedia;
import com.madhouse.platform.premiummad.model.MaterialMediaAuditResultModel;
import com.madhouse.platform.premiummad.model.MaterialMediaModel;
import com.madhouse.platform.premiummad.model.OperationResultModel;
import com.madhouse.platform.premiummad.rule.MaterialMediaRule;
import com.madhouse.platform.premiummad.rule.MediaRule;
import com.madhouse.platform.premiummad.service.IMaterialMediaService;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class MaterialMediaServiceImpl implements IMaterialMediaService {

	@Autowired
	private MaterialMediaMapper materialMediaDao;
	
	@Autowired
	private SysMediaMapper mediaDao;
	
	@Autowired
	private MaterialMapper materialDao;
	
	/**
	 * DSP 端查询素材审核状态
	 * @param ids DSP平台定义的素材 ID
	 * @return
	 */
	@Override
	public List<MaterialMediaAuditResultModel> getMaterialMediaAuditResult(String ids) {
		// 解析传入的素材ID
		String[] idStrs = MaterialMediaRule.parseStringToDistinctArray(ids);

		// 查询广告主的审核结果
		List<MaterialMediaAuditResultModel> results = new ArrayList<MaterialMediaAuditResultModel>();
		if (idStrs != null && idStrs.length > 1) {
			List<MaterialMediaUnion> selectAdvertiserMedias = materialMediaDao.selectMaterialMedias(idStrs);
			MaterialMediaRule.convert(selectAdvertiserMedias, results);
		}

		return results;
	}
	
	/**
	 * DSP端上传素材
	 * @param entity
	 * @return operationResultModel
	 */
	@Override
	public OperationResultModel upload(MaterialMediaModel entity) {
		OperationResultModel operationResult = new OperationResultModel();
		
		// 查询关联的媒体是否存在且有效
		String[] distinctMediaIds = MaterialMediaRule.parseListToDistinctArray(entity.getMediaId());
		List<SysMedia> uploadedMedias = mediaDao.selectMedias(distinctMediaIds);
		MediaRule.checkMedias(distinctMediaIds, uploadedMedias, operationResult);
		if (!operationResult.isSuccessful()) {
			return operationResult;
		}
		
		// 查询素材是否存在,不存在构建，否则更新
		Material material = materialDao.selectByMaterialKey(entity.getId());
		material = MaterialMediaRule.buildMaterial(material, entity);
		entity.setMaterialId(material.getId());
		
		// 判断素材与媒体是否已存在
		List<MaterialMediaUnion> advertiserMedias = materialMediaDao.selectByMaterialKeyAndMediaIds(entity);
		List<Map<Integer, MaterialMediaUnion>> classfiedMaps = new ArrayList<Map<Integer, MaterialMediaUnion>>();
		MaterialMediaRule.classifyMaterialAndMedias(advertiserMedias, entity, classfiedMaps);

		// 存在待审核、审核中，审核通过的不允许推送，提示信息
		String errorMsg = MaterialMediaRule.validateMaterialAndMedias(classfiedMaps, entity.getId());
		if (errorMsg != null) {
			operationResult.setSuccessful(Boolean.FALSE);
			operationResult.setErrorMessage(errorMsg);
			return operationResult;
		}
		
		// 数据存储
		// 广告主不存在插入一条新纪录
		if (material.getId() == null) {
			// 数据插入
			int effortRows = materialDao.insert(material);
			if (effortRows != 1) {
				operationResult.setSuccessful(Boolean.FALSE);
				operationResult.setErrorMessage("系统异常");
				return operationResult;
			}

			// 广告主ID回写
			MaterialMediaRule.relatedMaterialId(classfiedMaps, material.getId());
		} else {
			int effortRows = materialDao.updateByPrimaryKeySelective(material);
			if (effortRows != 1) {
				operationResult.setSuccessful(Boolean.FALSE);
				operationResult.setErrorMessage("系统异常");
				return operationResult;
			}
		}
		
		// 保存未提交的素材和媒体关系
		if (classfiedMaps.get(4) != null && classfiedMaps.get(4).size() > 0) {
			List<MaterialMedia> insertedRecords = MaterialMediaRule.convert(classfiedMaps.get(4));
			int effortRows = materialMediaDao.insertByBatch(insertedRecords);
			if (effortRows != insertedRecords.size()) {
				operationResult.setSuccessful(Boolean.FALSE);
				operationResult.setErrorMessage("系统异常");
				return operationResult;
			}
		}

		// 更新驳回的素材和媒体关系
		if (classfiedMaps.get(3) != null && classfiedMaps.get(3).size() > 0) {
			List<MaterialMedia> insertedRecords = MaterialMediaRule.convert(classfiedMaps.get(3));
			int effortRows = materialMediaDao.updateByBath(insertedRecords);
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
	
	private void audit(List<SysMedia> uploadedMedias, Map<Integer, MaterialMediaUnion> unUploadedMaterialMedias, Map<Integer, MaterialMediaUnion> rejectedMaterialMedias) {
		for (SysMedia media : uploadedMedias) {
			// 平台审核不处理
			if (MaterialAuditMode.MAM10002.getValue() == media.getMaterialAuditMode().intValue()) {
				continue;
			}
			
			MaterialMediaUnion item = unUploadedMaterialMedias.get(media.getId());
			if (item == null) {
				item = unUploadedMaterialMedias.get(media.getId());
			}
			if (item != null) {
				MaterialMedia updateItem = new MaterialMedia();
				// 如果模式是不审核，审核状态修改为审核通过
				if (MaterialAuditMode.MAM10001.getValue() == media.getMaterialAuditMode().intValue()) {
					updateItem.setAuditedTime(new Date());
					updateItem.setStatus(Byte.valueOf(String.valueOf(MaterialMediaStatusCode.MMSC10004.getValue())));

					updateItem.setMaterialId(item.getMaterialId());
					updateItem.setMediaId(item.getMediaId());
				}
				
				// 如果模式是媒体审核，推送给媒体，状态修改为审核中
				if (MaterialAuditMode.MAM10003.getValue() == media.getMaterialAuditMode().intValue()) {
					// 推送给媒体 TODO

					// 状态修改为审核中
					updateItem.setStatus(Byte.valueOf(String.valueOf(MaterialMediaStatusCode.MMSC10003.getValue())));
					updateItem.setAuditedTime(new Date());
					
					updateItem.setMaterialId(item.getMaterialId());
					updateItem.setMediaId(item.getMediaId());
				}
				
				materialMediaDao.updateForAudit(updateItem);
			}
		}
	}
}