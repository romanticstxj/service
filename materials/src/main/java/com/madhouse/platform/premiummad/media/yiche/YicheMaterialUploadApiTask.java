package com.madhouse.platform.premiummad.media.yiche;

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
import com.madhouse.platform.premiummad.constant.MaterialStatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dao.AdspaceDao;
import com.madhouse.platform.premiummad.dao.AdvertiserMapper;
import com.madhouse.platform.premiummad.dao.MaterialMapper;
import com.madhouse.platform.premiummad.entity.Adspace;
import com.madhouse.platform.premiummad.entity.Advertiser;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.media.yiche.constant.YicheConstant;
import com.madhouse.platform.premiummad.media.yiche.request.CreativeData;
import com.madhouse.platform.premiummad.media.yiche.request.UploadMaterialRequest;
import com.madhouse.platform.premiummad.media.yiche.response.UploadMaterialResponse;
import com.madhouse.platform.premiummad.media.yiche.util.YicheCommonUtil;
import com.madhouse.platform.premiummad.model.MaterialAuditResultModel;
import com.madhouse.platform.premiummad.service.IMaterialService;
import com.madhouse.platform.premiummad.service.IMediaService;
import com.madhouse.platform.premiummad.service.IPolicyService;
import com.madhouse.platform.premiummad.util.HttpUtils;
import com.madhouse.platform.premiummad.util.StringUtils;

