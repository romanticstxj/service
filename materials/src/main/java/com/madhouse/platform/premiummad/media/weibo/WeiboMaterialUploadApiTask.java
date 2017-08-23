package com.madhouse.platform.premiummad.media.weibo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.constant.Layout;
import com.madhouse.platform.premiummad.constant.MaterialStatusCode;
import com.madhouse.platform.premiummad.constant.MediaMapping;
import com.madhouse.platform.premiummad.dao.AdvertiserMapper;
import com.madhouse.platform.premiummad.dao.MaterialMapper;
import com.madhouse.platform.premiummad.entity.Advertiser;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.media.weibo.constant.WeiboConstant;
import com.madhouse.platform.premiummad.media.weibo.constant.WeiboErrorCode;
import com.madhouse.platform.premiummad.media.weibo.constant.WeiboIndustryMapping;
import com.madhouse.platform.premiummad.media.weibo.request.WeiboAdInfo;
import com.madhouse.platform.premiummad.media.weibo.request.WeiboBanner;
import com.madhouse.platform.premiummad.media.weibo.request.WeiboFeed;
import com.madhouse.platform.premiummad.media.weibo.request.WeiboFeedActivity;
import com.madhouse.platform.premiummad.media.weibo.request.WeiboFeedVideo;
import com.madhouse.platform.premiummad.media.weibo.request.WeiboMaterialUploadRequest;
import com.madhouse.platform.premiummad.media.weibo.response.WeiboMaterialUploadResponse;
import com.madhouse.platform.premiummad.model.MaterialAuditResultModel;
import com.madhouse.platform.premiummad.service.IMaterialService;
import com.madhouse.platform.premiummad.util.HttpUtils;
import com.madhouse.platform.premiummad.util.StringUtils;

