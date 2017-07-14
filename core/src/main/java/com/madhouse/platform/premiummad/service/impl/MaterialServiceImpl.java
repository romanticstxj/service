package com.madhouse.platform.premiummad.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.madhouse.platform.premiummad.constant.MaterialAuditMode;
import com.madhouse.platform.premiummad.constant.MaterialStatusCode;
import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.dao.MaterialMapper;
import com.madhouse.platform.premiummad.dao.SysMediaMapper;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.entity.SysMedia;
import com.madhouse.platform.premiummad.exception.BusinessException;
import com.madhouse.platform.premiummad.model.MaterialAuditResultModel;
import com.madhouse.platform.premiummad.model.MaterialModel;
import com.madhouse.platform.premiummad.rule.MaterialRule;
import com.madhouse.platform.premiummad.rule.MediaRule;
import com.madhouse.platform.premiummad.service.IAdvertiserService;
import com.madhouse.platform.premiummad.service.IMaterialService;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class MaterialServiceImpl implements IMaterialService {
	@Autowired
	private SysMediaMapper mediaDao;

	@Autowired
	private MaterialMapper materialDao;

	@Autowired
	private IAdvertiserService advertiserService;

	/**
	 * 根据媒体返回的结果更新状态
	 * 
	 * @param auditResults
	 */
	@Transactional
	@Override
	public void updateStatusToMedia(List<MaterialAuditResultModel> auditResults) {
		// 参数校验
		if (auditResults == null || auditResults.isEmpty()) {
			return;
		}

		// 获取素材ID列表
		List<String> materialIds = new ArrayList<String>();
		for (MaterialAuditResultModel item : auditResults) {
			materialIds.add(item.getId());
		}

		// 校验我方系统是否存在
		List<Material> materials = materialDao.selectByIds(materialIds);
		if (materials == null || materials.size() != materialIds.size()) {
			throw new BusinessException(StatusCode.SC500, "存在无效的素材ID");
		}

		// 已驳回或已通过的记录更新状态
		List<Material> updatedMaterials = new ArrayList<Material>();
		for (MaterialAuditResultModel item : auditResults) {
			// 审核中的素材不处理
			if (item.getStatus() == null || MaterialStatusCode.MSC10003.getValue() == item.getStatus().intValue()) {
				continue;
			}

			// 更新审核状态
			Material updateItem = new Material();
			updateItem.setStatus(Byte.valueOf(item.getStatus().toString()));
			updateItem.setUpdatedTime(new Date());
			updateItem.setId(Integer.valueOf(item.getId()));

			updatedMaterials.add(updateItem);
		}

		int effortRows = materialDao.updateByBath(updatedMaterials);
		if (effortRows != updatedMaterials.size()) {
			throw new BusinessException(StatusCode.SC500);
		}
	}

	/**
	 * 素材提交媒体后更改状态为审核中
	 * 
	 * @param materialIds
	 *            我方的广告主ID
	 */
	@Transactional
	@Override
	public void updateStatusAfterUpload(List<String> materialIds) {
		// 参数为空
		if (materialIds == null || materialIds.isEmpty()) {
			return;
		}

		// 获取我方的广告主，并校验我方系统是否存在
		List<Material> materials = materialDao.selectByIds(materialIds);
		if (materials == null || materials.size() != materialIds.size()) {
			throw new BusinessException(StatusCode.SC500, "存在无效的素材ID");
		}

		// 设置状态为审核中
		List<Material> updatedMaterials = new ArrayList<Material>();
		for (Material item : materials) {
			Material updateItem = new Material();

			// 状态修改为审核中
			updateItem.setStatus(Byte.valueOf(String.valueOf(MaterialStatusCode.MSC10003.getValue())));
			updateItem.setUpdatedTime(new Date());
			updateItem.setId(item.getId());

			updatedMaterials.add(updateItem);
		}

		int effortRows = materialDao.updateByBath(updatedMaterials);
		if (effortRows != updatedMaterials.size()) {
			throw new BusinessException(StatusCode.SC500);
		}
	}

	/**
	 * DSP 端查询素材审核状态
	 * 
	 * @param ids
	 *            DSP平台定义的素材 ID
	 * @param dspId
	 * @return
	 */
	@Override
	public List<MaterialAuditResultModel> getMaterialAuditResult(String ids, String dspId) {
		// 解析传入的素材ID
		String[] idStrs = MaterialRule.parseStringToDistinctArray(ids);

		// 查询广告主的审核结果
		List<MaterialAuditResultModel> results = new ArrayList<MaterialAuditResultModel>();
		if (idStrs != null && idStrs.length > 1) {
			List<Material> selectMaterials = materialDao.selectByMaterialKeysAndDspId(idStrs, dspId);
			MaterialRule.convert(selectMaterials, results);
		}

		return results;
	}

	/**
	 * DSP端上传素材
	 * 
	 * @param entity
	 * @return operationResultModel
	 */
	@Transactional
	@Override
	public void upload(MaterialModel entity) {
		// 校验参数合法性
		MaterialRule.paramterValidate(entity);

		// 查询关联的媒体是否存在且有效
		String[] distinctMediaIds = MaterialRule.parseListToDistinctArray(entity.getMediaId());
		List<SysMedia> uploadedMedias = mediaDao.selectMedias(distinctMediaIds);
		MediaRule.checkMedias(distinctMediaIds, uploadedMedias);

		// 判断所有需要广告主需要审核的媒体是否都已审核通过
		String errorMsg = advertiserService.validateAdKeyAndMedias(uploadedMedias, entity.getDspId(), entity.getAdvertiserId());
		if (!StringUtils.isBlank(errorMsg)) {
			throw new BusinessException(StatusCode.SC414, errorMsg);
		}

		// 判断素材与媒体是否已存在
		List<Material> materials = materialDao.selectByMaterialKeyAndMediaIds(entity);
		List<Map<Integer, Material>> classfiedMaps = new ArrayList<Map<Integer, Material>>();
		MaterialRule.classifyMaterials(materials, entity, classfiedMaps);

		// 存在待审核、审核中，审核通过的不允许推送，提示信息
		errorMsg = MaterialRule.validateMaterials(classfiedMaps, entity.getId());
		if (errorMsg != null) {
			throw new BusinessException(StatusCode.SC411, errorMsg);
		}

		// 数据存储
		// 保存未提交的素材和媒体关系
		if (classfiedMaps.get(4) != null && classfiedMaps.get(4).size() > 0) {
			List<Material> insertedRecords = MaterialRule.convert(classfiedMaps.get(4));
			for (Material item : insertedRecords) {
				int effortRows = materialDao.insertMaterial(item);
				if (effortRows != 1) {
					throw new BusinessException(StatusCode.SC500);
				}
			}
		}

		// 更新驳回的素材和媒体关系
		if (classfiedMaps.get(3) != null && classfiedMaps.get(3).size() > 0) {
			List<Material> updateRecords = MaterialRule.convert(classfiedMaps.get(3));
			int effortRows = materialDao.updateByBath(updateRecords);
			if (effortRows != updateRecords.size()) {
				throw new BusinessException(StatusCode.SC500);
			}
		}

		// 根据媒体审核模式处理
		audit(uploadedMedias, classfiedMaps.get(4), classfiedMaps.get(3));
	}

	private void audit(List<SysMedia> uploadedMedias, Map<Integer, Material> unUploadedMaterials, Map<Integer, Material> rejectedMaterials) {
		for (SysMedia media : uploadedMedias) {
			// 平台审核不处理
			if (MaterialAuditMode.MAM10002.getValue() == media.getMaterialAuditMode().intValue()) {
				continue;
			}

			Material item = unUploadedMaterials.get(media.getId());
			if (item == null) {
				item = unUploadedMaterials.get(media.getId());
			}
			if (item != null) {
				Material updateItem = new Material();
				// 如果模式是不审核，审核状态修改为审核通过
				if (MaterialAuditMode.MAM10001.getValue() == media.getMaterialAuditMode().intValue()) {
					updateItem.setStatus(Byte.valueOf(String.valueOf(MaterialStatusCode.MSC10004.getValue())));
					updateItem.setUpdatedTime(new Date());

					updateItem.setId(item.getId());
				}

				// 如果模式是媒体审核，推送给媒体，状态修改为审核中
				/*
				 * if (MaterialAuditMode.MAM10003.getValue() ==
				 * media.getMaterialAuditMode().intValue()) { // 推送给媒体 TODO
				 * 
				 * // 状态修改为审核中 updateItem.setStatus(Byte.valueOf(String.valueOf(
				 * MaterialStatusCode.MSC10003.getValue())));
				 * updateItem.setUpdatedTime(new Date());
				 * 
				 * updateItem.setId(item.getId()); }
				 */

				int effortRows = materialDao.updateByPrimaryKeySelective(updateItem);
				if (effortRows != 1) {
					throw new BusinessException(StatusCode.SC500);
				}
			}
		}
	}
}