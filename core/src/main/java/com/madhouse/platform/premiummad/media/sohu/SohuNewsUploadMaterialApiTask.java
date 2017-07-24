package com.madhouse.platform.premiummad.media.sohu;

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
import com.madhouse.platform.premiummad.constant.MediaMapping;
import com.madhouse.platform.premiummad.dao.AdvertiserMapper;
import com.madhouse.platform.premiummad.dao.MaterialMapper;
import com.madhouse.platform.premiummad.entity.Advertiser;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.media.model.SohuUploadMaterialRequest;
import com.madhouse.platform.premiummad.media.model.SohutvResponse;
import com.madhouse.platform.premiummad.service.IMaterialService;
import com.madhouse.platform.premiummad.util.DateUtils;
import com.madhouse.platform.premiummad.util.HttpUtils;
import com.madhouse.platform.premiummad.util.StringUtils;

@Component
public class SohuNewsUploadMaterialApiTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(SohuNewsUploadMaterialApiTask.class);

	@Value("${sohu.material.create}")
    private String materialCreateUrl;

	@Value("${imp.url}")
	private String impUrl;

	@Value("${clk.url}")
	private String clkUrl;
	
	@Autowired
	private SohuNewsAuth sohuAuth;
	
	@Autowired
	private MaterialMapper materialDao;
	
	@Autowired
	private IMaterialService materialService;
	
	@Autowired
	private AdvertiserMapper advertiserDao;
	
	/**
	 * 上传广告物料
	 */
	public void uploadSohuMaterial() {
		LOGGER.info("++++++++++Sohu News upload material begin+++++++++++");

		// 查询所有待审核且媒体的素材的审核状态是媒体审核的
		List<Material> unSubmitMaterials = materialDao.selectMediaMaterials(MediaMapping.SOHUNEWS.getValue(), MaterialStatusCode.MSC10002.getValue());
		if (unSubmitMaterials == null || unSubmitMaterials.isEmpty()) {
			LOGGER.info("搜狐没有未上传的广告主");
			LOGGER.info("++++++++++Sohu News upload material end+++++++++++");
			return;
		}

		// 上传到媒体
		LOGGER.info("SohuNewsUploadMaterialApiTask-sohuNews", unSubmitMaterials.size());

		Map<Integer, String> materialIdKeys = new HashMap<Integer, String>();
		for (Material material : unSubmitMaterials) {
			Map<String, Object> paramMap = buildMaterialRequest(material);
			if (paramMap == null) {
				continue;
			}
			String request = sohuAuth.setHttpMethod("POST").setApiUrl(materialCreateUrl).setParamMap(paramMap).buildRequest();
			LOGGER.info("SoHuUploadMaterial-buildRequest info " + request);
			String result = HttpUtils.post(materialCreateUrl, request);
			if (!StringUtils.isEmpty(result)) {
				SohutvResponse sohutvResponse = JSON.parseObject(result, SohutvResponse.class);
				if (sohutvResponse != null) {
					boolean status = sohutvResponse.isStatus();
					if (status) {// 上传物料，成功，更新物料task表status为2
						String content = (String)sohutvResponse.getContent();// 直接字符串
						if (content != null && !content.equals("")) {
							materialIdKeys.put(material.getId(), content);
						} else {
							LOGGER.error("素材[materialId=" + material.getId() + "]上传失败");
						}
					}
				}
			}
		}

		// 更新我方素材信息
		if (!materialIdKeys.isEmpty()) {
			materialService.updateStatusAfterUpload(materialIdKeys);
		}

		LOGGER.info("++++++++++Sohu News upload material end+++++++++++");
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
		List<Advertiser> advertisers = advertiserDao.selectByAdvertiserKeysAndDspId(advertiserKeys, String.valueOf(material.getDspId()));
		if (advertisers == null || advertisers.size() != 1) {
			LOGGER.error("广告主不存在[advertiserKey=" + material.getAdvertiserKey() + "dspId=" + material.getDspId() + "]");
			return null;
		}

		SohuUploadMaterialRequest uploadMaterialRequest = new SohuUploadMaterialRequest();
		// 媒体广告主key
		uploadMaterialRequest.setCustomer_key(advertisers.get(0).getMediaAdvertiserKey());
		uploadMaterialRequest.setMaterial_name(material.getMaterialName());

		// 素材上传地址，不可重复
		uploadMaterialRequest.setFile_source(""); // TODO

		// 曝光监测地址
		List<String> impUrls = new ArrayList<String>();
		if (material.getImpUrls() != null && !material.getImpUrls().isEmpty()) {
			// 素材表里以 |分割
			String[] impTrackUrlArray = material.getImpUrls().split("|");
			if (impTrackUrlArray != null) {
				for (int i = 0; i < impTrackUrlArray.length; i++) {
					impUrls.add(impTrackUrlArray[i]);
					// 媒体最多支持5个
					if (i == 4) {
						break;
					}
				}
			}
		}
		impUrls.add(getStr(impUrl, "?", "%%DISPLAY%%")); // 宏替换 TODO
		uploadMaterialRequest.setImp(impUrls);

		// 点击监测地址
		List<String> clkTrackUrl = new ArrayList<String>();
		if (material.getClkUrls() != null && !material.getClkUrls().isEmpty()) {
			// 素材表里以 |分割
			String[] clkTrackUrlArray = material.getClkUrls().split("|");
			if (null != clkTrackUrlArray) {
				for (int i = 0; i < clkTrackUrlArray.length; i++) {
					clkTrackUrl.add(clkTrackUrlArray[i]);
					// 媒体最多设置 5 个
					if (i == 4) {
						break;
					}
				}
			}
		}
		clkTrackUrl.add(this.getStr(clkUrl, "?", "%%CLICK%%"));// 宏替换 TODO
		uploadMaterialRequest.setClick_monitor(clkTrackUrl);

		// 落地页地址
		uploadMaterialRequest.setGotourl(material.getLpgUrl());

		// 素材所属的广告投放类型
		uploadMaterialRequest.setAdvertising_type("101000"); // 品牌，写死

		// 指定素材提交到哪个媒体审核。1：搜狐门户；2：搜狐视频。
		uploadMaterialRequest.setSubmit_to("1"); // 搜狐新闻

		// 指定素材将用于何种投放方式。1：RTB；2：PDB；3：PMP；4：Preferred Deal
		uploadMaterialRequest.setDelivery_type(getMediaDeliveryType(material.getDeliveryType()));

		// 执行单 ID，与竞价请求中的 impression.campaignId 对应，指定素材将 用于哪一个订单投放
		// 当投放方式为 PDB 或 Preferred Deal 时必填 TODO
		uploadMaterialRequest.setCampaign_id(String.valueOf(material.getDealId()));

		// 素材有效期
		uploadMaterialRequest.setExpire(getExpire(material.getStartDate(), material.getEndDate()));

		// 素材类型，应该与 file_source 的指向相符，可能取值包括
		// 1：图片；2：Flash；3：视频；4：iframe；5：script；6：文字；7：特型广告
		uploadMaterialRequest.setMaterial_type(7);

		// 特型广告模板，取值参见特型广告素材规范表。若填写该参数，表明当前上传的素 main_attr 和 slave 参数；若不填写该参数，
		// 则表明当前上传的素材将用于普通广告位，此时也不用提供 main_attr 和 slave 参数。
		uploadMaterialRequest.setTemplate(""); // TODO
		uploadMaterialRequest.setMain_attr(""); // TODO
		uploadMaterialRequest.setSlave(null); // TODO

		// 广告来源标识
		uploadMaterialRequest.setAd_source(null); // TODO

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
	 * @param startDate (非必须)
	 * @param endDate (必须)
	 * @return
	 */
	private int getExpire(Date startDate, Date endDate) {
		if(startDate != null && endDate != null){
			long days = DateUtils.getDateSubtract(startDate, endDate);
			// 如果时间超过6个月，传递六个月
			if (days > 180) {
				 return 6*30*24*60*60;
			} else {
				return (int) DateUtils.getDateSecondSubtract(startDate, endDate);
			}
		}
		return 30*24*60*60 ;
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
	
	private String getStr(String oldStr, String findStr, String replaceStr) {
		StringBuffer buffer = new StringBuffer(oldStr);
		int num = buffer.indexOf(findStr);
		if (num != -1) {
			buffer.replace(num + 1, oldStr.length(), replaceStr);
		}
		return buffer.toString();
	}
}
