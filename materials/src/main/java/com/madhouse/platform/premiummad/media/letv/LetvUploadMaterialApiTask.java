package com.madhouse.platform.premiummad.media.letv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.madhouse.platform.premiummad.constant.Layout;
import com.madhouse.platform.premiummad.constant.MaterialStatusCode;
import com.madhouse.platform.premiummad.constant.MediaMapping;
import com.madhouse.platform.premiummad.dao.AdvertiserMapper;
import com.madhouse.platform.premiummad.dao.MaterialMapper;
import com.madhouse.platform.premiummad.entity.Advertiser;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.media.letv.constant.LetvConstant;
import com.madhouse.platform.premiummad.media.letv.constant.LetvErrorCode;
import com.madhouse.platform.premiummad.media.letv.constant.LetvIndustryMapping;
import com.madhouse.platform.premiummad.media.letv.request.LetvTokenRequest;
import com.madhouse.platform.premiummad.media.letv.request.LetvUploadMaterialDetailRequest;
import com.madhouse.platform.premiummad.media.letv.request.LetvUploadMaterialRequest;
import com.madhouse.platform.premiummad.media.letv.response.LetvResponse;
import com.madhouse.platform.premiummad.model.MaterialAuditResultModel;
import com.madhouse.platform.premiummad.service.IMaterialService;
import com.madhouse.platform.premiummad.util.DateUtils;
import com.madhouse.platform.premiummad.util.HttpUtils;
import com.madhouse.platform.premiummad.util.StringUtils;

