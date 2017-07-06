package com.madhouse.platform.premiummad.rule;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.beans.BeanUtils;
import com.madhouse.platform.premiummad.constant.AdvertiserMediaStatusCode;
import com.madhouse.platform.premiummad.entity.Advertiser;
import com.madhouse.platform.premiummad.entity.AdvertiserMedia;
import com.madhouse.platform.premiummad.entity.AdvertiserMediaUnion;
import com.madhouse.platform.premiummad.model.AdvertiserMediaAuditResultModel;
import com.madhouse.platform.premiummad.model.AdvertiserMediaModel;

public class AdvertiserMediaRule extends BaseRule {
	
	/**
	 * Map 转换成 List
	 * @param maps
	 * @return
	 */
	public static List<AdvertiserMedia> convert(Map<Integer, AdvertiserMediaUnion> map) {
		List<AdvertiserMedia> list = new ArrayList<AdvertiserMedia>();

		Iterator<Entry<Integer, AdvertiserMediaUnion>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			AdvertiserMedia item = new AdvertiserMedia();
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
	public static void relatedAdvertiserId(List<Map<Integer, AdvertiserMediaUnion>> classfiedMaps, Integer advertiserId) {
		// 已驳回的
		if (classfiedMaps.get(3) != null && classfiedMaps.get(3).size() > 0) {
			processIterator(classfiedMaps.get(4), advertiserId);
		}

		// 未上传过
		if (classfiedMaps.get(4) != null && classfiedMaps.get(4).size() > 0) {
			processIterator(classfiedMaps.get(4), advertiserId);
		}
	}

	private static void processIterator(Map<Integer, AdvertiserMediaUnion> map, Integer advertiserId) {
		Iterator<Entry<Integer, AdvertiserMediaUnion>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Integer, AdvertiserMediaUnion> entry = iterator.next();
			entry.getValue().setAdvertiserId(advertiserId);
		}
	}
	
	/**
	 * 存在待审核、审核中，审核通过的不允许推送，提示信息
	 * @param classfiedMaps
	 * @param AdvertiserId
	 * @return
	 */
	public static String validateAdvertiserAndMedias(List<Map<Integer, AdvertiserMediaUnion>> classfiedMaps, String advertiserKey) {
		StringBuilder errorMsg = new StringBuilder();
		if (classfiedMaps.get(0) != null && classfiedMaps.get(0).size() > 0) {
			errorMsg.append("媒体[" + classfiedMaps.get(0).keySet().toArray() + "]重复提交，状态为待审核;");
		}
		if (classfiedMaps.get(1) != null && classfiedMaps.get(1).size() > 0) {
			errorMsg.append("媒体[" + classfiedMaps.get(1).keySet().toArray() + "]重复提交，状态为审核中;");
		}
		if (classfiedMaps.get(2) != null && classfiedMaps.get(2).size() > 0) {
			errorMsg.append("媒体[" + classfiedMaps.get(2).keySet().toArray() + "]重复提交，状态为审核通过;");
		}
		if (errorMsg.length() > 0) {
			return "广告主[" + advertiserKey + "]关联的" + errorMsg.toString();
		}
		return null;
	}
	
