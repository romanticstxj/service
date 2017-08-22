package com.madhouse.platform.premiummad.media.dianping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.constant.MaterialStatusCode;
import com.madhouse.platform.premiummad.constant.MediaMapping;
import com.madhouse.platform.premiummad.dao.MaterialMapper;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.media.model.DianpingCreativeInfoRequest;
import com.madhouse.platform.premiummad.media.model.DianpingUploadCreativeRequest;
import com.madhouse.platform.premiummad.media.model.DianpingUploadCreativeResponse;
import com.madhouse.platform.premiummad.media.util.DianpingHttpUtil;
import com.madhouse.platform.premiummad.service.IMaterialService;
import com.madhouse.platform.premiummad.util.StringUtils;

@Component
public class DianpingMaterialUploadApiTask {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DianpingMaterialUploadApiTask.class);
	
	@Value("${dianping.uploadMaterialUrl}")
	private String uploadMaterialUrl;

	@Value("${imp_montior_pm}")
	private String imp_montior_pm;
	
	@Value("${imp_montior_ex}")
	private String imp_montior_ex;

	@Value("${clk_montior_ex}")
	private String clk_montior_ex;
	
	@Value("${mh_ad_beta_mapping_ios_1}")
	private String mh_ad_beta_mapping_ios_1;

	@Value("${mh_ad_beta_mapping_android_1}")
	private String mh_ad_beta_mapping_android_1;

	@Value("${mh_ad_beta_mapping_ios_2}")
	private String mh_ad_beta_mapping_ios_2;

	@Value("${mh_ad_beta_mapping_android_2}")
	private String mh_ad_beta_mapping_android_2;

	@Value("${mh_ad_beta_mapping_ios_3}")
	private String mh_ad_beta_mapping_ios_3;

	@Value("${mh_ad_beta_mapping_android_3}")
	private String mh_ad_beta_mapping_android_3;

	@Autowired
	private MaterialMapper materialDao;
	
	@Autowired
	private IMaterialService materialService;

	@Autowired
	private DianpingHttpUtil dianpingHttpUtil;
	
	/**
	 * 上传物料
	 */
	public void uploadMaterial() {
		LOGGER.info("++++++++++Dianping upload material begin+++++++++++");

		// 查询所有待审核且媒体的素材的审核状态是媒体审核的
		List<Material> unSubmitMaterials = materialDao.selectMediaMaterials(MediaMapping.DIANPING.getValue(), MaterialStatusCode.MSC10002.getValue());
		if (unSubmitMaterials == null || unSubmitMaterials.isEmpty()) {
			LOGGER.info("美团点评没有未上传的广告主");
			LOGGER.info("++++++++++Dianping News upload material end+++++++++++");
			return;
		}

		// 上传到媒体
		LOGGER.info("DianpingMaterialUploadApiTask-Dianping", unSubmitMaterials.size());

		Map<Integer, String[]> materialIdKeys = new HashMap<Integer, String[]>();
		for (Material material : unSubmitMaterials) {
			Map<String, String> paramMap = buildMaterialRequest(material);
			String brandType = "001";// TODO
			String postResult = dianpingHttpUtil.post(uploadMaterialUrl, paramMap, brandType);
			if (!StringUtils.isEmpty(postResult)) {
				DianpingUploadCreativeResponse dianpingGetStatusResponse = JSON.parseObject(postResult, DianpingUploadCreativeResponse.class);
				// 0：成功
				if (dianpingGetStatusResponse.getRet() == 0) {
					LOGGER.info("DianpingUploadMaterial--SUCCESS");
					String[] mediaMaterialIdKeys = {dianpingGetStatusResponse.getData().getCreativeId(), dianpingGetStatusResponse.getData().getCreativeId()};
					materialIdKeys.put(material.getId(), mediaMaterialIdKeys);
				} else {
					LOGGER.error("素材[materialId=" + material.getId() + "]上传失败-" + dianpingGetStatusResponse.getRet() + " " + dianpingGetStatusResponse.getMsg());
				}
			} else {
				LOGGER.error("素材[materialId=" + material.getId() + "]上传失败");
			}
		}

		// 更新我方素材信息
		if (!materialIdKeys.isEmpty()) {
			materialService.updateStatusAfterUpload(materialIdKeys);
		}

		LOGGER.info("++++++++++Dianping upload material end+++++++++++");
	}
	
	/**
	 * 构建上传物料 request 
	 * 
	 * @param material
	 * @return
	 */
	private Map<String, String> buildMaterialRequest(Material material) {
		DianpingUploadCreativeRequest request = new DianpingUploadCreativeRequest();
		// 创意对应广告位 ID
		request.setSlotId(getMediaAdspaceId(material.getAdspaceId()));

		// 封装creativeInfoRequest对象
		DianpingCreativeInfoRequest creativeInfoRequest = new DianpingCreativeInfoRequest();
		
		// 物料上传地址
		List<String> imgList = new ArrayList<String>();
		if (null != material.getAdMaterials() && !material.getAdMaterials().equals("")) {
			String[] adMaterials = material.getAdMaterials().split("\\|");
			for (int i = 0; i < adMaterials.length; i++) {
				imgList.add(adMaterials[i]);
			}
		}
		creativeInfoRequest.setImg(imgList);

		// 物料标题
		List<String> textList = new ArrayList<String>();
		if (null != material.getTitle() && !material.getTitle().equals("")) {
			textList.add(material.getTitle());
		}
		creativeInfoRequest.setText(textList);
		request.setCreativeInfo(creativeInfoRequest);

		// 落地页
		request.setLandingPage(material.getLpgUrl());

		// 曝光打点检测地址(数组,JSON 格式)
		List<String> impressionMonitorList = new ArrayList<String>();
		String impressionMonitor0 = imp_montior_pm + "{IMP_MACRO0}";// PremiumMad Url														// URL
		String impressionMonitor1 = imp_montior_ex + "{IMP_MACRO2}";// exchange
		impressionMonitorList.add(impressionMonitor0);

		if (null != material.getImpUrls() && !material.getImpUrls().equals("")) {
			// 物料中第三方的监播地址
			impressionMonitor1 = material.getImpUrls();
			String[] impressionMonitorArray = impressionMonitor1.split("\\|");
			if (null != impressionMonitorArray) {
				for (int i = 0; i < impressionMonitorArray.length; i++) {
					// 时间
					if (impressionMonitorArray[i].matches("^-?\\d+$")) {
						continue;
					}
					impressionMonitorList.add(impressionMonitorArray[i]);
				}
			}
		}
		if (impressionMonitorList.size() == 1) {
			impressionMonitorList.add("");
		}
		impressionMonitorList.add(impressionMonitor1);
		request.setImpressionMonitor(impressionMonitorList);

		// 点击打点检测地址(数组,JSON 格式)
		List<String> clickMonitorList = new ArrayList<String>();
		clickMonitorList.add(clk_montior_ex + "{CLICK_MACRO0}");
		if (null != material.getClkUrls() && !material.getClkUrls().equals("")) {
			// 支持多个点击第三方监播地址
			String[] clkMonitorArray = material.getClkUrls().split("|");
			for (int i = 0; i < clkMonitorArray.length; i++) {
				clickMonitorList.add(clkMonitorArray[i]);
			}
		}
		request.setClickMonitor(clickMonitorList);

		// 将 request 对象封装成 map
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("slotId", request.getSlotId() + "");
		paramMap.put("creativeInfo", JSON.toJSONString(request.getCreativeInfo()));
		paramMap.put("landingPage", request.getLandingPage());
		paramMap.put("impressionMonitor", JSON.toJSONString(request.getImpressionMonitor()));
		paramMap.put("clickMonitor", JSON.toJSONString(request.getClickMonitor()));

		return paramMap;
	}
	
	/**
	 * 获取
	 * @param adspaceId
	 * @return
	 */
	private int getMediaAdspaceId(int adspaceId) {
		if (String.valueOf(adspaceId).equals(mh_ad_beta_mapping_ios_1) || String.valueOf(adspaceId).equals(mh_ad_beta_mapping_android_1)) {// 点评闪惠交易成功页
			return Integer.valueOf(mh_ad_beta_mapping_ios_1);
		} else if (String.valueOf(adspaceId).equals(mh_ad_beta_mapping_ios_2) || String.valueOf(adspaceId).equals(mh_ad_beta_mapping_android_2)) {// 点评电影交易成功页
			return Integer.valueOf(mh_ad_beta_mapping_ios_2);
		} else if (String.valueOf(adspaceId).equals(mh_ad_beta_mapping_ios_3) || String.valueOf(adspaceId).equals(mh_ad_beta_mapping_android_3)) {// 点评电影票详情页
			return Integer.valueOf(mh_ad_beta_mapping_ios_3);
		}
		return 0;
	}
}
