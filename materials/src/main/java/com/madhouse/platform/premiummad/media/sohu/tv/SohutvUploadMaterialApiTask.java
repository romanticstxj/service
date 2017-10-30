package com.madhouse.platform.premiummad.media.sohu.tv;

import java.util.ArrayList;
import java.util.Date;
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
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dao.AdvertiserMapper;
import com.madhouse.platform.premiummad.dao.MaterialMapper;
import com.madhouse.platform.premiummad.entity.Advertiser;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.media.sohu.request.SohuUploadMaterialRequest;
import com.madhouse.platform.premiummad.media.sohu.response.SohuResponse;
import com.madhouse.platform.premiummad.media.sohu.util.SohuAuth;
import com.madhouse.platform.premiummad.model.MaterialAuditResultModel;
import com.madhouse.platform.premiummad.service.IMaterialService;
import com.madhouse.platform.premiummad.service.IMediaService;
import com.madhouse.platform.premiummad.service.IPolicyService;
import com.madhouse.platform.premiummad.util.DateUtils;
import com.madhouse.platform.premiummad.util.HttpUtils;
import com.madhouse.platform.premiummad.util.MacroReplaceUtil;
import com.madhouse.platform.premiummad.util.StringUtils;

@Component
public class SohutvUploadMaterialApiTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(SohutvUploadMaterialApiTask.class);

	@Value("${sohu.material.create}")
    private String materialCreateUrl;

	@Value("${imp.url}")
	private String impUrl;

	@Value("${clk.url}")
	private String clkUrl;

	@Value("${material_meidaGroupMapping_sohuTV}")
	private String mediaGroupStr;
	
	@Autowired
	private SohuAuth sohuAuth;

	@Autowired
	private MaterialMapper materialDao;
	
	@Autowired
	private IMaterialService materialService;
	
	@Autowired
	private AdvertiserMapper advertiserDao;
	
	@Autowired
	private IPolicyService policyService;

	@Autowired
	private IMediaService mediaService;
	
	/**
	 * 宏替换替换映射
	 */
	private static Map<String, String> macroClickMap;
	private static Map<String, String> macroImageMap;
	
	static {
		macroClickMap = new HashMap<String, String>();
		macroClickMap.put("__EXT1__", "%%EXT1%%");
		macroClickMap.put("__EXT2__", "%%EXT2%%");
		macroClickMap.put("__EXT3__", "%%EXT3%%");
		
		macroImageMap = new HashMap<String, String>();
		macroImageMap.put("__EXT1__", "%%EXT1%%");
		macroImageMap.put("__EXT2__", "%%EXT2%%");
		macroImageMap.put("__EXT3__", "%%EXT3%%");
	}
	
	/**
	 * 上传广告物料
	 */
	public void uploadSohuMaterial() {
		LOGGER.info("++++++++++Sohu TV upload material begin+++++++++++");
		
		/* 代码配置处理方式
		// 媒体组没有映射到具体的媒体不处理
		String value = MediaTypeMapping.getValue(MediaTypeMapping.SOHUNEWS.getGroupId());
		if (StringUtils.isBlank(value)) {
			return;
		}

		// 获取媒体组下的具体媒体
		int[] mediaIds = StringUtils.splitToIntArray(value);
		*/
		
		// 根据媒体组ID和审核对象获取具体的媒体ID
		int[] mediaIds = mediaService.getMeidaIds(mediaGroupStr, SystemConstant.MediaAuditObject.MATERIAL);

		// 媒体组没有映射到具体的媒体不处理
		if (mediaIds == null || mediaIds.length < 1) {
			return;
		}
		
		// 查询所有待审核且媒体的素材的审核状态是媒体审核的
		List<Material> unSubmitMaterials = materialDao.selectMaterialsByMeidaIds(mediaIds, MaterialStatusCode.MSC10002.getValue());
		if (unSubmitMaterials == null || unSubmitMaterials.isEmpty()) {
			/*LOGGER.info(MediaMapping.getDescrip(mediaIds) + "没有未上传的素材");*/
			LOGGER.info("Sohu TV没有未上传的素材");
			return;
		}

		// 上传到媒体
		LOGGER.info("SohuNewsUploadMaterialApiTask-sohuNews", unSubmitMaterials.size());

		Map<Integer, String[]> materialIdKeys = new HashMap<Integer, String[]>();
		List<MaterialAuditResultModel> rejusedMaterials = new ArrayList<MaterialAuditResultModel>();
		for (Material material : unSubmitMaterials) {
			Map<String, Object> paramMap = buildMaterialRequest(material);
			if (paramMap == null) {
				continue;
			}
			String request = sohuAuth.setHttpMethod("POST").setApiUrl(materialCreateUrl).setParamMap(paramMap).buildRequest();
			LOGGER.info("SoHuUploadMaterial-buildRequest info " + request);
			String result = HttpUtils.post(materialCreateUrl, request);
			if (!StringUtils.isEmpty(result)) {
				SohuResponse sohutvResponse = JSON.parseObject(result, SohuResponse.class);
				if (sohutvResponse != null) {
					boolean status = sohutvResponse.isStatus();
					if (status) {// 上传物料成功
						String content = (String)sohutvResponse.getContent();// 直接字符串
						if (content != null && !content.equals("")) {
							String[] mediaQueryAndMaterialKeys = {content, content};
							materialIdKeys.put(material.getId(), mediaQueryAndMaterialKeys);
						} else {
							LOGGER.error("素材[materialId=" + material.getId() + "]上传失败-" + result);
						}
					} else {
						MaterialAuditResultModel rejuseItem = new MaterialAuditResultModel();
						rejuseItem.setId(String.valueOf(material.getId()));
						rejuseItem.setStatus(MaterialStatusCode.MSC10001.getValue());
						rejuseItem.setMediaIds(mediaIds);
						rejuseItem.setErrorMessage(sohutvResponse.getMessage());
						rejusedMaterials.add(rejuseItem);
						LOGGER.error("素材[materialId=" + material.getId() + "]上传失败-" + result);
					}
				} else {
					LOGGER.error("素材[materialId=" + material.getId() + "]上传失败");
				}
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

		LOGGER.info("++++++++++Sohu TV upload material end+++++++++++");
	}
	
	/**
	 * 处理上传物料api的请求json
	 * 
	 * @param material
	 * @return
	 */
	private Map<String, Object> buildMaterialRequest(Material material) {
		// 获取该素材的广告主,若广告主不存在不做处理
		String[] advertiserKeys = { material.getAdvertiserKey() };
		List<Advertiser> advertisers = advertiserDao.selectByAdvertiserKeysAndDspId(advertiserKeys, String.valueOf(material.getDspId()), material.getMediaId());
		if (advertisers == null || advertisers.size() != 1) {
			LOGGER.error("广告主不存在[advertiserKey=" + material.getAdvertiserKey() + "dspId=" + material.getDspId() + "]");
			return null;
		}

		SohuUploadMaterialRequest uploadMaterialRequest = new SohuUploadMaterialRequest();
		// 媒体广告主key
		uploadMaterialRequest.setCustomer_key(advertisers.get(0).getMediaAdvertiserKey());
		uploadMaterialRequest.setMaterial_name(material.getMaterialName());

		// 素材上传地址，不可重复
		uploadMaterialRequest.setFile_source(material.getAdMaterials().split("\\|")[0]);// 物料上传地址

		// 曝光监测地址
		List<String> impUrls = new ArrayList<String>();
		if (material.getImpUrls() != null && !material.getImpUrls().isEmpty()) {
			// 素材表里以 |分割  -> startDelay1`url1|startDelay2`url2
			String[] impTrackUrlArray = material.getImpUrls().split("\\|");
			if (impTrackUrlArray != null) {
				for (int i = 0; i < impTrackUrlArray.length; i++) {
					String[] track = impTrackUrlArray[i].split("`");
					impUrls.add(MacroReplaceUtil.macroReplaceImageUrl(macroImageMap, track[1])); // 宏替换
					// 媒体最多支持5个
					if (impUrls.size() == 3) {
						break;
					}
				}
			}
		}
		impUrls.add(MacroReplaceUtil.getStr(impUrl, "?", "%%DISPLAY%%")); // SSP 宏替换
		uploadMaterialRequest.setImp(impUrls);

		// 点击监测地址
		List<String> clkTrackUrl = new ArrayList<String>();
		if (material.getClkUrls() != null && !material.getClkUrls().isEmpty()) {
			// 素材表里以 |分割
			String[] clkTrackUrlArray = material.getClkUrls().split("\\|");
			if (null != clkTrackUrlArray) {
				for (int i = 0; i < clkTrackUrlArray.length; i++) {
					clkTrackUrl.add(MacroReplaceUtil.macroReplaceImageUrl(macroClickMap, clkTrackUrlArray[i]));// 宏替换
					// 媒体最多设置 5 个
					if (i == 3) {
						break;
					}
				}
			}
		}
		clkTrackUrl.add(MacroReplaceUtil.getStr(clkUrl,"?","%%CLICK%%"));// SSP 宏替换
		uploadMaterialRequest.setClick_monitor(clkTrackUrl);

		// 落地页地址
		uploadMaterialRequest.setGotourl(material.getLpgUrl());

		// 素材所属的广告投放类型
		uploadMaterialRequest.setAdvertising_type("101000"); // 品牌，写死

		// 指定素材提交到哪个媒体审核。1：搜狐门户；2：搜狐视频。
		uploadMaterialRequest.setSubmit_to("2");// 搜狐视频

		// 指定素材将用于何种投放方式。1：RTB；2：PDB；3：PMP；4：Preferred Deal
		uploadMaterialRequest.setDelivery_type(getMediaDeliveryType(material.getDeliveryType()));

		// 执行单 ID，与竞价请求中的 impression.campaignId 对应，指定素材将 用于哪一个订单投放
		// 当投放方式为 PDB 或 Preferred Deal 时必填
		uploadMaterialRequest.setCampaign_id(policyService.getMediaDealId(material.getDealId(), material.getMediaId()));

		// 素材有效期
		uploadMaterialRequest.setExpire(getExpire(material.getEndDate()));

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("customer_key", uploadMaterialRequest.getCustomer_key());
		paramMap.put("material_name", uploadMaterialRequest.getMaterial_name());
		paramMap.put("file_source", uploadMaterialRequest.getFile_source());
		paramMap.put("imp", JSON.toJSONString(uploadMaterialRequest.getImp()));
		paramMap.put("click_monitor", JSON.toJSONString(uploadMaterialRequest.getClick_monitor()));
		paramMap.put("gotourl", uploadMaterialRequest.getGotourl());
		paramMap.put("advertising_type", uploadMaterialRequest.getAdvertising_type());
		paramMap.put("submit_to", uploadMaterialRequest.getSubmit_to());
		paramMap.put("expire", uploadMaterialRequest.getExpire());
		paramMap.put("delivery_type", uploadMaterialRequest.getDelivery_type());
		paramMap.put("campaign_id", uploadMaterialRequest.getCampaign_id());
		paramMap.put("ad_source", uploadMaterialRequest.getAd_source());
		paramMap.put("slave", JSON.toJSONString(uploadMaterialRequest.getSlave()));
		paramMap.put("material_type", uploadMaterialRequest.getMaterial_type());
		if (null != uploadMaterialRequest.getTemplate()) {
			paramMap.put("template", uploadMaterialRequest.getTemplate());
		}
		if (null != uploadMaterialRequest.getMain_attr()) {
			paramMap.put("main_attr", uploadMaterialRequest.getMain_attr());
		}
		return paramMap;
	}
	
	/**
	 * 根据有效起止日期活的有效时间（以 s 计，上限 6 个月，不传递默认 30 天））
	 * 
	 * @param endDate
	 * @return
	 */
	private int getExpire(Date endDate) {
		if (endDate != null) {
			long days = DateUtils.getDateSubtract(DateUtils.getYmd(new Date()), endDate);
			// 如果时间超过6个月，传递六个月
			if (days > 180) {
				return 6 * 30 * 24 * 60 * 60;
			} else {
				return (int) DateUtils.getDateSecondSubtract(new Date(), endDate);
			}
		}
		return 30 * 24 * 60 * 60;
	}
	
	/**
	 * 获取媒体方的投放方式
	 * 媒体方-> 1：RTB；2：PDB；3：PMP；4：Preferred Deal
	 * DSP方-> 1：PDB、2：PD、4、PMP、8：RTB；
	 * @param dspDeliveryType
	 * @return
	 */
	private int getMediaDeliveryType(Byte dspDeliveryType) {
		if (dspDeliveryType == null) {
			return 0;
		}
		if (dspDeliveryType.intValue() == 1) {
			return 2;
		}
		if (dspDeliveryType.intValue() == 2) {
			return 4;
		}
		if (dspDeliveryType.intValue() == 4) {
			return 3;
		}
		if (dspDeliveryType.intValue() == 8) {
			return 1;
		}
		return 0;
	}
}
