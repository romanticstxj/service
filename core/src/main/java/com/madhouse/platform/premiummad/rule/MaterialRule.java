package com.madhouse.platform.premiummad.rule;

import java.text.ParseException;
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

import org.apache.commons.lang3.StringUtils;

import com.madhouse.platform.premiummad.constant.ActiveType;
import com.madhouse.platform.premiummad.constant.DeliveryType;
import com.madhouse.platform.premiummad.constant.Layout;
import com.madhouse.platform.premiummad.constant.MaterialAuditMode;
import com.madhouse.platform.premiummad.constant.MaterialStatusCode;
import com.madhouse.platform.premiummad.constant.MediaNeedAdspace;
import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.entity.Adspace;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.entity.Policy;
import com.madhouse.platform.premiummad.entity.SysMedia;
import com.madhouse.platform.premiummad.exception.BusinessException;
import com.madhouse.platform.premiummad.model.MaterialAuditResultModel;
import com.madhouse.platform.premiummad.model.MaterialModel;
import com.madhouse.platform.premiummad.model.MonitorModel;
import com.madhouse.platform.premiummad.model.TrackModel;
import com.madhouse.platform.premiummad.util.DateUtils;

public class MaterialRule extends BaseRule {

	public static byte ClKURL = 1;
	public static byte SECURL = 2;
	public static byte IMPURL = 3;
	
	/**
	 * 校验上传的策略合法性
	 * 
	 * @param policy
	 * @param entity
	 */
	public static void validatePolicy(Policy policy, MaterialModel entity) {
		if (policy == null) {
			throw new BusinessException(StatusCode.SC420, "策略ID不存在[dealId]");
		}
		// 只有 PD 及 PRD 才需要传dealId
		if (!(DeliveryType.DT10001.getValue() != policy.getType().intValue()) && !(DeliveryType.DT10002.getValue() != policy.getType().intValue())) {
			throw new BusinessException(StatusCode.SC420, "策略ID类型只支持PDB和PD两种类型[dealId]");
		}
		if (entity.getDeliveryType().intValue() != policy.getType().intValue()) {
			throw new BusinessException(StatusCode.SC420, "策略ID类型和投放方式不一致[dealId,deliveryType]");
		}
	}
	
	/**
	 * 校验上传的广告位的合法性
	 * 
	 * @param adspaces
	 * @param entity
	 */
	public static void validateAdsapce(List<Adspace> adspaces, MaterialModel entity) {
		if (adspaces == null || adspaces.size() < entity.getAdspaceId().size()) {
			throw new BusinessException(StatusCode.SC417, "存在无效的广告位ID[adspaceId]");
		}
		for (Adspace adspace : adspaces) {
			// 广告位的广告形式是图文信息流时需要组合校验 layout + materialCount
			int adspaceLayout = adspace.getLayout().intValue();
			if (adspaceLayout == 300) {
				adspaceLayout = adspaceLayout + adspace.getMaterialCount().intValue();
			}

			// 广告位的广告形式与素材的广告形式校验
			if (adspaceLayout != entity.getLayout().intValue()) {
				throw new BusinessException(StatusCode.SC417, "广告位的广告形式与素材的广告形式不一致[adspaceId=" + adspace.getId() + "]");
			}

			// 广告位的媒体与素材的媒体校验
			if (adspace.getMediaId().intValue() != entity.getMediaId().intValue()) {
				throw new BusinessException(StatusCode.SC417, "广告位所属媒体与素材所属媒体不一致[adspaceId=" + adspace.getId() + "]");
			}
		}
	}

