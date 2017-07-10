package com.madhouse.platform.premiummad.rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.springframework.beans.BeanUtils;

import com.madhouse.platform.premiummad.constant.MaterialMediaStatusCode;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.entity.MaterialMedia;
import com.madhouse.platform.premiummad.entity.MaterialMediaUnion;
import com.madhouse.platform.premiummad.model.MaterialMediaAuditResultModel;
import com.madhouse.platform.premiummad.model.MaterialMediaModel;

public class MaterialMediaRule extends BaseRule {
	
	/**
	 * Map 转换成 List
	 * @param maps
	 * @return
	 */
	public static List<MaterialMedia> convert(Map<Integer, MaterialMediaUnion> map) {
		List<MaterialMedia> list = new ArrayList<MaterialMedia>();

		Iterator<Entry<Integer, MaterialMediaUnion>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			MaterialMedia item = new MaterialMedia();
			BeanUtils.copyProperties(iterator.next().getValue(), item);
			list.add(item);
		}

		return list;
	}
	
	/**
	 * 回写生成的我方广告主ID到广告主和媒体的关系中
	 * @param classfiedMaps
	 * @param advertiserId
	 */
	public static void relatedMaterialId(List<Map<Integer, MaterialMediaUnion>> classfiedMaps, Integer materialId) {
		// 已驳回的
		if (classfiedMaps.get(3) != null && classfiedMaps.get(3).size() > 0) {
			processIterator(classfiedMaps.get(3), materialId);
		}
		
		// 未上传过
		if (classfiedMaps.get(4) != null && classfiedMaps.get(4).size() > 0) {
			processIterator(classfiedMaps.get(4), materialId);
		}
	}
	
	private static void processIterator(Map<Integer, MaterialMediaUnion> map, Integer materialId) {
		Iterator<Entry<Integer, MaterialMediaUnion>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Integer, MaterialMediaUnion> entry = iterator.next();
			entry.getValue().setMaterialId(materialId);
		}
	}
	
	/**
	 * 存在待审核、审核中，审核通过的不允许推送，提示信息
	 * @param classfiedMaps
	 * @param materailKey
	 * @return
	 */
	public static String validateMaterialAndMedias(List<Map<Integer, MaterialMediaUnion>> classfiedMaps, String materailKey) {
		StringBuilder errorMsg = new StringBuilder();
		if (classfiedMaps.get(0) != null && classfiedMaps.get(0).size() > 0) {
			errorMsg.append("媒体" + Arrays.toString(classfiedMaps.get(0).keySet().toArray()) + "重复提交，状态为待审核;");
		}
		if (classfiedMaps.get(1) != null && classfiedMaps.get(1).size() > 0) {
			errorMsg.append("媒体" + Arrays.toString(classfiedMaps.get(1).keySet().toArray()) + "重复提交，状态为审核中;");
		}
		if (classfiedMaps.get(2) != null && classfiedMaps.get(2).size() > 0) {
			errorMsg.append("媒体" + Arrays.toString(classfiedMaps.get(2).keySet().toArray()) + "重复提交，状态为审核通过;");
		}
		if (errorMsg.length() > 0) {
			return "素材[" + materailKey + "]关联的" + errorMsg.toString();
		}
		return null;
	}
	
	/**
	 * 将需要上传的广告主媒体关系分类（上传过和为上传过）
	 * @param materialMedias
	 * @param entity
	 * @param classfiedMaps
	 */
	public static void classifyMaterialAndMedias(List<MaterialMediaUnion> materialMedias, MaterialMediaModel entity, List<Map<Integer, MaterialMediaUnion>> classfiedMaps) {
		Map<Integer, MaterialMediaUnion> unUploadedMaterialMedias = new HashMap<Integer, MaterialMediaUnion>();
		Map<Integer, MaterialMediaUnion> unAuditedMaterialMedias = new HashMap<Integer, MaterialMediaUnion>();
		Map<Integer, MaterialMediaUnion> audittingMaterialMedias = new HashMap<Integer, MaterialMediaUnion>();
		Map<Integer, MaterialMediaUnion> auditedMaterialMedias = new HashMap<Integer, MaterialMediaUnion>();
		Map<Integer, MaterialMediaUnion> rejectedMaterialMedias = new HashMap<Integer, MaterialMediaUnion>();

		Set<Integer> usedMediaIdSet = new HashSet<Integer>();
		for (Integer mediaId : entity.getMediaId()) {
			// 重复媒体过滤
			if (usedMediaIdSet.contains(Integer.valueOf(mediaId))) {
				continue;
			}
			usedMediaIdSet.add(Integer.valueOf(mediaId));
	
			for (MaterialMediaUnion materialMedia : materialMedias) {
				if (entity.getMaterialId() != null && materialMedia.getMaterialId().intValue() == entity.getMaterialId().intValue() && materialMedia.getMediaId().intValue() == mediaId.intValue()) {
					// 已驳回
					if (MaterialMediaStatusCode.MMSC10001.getValue() == materialMedia.getStatus().intValue()) {
						materialMedia.setStatus(Byte.valueOf(String.valueOf(MaterialMediaStatusCode.MMSC10002.getValue())));// 状态置为待审核
						rejectedMaterialMedias.put(mediaId, materialMedia);
						continue;
					}
					// 待审核
					if (MaterialMediaStatusCode.MMSC10002.getValue() == materialMedia.getStatus().intValue()) {
						unAuditedMaterialMedias.put(mediaId, materialMedia);
						continue;
					}
					// 审核中
					if (MaterialMediaStatusCode.MMSC10003.getValue() == materialMedia.getStatus().intValue()) {
						audittingMaterialMedias.put(mediaId, materialMedia);
						continue;
					}
					// 审核通过
					if (MaterialMediaStatusCode.MMSC10004.getValue() == materialMedia.getStatus().intValue()) {
						auditedMaterialMedias.put(mediaId, materialMedia);
						continue;
					}
				}
			}
			// 未上传过
			MaterialMediaUnion newEntity = new MaterialMediaUnion();
			newEntity.setMaterialId(entity.getMaterialId()); //我放提供的素材ID
			newEntity.setMaterialKey(entity.getId()); //DSP端提供的广告主ID
			newEntity.setMediaId(mediaId);
			newEntity.setStatus(Byte.valueOf(String.valueOf(MaterialMediaStatusCode.MMSC10002.getValue()))); // 状态置为待审核
			newEntity.setCreatedTime(new Date());
			
			// set default value
			newEntity.setAuditedUser(Integer.valueOf(0));
			newEntity.setAuditedTime(newEntity.getCreatedTime());
			newEntity.setReason("");
			
			unUploadedMaterialMedias.put(mediaId, newEntity);
		}
		classfiedMaps.add(0, unAuditedMaterialMedias); // 待审核
		classfiedMaps.add(1, audittingMaterialMedias); // 审核中
		classfiedMaps.add(2, auditedMaterialMedias); // 审核通过
		classfiedMaps.add(3, rejectedMaterialMedias); // 已驳回
		classfiedMaps.add(4, unUploadedMaterialMedias); // 未上传过
	}
	
	/**
	 * 构建素材实体
	 * @param entity
	 * @return
	 */
	public static Material buildMaterial(Material material, MaterialMediaModel entity) {
		// 第一次新增
		if (material == null) {
			material = new Material();
			material.setMaterialKey(entity.getId());
			material.setCreatedTime(new Date());
			material.setUpdatedTime(material.getCreatedTime());
		} else { // 更新
			material.setUpdatedTime(new Date());
		}

		material.setActiveType(Byte.valueOf(entity.getActType().toString()));
		material.setAdMaterials(entity.getAdm().toString()); // 广告素材URL(多个用半角逗号分隔)
		material.setAdType(Short.valueOf(entity.getAdType().toString()));
		material.setAdvertiserId(Integer.valueOf(entity.getAdvertiserId()));
		material.setAgency(entity.getAgency());
		material.setBrand(entity.getBrand());
		material.setClkUrls(entity.getMonitor().getClkUrls().toArray().toString()); // 点击监测URL(多个用半角逗号分隔)
		material.setCover(entity.getCover());
		
		material.setDealId(Integer.valueOf(entity.getDealId()));
		material.setDeliveryType(Byte.valueOf(entity.getDeliveryType().toString()));
		material.setDescription(entity.getDesc());
		material.setDuration(entity.getDuration());
		material.setEndDate(entity.getEndDate());
		material.setIcon(entity.getIcon());
		material.setImpUrls(""); // 展示监测URL(多个用半角逗号分隔) TODO
		material.setLpgUrl(entity.getLpgUrl());
		material.setMaterialName(entity.getName());
		material.setSecUrls(entity.getMonitor().getSecUrls().toArray().toString()); // 品牌安全监测URL(多个用半角逗号分隔)
		material.setSize("0"); // 广告素材尺寸 TODO
		material.setStartDate(entity.getStartDate());
		material.setTitle(entity.getTitle());
		
		return material;
	}
	
	/**
	 * 转换器
	 * @param source
	 * @param destination
	 */
	public static void convert(List<MaterialMediaUnion> source, List<MaterialMediaAuditResultModel> destination) {
		if (source == null) {
			return;
		}
		
		if (source.isEmpty()) {
			return;
		}
		
		for (MaterialMediaUnion sourceItem : source) {
			MaterialMediaAuditResultModel destinationItem = new MaterialMediaAuditResultModel();
			destinationItem.setId(sourceItem.getMaterialKey()); //DSP 传过来的素材ID
			destinationItem.setMediaId(String.valueOf(sourceItem.getMediaId()));
			destinationItem.setStatus(Integer.valueOf(sourceItem.getStatus()));
			destinationItem.setErrorMessage(sourceItem.getReason());
			destination.add(destinationItem);
		}
	}
}