@Component
public class WeiboMaterialUploadApiTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(WeiboMaterialUploadApiTask.class);
	
	// 媒体创意类型
	private static String CREATIVE_BANNER = "001";
	private static String CREATIVE_FEED = "002";
	private static String CREATIVE_FEED_ACTIVITY = "003";
	private static String CREATIVE_FEED_VIDEO = "004";

	@Value("${weibo.materialUploadUrl}")
	private String uploadMaterialUrl;

	@Value("${weibo.dspid}")
	private String dspid;

	@Value("${weibo.token}")
	private String token;

	@Value("${weibo.uid}")
	private String uid;

	@Autowired
	private MaterialMapper materialDao;

	@Autowired
	private IMaterialService materialService;

	@Autowired
	private AdvertiserMapper advertiserDao;

	public void uploadMaterial() {
		LOGGER.info("++++++++++Weibo upload material begin+++++++++++");
		// 查询所有待审核且媒体的素材的审核状态是媒体审核的
		List<Material> unSubmitMaterials = materialDao.selectMediaMaterials(MediaMapping.WEIBO.getValue(), MaterialStatusCode.MSC10002.getValue());
		if (unSubmitMaterials == null || unSubmitMaterials.isEmpty()) {
			LOGGER.info("新浪微博没有未上传的物料");
			LOGGER.info("++++++++++Weibo upload material end+++++++++++");
			return;
		}

		// 上传到媒体
		LOGGER.info("WeiboMaterialUploadApiTask-weibo", unSubmitMaterials.size());

		Map<Integer, String[]> materialIdKeys = new HashMap<Integer, String[]>();
		List<MaterialAuditResultModel> rejusedMaterials = new ArrayList<MaterialAuditResultModel>();
		for (Material material : unSubmitMaterials) {
			WeiboMaterialUploadRequest uploadRequest = new WeiboMaterialUploadRequest();
			// 参数校验
			String errorMsg = buildRequest(material, uploadRequest);
			if (!StringUtils.isBlank(errorMsg)) {
				continue;
			}

			// 请求接口
			String requestJson = JSON.toJSONString(uploadRequest);
			LOGGER.info("FunadxUpload request Info: " + requestJson);
			String responseJson = HttpUtils.post(uploadMaterialUrl, requestJson);
			LOGGER.info("FunadxUpload response Info: " + responseJson);

			if (!StringUtils.isEmpty(responseJson)) {
				WeiboMaterialUploadResponse weiboMaterialUploadResponse = JSON.parseObject(responseJson, WeiboMaterialUploadResponse.class);
				Integer retCode = weiboMaterialUploadResponse.getRet_code();
				if (WeiboConstant.RESPONSE_SUCCESS.getValue() == retCode) {
					String[] mediaMaterialIdKeys = { String.valueOf(material.getId()) };
					materialIdKeys.put(material.getId(), mediaMaterialIdKeys);
				} else {
					// 系统错误
					if (WeiboErrorCode.WEC100.getValue() == weiboMaterialUploadResponse.getErr_code().intValue()) {
						LOGGER.error("素材[materialId=" + material.getId() + "]上传失败-" + WeiboErrorCode.getDescrip(weiboMaterialUploadResponse.getErr_code().intValue()));
						continue;
					}
					// 未知错误
					if (StringUtils.isBlank(WeiboErrorCode.getDescrip(weiboMaterialUploadResponse.getErr_code().intValue()))) {
						LOGGER.error("素材[materialId=" + material.getId() + "]上传失败-未知错误");
						continue;
					}

					// 已知业务错误
					MaterialAuditResultModel rejuseItem = new MaterialAuditResultModel();
					rejuseItem.setId(String.valueOf(material.getId()));
					rejuseItem.setStatus(MaterialStatusCode.MSC10001.getValue());
					rejuseItem.setMediaId(String.valueOf(MediaMapping.SOHUNEWS.getValue()));
					rejuseItem.setErrorMessage(WeiboErrorCode.getDescrip(weiboMaterialUploadResponse.getErr_code().intValue()));
					rejusedMaterials.add(rejuseItem);
					LOGGER.error("素材[materialId=" + material.getId() + "]上传失败-" + WeiboErrorCode.getDescrip(weiboMaterialUploadResponse.getErr_code().intValue()));
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

		LOGGER.info("++++++++++Weibo upload material end+++++++++++");
	}

	/**
	 * 构建上传物料请求
	 * 
	 * @param material
	 * @return
	 */
	private String buildRequest(Material material, WeiboMaterialUploadRequest uploadRequest) {
		// 获取该素材的广告主,若广告主不存在不做处理
		String[] advertiserKeys = { material.getAdvertiserKey() };
		List<Advertiser> advertisers = advertiserDao.selectByAdvertiserKeysAndDspId(advertiserKeys, String.valueOf(material.getDspId()), material.getMediaId());
		if (advertisers == null || advertisers.size() != 1) {
			LOGGER.error("广告主不存在[advertiserKey=" + material.getAdvertiserKey() + "dspId=" + material.getDspId() + "]");
			return "广告主不存在";
		}
		Advertiser advertiser = advertisers.get(0);

		uploadRequest.setDspid(dspid);
		uploadRequest.setToken(token);

		WeiboAdInfo weiboAdInfo = new WeiboAdInfo();
		List<WeiboBanner> weiboBanners = new ArrayList<WeiboBanner>();
		List<WeiboFeed> weiboFeeds = new ArrayList<WeiboFeed>();
		List<WeiboFeedActivity> weiboFeedActivitys = new ArrayList<WeiboFeedActivity>();
		List<WeiboFeedVideo> weiboFeedVideos = new ArrayList<WeiboFeedVideo>();

		// 曝光监控地址
		List<String> impUrls = new ArrayList<String>();
		if (material.getImpUrls() != null && !material.getImpUrls().isEmpty()) {
			// 素材表里以 |分割
			String[] impUrlArray = material.getImpUrls().split("\\|");
			if (impUrlArray != null) {
				for (int i = 0; i < impUrlArray.length; i++) {
					// 时间
					if (impUrlArray[i].matches("^-?\\d+$")) {
						continue;
					}

					impUrls.add(impUrlArray[i]);
				}
			}
		}

		// 点击监控地址
		List<String> clkUrls = new ArrayList<String>();
		if (material.getClkUrls() != null && !material.getClkUrls().isEmpty()) {
			// 素材表里以 |分割
			String[] clkUrlArray = material.getClkUrls().split("\\|");
			if (clkUrlArray != null) {
				for (int i = 0; i < clkUrlArray.length; i++) {
					clkUrls.add(clkUrlArray[i]);
				}
			}
		}

		String creativeType = getCreateType(material.getLayout());
		if (CREATIVE_BANNER.equals(creativeType)) {
			WeiboBanner weiboBanner = new WeiboBanner();
			weiboBanner.setAd_url(material.getAdMaterials().split("\\|")[0]);
			weiboBanner.setClient_id(String.valueOf(advertiser.getId()));
			weiboBanner.setClient_name(advertiser.getAdvertiserName());
			weiboBanner.setClk_url(clkUrls);
			weiboBanner.setContent_category(String.valueOf(WeiboIndustryMapping.getMediaIndustryId(advertiser.getIndustry())));
			weiboBanner.setCreative_id(String.valueOf(material.getId()));
			weiboBanner.setLandingpage_url(material.getLpgUrl());
			weiboBanner.setMonitor_url(impUrls);
			weiboBanners.add(weiboBanner);
		} else if (CREATIVE_FEED.equals(creativeType)) {
			WeiboFeed weiboFeed = new WeiboFeed();
			weiboFeed.setClient_id(String.valueOf(advertiser.getId()));
			weiboFeed.setClient_name(advertiser.getAdvertiserName());
			weiboFeed.setClk_url(clkUrls);
			weiboFeed.setContent_category(String.valueOf(WeiboIndustryMapping.getMediaIndustryId(advertiser.getIndustry())));
			weiboFeed.setCreative_id(String.valueOf(material.getId()));
			weiboFeed.setMblog_text(material.getDescription());
			weiboFeed.setMonitor_url(impUrls);
			weiboFeed.setObj_id("");
			weiboFeed.setPics(Arrays.asList(material.getAdMaterials().split("\\|")));
			weiboFeed.setUid(uid);
			weiboFeeds.add(weiboFeed);
		} else if (CREATIVE_FEED_ACTIVITY.equals(creativeType)) {
			WeiboFeedActivity weiboFeedActivity = new WeiboFeedActivity();
			weiboFeedActivity.setAd_url(material.getAdMaterials().split("\\|")[0]);
			weiboFeedActivity.setButton_type("none");
			weiboFeedActivity.setButton_url("");
			weiboFeedActivity.setClient_id(String.valueOf(advertiser.getId()));
			weiboFeedActivity.setClient_name(advertiser.getAdvertiserName());
			weiboFeedActivity.setClk_url(clkUrls);
			weiboFeedActivity.setContent_category(String.valueOf(WeiboIndustryMapping.getMediaIndustryId(advertiser.getIndustry())));
			weiboFeedActivity.setCreative_id(String.valueOf(material.getId()));
			weiboFeedActivity.setDesc(material.getDescription());
			weiboFeedActivity.setLandingpage_url(material.getLpgUrl());
			weiboFeedActivity.setMblog_text(material.getDescription());
			weiboFeedActivity.setMonitor_url(impUrls);
			weiboFeedActivity.setTitle(material.getTitle());
			weiboFeedActivity.setUid(uid);
			weiboFeedActivitys.add(weiboFeedActivity);
		} else if (CREATIVE_FEED_VIDEO.equals(creativeType)) {
			WeiboFeedVideo weiboFeedVideo = new WeiboFeedVideo();
			weiboFeedVideo.setAd_url(material.getAdMaterials().split("\\|")[0]);
			weiboFeedVideo.setButton_type("none");
			weiboFeedVideo.setButton_url("");
			weiboFeedVideo.setClient_id(String.valueOf(advertiser.getId()));
			weiboFeedVideo.setClient_name(advertiser.getAdvertiserName());
			weiboFeedVideo.setClk_url(clkUrls);
			weiboFeedVideo.setContent_category(String.valueOf(WeiboIndustryMapping.getMediaIndustryId(advertiser.getIndustry())));
			weiboFeedVideo.setCreative_id(String.valueOf(material.getId()));
			weiboFeedVideo.setDesc(material.getDescription());
			weiboFeedVideo.setLandingpage_url(material.getLpgUrl());
			weiboFeedVideo.setMblog_text(material.getDescription());
			weiboFeedVideo.setMonitor_url(impUrls);
			weiboFeedVideo.setTitle(material.getTitle());
			weiboFeedVideo.setUid(uid);
			weiboFeedVideos.add(weiboFeedVideo);
		} else {
			return "广告形式不支持";
		}

		weiboAdInfo.setBanner(weiboBanners);
		weiboAdInfo.setFeed(weiboFeeds);
		weiboAdInfo.setFeed_activity(weiboFeedActivitys);
		weiboAdInfo.setFeed_video(weiboFeedVideos);
		uploadRequest.setAd_info(weiboAdInfo);

		return "";
	}

	/**
	 * 根据广告形式转换成对应媒体的创意类型
	 * 
	 * @param layout
	 */
	private String getCreateType(int layout) {
		// Banner
		if (layout >= Layout.LO10001.getValue() && layout <= Layout.LO10007.getValue()) {
			return CREATIVE_BANNER;
		}

		// 视频信息流
		if (layout >= Layout.LO20001.getValue() && layout <= Layout.LO20007.getValue() || layout == Layout.LO30004.getValue()) {
			return CREATIVE_FEED;
		}

		// 图文信息流
		if (layout >= Layout.LO30001.getValue() && layout <= Layout.LO30003.getValue()) {
			return CREATIVE_FEED_VIDEO;
		}

		return "";
	}
}
