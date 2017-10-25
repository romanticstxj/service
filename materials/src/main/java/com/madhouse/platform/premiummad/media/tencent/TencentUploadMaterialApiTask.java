package com.madhouse.platform.premiummad.media.tencent;

import java.util.ArrayList;
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
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.madhouse.platform.premiummad.constant.MaterialStatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dao.AdvertiserMapper;
import com.madhouse.platform.premiummad.dao.MaterialMapper;
import com.madhouse.platform.premiummad.entity.Advertiser;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.media.tencent.constant.TencentErrorCode;
import com.madhouse.platform.premiummad.media.tencent.request.TencentCommonRequest;
import com.madhouse.platform.premiummad.media.tencent.request.TencentUploadMaterialData;
import com.madhouse.platform.premiummad.media.tencent.response.TencentUploadMaterialResponse;
import com.madhouse.platform.premiummad.media.tencent.response.TencentUploadMaterialReturnMessage;
import com.madhouse.platform.premiummad.media.tencent.util.TencentHttpUtil;
import com.madhouse.platform.premiummad.model.MaterialAuditResultModel;
import com.madhouse.platform.premiummad.service.IMaterialService;
import com.madhouse.platform.premiummad.service.IMediaService;
import com.madhouse.platform.premiummad.service.IPolicyService;
import com.madhouse.platform.premiummad.util.MacroReplaceUtil;
import com.madhouse.platform.premiummad.util.StringUtils;

/**
 * 上传广告信息
 */