@Component
public class YicheMaterialUploadApiTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(YicheMaterialUploadApiTask.class);

	@Value("${yiche.materialUploadUrl}")
	private String creativeUploadUrl;

	@Value("${yiche.dspId}")
	private String dspId;

	@Value("${yiche.signKey}")
	private String signKey;

	@Value("${material_meidaGroupMapping_yiche}")
	private String mediaGroupStr;

	@Autowired
	private MaterialMapper materialDao;

	@Autowired
	private IMaterialService materialService;

	@Autowired
	private AdvertiserMapper advertiserDao;

	@Autowired
	private IMediaService mediaService;

	@Autowired
	private IPolicyService policyService;

	@Autowired
	private AdspaceDao adspaceDao;

	public void uploadMaterial() {
		LOGGER.info("++++++++++yiche upload material begin+++++++++++");

		// 根据媒体组ID和审核对象获取具体的媒体ID
		int[] mediaIds = mediaService.getMeidaIds(mediaGroupStr, SystemConstant.MediaAuditObject.MATERIAL);

		// 媒体组没有映射到具体的媒体不处理
		if (mediaIds == null || mediaIds.length < 1) {
			return;
		}

		// 查询所有待审核且媒体的素材的审核状态是媒体审核的
		List<Material> unSubmitMaterials = materialDao.selectMeidaMaterials(mediaIds, MaterialStatusCode.MSC10002.getValue(), Boolean.TRUE);
		if (unSubmitMaterials == null || unSubmitMaterials.isEmpty()) {
			LOGGER.info("yiche没有未上传的素材");
			return;
		}

		// 上传到媒体，逐条上传
		LOGGER.info("YicheMaterialUploadApiTask", unSubmitMaterials.size());
		Map<Integer, String[]> materialIdKeys = new HashMap<Integer, String[]>();
		List<MaterialAuditResultModel> rejusedMaterials = new ArrayList<MaterialAuditResultModel>();
		for (Material material : unSubmitMaterials) {
			// 构造请求参数
			UploadMaterialRequest request = new UploadMaterialRequest();
			String errorMsg = buildRequest(request, material);
			if (!StringUtils.isBlank(errorMsg)) {
				MaterialAuditResultModel rejuseItem = new MaterialAuditResultModel();
				rejuseItem.setId(String.valueOf(material.getId()));
				rejuseItem.setStatus(MaterialStatusCode.MSC10001.getValue());
				rejuseItem.setMediaIds(mediaIds);
				rejuseItem.setErrorMessage(errorMsg.toString());
				rejusedMaterials.add(rejuseItem);
				LOGGER.error(errorMsg.toString());
				continue;
			}

			// 调用接口
			String requestJson = JSON.toJSONString(request);
			LOGGER.info("request:{}", requestJson);
			String responseJson = HttpUtils.post(creativeUploadUrl, requestJson);
			LOGGER.info("response:{}", responseJson);

			// 处理返回结果
			if (!StringUtils.isBlank(responseJson)) {
				UploadMaterialResponse uploadMaterialResponse = JSONObject.parseObject(responseJson, UploadMaterialResponse.class);

				// 成功
				if (YicheConstant.ErrorCode.SUCCESS == uploadMaterialResponse.getErrorCode()) {
					// 返回结果的条数与上传条数一致，按顺序返回主键ID
					if (uploadMaterialResponse.getResult() != null) {
						String mediaKey = uploadMaterialResponse.getResult().getDepositId();
						String[] mediaQueryAndMaterialKeys = { mediaKey, mediaKey };
						materialIdKeys.put(material.getId(), mediaQueryAndMaterialKeys);
					} else {
						LOGGER.error("返回结果的条数与上传条数不一致");
					}
				} else if (YicheConstant.ErrorCode.ERROR == uploadMaterialResponse.getErrorCode()) {
					// 上传失败
					MaterialAuditResultModel rejuseItem = new MaterialAuditResultModel();
					rejuseItem.setId(String.valueOf(material.getId()));
					rejuseItem.setStatus(MaterialStatusCode.MSC10001.getValue());
					rejuseItem.setMediaIds(mediaIds);
					rejuseItem.setErrorMessage(uploadMaterialResponse.getErrorMsg());
					rejusedMaterials.add(rejuseItem);
					LOGGER.error("素材[materialId=" + material.getId() + "]上传失败-" + uploadMaterialResponse.getErrorMsg());
				} else {
					// 其他情况
					LOGGER.error("素材[materialId=" + material.getId() + "]上传失败-" + responseJson);
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
	private String buildRequest(UploadMaterialRequest request, Material material) {
		request.setDspId(dspId);

		CreativeData creative = new CreativeData();

		// 订单编号
		creative.setOrderCode(policyService.getMediaDealId(material.getDealId(), material.getMediaId()));
		// 获取该素材的广告主,若广告主不存在返回错误信息
		String[] advertiserKeys = { material.getAdvertiserKey() };
		List<Advertiser> advertisers = advertiserDao.selectByAdvertiserKeysAndDspId(advertiserKeys, String.valueOf(material.getDspId()), material.getMediaId());
		if (advertisers == null || advertisers.size() != 1) {
			LOGGER.error("广告主不存在[advertiserKey=" + material.getAdvertiserKey() + "dspId=" + material.getDspId() + "]");
			return "广告主不存在";
		}
		Advertiser advertiser = advertisers.get(0);
		creative.setAdvertiserId(advertiser.getId());
		// 素材类型
		creative.setMaterialType(YicheConstant.MaterialType.NATIVE);
		// 模板ID TODO
		creative.setTemplateId(YicheConstant.Template.LISTAD);
		// 素材名称
		creative.setMaterialName(material.getMaterialName());
		// 广告位宽高，用图片尺寸
		String size = material.getSize();
		if (!StringUtils.isBlank(size) && size.split("\\*").length == 2) {
			creative.setWidth(Integer.valueOf(size.split("\\*")[0]));
			creative.setHeight(Integer.valueOf(size.split("\\*")[1]));
		}
		// 图片、flash 的 URL 地址
		creative.setImgUrl(material.getMediaMaterialUrl());
		// 标题
		creative.setTitle(material.getTitle());
		// 跳转链接地址
		creative.setLinkUrl(material.getLpgUrl());
		// 品牌ID 图文不必须，填为0
		creative.setBrandId(0);
		// 车型ID 图文不必须，填为0
		creative.setModelId(0);
		// 新闻简介
		creative.setSummary(material.getDescription());
		// 文字内容
		creative.setContent(material.getContent());
		// 平台
		List<Integer> list = new ArrayList<Integer>();
		list.add(material.getAdspaceId());
		List<Adspace> adspaces = adspaceDao.selectByIds(list);
		if (adspaces == null || adspaces.isEmpty()) {
			return "广告位不存在";
		}
		creative.setPlatform(getMediaPlatform(adspaces.get(0)));
		creative.setHtmlContent("");
		creative.setJsContent("");

		request.setMaterialList(creative);
		request.setTimestamp(System.currentTimeMillis());
		request.setExpireTime(StringUtils.addDay(material.getEndDate(), 1).getTime());

		// 校验串 TODO
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("timestamp", request.getTimestamp());
		jsonObj.put("dspId", dspId);
		jsonObj.put("expireTime", request.getExpireTime());
		jsonObj.put("materialList", JSON.parse(JSON.toJSONString(request.getMaterialList())));
		request.setSign(YicheCommonUtil.getSign(jsonObj, signKey));

		return "";
	}

	/**
	 * 根据我方广告位的终端类型和平台类型，对应媒体方的平台类型
	 * 
	 * @param adspace
	 * @return
	 */
	private int getMediaPlatform(Adspace adspace) {
		// Mobile
		if (adspace.getTerminalType() == 1) {
			// android
			if (adspace.getTerminalOs() == 1) {
				return YicheConstant.Platform.ANDROID;
			}
			// ios
			if (adspace.getTerminalOs() == 2) {
				return YicheConstant.Platform.IOS;
			}
		}

		// 其他情况返回PC/WAP
		return YicheConstant.Platform.PC_WAP;
	}
}