	/**
	 * 提取 map 的 values
	 * 
	 * @param map
	 * @return
	 */
	public static List<Material> convert(Map<Integer, Material> map) {
		List<Material> list = new ArrayList<Material>();
		Iterator<Entry<Integer, Material>> iterator = map.entrySet().iterator();
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
	public static void paramterValidate(MaterialModel entity) {
		if (Layout.getDescrip(entity.getLayout().intValue()) == null) {
			throw new BusinessException(StatusCode.SC400, "广告形式编码不存在[layout]");
		}
		if (DeliveryType.getDescrip(entity.getDeliveryType().intValue()) == null) {
			throw new BusinessException(StatusCode.SC400, "投放方式编码不存在[deliveryType]");
		}
		if (entity.getActType() != null) {
			if (ActiveType.getDescrip(entity.getActType()) == null) {
				throw new BusinessException(StatusCode.SC400, "广告点击行为编码不存在[actType]");
			}
		}
		if (entity.getAdm() == null || entity.getAdm().isEmpty()) {
			throw new BusinessException(StatusCode.SC400, "广告素材url必须[adm]");
		}
		if (!StringUtils.isBlank(entity.getDealId())) {
			if (!entity.getDealId().matches("^-?\\d+$")) {
				throw new BusinessException(StatusCode.SC400, "策略ID不存在[dealId]");
			}
		}
		
		// 当前日期
		String todayStr = parseToDateStr(new Date(), "yyyy-MM-dd");
		
		// 开始时间和结束时间格式和合法性校验 yyyy-MM-dd
		if (!StringUtils.isBlank(entity.getStartDate())) {
			if (!isDate(entity.getStartDate(), DateUtils.FORMATE_YYYY_MM_DD)) {
				throw new BusinessException(StatusCode.SC400, "有效日期格式错误,必须为yyyy-MM-dd[startDate]");
			}
		}
		if (!StringUtils.isBlank(entity.getEndDate())) {
			if (!isDate(entity.getEndDate(), DateUtils.FORMATE_YYYY_MM_DD)) {
				throw new BusinessException(StatusCode.SC400, "失效日期格式错误,必须为yyyy-MM-dd[endDate]");
			}
			if (entity.getEndDate().compareTo(todayStr) < 0) {
				throw new BusinessException(StatusCode.SC400, "失效日期[endDate]无效，必须大于当前日期");
			}
		}
		if (!StringUtils.isBlank(entity.getStartDate()) && !StringUtils.isBlank(entity.getEndDate())) {
			if (entity.getEndDate().compareTo(entity.getStartDate()) < 0) {
				throw new BusinessException(StatusCode.SC400, "失效日期[endDate]必须大于有效日期[startDate]");
			}
		}

		// 校验URL合法性
		if (!StringUtils.isBlank(entity.getIcon())) {
			if (!isUrl(entity.getIcon())) {
				throw new BusinessException(StatusCode.SC419, "信息流广告图标URL格式错误[icon]");
			}
		}
		if (!StringUtils.isBlank(entity.getCover())) {
			if (!isUrl(entity.getCover())) {
				throw new BusinessException(StatusCode.SC419, "视频信息流广告封面URL格式错误[cover]");
			}
		}
		if (!StringUtils.isBlank(entity.getLpgUrl())) {
			if (!isUrl(entity.getLpgUrl())) {
				throw new BusinessException(StatusCode.SC419, "广告落地页URL格式错误[lpgUrl]");
			}
		}
		if (entity.getAdm() != null && !entity.getAdm().isEmpty()) {
			for (String adm : entity.getAdm()) {
				if (!isUrl(adm)) {
					throw new BusinessException(StatusCode.SC419, "广告素材 URL格式错误[adm]");
				}
			}
		}
		if (entity.getMonitor() != null) {
			if (entity.getMonitor().getClkUrls() != null && !entity.getMonitor().getClkUrls().isEmpty()) {
				for (String clkUrl : entity.getMonitor().getClkUrls()) {
					if (!isUrl(clkUrl)) {
						throw new BusinessException(StatusCode.SC419, "点击监测URL格式错误[monitor.clkUrls]");
					}
				}
			}
			if (entity.getMonitor().getSecUrls() != null && !entity.getMonitor().getSecUrls().isEmpty()) {
				for (String secUrl : entity.getMonitor().getSecUrls()) {
					if (!isUrl(secUrl)) {
						throw new BusinessException(StatusCode.SC419, "品牌安全监测 URL格式错误[monitor.secUrls]");
					}
				}
			}
			if (entity.getMonitor().getImpUrls() != null && !entity.getMonitor().getImpUrls().isEmpty()) {
				for (TrackModel track : entity.getMonitor().getImpUrls()) {
					if (track.getStartDelay().intValue() < -2) {
						throw new BusinessException(StatusCode.SC400, "展示监测时间点错误[monitor.impUrls.startDelay],有效值为0~开始；-1~中点；-2~结束；大于0~实际秒数");
					}
					if (!isUrl(track.getUrl())) {
						throw new BusinessException(StatusCode.SC419, "展示监测 URL格式错误[monitor.impUrls.url]");
					}
				}
			}
		}
	}

	/**
	 * 存在待审核、审核中，审核通过的不允许推送，提示信息
	 * 
	 * @param classfiedMaps
	 * @param materailKey
	 * @return
	 */
	public static String validateMaterials(List<Map<Integer, Material>> classfiedMaps, String materailKey, SysMedia media) {
		StringBuilder errorMsg = new StringBuilder();
		if (classfiedMaps.get(0) != null && classfiedMaps.get(0).size() > 0) {
			errorMsg.append((MediaNeedAdspace.getValue(media.getApiType().intValue()) ? "广告主" + Arrays.toString(classfiedMaps.get(0).keySet().toArray()) : "") + "重复提交，状态为待提交");
		}
		if (classfiedMaps.get(1) != null && classfiedMaps.get(1).size() > 0) {
			errorMsg.append((MediaNeedAdspace.getValue(media.getApiType().intValue()) ? "广告主" + Arrays.toString(classfiedMaps.get(1).keySet().toArray()) : "") + "重复提交，状态为待审核");
		}
		if (classfiedMaps.get(2) != null && classfiedMaps.get(2).size() > 0) {
			errorMsg.append((MediaNeedAdspace.getValue(media.getApiType().intValue()) ? "广告主" + Arrays.toString(classfiedMaps.get(2).keySet().toArray()) : "") + "重复提交，状态为审核通过");
		}
		if (errorMsg.length() > 0) {
			return "素材[" + materailKey + "]媒体[" + media.getId() + "]" + errorMsg.toString();
		}
		return "";
	}

	/**
	 * 将需要上传的广告主媒体关系分类（上传过和未上传过）
	 * 
	 * @param adspaceIds
	 * @param materialMedias
	 * @param entity
	 * @param classfiedMaps
	 */
	public static void classifyMaterials(SysMedia media, List<Integer> adspaceIds, List<Material> materials, MaterialModel entity, List<Map<Integer, Material>> classfiedMaps) {
		Map<Integer, Material> unUploadedMaterials = new HashMap<Integer, Material>();
		Map<Integer, Material> unAuditedMaterials = new HashMap<Integer, Material>();
		Map<Integer, Material> audittingMaterials = new HashMap<Integer, Material>();
		Map<Integer, Material> auditedMaterials = new HashMap<Integer, Material>();
		Map<Integer, Material> rejectedMaterials = new HashMap<Integer, Material>();

		Integer mediaId = entity.getMediaId();
		
		// 不需要上传广告位的媒体设置一个默认值  为了循环一次
		if (!MediaNeedAdspace.getValue(media.getApiType().intValue())) {
			adspaceIds.clear();
			adspaceIds.add(0);
		}
		
		// 需要上传广告位的媒体过滤重复的
		Set<Integer> usedAdspaceIdSet = new HashSet<Integer>();

		for (Integer adspaceId : adspaceIds) {
			// 重复广告位过滤
			if (usedAdspaceIdSet.contains(Integer.valueOf(adspaceId))) {
				continue;
			}
			usedAdspaceIdSet.add(Integer.valueOf(adspaceId));

			boolean record = false;
			for (Material material : materials) {
				if ((!MediaNeedAdspace.getValue(media.getApiType().intValue()) || material.getAdspaceId().intValue() == adspaceId.intValue()) && material.getMaterialKey().equals(entity.getId()) && material.getMediaId().intValue() == mediaId.intValue()) {
					// 已驳回
					if (MaterialStatusCode.MSC10001.getValue() == material.getStatus().intValue()) {
						material = buildMaterial(media, material, entity, adspaceId);
						material.setStatus(getInitialStatus(media.getMaterialAuditMode()));// 状态置设置为初始状态
						rejectedMaterials.put(adspaceId, material);
						record = true;
						break;
					}
					// 待审核
					if (MaterialStatusCode.MSC10002.getValue() == material.getStatus().intValue()) {
						unAuditedMaterials.put(adspaceId, material);
						record = true;
						break;
					}
					// 审核中
					if (MaterialStatusCode.MSC10003.getValue() == material.getStatus().intValue()) {
						audittingMaterials.put(adspaceId, material);
						record = true;
						break;
					}
					// 审核通过
					if (MaterialStatusCode.MSC10004.getValue() == material.getStatus().intValue()) {
						auditedMaterials.put(adspaceId, material);
						record = true;
						break;
					}
				}
			}
			// 未上传过
			if (!record) {
				Material newEntity = buildMaterial(media, null, entity, adspaceId);
				unUploadedMaterials.put(adspaceId, newEntity);
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
	 * 
	 * @param entity
	 * @return
	 */
	public static Material buildMaterial(SysMedia media, Material material, MaterialModel entity, Integer adspaceId) {
		// 第一次新增
		if (material == null) {
			material = new Material();
			material.setDspId(Integer.valueOf(entity.getDspId()));
			material.setMaterialKey(entity.getId());
			material.setMediaId(entity.getMediaId());
			material.setStatus(getInitialStatus(media.getMaterialAuditMode())); // 状态置为待提交
			material.setCreatedTime(new Date());
			material.setUpdatedTime(material.getCreatedTime());
		} else { // 更新
			material.setUpdatedTime(new Date());
		}

		// 媒体不需要广告主，则不保存广告主信息
		material.setAdspaceId(!MediaNeedAdspace.getValue(media.getApiType().intValue()) ? null : adspaceId);
		material.setAdvertiserKey(entity.getAdvertiserId());
		material.setActiveType(entity.getActType() != null ? Byte.valueOf(entity.getActType().toString()) : 0);
		material.setAdMaterials(parseToString(entity.getAdm())); // 广告素材URL(多个用半角逗号分隔)
		material.setLayout(Short.valueOf(entity.getLayout().toString()));
		material.setAgency(entity.getAgency());
		material.setBrand(entity.getBrand());
		material.setClkUrls(parseToString(entity.getMonitor(), ClKURL)); // 点击监测URL(多个用半角逗号分隔)
		material.setCover(entity.getCover());

		material.setDealId(entity.getDealId());
		material.setDeliveryType(Byte.valueOf(entity.getDeliveryType().toString()));
		material.setDescription(entity.getDesc());
		material.setContent(entity.getContent());
		material.setDuration(entity.getDuration() != null ? entity.getDuration() : 0);
		try {
			material.setEndDate(parseToDate(entity.getEndDate(), "yyyy-MM-dd"));
			material.setStartDate(parseToDate(entity.getStartDate(), "yyyy-MM-dd"));
		} catch (ParseException e) {
			throw new BusinessException(StatusCode.SC500);
		}
		material.setIcon(entity.getIcon());
		material.setImpUrls(parseToString(entity.getMonitor(), IMPURL)); // 展示监测URL(多个用半角逗号分隔)
		material.setLpgUrl(entity.getLpgUrl());
		material.setMaterialName(entity.getName());
		material.setSecUrls(parseToString(entity.getMonitor(), SECURL)); // 品牌安全监测URL(多个用半角逗号分隔)
		material.setSize(entity.getW().toString() + "*" + entity.getH().toString()); // 广告素材尺寸 (width * height)
		material.setTitle(entity.getTitle());

		// set default value
		material.setAuditedUser(Integer.valueOf(0));
		material.setReason("");

		return material;
	}

	/**
	 * 根据媒体审核模式，获取媒体审核状态
	 * 
	 * @param mediaAuditMode
	 * @return
	 */
	public static byte getInitialStatus(int mediaAuditMode) {
		// 不审核 状态 -> 审核通过
		if (MaterialAuditMode.MAM10001.getValue() == mediaAuditMode) {
			return Byte.valueOf(String.valueOf(MaterialStatusCode.MSC10004.getValue()));
		}

		// 平台审核 -> 待审核
		if (MaterialAuditMode.MAM10002.getValue() == mediaAuditMode) {
			return Byte.valueOf(String.valueOf(MaterialStatusCode.MSC10003.getValue()));
		}

		// 媒体审核 -> 待提交
		if (MaterialAuditMode.MAM10003.getValue() == mediaAuditMode) {
			return Byte.valueOf(String.valueOf(MaterialStatusCode.MSC10002.getValue()));
		}

		// 审核方式异常
		throw new BusinessException(StatusCode.SC500, "关联媒体素材审核方式异常[" + mediaAuditMode + "]");
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
				result.append("`");
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
		for (String item : list) {
			result.append("|");
			result.append(item);
		}
		return result.toString().substring(1);
	}

	/**
	 * 转换器
	 * 
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
			destinationItem.setId(sourceItem.getMaterialKey()); // DSP 传过来的素材ID
			destinationItem.setMediaId(String.valueOf(sourceItem.getMediaId()));
			destinationItem.setStatus(Integer.valueOf(sourceItem.getStatus()));
			destinationItem.setAdspaceId(sourceItem.getAdspaceId());
			destinationItem.setErrorMessage(sourceItem.getReason());
			destination.add(destinationItem);
		}
	}
}