	/**
	 * 将需要上传的广告主媒体关系分类（上传过和为上传过）
	 * @param advertiserMedias
	 * @param entity
	 * @param classfiedMaps
	 */
	public static void classifyAdvertiserAndMedias(List<AdvertiserMediaUnion> advertiserMedias, AdvertiserMediaModel entity, List<Map<Integer, AdvertiserMediaUnion>> classfiedMaps) {
		Map<Integer, AdvertiserMediaUnion> unUploadedAdMedias = new HashMap<Integer, AdvertiserMediaUnion>();
		Map<Integer, AdvertiserMediaUnion> unAuditedAdMedias = new HashMap<Integer, AdvertiserMediaUnion>();
		Map<Integer, AdvertiserMediaUnion> audittingAdMedias = new HashMap<Integer, AdvertiserMediaUnion>();
		Map<Integer, AdvertiserMediaUnion> auditedAdMedias = new HashMap<Integer, AdvertiserMediaUnion>();
		Map<Integer, AdvertiserMediaUnion> rejectedAdMedias = new HashMap<Integer, AdvertiserMediaUnion>();

		for (Integer mediaId : entity.getMediaId()) {
			for (AdvertiserMediaUnion advertiserMedia : advertiserMedias) {
				if (advertiserMedia.getMediaId().intValue() == mediaId.intValue()) {
					// 已驳回
					if (AdvertiserMediaStatusCode.AMSC10001.getValue() == advertiserMedia.getStatus().intValue()) {
						advertiserMedia.setStatus(AdvertiserMediaStatusCode.AMSC10002.getValue());// 状态置为待审核
						rejectedAdMedias.put(mediaId, advertiserMedia);
						continue;
					}
					// 待审核
					if (AdvertiserMediaStatusCode.AMSC10002.getValue() == advertiserMedia.getStatus().intValue()) {
						unAuditedAdMedias.put(mediaId, advertiserMedia);
						continue;
					}
					// 审核中
					if (AdvertiserMediaStatusCode.AMSC10003.getValue() == advertiserMedia.getStatus().intValue()) {
						audittingAdMedias.put(mediaId, advertiserMedia);
						continue;
					}
					// 审核通过
					if (AdvertiserMediaStatusCode.AMSC10004.getValue() == advertiserMedia.getStatus().intValue()) {
						auditedAdMedias.put(mediaId, advertiserMedia);
						continue;
					}
				}
			}
			// 未上传过
			AdvertiserMediaUnion newEntity = new AdvertiserMediaUnion();
			newEntity.setAdvertiserId(entity.getAdvertiserId()); //我放提供的广告主ID
			newEntity.setAdvertiserKey(entity.getId()); //DSP端提供的广告主ID
			newEntity.setMediaId(mediaId);
			newEntity.setStatus(AdvertiserMediaStatusCode.AMSC10002.getValue()); // 状态置为待审核
			newEntity.setCreatedTime(new Date());
			unUploadedAdMedias.put(mediaId, newEntity);
		}
		classfiedMaps.add(0, unAuditedAdMedias); // 待审核
		classfiedMaps.add(1, audittingAdMedias); // 审核中
		classfiedMaps.add(2, auditedAdMedias); // 审核通过
		classfiedMaps.add(3, rejectedAdMedias); // 已驳回
		classfiedMaps.add(4, unUploadedAdMedias); // 未上传过
	}
	
	/**
	 * 构建广告主实体
	 * @param source
	 * @return
	 */
	public static Advertiser buildAdvertiser(AdvertiserMediaModel entity) {
		Advertiser advertiser = new Advertiser();
		
		advertiser.setAddress(entity.getAddress());
		advertiser.setAdvertiserKey(entity.getId());
		advertiser.setAdvertiserName(entity.getName());
		advertiser.setContact(entity.getContact());
		advertiser.setCreatedTime(new Date());
		advertiser.setDspId(Integer.valueOf(entity.getDspId()));
		advertiser.setIndustry(Short.valueOf(entity.getIndustry().toString()));
		advertiser.setLicense(entity.getLience());
		advertiser.setPhone(entity.getPhone());
		advertiser.setWebsite(entity.getWebSite());

		return advertiser;
	}
	
	/**
	 * 对象转换器
	 * @param source
	 * @param destination
	 */
	public static void convert(List<AdvertiserMediaUnion> source, List<AdvertiserMediaAuditResultModel> destination) {
		if (source == null) {
			return;
		}
		
		destination = new ArrayList<AdvertiserMediaAuditResultModel>();
		if (source.isEmpty()) {
			return;
		}
		
		for (AdvertiserMediaUnion sourceItem : source) {
			AdvertiserMediaAuditResultModel destinationItem = new AdvertiserMediaAuditResultModel();
			destinationItem.setId(sourceItem.getAdvertiserKey()); //DSP 传过来的广告主ID TODO
			destinationItem.setMediaId(String.valueOf(sourceItem.getMediaId()));
			destinationItem.setStatus(Integer.valueOf(sourceItem.getStatus()));
			destinationItem.setErrorMessage(sourceItem.getReason());
			destination.add(destinationItem);
		}
	}
}