@Component
public class LetvUploadMaterialApiTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(LetvUploadMaterialApiTask.class);

	@Value("${letv.materialUrl}")
	private String uploadMaterialApiUrl;

	@Autowired
	private LetvTokenRequest tokenRequest;

	@Autowired
	private MaterialMapper materialDao;

	@Autowired
	private IMaterialService materialService;

	@Autowired
	private AdvertiserMapper advertiserDao;

	/**
	 * 上传广告物料
	 */
	public void uploadMaterial() {
		LOGGER.info("++++++++++Letv upload material begin+++++++++++");

		// 查询所有待审核且媒体的素材的审核状态是媒体审核的
		List<Material> unSubmitMaterials = materialDao.selectMediaMaterials(MediaMapping.LETV.getValue(), MaterialStatusCode.MSC10002.getValue());
		if (unSubmitMaterials == null || unSubmitMaterials.isEmpty()) {
			LOGGER.info(MediaMapping.LETV.getDescrip() + "没有未上传的素材");
			LOGGER.info("++++++++++Letv upload material end+++++++++++");
			return;
		}

		// 上传到媒体
		LOGGER.info("LetvUploadMaterialApiTask-Letv", unSubmitMaterials.size());

		Map<String, String> materialKeyIdMap = new HashMap<String, String>();
		LetvUploadMaterialRequest materialRequest = buildMaterialRequest(unSubmitMaterials, materialKeyIdMap);
		String requestJson = JSON.toJSONString(materialRequest);
		LOGGER.info("LetvUpload request info " + requestJson);
		String responseJson = HttpUtils.post(uploadMaterialApiUrl, requestJson);
		LOGGER.info("LetvUpload response info " + responseJson);

		Map<Integer, String[]> materialIdKeys = new HashMap<Integer, String[]>();
		List<MaterialAuditResultModel> rejusedMaterials = new ArrayList<MaterialAuditResultModel>();
		Map<String, String> rejusedMsgMap = new HashMap<String, String>();
		if (!StringUtils.isEmpty(responseJson)) {
			LetvResponse letvResponse = JSON.parseObject(responseJson, LetvResponse.class);
			Integer result = letvResponse.getResult();
			Map<String, String> message = letvResponse.getMessage();
			if (result.equals(LetvConstant.RESPONSE_SUCCESS.getValue()) && message.size() == 0) {
				// 成功,更新物料任务表状态,媒体无自生成的key,故此处媒体用我方id标志
				for (Material material : unSubmitMaterials) {
					String[] mediaQueryAndMaterialKeys = {material.getMediaQueryKey()};
					materialIdKeys.put(material.getId(), mediaQueryAndMaterialKeys);
				}
			} else if (result.equals(LetvConstant.RESPONSE_PARAM_CHECK_FAIL.getValue()) && message.size() > 0) {
				Iterator<Entry<String, String>> iterator = message.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry<String, String> entry = iterator.next();
					int errorCode = Integer.parseInt((String) entry.getKey());
					String msgTemp = LetvErrorCode.getDescrip(errorCode);
					if (!StringUtils.isBlank(msgTemp)) {
						JSONArray mediaMaterialKeys = JSONArray.parseArray(entry.getValue());
						for (Object mediaMaterialKey : mediaMaterialKeys) {
							String key = (String) mediaMaterialKey;
							String errorMsg = "";
							if (rejusedMsgMap.containsKey(key)) {
								errorMsg = rejusedMsgMap.get(key);
							}
							errorMsg = ";" + msgTemp;
							rejusedMsgMap.put(key, errorMsg);
						}
					}
				}

				// 业务数据错误，自动驳回
				if (!rejusedMsgMap.isEmpty()) {
					Iterator<Entry<String, String>> iterator1 = rejusedMsgMap.entrySet().iterator();
					while (iterator1.hasNext()) {
						Entry<String, String> next = iterator1.next();
						MaterialAuditResultModel rejuseItem = new MaterialAuditResultModel();
						rejuseItem.setId(materialKeyIdMap.get(next.getKey()));
						rejuseItem.setStatus(MaterialStatusCode.MSC10001.getValue());
						rejuseItem.setMediaId(String.valueOf(MediaMapping.LETV.getValue()));
						rejuseItem.setErrorMessage(next.getValue().substring(1));
						rejusedMaterials.add(rejuseItem);
						LOGGER.error("素材[materialKey=" + materialKeyIdMap.get(next.getKey()) + "]上传失败-" + next.getValue().substring(1));
					}
				}

			} else {
				LOGGER.error("素材[materialId=" + Arrays.toString(getMaterialIds(unSubmitMaterials).toArray()) + "]上传失败" + "-系统认证失败");
			}
		}

		// 更新我方素材信息
		if (!materialIdKeys.isEmpty()) {
			materialService.updateStatusAfterUpload(materialIdKeys);
		}

		// 处理失败的结果，自动驳回 - 通过素材id更新
		if (!rejusedMaterials.isEmpty()) {
			materialService.updateStatusToMediaByMaterialId(rejusedMaterials);
		}

		LOGGER.info("++++++++++Letv upload material end+++++++++++");
	}

	/**
	 * 处理上传物料api的请求json
	 * 
	 * @param material
	 * @param materialKeyIdMap
	 * @return
	 */
	private LetvUploadMaterialRequest buildMaterialRequest(List<Material> unSubmitMaterials, Map<String, String> materialKeyIdMap) {
		LetvUploadMaterialRequest materialRequest = new LetvUploadMaterialRequest();
		List<LetvUploadMaterialDetailRequest> uploadMaterialRequests = new LinkedList<LetvUploadMaterialDetailRequest>();

		// 可以多个上传
		for (Material material : unSubmitMaterials) {
			String[] advertiserKeys = { material.getAdvertiserKey() };
			List<Advertiser> advertisers = advertiserDao.selectByAdvertiserKeysAndDspId(advertiserKeys, String.valueOf(material.getDspId()), material.getMediaId());
			if (advertisers == null || advertisers.size() != 1) {
				LOGGER.error("广告主不存在[advertiserKey=" + material.getAdvertiserKey() + "dspId=" + material.getDspId() + "]");
				return null;
			}

			LetvUploadMaterialDetailRequest uploadMaterialRequest = new LetvUploadMaterialDetailRequest();
			uploadMaterialRequest.setUrl(material.getAdMaterials().split("\\|")[0]);
			// 媒体方的主键是素材url
			material.setMediaQueryKey(uploadMaterialRequest.getUrl());
			// 为了推送失败驳回时使用
			materialKeyIdMap.put(material.getMediaQueryKey(), String.valueOf(material.getId()));

			uploadMaterialRequest.setLandingpage(Collections.singletonList(material.getLpgUrl()));
			uploadMaterialRequest.setAdvertiser(advertisers.get(0).getAdvertiserName());
			uploadMaterialRequest.setStartdate(DateUtils.getFormatStringByPattern("YYYY-MM-dd", material.getStartDate()));
			uploadMaterialRequest.setEnddate(DateUtils.getFormatStringByPattern("YYYY-MM-dd", material.getEndDate()));
			// eg. jpg
			int lastPotIndex = uploadMaterialRequest.getUrl().lastIndexOf(".");
			uploadMaterialRequest.setType((lastPotIndex >= 0 && uploadMaterialRequest.getUrl().length() > (lastPotIndex + 1)) ? uploadMaterialRequest.getUrl().substring(lastPotIndex + 1) : "");
			uploadMaterialRequest.setIndustry(String.valueOf(LetvIndustryMapping.getMediaIndustryId(advertisers.get(0).getIndustry())));// 添加物料行业

			// 广告位类型
			List<Integer> display = new ArrayList<Integer>();
			display.add(getDisplayByLayout(material.getLayout().intValue()));
			uploadMaterialRequest.setDisplay(display);
			uploadMaterialRequest.setDuration(material.getDuration());
			uploadMaterialRequest.setMedia(Collections.singletonList(LetvConstant.MEDIA_TYPE_LETV.getValue()));
			uploadMaterialRequests.add(uploadMaterialRequest);
		}

		materialRequest.setDspid(tokenRequest.getDspid());
		materialRequest.setToken(tokenRequest.getToken());
		materialRequest.setAd(uploadMaterialRequests);
		return materialRequest;
	}

	/**
	 * 根据我方的广告形式获取媒体方的广告位类型
	 * 
	 * @return
	 */
	private int getDisplayByLayout(int layout) {
		if (layout == Layout.LO20001.getValue()) {// 前贴
			return LetvConstant.AD_TYPE_PREVIOUS_POST.getValue();
		} else if (layout == Layout.LO20002.getValue()) {// 中贴
			return LetvConstant.AD_TYPE_IN_POST.getValue();
		} else if (layout == Layout.LO20003.getValue()) {// 后贴
			return LetvConstant.AD_TYPE_POST.getValue();
		} else if (layout == Layout.LO20004.getValue()) {// 暂停广告创意格式有：jpg、jpeg、swf、png
			return LetvConstant.AD_TYPE_SUSPEND.getValue();
		} else if (layout == Layout.LO10001.getValue()) { // 横幅
			return LetvConstant.AD_TYPE_BANNER.getValue();
		} else if (layout == Layout.LO10003.getValue()) { // 插屏
			return LetvConstant.AD_TYPE_ILLUSTRATION.getValue();
		} else if (layout == Layout.LO10002.getValue()) { // 焦点图
			return LetvConstant.AD_TYPE_FOCUSMAP.getValue();
		} else if (layout == Layout.LO10006.getValue()) { // 开机
			return LetvConstant.AD_TYPE_STARTING_UP.getValue();
		} else if (layout == Layout.LO10007.getValue()) { // 关机
			return LetvConstant.AD_TYPE_SHUTDOWN.getValue();
		} else if (layout == Layout.LO30001.getValue() || layout == Layout.LO30002.getValue() || layout == Layout.LO30003.getValue()) { // 信息流
			return LetvConstant.AD_TYPE_STREAM.getValue();
		}
		return 0;
	}

	/**
	 * 获取媒体ID列表
	 * 
	 * @param unSubmitMaterials
	 * @return
	 */
	private List<Integer> getMaterialIds(List<Material> unSubmitMaterials) {
		List<Integer> materialIds = new ArrayList<Integer>();
		if (unSubmitMaterials == null || unSubmitMaterials.isEmpty()) {
			return materialIds;
		}

		for (Material material : unSubmitMaterials) {
			materialIds.add(material.getId());
		}
		return materialIds;
	}
}
