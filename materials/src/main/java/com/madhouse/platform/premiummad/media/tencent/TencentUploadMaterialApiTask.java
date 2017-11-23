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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.madhouse.platform.premiummad.constant.MaterialStatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dao.AdvertiserMapper;
import com.madhouse.platform.premiummad.dao.MaterialMapper;
import com.madhouse.platform.premiummad.entity.Advertiser;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.media.tencent.constant.TencentConstant;
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
	@Value("${tencent_displayId_app_stream_img}")
	private String tencent_displayId_app_stream_img;
	@Value("${tencent_displayId_app_stream_video}")
	private String tencent_displayId_app_stream_video;
	@Value("${tencent_displayId_app_stream_native}")
	private String tencent_displayId_app_stream_native;
	@Value("${tencent_displayId_app_stream_gif}")
	private String tencent_displayId_app_stream_gif;
	@Value("${tencent_displayId_app_stream_icon}")
	private String tencent_displayId_app_stream_icon;
	@Value("${tencent_displayId_qqlive_appweb_video}")
	private String tencent_displayId_qqlive_appweb_video;
	@Value("${tencent_displayId_qqlive_appweb_img}")
	private String tencent_displayId_qqlive_appweb_img;
	@Value("${tencent_displayId_qqlive_appweb_phone_splash_img}")
	private String tencent_displayId_qqlive_appweb_phone_splash_img;
	@Value("${tencent_displayId_qqlive_appweb_phone_splash_video}")
	private String tencent_displayId_qqlive_appweb_phone_splash_video;
	@Value("${tencent_displayId_app_kuaibao_splash_img}")
	private String tencent_displayId_app_kuaibao_splash_img;
	@Value("${tencent_displayId_app_kuaibao_splash_video}")
	private String tencent_displayId_app_kuaibao_splash_video;

	// SSP 广告位
	@Value("${tencent_md_app_stream_img}")
	private String tencent_md_app_stream_img;// MadMax-新闻客户端-原生信息流
	@Value("${tencent_md_app_stream_video}")
	private String tencent_md_app_stream_video;// MadMax-新闻客户端-信息流GIF
	@Value("${tencent_md_app_stream_native}")
	private String tencent_md_app_stream_native;// MadMax-新闻客户端-信息流三小图
	@Value("${tencent_md_app_stream_gif}")
	private String tencent_md_app_stream_gif;// MadMax-腾讯视频-信息流视频
	@Value("${tencent_md_app_stream_icon}")
	private String tencent_md_app_stream_icon;// MadMax-腾讯视频-信息流大图
	@Value("${tencent_md_qqlive_appweb_video}")
	private String tencent_md_qqlive_appweb_video;// MadMax-新闻客户端-信息流广告
	@Value("${tencent_md_qqlive_appweb_img}")
	private String tencent_md_qqlive_appweb_img;// MadMax-腾讯视频APP-Phone版_信息流广告
	@Value("${tencent_md_qqlive_appweb_phone_splash_img}")
	private String tencent_md_qqlive_appweb_phone_splash_img;// MadMax-腾讯视频APP-Phone版-闪屏-静态展示
	@Value("${tencent_md_qqlive_appweb_phone_splash_video}")
	private String tencent_md_qqlive_appweb_phone_splash_video;// MadMax-腾讯视频APP-Phone版-闪屏-动态视频
	@Value("${tencent_md_app_kuaibao_splash_img}")
	private String tencent_md_app_kuaibao_splash_img;// MadMax-快播APP-闪屏-纯静态展示全屏点击
	@Value("${tencent_md_app_kuaibao_splash_video}")
	private String tencent_md_app_kuaibao_splash_video;// MadMax-快报APP-视频闪屏-动态展示全屏点击

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
	private static Map<String, String> macroMap;

	static {
		macroMap = new HashMap<String, String>();
		macroMap.put("__EXT1__", "${EXT}");
		macroMap.put("__EXT2__", "${EXT2}");
		macroMap.put("__EXT3__", "${EXT3}");
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
			
			// 媒体组没有映射到具体的媒体不处理
			if (mediaIds == null || mediaIds.length < 1) {
				return;
			}
			
			// 查询所有待审核且媒体的素材的审核状态是媒体审核的
			List<Material> unSubmitMaterials = materialDao.selectMaterialsByMeidaIds(mediaIds, MaterialStatusCode.MSC10002.getValue());
			if (unSubmitMaterials == null || unSubmitMaterials.isEmpty()) {
				/* LOGGER.info(MediaMapping.getDescrip(mediaIds) + "没有未上传的素材"); */
				LOGGER.info("Tencent" + mediaIds + "没有未上传的素材");
				continue;
			}

			// 构造请求对象
			TencentCommonRequest<List<TencentUploadMaterialData>> request = buildUploadRequest(mediaType, unSubmitMaterials);
			LOGGER.info("request:{}", JSON.toJSONString(request));
			String responseJson = tencentHttpUtil.post(adcreativeUploadUrl, request);
			LOGGER.info("response:{}", responseJson);
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
		// 封装广告位集合
		Map<Byte, Set<String>> map = new HashMap<>();
		map.put(TencentConstant.TENCENT_APP_STREAM_IMG, covertToSet(tencent_md_app_stream_img));
		map.put(TencentConstant.TENCENT_APP_STREAM_VIDEO, covertToSet(tencent_md_app_stream_video));
		map.put(TencentConstant.TENCENT_APP_STREAM_NATIVE, covertToSet(tencent_md_app_stream_native));
		map.put(TencentConstant.TENCENT_APP_STREAM_GIF, covertToSet(tencent_md_app_stream_gif));
		map.put(TencentConstant.TENCENT_APP_STREAM_ICON, covertToSet(tencent_md_app_stream_icon));
		map.put(TencentConstant.TENCENT_QQLIVE_APPWEB_VIDEO, covertToSet(tencent_md_qqlive_appweb_video));
		map.put(TencentConstant.TENCENT_QQLIVE_APPWEB_IMG, covertToSet(tencent_md_qqlive_appweb_img));
		map.put(TencentConstant.TENCENT_QQLIVE_APPWEB_PHONE_SPLASH_IMG, covertToSet(tencent_md_qqlive_appweb_phone_splash_img));
		map.put(TencentConstant.TENCENT_QQLIVE_APPWEB_PHONE_SPLASH_VIDEO, covertToSet(tencent_md_qqlive_appweb_phone_splash_video));
		map.put(TencentConstant.TENCENT_APP_KUAIBAO_SPLASH_IMG, covertToSet(tencent_md_app_kuaibao_splash_img));
		map.put(TencentConstant.TENCENT_APP_KUAIBAO_SPLASH_VIDEO, covertToSet(tencent_md_app_kuaibao_splash_video));

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
			//advertUploadRequest.setAdvertiser_id(advertisers.get(0).getMediaAdvertiserKey());
			advertUploadRequest.setDisplay_id(getDisplayId(material.getAdspaceId(), map));
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
						monitorList.add(MacroReplaceUtil.macroReplaceImageUrl(macroMap, track[1])); // 宏替换
					}
				}
			}
			monitorList.add(MacroReplaceUtil.getStr(impUrl, "?", "${DISPLAY_EXT}")); // SSP 宏替换
			advertUploadRequest.setMonitor_url(monitorList);

			// 点击监播
			List<String> clkMonitorList = new ArrayList<>();
			if (!StringUtils.isEmpty(material.getClkUrls())) {
				String clk = material.getClkUrls();
				String[] clkTrackingUrlArray = clk.split("\\|");
				if (null != clkTrackingUrlArray) {
					for (int i = 0; i < clkTrackingUrlArray.length; i++) {
						clkMonitorList.add(MacroReplaceUtil.macroReplaceClickUrl(macroMap, clkTrackingUrlArray[i]));
					}
				}
			}
			clkMonitorList.add(MacroReplaceUtil.getStr(clkUrl, "?", "${CLICK_EXT1}"));// SSP 宏替换
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
		if (Integer.valueOf(tencent_displayId_app_stream_img) == mediaDisplayId || Integer.valueOf(tencent_displayId_app_stream_gif) == mediaDisplayId) {
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
		} else if (Integer.valueOf(tencent_displayId_app_stream_native) == mediaDisplayId) {
			// 广告标题
			Map<String, String> adContentMap = new HashMap<>();
			adContentMap.put("file_text", material.getTitle());
			adContents.add(adContentMap);

			// 摘要 使用信息流广告正文设值
			adContentMap = new HashMap<>();
			adContentMap.put("file_text", material.getContent());
			adContents.add(adContentMap);

			// 缩略图
			adContentMap = new HashMap<>();
			adContentMap.put("file_url", material.getAdMaterials().split("\\|")[0]);
			adContents.add(adContentMap);

			// 客户名称
			adContentMap = new HashMap<>();
			adContentMap.put("file_text", advertiserName);
			adContents.add(adContentMap);

			// 长标题 使用信息流广告描述设值
			adContentMap = new HashMap<>();
			adContentMap.put("file_text", material.getDescription());
			adContents.add(adContentMap);
		} else if (Integer.valueOf(tencent_displayId_app_stream_video) == mediaDisplayId) {
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
		} else if (Integer.valueOf(tencent_displayId_app_stream_icon) == mediaDisplayId) {
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
		} else if (Integer.valueOf(tencent_displayId_qqlive_appweb_img) == mediaDisplayId || Integer.valueOf(tencent_displayId_app_kuaibao_splash_img) == mediaDisplayId) {
			// 图片素材
			Map<String, String> adContentMap = new HashMap<>();
			adContentMap = new HashMap<>();
			adContentMap.put("file_url", material.getAdMaterials().split("\\|")[0]);
			adContents.add(adContentMap);

			// 标题
			adContentMap = new HashMap<>();
			adContentMap = new HashMap<>();
			adContentMap.put("file_text", material.getTitle());
			adContents.add(adContentMap);
		} else if (Integer.valueOf(tencent_displayId_qqlive_appweb_video) == mediaDisplayId) {
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
		} else if (Integer.valueOf(tencent_displayId_qqlive_appweb_phone_splash_img) == mediaDisplayId) {
			// 图片素材
			Map<String, String> adContentMap = new HashMap<>();
			adContentMap.put("file_url", material.getAdMaterials().split("\\|")[0]);
			adContents.add(adContentMap);

			// 标题
			adContentMap = new HashMap<>();
			adContentMap.put("file_text", material.getTitle());
			adContents.add(adContentMap);

			// 摘要
			adContentMap = new HashMap<>();
			adContentMap.put("file_text", material.getDescription());
			adContents.add(adContentMap);
		} else if (Integer.valueOf(tencent_displayId_qqlive_appweb_phone_splash_video) == mediaDisplayId) {
			// 图片素材
			Map<String, String> adContentMap = new HashMap<>();
			adContentMap.put("file_url", material.getCover());
			adContents.add(adContentMap);

			// 视频素材
			adContentMap = new HashMap<>();
			adContentMap.put("file_url", material.getAdMaterials().split("\\|")[0]);
			adContents.add(adContentMap);

			// 标题
			adContentMap = new HashMap<>();
			adContentMap.put("file_text", material.getTitle());
			adContents.add(adContentMap);

			// 摘要
			adContentMap = new HashMap<>();
			adContentMap.put("file_text", material.getDescription());
			adContents.add(adContentMap);
		} else if (Integer.valueOf(tencent_displayId_app_kuaibao_splash_video) == mediaDisplayId) {
			// 图片素材
			Map<String, String> adContentMap = new HashMap<>();
			adContentMap.put("file_url", material.getCover());
			adContents.add(adContentMap);

			// 视频素材
			adContentMap = new HashMap<>();
			adContentMap.put("file_url", material.getAdMaterials().split("\\|")[0]);
			adContents.add(adContentMap);

			// 标题
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
	private Integer getDisplayId(Integer adspaceId, Map<Byte, Set<String>> map) {
		if (adspaceId == null) {
			return null;
		}
		if (map.get(TencentConstant.TENCENT_APP_STREAM_IMG).contains(String.valueOf(adspaceId))) {
			return Integer.valueOf(tencent_displayId_app_stream_img);
		}
		if (map.get(TencentConstant.TENCENT_APP_STREAM_VIDEO).contains(String.valueOf(adspaceId))) {
			return Integer.valueOf(tencent_displayId_app_stream_video);
		}
		if (map.get(TencentConstant.TENCENT_APP_STREAM_NATIVE).contains(String.valueOf(adspaceId))) {
			return Integer.valueOf(tencent_displayId_app_stream_native);
		}
		if (map.get(TencentConstant.TENCENT_APP_STREAM_GIF).contains(String.valueOf(adspaceId))) {
			return Integer.valueOf(tencent_displayId_app_stream_gif);
		}
		if (map.get(TencentConstant.TENCENT_APP_STREAM_ICON).contains(String.valueOf(adspaceId))) {
			return Integer.valueOf(tencent_displayId_app_stream_icon);
		}
		if (map.get(TencentConstant.TENCENT_QQLIVE_APPWEB_VIDEO).contains(String.valueOf(adspaceId))) {
			return Integer.valueOf(tencent_displayId_qqlive_appweb_video);
		}
		if (map.get(TencentConstant.TENCENT_QQLIVE_APPWEB_IMG).contains(String.valueOf(adspaceId))) {
			return Integer.valueOf(tencent_displayId_qqlive_appweb_img);
		}
		if (map.get(TencentConstant.TENCENT_QQLIVE_APPWEB_PHONE_SPLASH_IMG).contains(String.valueOf(adspaceId))) {
			return Integer.valueOf(tencent_displayId_qqlive_appweb_phone_splash_img);
		}
		if (map.get(TencentConstant.TENCENT_QQLIVE_APPWEB_PHONE_SPLASH_VIDEO).contains(String.valueOf(adspaceId))) {
			return Integer.valueOf(tencent_displayId_qqlive_appweb_phone_splash_video);
		}
		if (map.get(TencentConstant.TENCENT_APP_KUAIBAO_SPLASH_IMG).contains(String.valueOf(adspaceId))) {
			return Integer.valueOf(tencent_displayId_app_kuaibao_splash_img);
		}
		if (map.get(TencentConstant.TENCENT_APP_KUAIBAO_SPLASH_VIDEO).contains(String.valueOf(adspaceId))) {
			return Integer.valueOf(tencent_displayId_app_kuaibao_splash_video);
		}
		return Integer.valueOf(0);
	}
	
	/**
	 * 将字符串切分位集合返回
	 * 
	 * @param src
	 * @return
	 */
	private static Set<String> covertToSet(String src) {
		Set<String> set = new HashSet<String>();
		if (!StringUtils.isBlank(src)) {
			String[] array = src.split(",");
			for (String item : array) {
				set.add(item);
			}
		}
		return set;
	}
}
