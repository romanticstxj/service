package com.madhouse.platform.premiummad.media.letv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.constant.AdevertiserIndustry;
import com.madhouse.platform.premiummad.constant.Layout;
import com.madhouse.platform.premiummad.constant.MaterialStatusCode;
import com.madhouse.platform.premiummad.constant.MediaMapping;
import com.madhouse.platform.premiummad.dao.AdvertiserMapper;
import com.madhouse.platform.premiummad.dao.MaterialMapper;
import com.madhouse.platform.premiummad.entity.Advertiser;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.media.constant.LetvConstant;
import com.madhouse.platform.premiummad.media.model.LetvResponse;
import com.madhouse.platform.premiummad.media.model.LetvTokenRequest;
import com.madhouse.platform.premiummad.media.model.LetvUploadMaterialDetailRequest;
import com.madhouse.platform.premiummad.media.model.LetvUploadMaterialRequest;
import com.madhouse.platform.premiummad.service.IMaterialService;
import com.madhouse.platform.premiummad.util.DateUtils;
import com.madhouse.platform.premiummad.util.HttpUtils;
import com.madhouse.platform.premiummad.util.StringUtils;

@Component
public class LetvUploadMaterialApiTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(LetvUploadMaterialApiTask.class);

	@Value("${letv.materialUrl}")
	private String uploadMaterialApiUrl;

	@Value("${letv.isDebug}")
	private Boolean isDebug;
	
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
			LOGGER.info(MediaMapping.LETV.getDescrip() + "没有未上传的广告主");
			LOGGER.info("++++++++++Letv upload material end+++++++++++");
			return;
		}

		// 上传到媒体
		LOGGER.info("LetvUploadMaterialApiTask-Letv", unSubmitMaterials.size());

		LetvUploadMaterialRequest materialRequest = buildMaterialRequest(unSubmitMaterials);
		String requestJson = JSON.toJSONString(materialRequest);
		LOGGER.info("LetvUpload request info " + requestJson);
		String responseJson = HttpUtils.post(uploadMaterialApiUrl, requestJson);
		LOGGER.info("LetvUpload response info " + responseJson);
		if (!StringUtils.isEmpty(responseJson)) {
			LetvResponse letvResponse = JSON.parseObject(responseJson, LetvResponse.class);
			Integer result = letvResponse.getResult();
			Map<Object, Object> message = letvResponse.getMessage();
			if (result.equals(LetvConstant.RESPONSE_SUCCESS.getValue()) && message.size() == 0) {
				// 成功,更新物料任务表状态,媒体无自生成的key,故此处媒体用我方id标志
				Map<Integer, String> materialIdKeys = new HashMap<Integer, String>();
				for (Material material : unSubmitMaterials) {
					materialIdKeys.put(material.getId(), material.getMediaMaterialKey());
				}
			} else if (result.equals(LetvConstant.RESPONSE_PARAM_CHECK_FAIL.getValue()) && message.size() > 0) {
				// 失败,纪录错误日志
				LOGGER.error("素材[materialId=" + Arrays.toString(getMaterialIds(unSubmitMaterials).toArray()) + "]上传失败");
			}
		}

		LOGGER.info("++++++++++Letv upload material end+++++++++++");
	}

	/**
	 * 处理上传物料api的请求json
	 * 
	 * @param material
	 * @return
	 */
	private LetvUploadMaterialRequest buildMaterialRequest(List<Material> unSubmitMaterials) {
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
			uploadMaterialRequest.setUrl(material.getAdMaterials());
			// 媒体方的主键是素材url
			material.setMediaMaterialKey(uploadMaterialRequest.getUrl());
			uploadMaterialRequest.setLandingpage(Collections.singletonList(material.getLpgUrl()));

			uploadMaterialRequest.setAdvertiser(advertisers.get(0).getAdvertiserName());
			uploadMaterialRequest.setStartdate(DateUtils.getFormatStringByPattern("YYYY-MM-dd", material.getStartDate()));
			uploadMaterialRequest.setEnddate(DateUtils.getFormatStringByPattern("YYYY-MM-dd", material.getEndDate()));
			// eg. jpg
			uploadMaterialRequest.setType(material.getAdMaterials().substring(material.getAdMaterials().length() - 3)); // TODO
			uploadMaterialRequest.setIndustry(AdevertiserIndustry.getDescrip(advertisers.get(0).getIndustry()));// 添加物料行业

			// 前贴，中贴，后贴
			int adType = material.getLayout().intValue();
			List<Integer> display = new ArrayList<Integer>();
			if (adType == Layout.LO20001.getValue()) {// 前贴
				display.add(LetvConstant.AD_TYPE_PREVIOUS_POST.getValue());
			} else if (adType == Layout.LO20002.getValue()) {// 中贴
				display.add(LetvConstant.AD_TYPE_IN_POST.getValue());
			} else if (adType == Layout.LO20003.getValue()) {// 后贴
				display.add(LetvConstant.AD_TYPE_POST.getValue());
			} else if (adType == Layout.LO20004.getValue()) {// 暂停广告创意格式有：jpg、jpeg、swf、png
				display.add(LetvConstant.AD_TYPE_SUSPEND.getValue());
			}
			uploadMaterialRequest.setDisplay(display);

			if (material.getLayout().intValue() > 100 && material.getLayout().intValue() < 200) {
				uploadMaterialRequest.setDuration(0);
			} else if (material.getLayout().intValue() > 200 && material.getLayout().intValue() < 300) {
				uploadMaterialRequest.setDuration(material.getDuration());
			} else { // zip is wrong
				LOGGER.info("Material type is wrong " + Layout.getDescrip(material.getLayout()));
				continue;
			}
			uploadMaterialRequest.setMedia(Collections.singletonList(LetvConstant.MEDIA_TYPE_LETV.getValue()));
			uploadMaterialRequests.add(uploadMaterialRequest);
		}

		materialRequest.setDspid(tokenRequest.getDspid());
		materialRequest.setToken(tokenRequest.getToken());
		materialRequest.setAd(uploadMaterialRequests);
		return materialRequest;
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
