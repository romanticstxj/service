package com.madhouse.platform.premiummad.rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.madhouse.platform.premiummad.constant.AdvertiserType;
import com.madhouse.platform.premiummad.constant.DeliveryType;
import com.madhouse.platform.premiummad.constant.MaterialStatusCode;
import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.exception.BusinessException;
import com.madhouse.platform.premiummad.model.MaterialAuditResultModel;
import com.madhouse.platform.premiummad.model.MaterialModel;
import com.madhouse.platform.premiummad.model.MonitorModel;
import com.madhouse.platform.premiummad.model.TrackModel;

public class MaterialRule extends BaseRule {
	
	public static byte ClKURL = 1;
	public static byte SECURL = 2;
	public static byte IMPURL = 3;
	
	/**
	 * 提取 map 的 values
	 * @param map
	 * @return
	 */
	public static List<Material> convert(Map<Integer, Material> map) {
		List<Material> list = new ArrayList<Material>();
		Iterator<Entry<Integer, Material>> iterator = map.entrySet().iterator();
		while(iterator.hasNext()) {
			list.add(iterator.next().getValue());
		}
		return list;
	}
	
	/**
	 * 校验参数合法性
	 * @param entity
	 */
	public static void paramterValidate(MaterialModel entity) {
		if (AdvertiserType.getDescrip(entity.getActType().intValue()) == null) {
			throw new BusinessException(StatusCode.SC419, "广告类型编码不存在");
		}
		if (DeliveryType.getDescrip(entity.getDeliveryType().intValue()) == null) {
			throw new BusinessException(StatusCode.SC415, "投放方式编码不存在");
		}
		if (entity.getMediaId() == null || entity.getMediaId().isEmpty()) {
			throw new BusinessException(StatusCode.SC400, "素材关联的媒体ID必须");
		}
		if (entity.getAdm() == null || entity.getAdm().isEmpty()) {
			throw new BusinessException(StatusCode.SC400, "广告素材url必须");
		}
	}

