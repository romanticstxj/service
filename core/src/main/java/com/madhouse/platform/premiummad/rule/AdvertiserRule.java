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

import com.madhouse.platform.premiummad.constant.AdevertiserIndustry;
import com.madhouse.platform.premiummad.constant.AdvertiserStatusCode;
import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.entity.Advertiser;
import com.madhouse.platform.premiummad.exception.BusinessException;
import com.madhouse.platform.premiummad.model.AdvertiserAuditResultModel;
import com.madhouse.platform.premiummad.model.AdvertiserModel;

public class AdvertiserRule extends BaseRule {

	/**
	 * 提取 map 的 values
	 * 
	 * @param map
	 * @return
	 */
	public static List<Advertiser> convert(Map<Integer, Advertiser> map) {
		List<Advertiser> list = new ArrayList<Advertiser>();
		Iterator<Entry<Integer, Advertiser>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			list.add(iterator.next().getValue());
		}
		return list;
	}

	/**
	 * 校验参数合法性
	 * 
	 * @param entity
	 */
	public static void paramterValidate(AdvertiserModel entity) {
		if (AdevertiserIndustry.getDescrip(entity.getIndustry().intValue()) == null) {
			throw new BusinessException(StatusCode.SC412, "广告主所属工业编码不存在");
		}
		if (entity.getMediaId() == null || entity.getMediaId().isEmpty()) {
			throw new BusinessException(StatusCode.SC400, "广告主关联的媒体ID必须");
		}
	}

	/**
	 * 存在待审核、审核中，审核通过的不允许推送，提示信息
	 * 
	 * @param classfiedMaps
	 * @param AdvertiserId
	 * @return
	 */
	public static String validateAdvertisers(List<Map<Integer, Advertiser>> classfiedMaps, String advertiserKey) {
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
			return "广告主[" + advertiserKey + "]关联的" + errorMsg.toString();
		}
		return null;
	}

	/**
	 * 将需要上传的广告主媒体关系分类（上传过和为上传过）
	 * 
	 * @param advertiserMedias
	 * @param entity
	 * @param classfiedMaps
	 */
	public static void classifyAdvertisers(List<Advertiser> advertisers, AdvertiserModel entity, List<Map<Integer, Advertiser>> classfiedMaps) {
		Map<Integer, Advertiser> unUploadedAdvertisers = new HashMap<Integer, Advertiser>();
		Map<Integer, Advertiser> unAuditedAdvertisers = new HashMap<Integer, Advertiser>();
		Map<Integer, Advertiser> audittingAdvertisers = new HashMap<Integer, Advertiser>();
		Map<Integer, Advertiser> auditedAdvertisers = new HashMap<Integer, Advertiser>();
		Map<Integer, Advertiser> rejectedAdvertisers = new HashMap<Integer, Advertiser>();

		Set<Integer> usedMediaIdSet = new HashSet<Integer>();
		for (Integer mediaId : entity.getMediaId()) {
			// 重复媒体过滤
			if (usedMediaIdSet.contains(Integer.valueOf(mediaId))) {
				continue;
			}
			usedMediaIdSet.add(Integer.valueOf(mediaId));

			boolean record = false;
			for (Advertiser advertiser : advertisers) {
				if (advertiser.getAdvertiserKey().equals(entity.getId()) && advertiser.getMediaId().intValue() == mediaId.intValue()) {
					// 已驳回,更新广告信息，状态改为待审核
					if (AdvertiserStatusCode.ASC10001.getValue() == advertiser.getStatus().intValue()) {
						advertiser = buildAdvertiser(advertiser, entity, mediaId);
						advertiser.setStatus(Byte.valueOf(String.valueOf(AdvertiserStatusCode.ASC10002.getValue())));// 状态置为待审核
						rejectedAdvertisers.put(mediaId, advertiser);
						record = true;
						break;
					}
					// 待审核
					if (AdvertiserStatusCode.ASC10002.getValue() == advertiser.getStatus().intValue()) {
						unAuditedAdvertisers.put(mediaId, advertiser);
						record = true;
						break;
					}
					// 审核中
					if (AdvertiserStatusCode.ASC10003.getValue() == advertiser.getStatus().intValue()) {
						audittingAdvertisers.put(mediaId, advertiser);
						record = true;
						break;
					}
					// 审核通过
					if (AdvertiserStatusCode.ASC10004.getValue() == advertiser.getStatus().intValue()) {
						auditedAdvertisers.put(mediaId, advertiser);
						break;
					}
				}
			}
			// 未上传过
			if (!record) {
				Advertiser newEntity = buildAdvertiser(null, entity, mediaId);
				unUploadedAdvertisers.put(mediaId, newEntity);
			}
		}
		classfiedMaps.add(0, unAuditedAdvertisers); // 待审核
		classfiedMaps.add(1, audittingAdvertisers); // 审核中
		classfiedMaps.add(2, auditedAdvertisers); // 审核通过
		classfiedMaps.add(3, rejectedAdvertisers); // 已驳回
		classfiedMaps.add(4, unUploadedAdvertisers); // 未上传过
	}

	/**
	 * 构建广告主实体
	 * 
	 * @param source
	 * @return
	 */
	public static Advertiser buildAdvertiser(Advertiser advertiser, AdvertiserModel entity, Integer mediaId) {
		// 第一次新增
		if (advertiser == null) {
			advertiser = new Advertiser();
			advertiser.setDspId(Integer.valueOf(entity.getDspId()));
			advertiser.setAdvertiserKey(entity.getId()); // DSP端提供的广告主ID
			advertiser.setMediaId(mediaId);
			advertiser.setStatus(Byte.valueOf(String.valueOf(AdvertiserStatusCode.ASC10002.getValue()))); // 状态置为待审核
			advertiser.setCreatedTime(new Date());
			advertiser.setUpdatedTime(advertiser.getCreatedTime());
		} else { // 更新
			advertiser.setUpdatedTime(new Date());
		}

		advertiser.setAddress(entity.getAddress());
		advertiser.setAdvertiserName(entity.getName());
		advertiser.setContact(entity.getContact());
		advertiser.setIndustry(Short.valueOf(entity.getIndustry().toString()));
		advertiser.setLicense(entity.getLience());
		advertiser.setPhone(entity.getPhone());
		advertiser.setWebsite(entity.getWebSite());

		// set default value
		advertiser.setAuditedUser(Integer.valueOf(0));
		advertiser.setReason("");

		return advertiser;
	}

	/**
	 * 对象转换器
	 * 
	 * @param source
	 * @param destination
	 */
	public static void convert(List<Advertiser> source, List<AdvertiserAuditResultModel> destination) {
		if (source == null) {
			return;
		}

		if (source.isEmpty()) {
			return;
		}

		for (Advertiser sourceItem : source) {
			AdvertiserAuditResultModel destinationItem = new AdvertiserAuditResultModel();
			destinationItem.setId(sourceItem.getAdvertiserKey()); // DSP
																	// 传过来的广告主ID
			destinationItem.setMediaId(String.valueOf(sourceItem.getMediaId()));
			destinationItem.setStatus(Integer.valueOf(sourceItem.getStatus()));
			destinationItem.setErrorMessage(sourceItem.getReason());
			destination.add(destinationItem);
		}
	}
}
