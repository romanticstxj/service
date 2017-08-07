package com.madhouse.platform.premiummad.media.tencent;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.madhouse.platform.premiummad.constant.MaterialStatusCode;
import com.madhouse.platform.premiummad.constant.MediaMapping;
import com.madhouse.platform.premiummad.dao.MaterialMapper;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.media.model.AdvertUploadRequest;
import com.madhouse.platform.premiummad.media.model.AdvertUploadRsponse;
import com.madhouse.platform.premiummad.media.util.TencentHttpUtil;
import com.madhouse.platform.premiummad.service.IMaterialService;
import com.madhouse.platform.premiummad.util.StringUtils;

/**
 * 上传广告信息
 */
@Component
public class AdvertUploadApiTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(AdvertUploadApiTask.class);
	private static final int ITERATOR_TIMES = 2;
	private static final int TECENT_OTV_ITERATOR = 1;

	@Value("${tencent.advertUpload}")
	private String advertUploadUrl;

	@Value("${tencent.imp.url}")
	private String impUrl;

	@Value("${tencent.clk.url}")
	private String clkUrl;

	@Value("${tencent.imp.urlssp}")
	private String impUrlSsp;

	@Value("${tencent.clk.urlssp}")
	private String clkUrlSsp;

	@Autowired
	private TencentHttpUtil tencentHttpUtil;

	@Autowired
	private MaterialMapper materialDao;

	@Autowired
	private IMaterialService materialService;

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
				return;
			}

			// 构造请求对象
			Map<String, String> paramMap = buildUploadRequest(mediaType, unSubmitMaterials);
			String responseJson = tencentHttpUtil.post(advertUploadUrl, paramMap);
			LOGGER.info("Tencent上传广告信息返回信息：{}", responseJson);
			// 上传成功时,responseJson 为空字符串或者null
			AdvertUploadRsponse advertUploadRsponse = null;
			try {
				advertUploadRsponse = JSONObject.parseObject(responseJson, AdvertUploadRsponse.class);
			} catch (Exception e) {
				String message = "syntax error, expect {, actual EOF, pos 0";
				if (e instanceof JSONException || e.getMessage().equals(message)) {
					LOGGER.info("Tencent上传广告位返回解析错误进入到JsonException中 :直接更新物料 task表为上传成功");
					handleSuccessResult(unSubmitMaterials);
				} else {
					LOGGER.info("Tencent上传广告位返回解析出错 : " + e.getMessage());
				}
				if (!StringUtils.isEmpty(paramMap.get("order_info")) && advertUploadRsponse != null) {
					if (advertUploadRsponse.getRet_code() == 0 && "完全正确".equals(advertUploadRsponse.getRet_msg())) {
						handleSuccessResult(unSubmitMaterials);
					} else if (advertUploadRsponse.getRet_code() > 0) {
						LOGGER.info("Tencent上传广告位出现错误:" + advertUploadRsponse.getRet_code() + "_" + advertUploadRsponse.getError_code());
					}
				} else {
					LOGGER.info("Tencent上传广告位返回出错 : AdvertUploadRsponse is null");
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
	private void handleSuccessResult(List<Material> unSubmitMaterials) {
		Map<Integer, String> materialIdKeys = new HashMap<Integer, String>();

		// 媒体方唯一标识是我方媒体id
		for (Material material : unSubmitMaterials) {
			materialIdKeys.put(material.getId(), String.valueOf(material.getId()));
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
	private Map<String, String> buildUploadRequest(int mediaType, List<Material> unSubmitMaterials) {
		List<AdvertUploadRequest> advertUploadRequestList = new ArrayList<>();

		// 批量上传物料
		for (Material material : unSubmitMaterials) {
			AdvertUploadRequest advertUploadRequest = new AdvertUploadRequest();

			// 创意素材内容
			List<Map<String, String>> adContents = new ArrayList<>();
			Map<String, String> adContentMap = new HashMap<>();
			adContentMap.put("file_url", material.getAdMaterials());
			adContents.add(adContentMap);
			advertUploadRequest.setAd_content(adContents);

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

			// 广告主的中文名称 TODO
			String cn = convert(String.valueOf(material.getDealId()));
			LOGGER.info("++++++++++++++" + cn);
			String clientName = cn.replace("\\u005f", "_"); // 替换下划线
			LOGGER.info("++++++++++++++" + clientName);
			advertUploadRequest.setClient_name(clientName);

			// 3095 腾讯新增 广告形式和静态落地页(供审核查看)必须 映射 TODO
			advertUploadRequest.setDisplay_id(material.getLayout());
			advertUploadRequest.setLanding_page(material.getLpgUrl()); // 静态落地页，腾讯必填 TODO

			advertUploadRequestList.add(advertUploadRequest);
		}

		Map<String, String> paramMap = new HashMap<>();
		String requestJson = JSONObject.toJSONString(advertUploadRequestList);
		String newjson = requestJson.replace("\\\\", "\\");
		LOGGER.info("Tencent上传广告位信息 1：{}", requestJson);
		LOGGER.info("Tencent上传广告位信息 2：{}", newjson);
		paramMap.put("order_info", newjson);
		return paramMap;
	}

	/**
	 * 避免添加新的列，造成数据库冗余(SouHuCampaignId表中只有搜狐使用，目前保存的有腾讯客户名、搜狐的执行单id)
	 * 
	 * @param str
	 * @return
	 */
	private String convert(String str) {
		str = (str == null ? "" : str);
		String tmp;
		StringBuffer sb = new StringBuffer(1000);
		char c;
		int i, j;
		sb.setLength(0);
		for (i = 0; i < str.length(); i++) {
			c = str.charAt(i);
			sb.append("\\u");
			j = (c >>> 8); // 取出高8位
			tmp = Integer.toHexString(j);
			if (tmp.length() == 1)
				sb.append("0");
			sb.append(tmp);
			j = (c & 0xFF); // 取出低8位
			tmp = Integer.toHexString(j);
			if (tmp.length() == 1)
				sb.append("0");
			sb.append(tmp);

		}
		return (new String(sb));
	}
}
