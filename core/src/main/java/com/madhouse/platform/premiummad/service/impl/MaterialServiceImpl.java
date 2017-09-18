package com.madhouse.platform.premiummad.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.madhouse.platform.premiummad.constant.MaterialAuditMode;
import com.madhouse.platform.premiummad.constant.MaterialStatusCode;
import com.madhouse.platform.premiummad.constant.MediaNeedAdspace;
import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.dao.AdspaceDao;
import com.madhouse.platform.premiummad.dao.MaterialMapper;
import com.madhouse.platform.premiummad.dao.PolicyDao;
import com.madhouse.platform.premiummad.dao.SysMediaMapper;
import com.madhouse.platform.premiummad.entity.Adspace;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.entity.Policy;
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
	
	private Logger LOGGER = LoggerFactory.getLogger(MaterialServiceImpl.class);
	
	@Autowired
	private SysMediaMapper mediaDao;

	@Autowired
	private MaterialMapper materialDao;

	@Autowired
	private IAdvertiserService advertiserService;

	@Autowired
	private PolicyDao policyDao;
	
	@Autowired
	private AdspaceDao adspaceDao;
	
	/**
	 * 根据媒体返回的结果更新状态-通过媒体key更新
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

		// 已驳回或已通过的记录更新状态
		for (MaterialAuditResultModel item : auditResults) {
			// 审核中的素材不处理
			if (item.getStatus() == null || MaterialStatusCode.MSC10003.getValue() == item.getStatus().intValue()) {
				continue;
			}

			// 更新审核状态
			Material updateItem = new Material();
			updateItem.setStatus(Byte.valueOf(item.getStatus().toString()));
			updateItem.setUpdatedTime(new Date());
			updateItem.setReason(item.getErrorMessage());
			updateItem.setMediaQueryKey(item.getMediaQueryKey());
			// 部分媒体审核成功后会生产一个新的key作为投放时候使用，此处做更新
			updateItem.setMediaMaterialKey(item.getMediaMaterialKey());
			updateItem.setMediaMaterialUrl(item.getMediaMaterialUrl());
			updateItem.setMediaId(Integer.valueOf(item.getMediaId()));
			
			int effortRows = materialDao.updateByMediaAndMediaQueryKey(updateItem);
			if (effortRows != 1) {
				LOGGER.info("素材更新失败[mediaId=" + updateItem.getMediaId() + ",mediaQueryKey=" + updateItem.getMediaQueryKey() + "]");
				continue;
			}
		}
	}
	
	/**
	 * 根据媒体返回的结果更新状态-通过素材ID更新
	 * 
	 * @param auditResults
	 */
	@Transactional
	@Override
	public void updateStatusToMediaByMaterialId(List<MaterialAuditResultModel> auditResults) {
		// 参数校验
		if (auditResults == null || auditResults.isEmpty()) {
			return;
		}

		// 已驳回或已通过的记录更新状态
		for (MaterialAuditResultModel item : auditResults) {
			// 审核中的素材不处理
			if (item.getStatus() == null || MaterialStatusCode.MSC10003.getValue() == item.getStatus().intValue()) {
				continue;
			}

			// 更新审核状态
			Material updateItem = new Material();
			updateItem.setStatus(Byte.valueOf(item.getStatus().toString()));
			updateItem.setUpdatedTime(new Date());
			updateItem.setReason(item.getErrorMessage());
			// 部分媒体审核成功后会生产一个新的key作为投放时候使用，此处做更新
			updateItem.setMediaMaterialKey(item.getMediaMaterialKey());
			updateItem.setMediaMaterialUrl(item.getMediaMaterialUrl());
			updateItem.setId(Integer.valueOf(item.getId()));
			
			int effortRows = materialDao.updateByPrimaryKeySelective(updateItem);
			if (effortRows != 1) {
				LOGGER.info("素材更新失败[mediaId=" + updateItem.getMediaId() + ",materialId=" + updateItem.getId() + "]");
				continue;
			}
		}
	}

	/**
	 * 素材提交媒体后更改状态为审核中
	 * 
	 * @param materialIdKeys <materialId, mediaMaterialKey-mediaMaterial>
	 *            我方的广告主ID
	 */
	@Transactional
	@Override
	public void updateStatusAfterUpload(Map<Integer, String[]> materialIdKeys) {
		// 参数为空
		if (materialIdKeys == null || materialIdKeys.isEmpty()) {
			return;
		}

		// 获取我方的广告主，并校验我方系统是否存在
		List<Integer> materialIds = new ArrayList<>();
		materialIds.addAll(materialIdKeys.keySet());
		List<Material> materials = materialDao.selectByIds(materialIds);
		if (materials == null || materials.size() != materialIds.size()) {
			throw new BusinessException(StatusCode.SC418, "存在无效的素材ID");
		}

		// 设置状态为审核中
		List<Material> updatedMaterials = new ArrayList<Material>();
		for (Material item : materials) {
			Material updateItem = new Material();
			
			// 状态修改为审核中
			updateItem.setStatus(Byte.valueOf(String.valueOf(MaterialStatusCode.MSC10003.getValue())));
			
			String[] mediaQueryAndMaterialKeys = materialIdKeys.get(item.getId());
			// 查询媒体方素材状态所用key
			updateItem.setMediaQueryKey(mediaQueryAndMaterialKeys[0]);
			// 媒体分配的key
			if (mediaQueryAndMaterialKeys.length > 1 && !StringUtils.isBlank(mediaQueryAndMaterialKeys[1])) {
				updateItem.setMediaMaterialKey(item.getMediaMaterialKey());
			}
			updateItem.setUpdatedTime(new Date());
			updateItem.setId(item.getId());

			updatedMaterials.add(updateItem);
		}

		int effortRows = materialDao.updateByBath(updatedMaterials);
		if (effortRows != updatedMaterials.size()) {
			LOGGER.info("素材部分更新失败");
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
		// 参数校验
		if (StringUtils.isBlank(ids)) {
			throw new BusinessException(StatusCode.SC400, "DSP定义的素材ID必须[id]");
		}
		
		// 解析传入的素材ID
		String[] idStrs = MaterialRule.parseStringToDistinctArray(ids);

		// 查询广告主的审核结果
		List<MaterialAuditResultModel> results = new ArrayList<MaterialAuditResultModel>();
		if (idStrs != null && idStrs.length > 0) {
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
		String[] distinctMediaIds = {entity.getMediaId().toString()};
		List<SysMedia> uploadedMedias = mediaDao.selectMedias(distinctMediaIds);
		MediaRule.checkMedias(distinctMediaIds, uploadedMedias);

		// 判断需要广告位是否与媒体关联
		if (MediaNeedAdspace.getValue(entity.getMediaId())) {
			List<Adspace> adspaces = adspaceDao.selectByIds(entity.getAdspaceId());
			MaterialRule.validateAdsapce(adspaces, entity);
		}

		// 判断提交DealID，是否存在且类型为1、2并且与deliveryType一致
		if (!StringUtils.isBlank(entity.getDealId())) {
			Policy policy = policyDao.selectByPrimaryKey(Integer.valueOf(entity.getDealId()));
			MaterialRule.validatePolicy(policy, entity);
		}
		
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
		errorMsg = MaterialRule.validateMaterials(classfiedMaps, entity.getId(), entity.getMediaId());
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
			if (effortRows != 1) {
				throw new BusinessException(StatusCode.SC500);
			}
		}

		// 根据媒体审核模式处理
		audit(uploadedMedias, classfiedMaps.get(4), classfiedMaps.get(3));
	}

	private void audit(List<SysMedia> uploadedMedias, Map<Integer, Material> unUploadedMaterials, Map<Integer, Material> rejectedMaterials) {
		for (SysMedia media : uploadedMedias) {
			// 平台、媒体审核不处理
			if (MaterialAuditMode.MAM10002.getValue() == media.getMaterialAuditMode().intValue() || MaterialAuditMode.MAM10003.getValue() == media.getMaterialAuditMode().intValue()) {
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
					
					int effortRows = materialDao.updateByPrimaryKeySelective(updateItem);
					if (effortRows != 1) {
						throw new BusinessException(StatusCode.SC500);
					}
				}
			}
		}
	}

	@Override
	public List<Material> queryAll(List<Integer> mediaIds) {
		return materialDao.queryAll(mediaIds);
	}

	@Override
	public Material queryById(Integer id) {
		return materialDao.queryById(id);
	}

	@Override
	public boolean auditMaterial(String[] ids, Integer status, String reason, Integer userId) {
		if(ids == null || ids.length == 0){
			throw new BusinessException(StatusCode.SC422);
		}
		
		List<String> idList = materialDao.selectAuditableMaterials(ids);
		if(idList == null || idList.size() == 0){ //请至少选择一个可以审核的记录
			throw new BusinessException(StatusCode.SC421);
		}
		String[] auditableIds = idList.toArray(new String[]{});
		materialDao.auditMaterial(auditableIds, status, reason, userId);
		return idList.size() == ids.length;
	}
}
