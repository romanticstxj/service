package com.madhouse.platform.premiummad.media.sohu.news;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.constant.Layout;
import com.madhouse.platform.premiummad.constant.MaterialStatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dao.AdvertiserMapper;
import com.madhouse.platform.premiummad.dao.MaterialMapper;
import com.madhouse.platform.premiummad.entity.Advertiser;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.media.sohu.request.SohuSlave;
import com.madhouse.platform.premiummad.media.sohu.request.SohuUploadMaterialRequest;
import com.madhouse.platform.premiummad.media.sohu.response.SohuResponse;
import com.madhouse.platform.premiummad.media.sohu.util.SohuNewsAuth;
import com.madhouse.platform.premiummad.model.MaterialAuditResultModel;
import com.madhouse.platform.premiummad.service.IMaterialService;
import com.madhouse.platform.premiummad.service.IMediaService;
import com.madhouse.platform.premiummad.service.IPolicyService;
import com.madhouse.platform.premiummad.util.DateUtils;
import com.madhouse.platform.premiummad.util.HttpUtils;
import com.madhouse.platform.premiummad.util.MacroReplaceUtil;
import com.madhouse.platform.premiummad.util.StringUtils;

@Component
public class SohuNewsUploadMaterialApiTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(SohuNewsUploadMaterialApiTask.class);

	@Value("${sohu.material.create}")
    private String materialCreateUrl;
	
	@Value("${sohu.material.delete}")
    private String materialDeleteUrl;
	
	/**
	 * 为了发送给搜狐检查请求参数
	 */
	@Value("${sohu.test.apiauth}")
	private String apiAuthUrl;

	@Value("${imp.url}")
	private String impUrl;

	@Value("${clk.url}")
	private String clkUrl;
	
	@Value("${material_meidaGroupMapping_sohuNews}")
	private String mediaGroupStr;
	
	@Autowired
	private SohuNewsAuth sohuAuth;
	
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
	private static Map<String, String> macroMap;
	
	/**
	 * 支持的广告形式
	 */
	private static Set<Integer> supportedLayoutSet;
	
	static {
		macroMap = new HashMap<String, String>();
		macroMap.put("__EXT1__", "%%EXT1%%");
		macroMap.put("__EXT2__", "%%EXT2%%");
		macroMap.put("__EXT3__", "%%EXT3%%");
		
		supportedLayoutSet = new HashSet<Integer>();
		supportedLayoutSet.add(Integer.valueOf(Layout.LO10005.getValue()));//开屏图片
		supportedLayoutSet.add(Integer.valueOf(Layout.LO30001.getValue()));//图文信息流
		supportedLayoutSet.add(Integer.valueOf(Layout.LO30011.getValue()));//视频信息流
	}
	
	/**
	 * 上传广告物料
	 */
	public void uploadSohuMaterial() {
		LOGGER.info("++++++++++Sohu News upload material begin+++++++++++");
		
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
			LOGGER.info("Sohu News没有未上传的素材");
			return;
		}

		// 上传到媒体
		LOGGER.info("SohuNewsUploadMaterialApiTask-sohuNews", unSubmitMaterials.size());

		Map<Integer, String[]> materialIdKeys = new HashMap<Integer, String[]>();
		List<MaterialAuditResultModel> rejusedMaterials = new ArrayList<MaterialAuditResultModel>();
		for (Material material : unSubmitMaterials) {
			// 如果素材上传过，重新上传需要先删除该素材
//			if (!StringUtils.isBlank(material.getMediaQueryKey())) {
//				boolean success = deleteMaterial(material);
//				if (!success) {
//					LOGGER.info("物料删除失败[materialId=" + material.getId() + "]");
//					continue;
//				}
//			}
			
			// 上传到媒体
			StringBuilder errMsg = new StringBuilder();
			Map<String, Object> paramMap = buildMaterialRequest(material, errMsg);
			if (!StringUtils.isBlank(errMsg)) {
				MaterialAuditResultModel rejuseItem = new MaterialAuditResultModel();
				rejuseItem.setId(String.valueOf(material.getId()));
				rejuseItem.setStatus(MaterialStatusCode.MSC10001.getValue());
				rejuseItem.setMediaIds(mediaIds);
				rejuseItem.setErrorMessage(errMsg.toString());
				rejusedMaterials.add(rejuseItem);
				LOGGER.error(rejuseItem.getErrorMessage());
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

		LOGGER.info("++++++++++Sohu News upload material end+++++++++++");
	}
	
	/**
	 * 删除已提交的素材 - 针对已驳回的情况
	 * 
	 * @param material
	 */
	public boolean deleteMaterial(Material material) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("file_source", material.getMediaQueryKey());
		String request = sohuAuth.setHttpMethod("POST").setApiUrl(materialDeleteUrl).setParamMap(paramMap).buildRequest();
		LOGGER.info("SoHuUploadMaterial-deleteMaterial-request" + request);
		String result = HttpUtils.post(materialDeleteUrl, request);
		LOGGER.info("SoHuUploadMaterial-deleteMaterial-response" + result);
		if (!StringUtils.isEmpty(result)) {
			SohuResponse sohutvResponse = JSON.parseObject(result, SohuResponse.class);
			if (sohutvResponse != null) {
				boolean status = sohutvResponse.isStatus();
				if (status) {// 删除物料成功
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 处理上传物料api的请求json
	 * 
	 * 
	 * @param material
	 * @param errMsg
	 * @return
	 */
	private Map<String, Object> buildMaterialRequest(Material material, StringBuilder errMsg) {
		// 获取该素材的广告主,若广告主不存在不做处理
		String[] advertiserKeys = { material.getAdvertiserKey() };
		List<Advertiser> advertisers = advertiserDao.selectByAdvertiserKeysAndDspId(advertiserKeys, String.valueOf(material.getDspId()), material.getMediaId());
		if (advertisers == null || advertisers.size() != 1) {
			errMsg.append("广告主不存在[advertiserKey=" + material.getAdvertiserKey() + "dspId=" + material.getDspId() + "]");
			LOGGER.error(errMsg.toString());
			return null;
		}
		
		// 校验广告形式是否支持
		if (!(supportedLayoutSet.contains(Integer.valueOf(material.getLayout())))) {
			errMsg.append("媒体只支持如下广告形式：" + Arrays.toString(supportedLayoutSet.toArray()));
			LOGGER.error(errMsg.toString());
			return null;
		}

		SohuUploadMaterialRequest uploadMaterialRequest = new SohuUploadMaterialRequest();
		// 媒体广告主key
		uploadMaterialRequest.setCustomer_key(advertisers.get(0).getMediaAdvertiserKey());
		uploadMaterialRequest.setMaterial_name(material.getMaterialName());

		// 素材上传地址，不可重复
		uploadMaterialRequest.setFile_source(material.getAdMaterials().split("\\|")[0]);// 物料上传地址,如果多条取一个

		// 曝光监测地址
		List<String> impUrls = new ArrayList<String>();
		if (material.getImpUrls() != null && !material.getImpUrls().isEmpty()) {
			// 素材表里以 |分割  -> startDelay1`url1|startDelay2`url2
			String[] impTrackUrlArray = material.getImpUrls().split("\\|");
			if (impTrackUrlArray != null) {
				for (int i = 0; i < impTrackUrlArray.length; i++) {
					String[] track = impTrackUrlArray[i].split("`");
					impUrls.add(MacroReplaceUtil.macroReplaceImageUrl(macroMap, track[1])); // 宏替换
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
					clkTrackUrl.add(MacroReplaceUtil.macroReplaceImageUrl(macroMap, clkTrackUrlArray[i]));// 宏替换
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
		uploadMaterialRequest.setSubmit_to("1"); // 搜狐新闻

		// 指定素材将用于何种投放方式。1：RTB；2：PDB；3：PMP；4：Preferred Deal
		uploadMaterialRequest.setDelivery_type(getMediaDeliveryType(material.getDeliveryType()));

		// 执行单 ID，与竞价请求中的 impression.campaignId 对应，指定素材将 用于哪一个订单投放
		// 当投放方式为 PDB 或 Preferred Deal 时必填
		uploadMaterialRequest.setCampaign_id(policyService.getMediaDealId(material.getDealId(), material.getMediaId()));

		// 素材有效期
		uploadMaterialRequest.setExpire(getExpire(material.getEndDate()));

		// 素材类型，应该与 file_source 的指向相符，可能取值包括
		// 1：图片；2：Flash；3：视频；4：iframe；5：script；6：文字；7：特型广告
		uploadMaterialRequest.setMaterial_type(7);

		// 特型广告模板，取值参见特型广告素材规范表。若填写该参数，表明当前上传的素 main_attr 和 slave 参数；若不填写该参数，
		// 则表明当前上传的素材将用于普通广告位，此时也不用提供 main_attr 和 slave 参数。
		//(搜狐新闻中)开屏广告位:一个物料如果绑定了一个开屏，一个非开屏，那么就传一次开屏的广告位的请求
		if (Layout.LO10005.getValue() == material.getLayout().intValue()) {
			uploadMaterialRequest.setTemplate("apploading");
			uploadMaterialRequest.setMain_attr("loading_pic");

			List<SohuSlave> slave = new ArrayList<SohuSlave>();
			if (!StringUtils.isEmpty(material.getTitle())) {
				SohuSlave title = new SohuSlave();
				title.setSource(material.getTitle());
				title.setAttr("ad_txt");
				slave.add(title);
			} else {
				SohuSlave title = new SohuSlave();
				title.setSource(" ");
				title.setAttr("ad_txt");
				slave.add(title);
			}
			if (!StringUtils.isEmpty(material.getDescription())) {
				SohuSlave text = new SohuSlave();
				text.setSource(material.getDescription());
				text.setAttr("share_txt");
				slave.add(text);
			} else {
				SohuSlave text = new SohuSlave();
				text.setSource(" ");
				text.setAttr("share_txt");
				slave.add(text);
			}
			uploadMaterialRequest.setSlave(slave);

			LOGGER.info("material-souhuNews:materialId:" + material.getId() + "开屏");
		} else {
			// 非开屏：图文信息流(物料格式是图片)和视频信息流(物料格式是.mp4且有封面)
			List<SohuSlave> slave = new ArrayList<SohuSlave>();
			// 视频信息流
			if (Layout.LO30011.getValue() == material.getLayout().intValue()) {
				// 1:file_source变为封面图片地址
				// 物料地址：slave:
				// [{""source"":""示例广告标题"",""attr"":""title""},{""source"":""http://www.example.com/ad/video.mp4"",""attr"":""video""}]"
				// 图文信息流
				if (!StringUtils.isEmpty(material.getDescription())) {
					SohuSlave text = new SohuSlave();
					text.setSource(material.getAdMaterials()
							);// 物料.mp4
					text.setAttr("video");
					slave.add(text);
				} else {
					SohuSlave text = new SohuSlave();
					text.setSource(" ");
					text.setAttr("video");
					slave.add(text);
				}
				String coverPath = StringUtils.isEmpty(material.getCover()) ? "" : material.getCover();
				uploadMaterialRequest.setFile_source(coverPath);// 重新赋值：图片封面地址
				uploadMaterialRequest.setTemplate("info_video");
				LOGGER.info("material-souhuNews:materialId:" + material.getId() + "非开屏:视频信息流");
			} else if (Layout.LO30001.getValue() == material.getLayout().intValue()) {
				// 图文信息流
				if (!StringUtils.isEmpty(material.getDescription())) {
					SohuSlave text = new SohuSlave();
					text.setSource(material.getDescription());
					text.setAttr("summary");
					slave.add(text);
				} else {
					SohuSlave text = new SohuSlave();
					text.setSource(" ");
					text.setAttr("summary");
					slave.add(text);
				}
				uploadMaterialRequest.setTemplate("info_pictxt");
				LOGGER.info("material-souhuNews:materialId:" + material.getId() + "非开屏:图文信息流");
			}
			
			// 图片信息流和视频信息流 公共参数
			if (!StringUtils.isEmpty(material.getTitle())) {
				SohuSlave title = new SohuSlave();
				title.setSource(material.getTitle());
				title.setAttr("title");
				slave.add(title);
			} else {
				SohuSlave title = new SohuSlave();
				title.setSource(" ");
				title.setAttr("title");
				slave.add(title);
			}
			uploadMaterialRequest.setSlave(slave);
			uploadMaterialRequest.setMain_attr("picture");
		}

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
