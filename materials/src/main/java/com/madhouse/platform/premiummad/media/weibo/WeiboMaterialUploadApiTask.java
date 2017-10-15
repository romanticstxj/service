package com.madhouse.platform.premiummad.media.weibo;

/*import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;*/
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.druid.util.Base64;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import com.madhouse.platform.premiummad.media.weibo.request.WeiboMediaInitRequest;
import com.madhouse.platform.premiummad.media.weibo.request.WeiboMediaUploadRequest;
import com.madhouse.platform.premiummad.media.weibo.response.WeiboData;
import com.madhouse.platform.premiummad.media.weibo.response.WeiboMaterialUploadErrorResponse;
import com.madhouse.platform.premiummad.media.weibo.response.WeiboMaterialUploadResponse;
import com.madhouse.platform.premiummad.media.weibo.response.WeiboResponse;
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

	@Value("${weibo.meidaInitUrl}")
	private String meidaInitUrl;

	@Value("${weibo.mediaUploadUrl}")
	private String mediaUploadUrl;

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

	private static HttpClient httpClient = new HttpClient();

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
			String creativeType = getCreateType(material.getLayout());
			// 视频的话需要先上传视频到媒体 - 视频上传成功但是素材上传失败的时候不需要再上传视频
			if (CREATIVE_FEED_VIDEO.equals(creativeType) && (StringUtils.isBlank(material.getMediaMaterialUrl()) || !StringUtils.isBlank(material.getMediaQueryKey()))) {
				StringBuilder mediaMaterialUrl = new StringBuilder();
				String uploadMaterialErrorMsg = this.uploadVideo(material.getAdMaterials().split("\\|")[0], mediaMaterialUrl);
				// 已知错误拒绝
				if (!StringUtils.isBlank(uploadMaterialErrorMsg)) {
					MaterialAuditResultModel rejuseItem = new MaterialAuditResultModel();
					rejuseItem.setId(String.valueOf(material.getId()));
					rejuseItem.setStatus(MaterialStatusCode.MSC10001.getValue());
					rejuseItem.setMediaId(String.valueOf(MediaMapping.WEIBO.getValue()));
					rejuseItem.setErrorMessage(uploadMaterialErrorMsg.toString());
					rejusedMaterials.add(rejuseItem);
					LOGGER.error("素材关联的视频上传到媒体失败[materialId=" + material.getId() + "]-" + uploadMaterialErrorMsg.toString());
				}

				// 未知错误记录日志
				if (StringUtils.isBlank(mediaMaterialUrl)) {
					LOGGER.info("素材关联的视频上传到媒体失败[materialId=" + material.getId() + "]");
					continue;
				}
				material.setMediaMaterialUrl(mediaMaterialUrl.toString());
				
				// 视频上传地址回写-防止下面操作失败导致上传地址丢失
				Material updateMeterialMediaUrlItem = new Material();
				updateMeterialMediaUrlItem.setMediaMaterialUrl(material.getMediaMaterialUrl());
				updateMeterialMediaUrlItem.setId(material.getId());
				materialDao.updateByPrimaryKeySelective(updateMeterialMediaUrlItem);
			}

			// 参数校验
			WeiboMaterialUploadRequest uploadRequest = new WeiboMaterialUploadRequest();
			String errorMsg = buildRequest(material, uploadRequest);
			if (!StringUtils.isBlank(errorMsg)) {
				continue;
			}
			
			// 请求接口
			String requestJson = JSON.toJSONString(uploadRequest);
			LOGGER.info("WeiboMaterialUpload request Info: " + requestJson);
			String responseJson = HttpUtils.post(uploadMaterialUrl, requestJson);
			LOGGER.info("WeiboMaterialUpload response Info: " + responseJson);

			if (!StringUtils.isEmpty(responseJson)) {
				WeiboMaterialUploadResponse weiboMaterialUploadResponse = JSON.parseObject(responseJson, WeiboMaterialUploadResponse.class);
				int retCode = weiboMaterialUploadResponse.getRet_code().intValue();
				int errorCode = weiboMaterialUploadResponse.getErr_code().intValue();
				if (WeiboConstant.RESPONSE_SUCCESS.getValue() == retCode && WeiboErrorCode.WEC000.getValue() == errorCode) {
					String[] mediaQueryAndMaterialKeys = { String.valueOf(material.getId()) };
					materialIdKeys.put(material.getId(), mediaQueryAndMaterialKeys);
				} else {
					WeiboMaterialUploadErrorResponse weiboMaterialUploadErrorResponse = JSON.parseObject(responseJson, WeiboMaterialUploadErrorResponse.class);

					// 系统错误
					if (WeiboErrorCode.WEC100.getValue() == errorCode) {
						LOGGER.error("素材[materialId=" + material.getId() + "]上传失败-" + WeiboErrorCode.getDescrip(errorCode));
						continue;
					}

					// 未知错误
					if (StringUtils.isBlank(WeiboErrorCode.getDescrip(errorCode)) || weiboMaterialUploadErrorResponse.getRet_msg() == null || weiboMaterialUploadErrorResponse.getRet_msg().isEmpty() || StringUtils.isBlank(WeiboErrorCode.getDescrip(weiboMaterialUploadErrorResponse.getRet_msg().get(0).getErr_code()))) {
						LOGGER.error("素材[materialId=" + material.getId() + "]上传失败-未知错误");
						continue;
					}

					// 已知业务错误
					MaterialAuditResultModel rejuseItem = new MaterialAuditResultModel();
					rejuseItem.setId(String.valueOf(material.getId()));
					rejuseItem.setStatus(MaterialStatusCode.MSC10001.getValue());
					rejuseItem.setMediaId(String.valueOf(MediaMapping.WEIBO.getValue()));
					rejuseItem.setErrorMessage(WeiboErrorCode.getDescrip(weiboMaterialUploadErrorResponse.getRet_msg().get(0).getErr_code()));
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
			// 素材表里以 |分割  -> startDelay1`url1|startDelay2`url2
			String[] impUrlArray = material.getImpUrls().split("\\|");
			if (impUrlArray != null) {
				for (int i = 0; i < impUrlArray.length; i++) {
					String[] track = impUrlArray[i].split("`");
					impUrls.add(track[1]);
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
			weiboFeed.setMblog_text(material.getContent());
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
			weiboFeedActivity.setMblog_text(material.getContent());
			weiboFeedActivity.setMonitor_url(impUrls);
			weiboFeedActivity.setTitle(material.getTitle());
			weiboFeedActivity.setUid(uid);
			weiboFeedActivitys.add(weiboFeedActivity);
		} else if (CREATIVE_FEED_VIDEO.equals(creativeType)) {
			WeiboFeedVideo weiboFeedVideo = new WeiboFeedVideo();
			weiboFeedVideo.setAd_url(material.getCover()); // 封面url
			weiboFeedVideo.setVideo_url(material.getMediaMaterialUrl()); // 上传到媒体的地址
			weiboFeedVideo.setButton_type("none");
			weiboFeedVideo.setButton_url("");
			weiboFeedVideo.setClient_id(String.valueOf(advertiser.getId()));
			weiboFeedVideo.setClient_name(advertiser.getAdvertiserName());
			weiboFeedVideo.setClk_url(clkUrls);
			weiboFeedVideo.setContent_category(String.valueOf(WeiboIndustryMapping.getMediaIndustryId(advertiser.getIndustry())));
			weiboFeedVideo.setCreative_id(String.valueOf(material.getId()));
			weiboFeedVideo.setDesc(material.getDescription());
			weiboFeedVideo.setLandingpage_url(material.getLpgUrl());
			weiboFeedVideo.setMblog_text(material.getContent());
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
		if (layout >= Layout.LO20001.getValue() && layout <= Layout.LO20007.getValue() || layout == Layout.LO30011.getValue()) {
			return CREATIVE_FEED_VIDEO;
		}

		// 图文信息流
		if (layout >= Layout.LO30001.getValue() && layout <= Layout.LO30009.getValue()) {
			return CREATIVE_FEED;
		}

		return "";
	}

	/**
	 * 上传视频到媒体
	 * 
	 * @param filePath
	 * @param mediaMaterialUrl
	 * @return errorMsg
	 */
	private String uploadVideo(String filePath, StringBuilder mediaMaterialUrl) {
		// step 0 校验视频文件
		int pos = filePath.lastIndexOf("/");
		if (pos < 0) {
			return "文件格式有误";
		}
		InputStream inputStream = getStreamContent(filePath);
		if (inputStream == null) {
			return "视频获取失败";
		}

		// step 1 调用初始化接口获取uploadId 和 最大长度
		Map<String, Integer> md5LenghtLength = StringUtils.getFileMD5(inputStream);
		WeiboData weiboData = initMedia(md5LenghtLength, filePath.substring(pos + 1));
		if (weiboData == null) {
			return "初始化上传视频失败";
		}

		// step2 将内容分块并上传
		byte[] buffer = new byte[weiboData.getLength()];
		inputStream = getStreamContent(filePath);
		int totalTime = (md5LenghtLength.entrySet().iterator().next().getValue() - 1) / weiboData.getLength() + 1;
		try {
			/*File file = new File("D:\\response.txt");
			PrintStream ps = new PrintStream(new FileOutputStream(file));*/
			for (int partNumber = 0; partNumber < totalTime; partNumber ++) {
				int subLength = 0;
				for (subLength = 0; subLength < weiboData.getLength();) {
					int readlen = inputStream.read(buffer, subLength, weiboData.getLength() - subLength);
					if (readlen == -1) {
						break;
					}
					subLength += readlen;
				}
				String result = uploadMedia(weiboData.getUploadId(), partNumber, subLength, buffer/*, ps*/);
				if (!StringUtils.isBlank(result)) {
					mediaMaterialUrl = mediaMaterialUrl.append(result);
				}
			}
		} catch (IOException e) {
			LOGGER.info("系统异常");
		}

		return null;
	}
	
	/**
	 * 分块上传
	 * 
	 * @param uploadId
	 * @param partNumber
	 * @param content
	 * @param result
	 */
	private String uploadMedia(String uploadId, int partNumber, int length, byte[] wholeContent/*, PrintStream ps*/) {
		// 构造请求对象
		WeiboMediaUploadRequest weiboMediaUploadRequest = new WeiboMediaUploadRequest();
		weiboMediaUploadRequest.setDspid(dspid);
		weiboMediaUploadRequest.setToken(token);
		weiboMediaUploadRequest.setUploadId(uploadId);
		weiboMediaUploadRequest.setPartNumber(partNumber);
		weiboMediaUploadRequest.setSliceCheck(StringUtils.getMD5(wholeContent, 0, length));
		weiboMediaUploadRequest.setContent(Base64.byteArrayToBase64(Arrays.copyOfRange(wholeContent, 0, length)));

		// 发送请求
		String requestJson = JSON.toJSONString(weiboMediaUploadRequest);
		LOGGER.info("WeiboMediaUploadRequest: " + requestJson);
		String responseJson = HttpUtils.post(mediaUploadUrl, requestJson);
		LOGGER.info("WeiboMediaUploadResponse: " + responseJson);
		/*ps.append("partNumber-" + weiboMediaUploadRequest.getPartNumber() + ",md5-" + weiboMediaUploadRequest.getSliceCheck());
		ps.append("length-" + length + "-");
		ps.append(responseJson);*/
		// 处理返回的结果
		if (!StringUtils.isBlank(responseJson)) {
			WeiboResponse weiboResponse = JSON.parseObject(responseJson, WeiboResponse.class);
			if (WeiboConstant.RESPONSE_SUCCESS.getValue() == weiboResponse.getRet_code().intValue() && WeiboErrorCode.WEC000.getValue() == weiboResponse.getErr_code().intValue()) {
				WeiboData data = JSONObject.parseObject(weiboResponse.getRet_msg().toString(), WeiboData.class);
				if (!StringUtils.isEmpty(data.getUrl())) {
					return data.getUrl();
				}
			}
		}

		return "";
	}

	/**
	 * 初始化接口
	 * 
	 * @param inputStream
	 * @param fileName
	 * @return
	 */
	private WeiboData initMedia(Map<String, Integer> md5LenghtLength, String fileName) {
		// 构造请求
		Entry<String, Integer> entry = md5LenghtLength.entrySet().iterator().next();
		WeiboMediaInitRequest weiboMediaInitRequest = new WeiboMediaInitRequest();
		weiboMediaInitRequest.setDspid(dspid);
		weiboMediaInitRequest.setToken(token);
		weiboMediaInitRequest.setName(fileName);
		weiboMediaInitRequest.setCheck(entry.getKey());
		try {
			weiboMediaInitRequest.setLength(entry.getValue());
		} catch (Exception e) {
			LOGGER.info("系统异常");
			return null;
		}

		// 发送请求
		String requestJson = JSON.toJSONString(weiboMediaInitRequest);
		LOGGER.info("weiboMediaInitRequest: " + requestJson);
		String responseJson = HttpUtils.post(meidaInitUrl, requestJson);
		LOGGER.info("weiboMediaInitResponse Info: " + responseJson);

		// 处理返回的结果
		if (!StringUtils.isBlank(responseJson)) {
			WeiboResponse weiboResponse = JSON.parseObject(responseJson, WeiboResponse.class);
			if (WeiboConstant.RESPONSE_SUCCESS.getValue() == weiboResponse.getRet_code().intValue() && WeiboErrorCode.WEC000.getValue() == weiboResponse.getErr_code().intValue()) {
				WeiboData data = JSONObject.parseObject(weiboResponse.getRet_msg().toString(), WeiboData.class);
				return data;
			} else {
				LOGGER.info(weiboResponse.getRet_msg());
			}
		}

		return null;
	}

	/**
	 * 获取流文件内容
	 * 
	 * @param filePath
	 * @return
	 */
	private InputStream getStreamContent(String filePath) {
		GetMethod getMethod = new GetMethod(filePath);
		InputStream inputStream = null;
		try {
			if (httpClient.executeMethod(getMethod) == HttpStatus.SC_OK) {
				inputStream = getMethod.getResponseBodyAsStream();
			}
		} catch (Exception e) {
			LOGGER.info("获取视频出现异常-" + e.getMessage());
		}
		return inputStream;
	}
}
