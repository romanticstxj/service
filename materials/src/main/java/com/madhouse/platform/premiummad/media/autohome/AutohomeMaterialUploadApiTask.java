package com.madhouse.platform.premiummad.media.autohome;

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
import com.alibaba.fastjson.JSONObject;
import com.madhouse.platform.premiummad.constant.AdevertiserIndustry;
import com.madhouse.platform.premiummad.constant.MaterialStatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dao.AdvertiserMapper;
import com.madhouse.platform.premiummad.dao.MaterialMapper;
import com.madhouse.platform.premiummad.entity.Advertiser;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.media.autohome.constant.AutohomeConstant;
import com.madhouse.platform.premiummad.media.autohome.constant.AutohomeIndustryMapping;
import com.madhouse.platform.premiummad.media.autohome.request.AppAdsnippet;
import com.madhouse.platform.premiummad.media.autohome.request.AppCreative;
import com.madhouse.platform.premiummad.media.autohome.request.CreativeUploadRequest;
import com.madhouse.platform.premiummad.media.autohome.response.CreativeUploadResponse;
import com.madhouse.platform.premiummad.media.autohome.util.AutohomeCommonUtil;
import com.madhouse.platform.premiummad.model.MaterialAuditResultModel;
import com.madhouse.platform.premiummad.service.IMaterialService;
import com.madhouse.platform.premiummad.service.IMediaService;
import com.madhouse.platform.premiummad.util.HttpUtils;
import com.madhouse.platform.premiummad.util.StringUtils;