	/**
	 * 存在待审核、审核中，审核通过的不允许推送，提示信息
	 * @param classfiedMaps
	 * @param materailKey
	 * @return
	 */
	public static String validateMaterials(List<Map<Integer, Material>> classfiedMaps, String materailKey) {
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
	public static void classifyMaterials(List<Material> materials, MaterialModel entity, List<Map<Integer, Material>> classfiedMaps) {
		Map<Integer, Material> unUploadedMaterials = new HashMap<Integer, Material>();
		Map<Integer, Material> unAuditedMaterials = new HashMap<Integer, Material>();
		Map<Integer, Material> audittingMaterials = new HashMap<Integer, Material>();
		Map<Integer, Material> auditedMaterials = new HashMap<Integer, Material>();
		Map<Integer, Material> rejectedMaterials = new HashMap<Integer, Material>();

		Set<Integer> usedMediaIdSet = new HashSet<Integer>();
		for (Integer mediaId : entity.getMediaId()) {
			// 重复媒体过滤
			if (usedMediaIdSet.contains(Integer.valueOf(mediaId))) {
				continue;
			}
			usedMediaIdSet.add(Integer.valueOf(mediaId));
	
			boolean record = false;
			for (Material material : materials) {
				if (material.getMaterialKey().equals(entity.getId()) && material.getMediaId().intValue() == mediaId.intValue()) {
					// 已驳回
					if (MaterialStatusCode.MSC10001.getValue() == material.getStatus().intValue()) {
						material = buildMaterial(material, entity, mediaId);
						material.setStatus(Byte.valueOf(String.valueOf(MaterialStatusCode.MSC10002.getValue())));// 状态置为待审核
						rejectedMaterials.put(mediaId, material);
						record = true;
						break;
					}
					// 待审核
					if (MaterialStatusCode.MSC10002.getValue() == material.getStatus().intValue()) {
						unAuditedMaterials.put(mediaId, material);
						record = true;
						break;
					}
					// 审核中
					if (MaterialStatusCode.MSC10003.getValue() == material.getStatus().intValue()) {
						audittingMaterials.put(mediaId, material);
						record = true;
						break;
					}
					// 审核通过
					if (MaterialStatusCode.MSC10004.getValue() == material.getStatus().intValue()) {
						auditedMaterials.put(mediaId, material);
						record = true;
						break;
					}
				}
			}
			// 未上传过
			if (!record) {
				Material newEntity = buildMaterial(null, entity, mediaId);;			
				unUploadedMaterials.put(mediaId, newEntity);
			}
		}
		classfiedMaps.add(0, unAuditedMaterials); // 待审核
		classfiedMaps.add(1, audittingMaterials); // 审核中
		classfiedMaps.add(2, auditedMaterials); // 审核通过
		classfiedMaps.add(3, rejectedMaterials); // 已驳回
		classfiedMaps.add(4, unUploadedMaterials); // 未上传过
	}
	
	/**
	 * 构建素材实体
	 * @param entity
	 * @return
	 */
	public static Material buildMaterial(Material material, MaterialModel entity, Integer mediaId) {
		// 第一次新增
		if (material == null) {
			material = new Material();
			material.setDspId(Integer.valueOf(entity.getDspId()));
			material.setMaterialKey(entity.getId());
			material.setMediaId(mediaId);
			material.setStatus(Byte.valueOf(String.valueOf(MaterialStatusCode.MSC10002.getValue()))); // 状态置为待审核
			material.setCreatedTime(new Date());
			material.setUpdatedTime(material.getCreatedTime());
		} else { // 更新
			material.setUpdatedTime(new Date());
		}

		material.setAdvertiserKey(entity.getAdvertiserId());
		material.setActiveType(entity.getActType() != null ? Byte.valueOf(entity.getActType().toString()) : 0);
		material.setAdMaterials(parseToString(entity.getAdm())); // 广告素材URL(多个用半角逗号分隔)
		material.setAdType(Short.valueOf(entity.getAdType().toString()));
		material.setAgency(entity.getAgency());
		material.setBrand(entity.getBrand());
		material.setClkUrls(parseToString(entity.getMonitor(), ClKURL)); // 点击监测URL(多个用半角逗号分隔)
		material.setCover(entity.getCover());
		
		material.setDealId(entity.getDealId() != null ? Integer.valueOf(entity.getDealId()) : 0);
		material.setDeliveryType(Byte.valueOf(entity.getDeliveryType().toString()));
		material.setDescription(entity.getDesc());
		material.setDuration(entity.getDuration() != null ? entity.getDuration() : 0);
		material.setEndDate(entity.getEndDate());
		material.setIcon(entity.getIcon());
		material.setImpUrls(parseToString(entity.getMonitor(), IMPURL)); // 展示监测URL(多个用半角逗号分隔)
		material.setLpgUrl(entity.getLpgUrl());
		material.setMaterialName(entity.getName());
		material.setSecUrls(parseToString(entity.getMonitor(), SECURL)); // 品牌安全监测URL(多个用半角逗号分隔)
		material.setSize("0"); // 广告素材尺寸 TODO
		material.setStartDate(entity.getStartDate());
		material.setTitle(entity.getTitle());
		
		// set default value
		material.setAuditedUser(Integer.valueOf(0));
		material.setReason("");

		return material;
	}
	
	public static String parseToString(MonitorModel monitor, byte type) {
		if (monitor == null) {
			return "";
		}
		if (type == ClKURL) {
			return parseToString(monitor.getClkUrls());
		}
		if (type == SECURL) {
			return parseToString(monitor.getSecUrls());
		}
		if (type == IMPURL) {
			if (monitor.getImpUrls() == null || monitor.getImpUrls().isEmpty()) {
				return "";
			}
			StringBuffer result = new StringBuffer();
			for (TrackModel track : monitor.getImpUrls()) {
				result.append("|");
				result.append(track.getStartDelay());
				result.append("|");
				result.append(track.getUrl());
			}
			return result.substring(1);
		}
		return "";
	}
	
	public static String parseToString(List<String> list) {
		if (list == null || list.isEmpty()) {
			return "";
		}
		
		StringBuffer result = new StringBuffer();
		for (String item :list) {
			result.append("|");
			result.append(item);
		}
		return result.toString().substring(1);
	}
	
	/**
	 * 转换器
	 * @param source
	 * @param destination
	 */
	public static void convert(List<Material> source, List<MaterialAuditResultModel> destination) {
		if (source == null) {
			return;
		}
		
		if (source.isEmpty()) {
			return;
		}
		
		for (Material sourceItem : source) {
			MaterialAuditResultModel destinationItem = new MaterialAuditResultModel();
			destinationItem.setId(sourceItem.getMaterialKey()); //DSP 传过来的素材ID
			destinationItem.setMediaId(String.valueOf(sourceItem.getMediaId()));
			destinationItem.setStatus(Integer.valueOf(sourceItem.getStatus()));
			destinationItem.setErrorMessage(sourceItem.getReason());
			destination.add(destinationItem);
		}
	}
}