package com.madhouse.platform.premiummad.media.tencent;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
import com.madhouse.platform.premiummad.constant.MediaMapping;
import com.madhouse.platform.premiummad.dao.AdvertiserMapper;
import com.madhouse.platform.premiummad.dao.MaterialMapper;
import com.madhouse.platform.premiummad.entity.Advertiser;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.media.constant.TencentErrorCode;
import com.madhouse.platform.premiummad.media.model.AdvertUploadData;
import com.madhouse.platform.premiummad.media.model.AdvertUploadResponse;
import com.madhouse.platform.premiummad.media.model.AdvertUploadReturnMessage;
import com.madhouse.platform.premiummad.media.model.TencentCommonRequest;
import com.madhouse.platform.premiummad.media.util.TencentHttpUtil;
import com.madhouse.platform.premiummad.model.MaterialAuditResultModel;
import com.madhouse.platform.premiummad.service.IMaterialService;
import com.madhouse.platform.premiummad.service.IPolicyService;
import com.madhouse.platform.premiummad.util.StringUtils;

/**
 * 上传广告信息
 */
@Component
public class AdvertUploadApiTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(AdvertUploadApiTask.class);
	private static final int ITERATOR_TIMES = 2;
	private static final int TECENT_OTV_ITERATOR = 1;

	@Value("${tencent.adcreativeUpload}")
	private String advertUploadUrl;

	@Value("${tencent.imp.url}")
	private String impUrl;

	@Value("${tencent.clk.url}")
	private String clkUrl;

	@Value("${tencent.imp.urlssp}")
	private String impUrlSsp;

	@Value("${tencent.clk.urlssp}")
	private String clkUrlSsp;
	
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
	
	public void advertUpload() {
		// TENCENT 对应两个媒体 OTV 和 非 OTV
		for (int mediaType = 0; mediaType < ITERATOR_TIMES; mediaType++) {
			int mediaId = 0;
			if (mediaType != TECENT_OTV_ITERATOR) {
				mediaId = MediaMapping.TENCENT_NOT_OTV.getValue();
			} else {
				mediaId = MediaMapping.TENCENT.getValue();
			}
			LOGGER.info(MediaMapping.getDescrip(mediaId) + " AdvertUploadApiTask-advertUpload start");

			// 查询所有待审核且媒体的素材的审核状态是媒体审核的
			List<Material> unSubmitMaterials = materialDao.selectMediaMaterials(mediaId, MaterialStatusCode.MSC10002.getValue());
			if (unSubmitMaterials == null || unSubmitMaterials.isEmpty()) {
				LOGGER.info(MediaMapping.getDescrip(mediaId) + "没有未上传的广告主");
				continue;
			} 

			// 构造请求对象
			TencentCommonRequest<List<AdvertUploadData>> request = buildUploadRequest(mediaType, unSubmitMaterials);
			String responseJson = tencentHttpUtil.post(advertUploadUrl, request);
			LOGGER.info("Tencent上传广告信息返回信息：{}", responseJson);
			// 上传成功时,responseJson 为空字符串或者null
			AdvertUploadResponse advertUploadRsponse = null;
			try {
				advertUploadRsponse = JSONObject.parseObject(responseJson, AdvertUploadResponse.class);
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
						for (AdvertUploadReturnMessage item : advertUploadRsponse.getRet_msg()) {
							// 上传成功
							if (StringUtils.isBlank(item.getErr_code()) && StringUtils.isBlank(item.getErr_msg())) {
								successfulMaterialIds.add(item.getDsp_order_id());
							} else {
								// 自动驳回
								MaterialAuditResultModel rejuseItem = new MaterialAuditResultModel();
								rejuseItem.setId(item.getDsp_order_id());
								rejuseItem.setStatus(MaterialStatusCode.MSC10001.getValue());
								rejuseItem.setMediaId(String.valueOf(mediaId));
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
			LOGGER.info(MediaMapping.getDescrip(mediaId) + " AdvertUploadApiTask-advertUpload end");
		}
	}

	/**
	 * 处理成功信息,更新task状态
	 *
	 * @param materialTasks
	 *            task数据集合
	 */
	private void handleSuccessResult(List<Material> unSubmitMaterials, Set<String> successfulMaterialIds) {
		Map<Integer, String> materialIdKeys = new HashMap<Integer, String>();

		// 媒体方唯一标识是我方媒体id
		for (Material material : unSubmitMaterials) {
			if (successfulMaterialIds.contains(String.valueOf(material.getId()))) {
				materialIdKeys.put(material.getId(), String.valueOf(material.getId()));
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
	private TencentCommonRequest<List<AdvertUploadData>> buildUploadRequest(int mediaType, List<Material> unSubmitMaterials) {
		TencentCommonRequest<List<AdvertUploadData>> request = new TencentCommonRequest<List<AdvertUploadData>>();
		List<AdvertUploadData> advertUploadRequestList = new ArrayList<>();

		// 批量上传物料
		for (Material material : unSubmitMaterials) {
			// 获取该素材的广告主,若广告主不存在不做处理
			String[] advertiserKeys = { material.getAdvertiserKey() };
			List<Advertiser> advertisers = advertiserDao.selectByAdvertiserKeysAndDspId(advertiserKeys, String.valueOf(material.getDspId()), material.getMediaId());
			if (advertisers == null || advertisers.size() != 1) {
				LOGGER.error("广告主不存在[advertiserKey=" + material.getAdvertiserKey() + "dspId=" + material.getDspId() + "]");
				return null;
			}
			
			AdvertUploadData advertUploadRequest = new AdvertUploadData();

			// 3095 腾讯新增 广告形式和静态落地页(供审核查看)必须 映射
			// 广告主名称
			String advertiserName = advertisers.get(0).getAdvertiserName().replace("\\u005f", "_"); // 替换下划线
			advertUploadRequest.setAdvertiser_name(advertiserName);
			advertUploadRequest.setAdvertiser_id(advertisers.get(0).getMediaAdvertiserKey());
			advertUploadRequest.setDisplay_id(getDisplayId(material.getAdspaceId()));
			advertUploadRequest.setLanding_page(material.getLpgUrl()); // 静态落地页，腾讯必填  用落地页
			
			// 创意素材内容
			List<Map<String, String>> adContents = new ArrayList<>();
			
			// 标题
			Map<String, String> adContentMap = new HashMap<>();
			adContentMap.put("file_text", material.getTitle());
			adContents.add(adContentMap);
			
			// 素材上传地址
			adContentMap = new HashMap<>();
			String[] adMaterials = material.getAdMaterials().split("\\|");
			for (String adMaterial : adMaterials) {
				adContentMap.put("file_url", adMaterial);
			}
			adContents.add(adContentMap);
			
			// 封面
			adContentMap = new HashMap<>();
			adContentMap.put("file_url", material.getCover());
			adContents.add(adContentMap);
			
			// 摘要
			adContentMap = new HashMap<>();
			adContentMap.put("file_text", material.getDescription());
			adContents.add(adContentMap);
			
			// 广告主名称
			adContentMap = new HashMap<>();
			adContentMap.put("file_text", advertiserName.substring(0, 7));//TODO
			adContents.add(adContentMap);
			advertUploadRequest.setAd_content(adContents); //new TODO

			// 展示监播
			List<String> monitorList = new ArrayList<>();
			if (mediaType != TECENT_OTV_ITERATOR) {
				// 非otv外投
				monitorList.add(impUrl + "${EXT2}");
				monitorList.add(impUrlSsp + "${DISPLAY_EXT1}");
			} else {
				// otv直投
				monitorList.add(impUrl + "${EXT2}");
			}
			if (!StringUtils.isEmpty(material.getImpUrls())) {
				String impTrackingUrl = material.getImpUrls();// 支持多个
				String[] impTrackingUrlArray = impTrackingUrl.split("\\|");
				if (null != impTrackingUrlArray) {
					for (int i = 0; i < impTrackingUrlArray.length; i++) {
						// 时间过滤
						if (impTrackingUrlArray[i].matches("^-?\\d+$")) {
							continue;
						}
						monitorList.add(impTrackingUrlArray[i]);
					}
				}
			}
			advertUploadRequest.setMonitor_url(monitorList);

			// 展示监播
			List<String> clkMonitorList = new ArrayList<>();
			if (!StringUtils.isEmpty(material.getClkUrls())) {
				String clk = material.getClkUrls();
				String[] clkTrackingUrlArray = clk.split("\\|");
				if (null != clkTrackingUrlArray) {
					for (int i = 0; i < clkTrackingUrlArray.length; i++) {
						clkMonitorList.add(clkTrackingUrlArray[i]);
					}
				}
			}
			advertUploadRequest.setClick_monitor_url(clkMonitorList);

			String encodeTargetUrl = "";
			try {
				encodeTargetUrl = URLEncoder.encode(material.getLpgUrl(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				LOGGER.info("Tencent targetUrl encode exception");
				e.printStackTrace();
			}

			if (mediaType != TECENT_OTV_ITERATOR) {
				advertUploadRequest.setTargeting_url(clkUrl + encodeTargetUrl + "&${EXT}");
				advertUploadRequest.setTargeting_url(clkUrlSsp + encodeTargetUrl + "${CLICK_EXT1}");
			} else {
				advertUploadRequest.setTargeting_url(clkUrl + encodeTargetUrl + "&${EXT}");
			}

			advertUploadRequest.setDsp_order_id(String.valueOf(material.getId()));

			advertUploadRequestList.add(advertUploadRequest);
		}
		
		request.setData(advertUploadRequestList);
		return request;
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