@Component
public class TencentUploadMaterialApiTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(TencentUploadMaterialApiTask.class);
	private static final int ITERATOR_TIMES = 2;
	private static final int TECENT_OTV_ITERATOR = 1;

	@Value("${tencent.adcreativeUpload}")
	private String adcreativeUploadUrl;

	@Value("${tencent.clk.url}")
	private String tencentClkUrl;

	@Value("${tencent.clk.urlssp}")
	private String tencentClkUrlSsp;

	// 腾讯支持的广告样式
	@Value("${tencent_displayId_md_app_stream_img}")
	private String tencent_displayId_md_app_stream_img;
	@Value("${tencent_displayId_md_app_stream_vedio}")
	private String tencent_displayId_md_app_stream_vedio;
	@Value("${tencent_displayId_md_app_stream_native}")
	private String tencent_displayId_md_app_stream_native;
	@Value("${tencent_displayId_md_app_stream_gif}")
	private String tencent_displayId_md_app_stream_gif;
	@Value("${tencent_displayId_md_app_stream_icon}")
	private String tencent_displayId_md_app_stream_icon;
	@Value("${tencent_displayId_md_qqlive_appweb_vedio}")
	private String tencent_displayId_md_qqlive_appweb_vedio;
	@Value("${tencent_displayId_md_qqlive_appweb_img}")
	private String tencent_displayId_md_qqlive_appweb_img;

	// SSP 广告位
	@Value("${tencent_md_app_stream_android_img}")
	private String tencent_md_app_stream_android_img;// MadMax-新闻客户端-原生信息流_android
	@Value("${tencent_md_app_stream_ios_img}")
	private String tencent_md_app_stream_ios_img;// MadMax-新闻客户端-原生信息流_ios
	@Value("${tencent_md_app_stream_android_vedio}")
	private String tencent_md_app_stream_android_vedio;// MadMax-新闻客户端-信息流GIF_android
	@Value("${tencent_md_app_stream_ios_vedio}")
	private String tencent_md_app_stream_ios_vedio;// MadMax-新闻客户端-信息流GIF_ios
	@Value("${tencent_md_app_stream_android_native}")
	private String tencent_md_app_stream_android_native;// MadMax-新闻客户端-信息流三小图_android
	@Value("${tencent_md_app_stream_ios_native}")
	private String tencent_md_app_stream_ios_native;// MadMax-新闻客户端-信息流三小图_ios
	@Value("${tencent_md_app_stream_android_gif}")
	private String tencent_md_app_stream_android_gif;// MadMax-腾讯视频-信息流视频_android
	@Value("${tencent_md_app_stream_ios_gif}")
	private String tencent_md_app_stream_ios_gif;// MadMax-腾讯视频-信息流视频_ios
	@Value("${tencent_md_app_stream_android_icon}")
	private String tencent_md_app_stream_android_icon;// MadMax-腾讯视频-信息流大图_android
	@Value("${tencent_md_app_stream_ios_icon}")
	private String tencent_md_app_stream_ios_icon;// MadMax-腾讯视频-信息流大图_ios
	@Value("${tencent_md_qqlive_appweb_android_vedio}")
	private String tencent_md_qqlive_appweb_android_vedio;// Tencent-新闻客户端-信息流广告_android
	@Value("${tencent_md_qqlive_appweb_ios_vedio}")
	private String tencent_md_qqlive_appweb_ios_vedio;// Tencent-新闻客户端-信息流广告_ios
	@Value("${tencent_md_qqlive_appweb_android_img}")
	private String tencent_md_qqlive_appweb_android_img;// Tencent-腾讯视频APP-Phone版_信息流广告_android
	@Value("${tencent_md_qqlive_appweb_ios_img}")
	private String tencent_md_qqlive_appweb_ios_img;// Tencent-腾讯视频APP-Phone版_信息流广告_ios

	@Value("${imp.url}")
	private String impUrl;

	@Value("${clk.url}")
	private String clkUrl;

	@Value("${material_meidaGroupMapping_tencentNotOtv}")
	private String mediaNotOtvGroupStr;
	
	@Value("${material_meidaGroupMapping_tencentOtv}")
	private String mediaOtvGroupStr;
	
	@Autowired
	private TencentHttpUtil tencentHttpUtil;

	@Autowired
	private MaterialMapper materialDao;

	@Autowired
	private IMaterialService materialService;

	@Autowired
	private IPolicyService policyService;

	@Autowired
	private AdvertiserMapper advertiserDao;

	@Autowired
	private IMediaService mediaService;
	
	/**
	 * 宏替换替换映射
	 */
	private static Map<String, String> macroClickMap;
	private static Map<String, String> macroImageMap;

	static {
		macroClickMap = new HashMap<String, String>();
		macroClickMap.put("__EXT1__", "${CLICK_EXT1}");
		macroClickMap.put("__EXT2__", "${CLICK_EXT2}");
		macroClickMap.put("__EXT3__", "${CLICK_EXT3}");

		macroImageMap = new HashMap<String, String>();
		macroImageMap.put("__EXT1__", "${DISPLAY_EXT1}");
		macroImageMap.put("__EXT2__", "${DISPLAY_EXT2}");
		macroImageMap.put("__EXT3__", "${DISPLAY_EXT3}");
	}

	public void uploadMaterial() {
		// TENCENT 对应两个媒体 OTV 和 非 OT
		for (int mediaType = 0; mediaType < ITERATOR_TIMES; mediaType++) {
			/*代码配置处理方式
			int mediaIdGroup = 0;
			if (mediaType != TECENT_OTV_ITERATOR) {
				mediaIdGroup = MediaTypeMapping.TENCENT_NOT_OTV.getGroupId();
			} else {
				mediaIdGroup = MediaTypeMapping.TENCENT.getGroupId();
			}
			
			// 媒体组没有映射到具体的媒体不处理
			String value = MediaTypeMapping.getValue(mediaIdGroup);
			if (StringUtils.isBlank(value)) {
				return;
			}
			
			// 获取媒体组下的具体媒体
			int[] mediaIds = StringUtils.splitToIntArray(value);
			*/

			String mediaGroupStr = "";
			if (mediaType != TECENT_OTV_ITERATOR) {
				mediaGroupStr = mediaNotOtvGroupStr;
			} else {
				mediaGroupStr = mediaOtvGroupStr;
			}
			// 根据媒体组ID和审核对象获取具体的媒体ID
			int[] mediaIds = mediaService.getMeidaIds(mediaGroupStr, SystemConstant.MediaAuditObject.MATERIAL);

			// 查询所有待审核且媒体的素材的审核状态是媒体审核的
			List<Material> unSubmitMaterials = materialDao.selectMaterialsByMeidaIds(mediaIds, MaterialStatusCode.MSC10002.getValue());
			if (unSubmitMaterials == null || unSubmitMaterials.isEmpty()) {
				/* LOGGER.info(MediaMapping.getDescrip(mediaIds) + "没有未上传的素材"); */
				LOGGER.info("Tencent" + mediaIds + "没有未上传的素材");
				continue;
			}

			// 构造请求对象
			TencentCommonRequest<List<TencentUploadMaterialData>> request = buildUploadRequest(mediaType, unSubmitMaterials);
			String responseJson = tencentHttpUtil.post(adcreativeUploadUrl, request);
			LOGGER.info("Tencent上传广告信息返回信息：{}", responseJson);
			// 上传成功时,responseJson 为空字符串或者null
			TencentUploadMaterialResponse advertUploadRsponse = null;
			try {
				advertUploadRsponse = JSONObject.parseObject(responseJson, TencentUploadMaterialResponse.class);
				if (!request.getData().isEmpty() && advertUploadRsponse != null) {
					// 系统错误或权限校验错误
					if (TencentErrorCode.TEC100.getValue() == advertUploadRsponse.getError_code() || TencentErrorCode.TEC101.getValue() == advertUploadRsponse.getError_code()) {
						LOGGER.info("Tencent上传广告出现错误:" + (TencentErrorCode.TEC100.getDescrip()));
						return;
					}

					// 获取上传成功和失败的记录
					Set<String> successfulMaterialIds = new HashSet<String>();
					List<MaterialAuditResultModel> rejusedMaterials = new ArrayList<MaterialAuditResultModel>();
					if (advertUploadRsponse.getRet_msg() != null && !advertUploadRsponse.getRet_msg().isEmpty()) {
						for (TencentUploadMaterialReturnMessage item : advertUploadRsponse.getRet_msg()) {
							// 上传成功
							if (StringUtils.isBlank(item.getErr_code()) && StringUtils.isBlank(item.getErr_msg())) {
								successfulMaterialIds.add(item.getDsp_order_id());
							} else {
								// 自动驳回
								MaterialAuditResultModel rejuseItem = new MaterialAuditResultModel();
								rejuseItem.setId(item.getDsp_order_id());
								rejuseItem.setStatus(MaterialStatusCode.MSC10001.getValue());
								rejuseItem.setMediaIds(mediaIds);
								rejuseItem.setErrorMessage(TencentErrorCode.getDescrip(Integer.valueOf(item.getErr_code())) + "[" + item.getErr_msg() + "]");
								rejusedMaterials.add(rejuseItem);
							}
						}
					}

					// 处理成功的结果
					if (!successfulMaterialIds.isEmpty()) {
						handleSuccessResult(unSubmitMaterials, successfulMaterialIds);
					}

					// 处理失败的结果，自动驳回 - 通过素材id更新
					if (!rejusedMaterials.isEmpty()) {
						materialService.updateStatusToMediaByMaterialId(rejusedMaterials);
					}
				} else {
					LOGGER.info("Tencent上传广告返回出错 : AdvertUploadRsponse is null");
				}
			} catch (Exception e) {
				String message = "syntax error, expect {, actual EOF, pos 0";
				if (e instanceof JSONException || e.getMessage().equals(message)) {
					LOGGER.info("Tencent上传广告返回解析错误进入到JsonException中 :直接更新物料 task表为上传成功");
					Set<String> successfulMaterialIds = new HashSet<String>();
					for (Material material : unSubmitMaterials) {
						successfulMaterialIds.add(String.valueOf(material.getId()));
					}
					handleSuccessResult(unSubmitMaterials, successfulMaterialIds);
				} else {
					LOGGER.info("Tencent上传广告返回解析出错 : " + e.getMessage());
				}
			}
			LOGGER.info("Tencent" + mediaIds + " AdvertUploadApiTask-advertUpload end");
		}
	}

	/**
	 * 处理成功信息,更新task状态
	 *
	 * @param materialTasks
	 *            task数据集合
	 */
	private void handleSuccessResult(List<Material> unSubmitMaterials, Set<String> successfulMaterialIds) {
		Map<Integer, String[]> materialIdKeys = new HashMap<Integer, String[]>();

		// 媒体方唯一标识是我方媒体id
		for (Material material : unSubmitMaterials) {
			if (successfulMaterialIds.contains(String.valueOf(material.getId()))) {
				String[] mediaQueryAndMaterialKeys = { String.valueOf(material.getId()) };
				materialIdKeys.put(material.getId(), mediaQueryAndMaterialKeys);
			}
		}

		// 更新我方素材信息
		if (!materialIdKeys.isEmpty()) {
			materialService.updateStatusAfterUpload(materialIdKeys);
		}
	}

	/**
	 * 构造上传物料请求对象
	 * 
	 * @param mediaType
	 * @param unSubmitMaterials
	 * @return
	 */
	private TencentCommonRequest<List<TencentUploadMaterialData>> buildUploadRequest(int mediaType, List<Material> unSubmitMaterials) {
		TencentCommonRequest<List<TencentUploadMaterialData>> request = new TencentCommonRequest<List<TencentUploadMaterialData>>();
		List<TencentUploadMaterialData> advertUploadRequestList = new ArrayList<>();

		// 批量上传物料
		for (Material material : unSubmitMaterials) {
			// 获取该素材的广告主,若广告主不存在不做处理
			String[] advertiserKeys = { material.getAdvertiserKey() };
			List<Advertiser> advertisers = advertiserDao.selectByAdvertiserKeysAndDspId(advertiserKeys, String.valueOf(material.getDspId()), material.getMediaId());
			if (advertisers == null || advertisers.size() != 1) {
				LOGGER.error("广告主不存在[advertiserKey=" + material.getAdvertiserKey() + "dspId=" + material.getDspId() + "]");
				return null;
			}

			TencentUploadMaterialData advertUploadRequest = new TencentUploadMaterialData();

			// 3095 腾讯新增 广告形式和静态落地页(供审核查看)必须 映射
			// 广告主名称
			String advertiserName = advertisers.get(0).getAdvertiserName().replace("\\u005f", "_"); // 替换下划线
			advertUploadRequest.setAdvertiser_name(advertiserName);
			advertUploadRequest.setAdvertiser_id(advertisers.get(0).getMediaAdvertiserKey());
			advertUploadRequest.setDisplay_id(getDisplayId(material.getAdspaceId()));
			advertUploadRequest.setLanding_page(material.getLpgUrl()); // 静态落地页，腾讯必填
																		// 用落地页

			// 客户名称长度不能大于7
			if (advertiserName.length() > 7) {
				advertiserName = advertiserName.substring(0, 7);
			}
			// 创意素材内容
			List<Map<String, String>> adContents = getAdContent(material, advertiserName, advertUploadRequest.getDisplay_id());
			advertUploadRequest.setAd_content(adContents);

			// 展示监播
			List<String> monitorList = new ArrayList<>();
			if (!StringUtils.isEmpty(material.getImpUrls())) {
				String impTrackingUrl = material.getImpUrls();// 支持多个
				String[] impTrackingUrlArray = impTrackingUrl.split("\\|");
				if (null != impTrackingUrlArray) {
					for (int i = 0; i < impTrackingUrlArray.length; i++) {
						String[] track = impTrackingUrlArray[i].split("`");
						monitorList.add(MacroReplaceUtil.macroReplaceImageUrl(macroImageMap, track[1])); // 宏替换
					}
				}
			}
			monitorList.add(MacroReplaceUtil.getStr(impUrl, "?", "${EXT}")); // SSP
																				// 宏替换
			advertUploadRequest.setMonitor_url(monitorList);

			// 点击监播
			List<String> clkMonitorList = new ArrayList<>();
			if (!StringUtils.isEmpty(material.getClkUrls())) {
				String clk = material.getClkUrls();
				String[] clkTrackingUrlArray = clk.split("\\|");
				if (null != clkTrackingUrlArray) {
					for (int i = 0; i < clkTrackingUrlArray.length; i++) {
						clkMonitorList.add(MacroReplaceUtil.macroReplaceClickUrl(macroClickMap, clkTrackingUrlArray[i]));
					}
				}
			}
			clkMonitorList.add(MacroReplaceUtil.getStr(clkUrl, "?", "${EXT}"));// SSP
																				// 宏替换
			advertUploadRequest.setClick_monitor_url(clkMonitorList);

//			String encodeTargetUrl = "";
//			try {
//				encodeTargetUrl = URLEncoder.encode(material.getLpgUrl(), "UTF-8");
//			} catch (UnsupportedEncodingException e) {
//				LOGGER.info("Tencent targetUrl encode exception");
//				e.printStackTrace();
//			}
//			if (mediaType != TECENT_OTV_ITERATOR) {
//				advertUploadRequest.setTargeting_url(tencentClkUrlSsp + encodeTargetUrl + "${CLICK_EXT1}");
//			} else {
//				advertUploadRequest.setTargeting_url(tencentClkUrl + encodeTargetUrl + "&${EXT}");
//			}
			advertUploadRequest.setTargeting_url(material.getLpgUrl());
			advertUploadRequest.setDsp_order_id(String.valueOf(material.getId()));
			advertUploadRequestList.add(advertUploadRequest);
		}

		request.setData(advertUploadRequestList);
		return request;
	}

	/**
	 * 封装创意素材内容
	 * 
	 * @param material
	 * @param mediaDisplayId
	 * @param advertiserName
	 */
	private List<Map<String, String>> getAdContent(Material material, String advertiserName, int mediaDisplayId) {
		List<Map<String, String>> adContents = new ArrayList<>();
		if (Integer.valueOf(tencent_displayId_md_app_stream_img) == mediaDisplayId || Integer.valueOf(tencent_displayId_md_app_stream_gif) == mediaDisplayId) {
			// 广告标题
			Map<String, String> adContentMap = new HashMap<>();
			adContentMap.put("file_text", material.getTitle());
			adContents.add(adContentMap);

			// 图片
			adContentMap = new HashMap<>();
			adContentMap.put("file_url", material.getAdMaterials().split("\\|")[0]);
			adContents.add(adContentMap);

			// 分享配图地址存储在LOGO里
			adContentMap = new HashMap<>();
			adContentMap.put("file_url", material.getIcon());
			adContents.add(adContentMap);

			// 摘要
			adContentMap = new HashMap<>();
			adContentMap.put("file_text", material.getDescription());
			adContents.add(adContentMap);

			// 客户名称
			adContentMap = new HashMap<>();
			adContentMap.put("file_text", advertiserName);
			adContents.add(adContentMap);
		} else if (Integer.valueOf(tencent_displayId_md_app_stream_native) == mediaDisplayId) {
			// 广告标题
			Map<String, String> adContentMap = new HashMap<>();
			adContentMap.put("file_text", material.getTitle());
			adContents.add(adContentMap);

			// 摘要
			adContentMap = new HashMap<>();
			adContentMap.put("file_text", material.getDescription());
			adContents.add(adContentMap);

			// 缩略图
			adContentMap = new HashMap<>();
			adContentMap.put("file_url", material.getIcon());
			adContents.add(adContentMap);

			// 客户名称
			adContentMap = new HashMap<>();
			adContentMap.put("file_text", advertiserName);
			adContents.add(adContentMap);

			// 长标题
			adContentMap = new HashMap<>();
			adContentMap.put("file_text", material.getTitle());
			adContents.add(adContentMap);
		} else if (Integer.valueOf(tencent_displayId_md_app_stream_vedio) == mediaDisplayId) {
			// 广告标题
			Map<String, String> adContentMap = new HashMap<>();
			adContentMap.put("file_text", material.getTitle());
			adContents.add(adContentMap);

			// 视频
			adContentMap = new HashMap<>();
			adContentMap.put("file_url", material.getAdMaterials().split("\\|")[0]);
			adContents.add(adContentMap);

			// 贴底图片
			adContentMap = new HashMap<>();
			adContentMap.put("file_url", material.getCover());
			adContents.add(adContentMap);

			// 分享配图
			adContentMap = new HashMap<>();
			adContentMap.put("file_url", material.getIcon());
			adContents.add(adContentMap);

			// 分享摘要
			adContentMap = new HashMap<>();
			adContentMap.put("file_text", material.getDescription());
			adContents.add(adContentMap);

			// 广告主名称
			adContentMap = new HashMap<>();
			adContentMap.put("file_text", advertiserName);
			adContents.add(adContentMap);
		} else if (Integer.valueOf(tencent_displayId_md_app_stream_icon) == mediaDisplayId) {
			// 广告标题
			Map<String, String> adContentMap = new HashMap<>();
			adContentMap.put("file_text", material.getTitle());
			adContents.add(adContentMap);

			// 三张图片
			String[] adMaterials = material.getAdMaterials().split("\\|");
			for (String adMaterial : adMaterials) {
				adContentMap = new HashMap<>();
				adContentMap.put("file_url", adMaterial);
				adContents.add(adContentMap);
				if (adContentMap.size() == 3) {
					break;
				}
			}
			// 广告主名称
			adContentMap = new HashMap<>();
			adContentMap.put("file_text", advertiserName);
			adContents.add(adContentMap);

			// 分享配图
			adContentMap = new HashMap<>();
			adContentMap.put("file_url", material.getIcon());
			adContents.add(adContentMap);

			// 分享摘要
			adContentMap = new HashMap<>();
			adContentMap.put("file_text", material.getDescription());
			adContents.add(adContentMap);
		} else if (Integer.valueOf(tencent_displayId_md_qqlive_appweb_img) == mediaDisplayId) {
			// 视频素材
			Map<String, String> adContentMap = new HashMap<>();
			adContentMap = new HashMap<>();
			adContentMap.put("file_url", material.getAdMaterials().split("\\|")[0]);
			adContents.add(adContentMap);

			// 标题
			adContentMap = new HashMap<>();
			adContentMap = new HashMap<>();
			adContentMap.put("file_text", material.getTitle());
			adContents.add(adContentMap);
		} else if (Integer.valueOf(tencent_displayId_md_qqlive_appweb_vedio) == mediaDisplayId) {
			// 文字标题
			Map<String, String> adContentMap = new HashMap<>();
			adContentMap.put("file_text", material.getTitle());
			adContents.add(adContentMap);

			// 贴底图片
			adContentMap = new HashMap<>();
			adContentMap.put("file_url", material.getCover());
			adContents.add(adContentMap);

			// 视频
			adContentMap = new HashMap<>();
			adContentMap.put("file_url", material.getAdMaterials().split("\\|")[0]);
			adContents.add(adContentMap);

			// 分享配图
			adContentMap = new HashMap<>();
			adContentMap.put("file_url", material.getIcon());
			adContents.add(adContentMap);

			// 分享摘要
			adContentMap = new HashMap<>();
			adContentMap.put("file_text", material.getDescription());
			adContents.add(adContentMap);

			// 分享标题
			adContentMap = new HashMap<>();
			adContentMap.put("file_text", material.getTitle());
			adContents.add(adContentMap);
		}
		return adContents;
	}

	/**
	 * 根据广告位ID获取媒体的广告形式
	 * 
	 * @param adspaceID
	 * @return
	 */
	private Integer getDisplayId(Integer adspaceId) {
		if (adspaceId == null) {
			return null;
		}
		if (String.valueOf(adspaceId).equals(tencent_md_app_stream_android_img) || String.valueOf(adspaceId).equals(tencent_md_app_stream_ios_img)) {
			return Integer.valueOf(tencent_displayId_md_app_stream_img);
		}
		if (String.valueOf(adspaceId).equals(tencent_md_app_stream_android_vedio) || String.valueOf(adspaceId).equals(tencent_md_app_stream_ios_vedio)) {
			return Integer.valueOf(tencent_displayId_md_app_stream_vedio);
		}
		if (String.valueOf(adspaceId).equals(tencent_md_app_stream_android_native) || String.valueOf(adspaceId).equals(tencent_md_app_stream_ios_native)) {
			return Integer.valueOf(tencent_displayId_md_app_stream_native);
		}
		if (String.valueOf(adspaceId).equals(tencent_md_app_stream_android_gif) || String.valueOf(adspaceId).equals(tencent_md_app_stream_ios_gif)) {
			return Integer.valueOf(tencent_displayId_md_app_stream_gif);
		}
		if (String.valueOf(adspaceId).equals(tencent_md_app_stream_android_icon) || String.valueOf(adspaceId).equals(tencent_md_app_stream_ios_icon)) {
			return Integer.valueOf(tencent_displayId_md_app_stream_icon);
		}
		if (String.valueOf(adspaceId).equals(tencent_md_qqlive_appweb_android_vedio) || String.valueOf(adspaceId).equals(tencent_md_qqlive_appweb_ios_vedio)) {
			return Integer.valueOf(tencent_displayId_md_qqlive_appweb_vedio);
		}
		if (String.valueOf(adspaceId).equals(tencent_md_qqlive_appweb_android_img) || String.valueOf(adspaceId).equals(tencent_md_qqlive_appweb_ios_img)) {
			return Integer.valueOf(tencent_displayId_md_qqlive_appweb_img);
		}
		return Integer.valueOf(0);
	}
}