@Component
public class AutohomeMaterialUploadApiTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(AutohomeMaterialUploadApiTask.class);

	@Value("${autohome.creativeUploadUrl}")
	private String creativeUploadUrl;

	@Value("${autohome.dspId}")
	private int dspId;

	@Value("${autohome.dspName}")
	private String dspName;

	@Value("${autohome.signKey}")
	private String signKey;

	@Value("${material_meidaGroupMapping_autohome}")
	private String mediaGroupStr;

	@Autowired
	private MaterialMapper materialDao;

	@Autowired
	private IMaterialService materialService;

	@Autowired
	private AdvertiserMapper advertiserDao;

	@Autowired
	private IMediaService mediaService;
	
	// TODO
	@Value("${imp.url}")
	private String impUrl;

	// TODO
	@Value("${clk.url}")
	private String clkUrl;

	public void uploadMaterial() {
		LOGGER.info("++++++++++autohome upload material begin+++++++++++");

		// 根据媒体组ID和审核对象获取具体的媒体ID
		int[] mediaIds = mediaService.getMeidaIds(mediaGroupStr, SystemConstant.MediaAuditObject.MATERIAL);

		// 媒体组没有映射到具体的媒体不处理
		if (mediaIds == null || mediaIds.length < 1) {
			return;
		}

		// 查询所有待审核且媒体的素材的审核状态是媒体审核的
		List<Material> unSubmitMaterials = materialDao.selectMaterialsByMeidaIds(mediaIds, MaterialStatusCode.MSC10002.getValue());
		if (unSubmitMaterials == null || unSubmitMaterials.isEmpty()) {
			LOGGER.info("autohome没有未上传的素材");
			return;
		}

		// 上传到媒体，逐条上传
		LOGGER.info("AutohomeMaterialUploadApiTask", unSubmitMaterials.size());
		Map<Integer, String[]> materialIdKeys = new HashMap<Integer, String[]>();
		List<MaterialAuditResultModel> rejusedMaterials = new ArrayList<MaterialAuditResultModel>();

		// 为了过滤DSP相同的key只上传一次
		Map<String, String[]> successMaps = new HashMap<String, String[]>();// <key, mediaQueryAndMaterialKeys>
		Map<String, String> refusedMap = new HashMap<String, String>();// <key, errorMsg>
		for (Material material : unSubmitMaterials) {
			String key = material.getDspId() + "-" + material.getMaterialKey() + "-" + material.getMediaId();
			// 上传成功的
			if (successMaps.containsKey(key)) {
				materialIdKeys.put(material.getId(), successMaps.get(key));
				continue;
			}
			// 自动驳回的
			if (refusedMap.containsKey(key)) {
				MaterialAuditResultModel rejuseItem = new MaterialAuditResultModel();
				rejuseItem.setId(String.valueOf(material.getId()));
				rejuseItem.setStatus(MaterialStatusCode.MSC10001.getValue());
				rejuseItem.setMediaIds(mediaIds);
				rejuseItem.setErrorMessage(refusedMap.get(key));
				rejusedMaterials.add(rejuseItem);
				continue;
			}
			
			// 构造请求参数
			CreativeUploadRequest<AppCreative> request = new CreativeUploadRequest<AppCreative>();
			String errorMsg = buildRequest(request, material);
			if (!StringUtils.isBlank(errorMsg)) {
				MaterialAuditResultModel rejuseItem = new MaterialAuditResultModel();
				rejuseItem.setId(String.valueOf(material.getId()));
				rejuseItem.setStatus(MaterialStatusCode.MSC10001.getValue());
				rejuseItem.setMediaIds(mediaIds);
				rejuseItem.setErrorMessage(errorMsg.toString());
				rejusedMaterials.add(rejuseItem);
				
				// 过滤重复的 MaterialKey
				refusedMap.put(key, errorMsg.toString());
				LOGGER.error(rejuseItem.getErrorMessage());
				continue;
			}

			// 调用接口
			String requestJson = JSON.toJSONString(request);
			LOGGER.info("request:{}", requestJson);
			String responseJson = HttpUtils.post(creativeUploadUrl, requestJson);
			LOGGER.info("response:{}", responseJson);

			// TODO just test
			if (responseJson.contains("403 Forbidden")) {
				MaterialAuditResultModel rejuseItem = new MaterialAuditResultModel();
				rejuseItem.setId(String.valueOf(material.getId()));
				rejuseItem.setStatus(MaterialStatusCode.MSC10001.getValue());
				rejuseItem.setMediaIds(mediaIds);
				rejuseItem.setErrorMessage("403 Forbidden");
				rejusedMaterials.add(rejuseItem);

				// 过滤重复的 MaterialKey
				refusedMap.put(key, "403 Forbidden");
				continue;
			}
			
			// 处理返回结果
			if (!StringUtils.isBlank(responseJson)) {
				CreativeUploadResponse creativeUploadResponse = JSONObject.parseObject(responseJson, CreativeUploadResponse.class);

				// 成功
				if (AutohomeConstant.RetCode.AUTOHOME_STATUS_SUCCESS == creativeUploadResponse.getStatus()) {
					// 返回结果的条数与上传条数一致，按顺序返回主键ID
					if (creativeUploadResponse.getData() != null && creativeUploadResponse.getData().getCreativeIds() != null && creativeUploadResponse.getData().getCreativeIds().size() == unSubmitMaterials.size()) {
						for (int i = 0; i < creativeUploadResponse.getData().getCreativeIds().size(); i++) {
							String mediaKey = String.valueOf(creativeUploadResponse.getData().getCreativeIds().get(i));
							String[] mediaQueryAndMaterialKeys = { mediaKey, mediaKey };
							materialIdKeys.put(unSubmitMaterials.get(i).getId(), mediaQueryAndMaterialKeys);
							
							// 过滤重复的 MaterialKey
							successMaps.put(key, mediaQueryAndMaterialKeys);
						}
					} else {
						LOGGER.error("返回结果的条数与上传条数不一致");
					}
				} else {
					// 其他情况 TODO
					MaterialAuditResultModel rejuseItem = new MaterialAuditResultModel();
					rejuseItem.setId(String.valueOf(material.getId()));
					rejuseItem.setStatus(MaterialStatusCode.MSC10001.getValue());
					rejuseItem.setMediaIds(mediaIds);
					rejuseItem.setErrorMessage(creativeUploadResponse.getStatusInfo().getGlobals());
					rejusedMaterials.add(rejuseItem);
					
					// 过滤重复的 MaterialKey
					refusedMap.put(key, creativeUploadResponse.getStatusInfo().getGlobals());
					LOGGER.error("素材[materialId=" + material.getId() + "]上传失败-" + creativeUploadResponse.getStatusInfo().getGlobals());
				}
			} else {
				LOGGER.info("Response is null");
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

		LOGGER.info("++++++++++autohome upload material end+++++++++++");
	}

	/**
	 * 构建请求参数
	 * 
	 * @param unSubmitMaterials
	 * @return
	 */
	private String buildRequest(CreativeUploadRequest<AppCreative> request, Material material) {
		request.setDspId(dspId);
		request.setDspName(dspName);

		// 创意内容
		List<AppCreative> creative = new ArrayList<AppCreative>();
		AppCreative item = new AppCreative();

		// 获取该素材的广告主,若广告主不存在返回错误信息
		String[] advertiserKeys = { material.getAdvertiserKey() };
		List<Advertiser> advertisers = advertiserDao.selectByAdvertiserKeysAndDspId(advertiserKeys, String.valueOf(material.getDspId()), material.getMediaId());
		if (advertisers == null || advertisers.size() != 1) {
			LOGGER.error("广告主不存在[advertiserKey=" + material.getAdvertiserKey() + "dspId=" + material.getDspId() + "]");
			return "广告主不存在";
		}
		Advertiser advertiser = advertisers.get(0);
		item.setAdvertiserId(advertiser.getId());
		item.setAdvertiserName(advertiser.getAdvertiserName());
		// 所属行业
		item.setIndustryId(AutohomeIndustryMapping.getMediaIndustryId(advertiser.getIndustry().intValue()));
		item.setIndustryName(AdevertiserIndustry.getDescrip(advertiser.getIndustry().intValue()));
		// 素材类型
		item.setCreativeTypeId(AutohomeConstant.MaterialType.NATIVE);
		// 广告位宽高，用图片尺寸
		String size = material.getSize();
		if (!StringUtils.isBlank(size) && size.split("\\*").length == 2) {
			item.setWidth(Integer.valueOf(size.split("\\*")[0]));
			item.setHeight(Integer.valueOf(size.split("\\*")[1]));
		}
		// 模板ID
		item.setTemplateId(AutohomeConstant.Template.APP_NATIVE);
		// 去重码，预留字段，填充固定值 ""
		item.setRepeatedCode("");
		// 广告内容
		item.setAdsnippet(getAdsnippet(material, item.getTemplateId()));
		// 平台
		item.setPlatform(AutohomeConstant.Platform.APP);
		creative.add(item);
		request.setCreative(creative);

		// 校验串 TODO
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("dspId", dspId);
		jsonObj.put("dspName", dspName);
		jsonObj.put("creative", JSON.parse(JSON.toJSONString(request.getCreative())));
		request.setSign(AutohomeCommonUtil.getSign(jsonObj, signKey));

		// 时间戳
		request.setTimestamp(System.currentTimeMillis());

		return "";
	}
	
	/**
	 * 根据广告模板获取广告内容
	 * 
	 * @param material
	 * @return
	 */
	private AppAdsnippet getAdsnippet(Material material, int templateId) {
		AppAdsnippet appAdsnippet = new AppAdsnippet();

		List<Map<String, String>> content = new ArrayList<>();
		// APP- 信息流广告
		if (templateId == AutohomeConstant.Template.APP_NATIVE) {
			// 小图地址
			if (!StringUtils.isBlank(material.getIcon())) {
				Map<String, String> adContentMap = new HashMap<>();
				adContentMap.put("simg", material.getIcon());
				content.add(adContentMap);
			}

			// 大图地址
			Map<String, String> adContentMap = new HashMap<>();
			adContentMap.put("bimg", material.getAdMaterials().split("\\|")[0]);
			content.add(adContentMap);

			// 标题内容
			adContentMap = new HashMap<>();
			adContentMap.put("text", material.getTitle());
			content.add(adContentMap);
		}
		appAdsnippet.setContent(content);

		// 曝光监控地址
		List<String> pv = new ArrayList<>();
		if (!StringUtils.isEmpty(material.getImpUrls())) {
			String impTrackingUrl = material.getImpUrls();// 支持多个
			String[] impTrackingUrlArray = impTrackingUrl.split("\\|");
			if (null != impTrackingUrlArray) {
				for (int i = 0; i < impTrackingUrlArray.length; i++) {
					String[] track = impTrackingUrlArray[i].split("`");
					pv.add(track[1]);
				}
			}
		}
		appAdsnippet.setPv(pv);

		// 点击地址（302跳转）TODO
		appAdsnippet.setLink(material.getClkUrls().split("\\|")[0]);

		return appAdsnippet;
	}
}
