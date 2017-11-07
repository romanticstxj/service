package com.madhouse.platform.premiummad.media.momo;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
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
import com.madhouse.platform.premiummad.media.momo.constant.MomoConstant;
import com.madhouse.platform.premiummad.media.momo.constant.MomoIndustryMapping;
import com.madhouse.platform.premiummad.media.momo.request.MomoUploadRequest;
import com.madhouse.platform.premiummad.media.momo.request.MomoUploadRequest.NativeCreativeBean.ImageBean;
import com.madhouse.platform.premiummad.media.momo.response.MomoUploadResponse;
import com.madhouse.platform.premiummad.media.momo.util.MomoHttpUtils;
import com.madhouse.platform.premiummad.model.MaterialAuditResultModel;
import com.madhouse.platform.premiummad.service.IMaterialService;
import com.madhouse.platform.premiummad.service.IMediaService;
import com.madhouse.platform.premiummad.util.StringUtils;

@Component
public class MomoMaterialUploadApiTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(MomoMaterialUploadApiTask.class);

	@Value("${momo_upload_url}")
	private String uploadMaterialUrl;

	@Value("${momo_upload_dspid}")
	private String dspId;

	@Value("${momo_upload_appkey}")
	private String appkey;

	@Value("${material_meidaGroupMapping_momo}")
	private String mediaGroupStr;
	
	@Autowired
	private MomoHttpUtils momoHttpUtil;

	@Autowired
	private MaterialMapper materialDao;

	@Autowired
	private IMaterialService materialService;

	@Autowired
	private AdvertiserMapper advertiserDao;

	@Autowired
	private IMediaService mediaService;
	
	/**
	 * 支持的广告形式
	 */
	private static Set<String> supportedLayoutSet;

	static {
		supportedLayoutSet = new HashSet<String>();
		supportedLayoutSet.add(String.valueOf(Layout.LO30001.getValue()) + "(" + MomoConstant.FEED_LANDING_PAGE_LARGE_IMG.getDescription() + ")"); // FEED_LANDING_PAGE_LARGE_IMG(大图样式落地页广告)
		supportedLayoutSet.add(String.valueOf(Layout.LO30001.getValue()) + "(" + MomoConstant.FEED_LANDING_PAGE_SQUARE_IMG.getDescription() + ")"); // FEED_LANDING_PAGE_LARGE_IMG(大图样式落地页广告)
		supportedLayoutSet.add(String.valueOf(Layout.LO30001.getValue()) + "(" + MomoConstant.NEARBY_LANDING_PAGE_NO_IMG.getDescription() + ")"); // NEARBY_LANDING_PAGE_NO_IMG(图标样式落地页广告)
		supportedLayoutSet.add(String.valueOf(Layout.LO30003.getValue()));// FEED_LANDING_PAGE_SMALL_IMG(三图样式落地页广告)
		supportedLayoutSet.add(String.valueOf(Layout.LO30011.getValue())); // FEED_LANDING_PAGE_VIDEO(横版视频落地页广告)
	}

	/**
	 * 上传物料
	 */
	public void uploadMaterial() {
		LOGGER.info("++++++++++Momo upload material begin+++++++++++");

		/* 代码处理方式
		// 媒体组没有映射到具体的媒体不处理
		String value = MediaTypeMapping.getValue(MediaTypeMapping.MOMO.getGroupId());
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
			LOGGER.info("Momo没有未上传的素材");
			return;
		}

		// 上传到媒体
		LOGGER.info("MomoMaterialUploadApiTask-Momo", unSubmitMaterials.size());

		List<MaterialAuditResultModel> rejusedMaterials = new ArrayList<MaterialAuditResultModel>();
		Map<Integer, String[]> materialIdKeys = new HashMap<Integer, String[]>();
		for (Material material : unSubmitMaterials) {
			// 校验广告形式是否支持,不支持自动驳回
			String size = material.getLayout().intValue() == 301 ? "(" + material.getSize() +")" : "";
			if (!(supportedLayoutSet.contains(Integer.valueOf(material.getLayout()) + size))) {
				MaterialAuditResultModel rejuseItem = new MaterialAuditResultModel();
				rejuseItem.setId(String.valueOf(material.getId()));
				rejuseItem.setStatus(MaterialStatusCode.MSC10001.getValue());
				rejuseItem.setMediaIds(mediaIds);
				rejuseItem.setErrorMessage("媒体只支持如下广告形式：" + Arrays.toString(supportedLayoutSet.toArray()));
				rejusedMaterials.add(rejuseItem);
				LOGGER.error(rejuseItem.getErrorMessage());
				continue;
			}

			MomoUploadRequest request = new MomoUploadRequest();
			String errorMsg = buildUploadMaterialRequest(material, request);
			if (!StringUtils.isBlank(errorMsg)) {
				// 构建请求异常情况自动驳回
				MaterialAuditResultModel rejuseItem = new MaterialAuditResultModel();
				rejuseItem.setId(String.valueOf(material.getId()));
				rejuseItem.setStatus(MaterialStatusCode.MSC10001.getValue());
				rejuseItem.setMediaIds(mediaIds);
				rejuseItem.setErrorMessage(errorMsg);
				rejusedMaterials.add(rejuseItem);
				continue;
			}
			
			// 向媒体发请求
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("data", JSON.toJSONString(request));
			String responseJson = momoHttpUtil.post(uploadMaterialUrl, paramMap);
			if (!StringUtils.isEmpty(responseJson)) {
				MomoUploadResponse response = JSON.parseObject(responseJson, MomoUploadResponse.class);
				// 200：成功
				if (response.getEc() == 200) {
					String[] mediaQueryAndMaterialKeys = {material.getMediaQueryKey()};
					materialIdKeys.put(material.getId(), mediaQueryAndMaterialKeys);
				} else {
					// 自动驳回
					MaterialAuditResultModel rejuseItem = new MaterialAuditResultModel();
					rejuseItem.setId(String.valueOf(material.getId()));
					rejuseItem.setStatus(MaterialStatusCode.MSC10001.getValue());
					rejuseItem.setMediaIds(mediaIds);
					rejuseItem.setErrorMessage(response.getEm());
					rejusedMaterials.add(rejuseItem);
					LOGGER.error("素材[materialId=" + material.getId() + "]上传失败-" + response.getEm());
				}
			} else {
				LOGGER.error("素材[materialId=" + material.getId() + "]上传失败");
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

		LOGGER.info("++++++++++Momo upload material end+++++++++++");
	}

	/**
	 * 构建请求参数
	 * 
	 * @return errorMsg
	 * @throws Exception
	 */
	private String buildUploadMaterialRequest(Material material, MomoUploadRequest request) {
		// 获取广告主
		String[] advertiserKeys = { material.getAdvertiserKey() };
		List<Advertiser> advertisers = advertiserDao.selectByAdvertiserKeysAndDspId(advertiserKeys, String.valueOf(material.getDspId()), material.getMediaId());
		if (advertisers == null || advertisers.isEmpty()) {
			LOGGER.error("广告主不存在[advertiserKey=" + material.getAdvertiserKey() + "dspId=" + material.getDspId() + "]");
			return "广告主不存在[advertiserKey=" + material.getAdvertiserKey() + "]";
		}

		String crid = StringUtils.getMD5(material.getId().toString());
		material.setMediaQueryKey(crid);
		request.setDspid(dspId);
		request.setCid(crid);// 活动ID
		request.setAdid(crid); // 广告ID
		request.setCrid(crid);// 物料ID

		request.setAdvertiser_id(advertisers.get(0).getId().toString()); // 广告主ID
		request.setAdvertiser_name(advertisers.get(0).getAdvertiserName()); // 广告主名称
		request.setQuality_level(1);
		List<String> cats = new ArrayList<String>();
		cats.add(String.valueOf(MomoIndustryMapping.getMediaIndustryId(advertisers.get(0).getIndustry())));
		request.setCat(cats); // 行业类目 

		MomoUploadRequest.NativeCreativeBean creativeBean = new MomoUploadRequest.NativeCreativeBean();
		MomoUploadRequest.NativeCreativeBean.ImageBean imageBean = new MomoUploadRequest.NativeCreativeBean.ImageBean();
		MomoUploadRequest.NativeCreativeBean.LogoBean logoBean = new MomoUploadRequest.NativeCreativeBean.LogoBean();
		MomoUploadRequest.NativeCreativeBean.VideoBean videoBean = new MomoUploadRequest.NativeCreativeBean.VideoBean();
		
		// 根据我方广告形式对应媒体的广告类型
		if (material.getLayout().intValue() == Layout.LO30001.getValue()) {
			// FEED_LANDING_PAGE_LARGE_IMG(大图样式落地页广告) or FEED_LANDING_PAGE_SQUARE_IMG(单图样式落地页广告)
			imageBean.setUrl(material.getAdMaterials().split("\\|")[0]);
			imageBean.setHeight(Integer.valueOf(material.getSize().split("\\*")[1]));
			imageBean.setWidth(Integer.valueOf(material.getSize().split("\\*")[0]));
			creativeBean.setImage(Collections.singletonList(imageBean));

			// 广告样式
			if (MomoConstant.FEED_LANDING_PAGE_LARGE_IMG.getDescription().equals(material.getSize())) {
				creativeBean.setNative_format("FEED_LANDING_PAGE_LARGE_IMG");
			} else if (MomoConstant.FEED_LANDING_PAGE_SQUARE_IMG.getDescription().equals(material.getSize())) {
				creativeBean.setNative_format("FEED_LANDING_PAGE_SQUARE_IMG");
			}
		} else if (material.getLayout().intValue() == Layout.LO30003.getValue()) {
			// FEED_LANDING_PAGE_SMALL_IMG 三图样式落地页广告
			List<MomoUploadRequest.NativeCreativeBean.ImageBean> imageBeans = new ArrayList<MomoUploadRequest.NativeCreativeBean.ImageBean>();
			String[] adms = material.getAdMaterials().split("\\|");
			for (int i = 0; i < adms.length; i++) {
				MomoUploadRequest.NativeCreativeBean.ImageBean item = new MomoUploadRequest.NativeCreativeBean.ImageBean();
				item.setUrl(adms[i]);
				item.setHeight(Integer.valueOf(material.getSize().split("\\*")[1]));
				item.setWidth(Integer.valueOf(material.getSize().split("\\*")[0]));
				imageBeans.add(item);
				// 三图只取三张
				if (imageBeans.size() == 3) {
					break;
				}
			}
			creativeBean.setImage(imageBeans);
			creativeBean.setNative_format("FEED_LANDING_PAGE_SMALL_IMG");
		} else if (material.getLayout().intValue() == Layout.LO30011.getValue()) {
			// FEED_LANDING_PAGE_VIDEO 横版视频落地页广告
			imageBean.setUrl(material.getCover());
			imageBean.setHeight(Integer.valueOf(material.getSize().split("\\*")[1]));
			imageBean.setWidth(Integer.valueOf(material.getSize().split("\\*")[0]));
			videoBean.setCover_img(imageBean);

			videoBean.setUrl(material.getAdMaterials().split("\\|")[0]);
			creativeBean.setVideo(Collections.singletonList(videoBean));
			creativeBean.setNative_format("FEED_LANDING_PAGE_VIDEO"); // 广告样式
			creativeBean.setCard_title(material.getTitle());
			creativeBean.setCard_desc(material.getDescription());

			ImageBean card_img = new ImageBean();
			card_img.setHeight(150);
			card_img.setWidth(150);
			card_img.setUrl(material.getIcon());
			creativeBean.setCard_img(card_img);
		}
		creativeBean.setLandingpage_url(material.getLpgUrl());// 落地页
		creativeBean.setTitle(material.getBrand());
		creativeBean.setDesc(material.getContent());

		// LOGO (图标样式落地页广告 LOGO 从 素材URL获取)
		if (material.getLayout().intValue() == Layout.LO30001.getValue() && MomoConstant.NEARBY_LANDING_PAGE_NO_IMG.getDescription().equals(material.getSize())) {
			creativeBean.setNative_format("NEARBY_LANDING_PAGE_NO_IMG");
			logoBean.setUrl(material.getAdMaterials().split("\\|")[0]);
			logoBean.setHeight(Integer.valueOf(material.getSize().split("\\*")[1]));
			logoBean.setWidth(Integer.valueOf(material.getSize().split("\\*")[0]));
		} else {
			logoBean.setUrl(material.getIcon());
			logoBean.setHeight(150);
			logoBean.setWidth(150);
		}
		creativeBean.setLogo(logoBean);

		request.setNative_creative(creativeBean);
		request.setUptime(System.currentTimeMillis() / 1000);
		request.setExpiry_date(get120DaysLater());
		String sign = "appkey" + appkey + "crid" + crid + "uptime" + request.getUptime();
		try {
			request.setSign(getMd5(sign));
		} catch (Exception e) {
			LOGGER.info("上传素材时出现异常[materailId=" + material.getId() + "-" + e.getMessage());
		}
		
		return "";
	}

	/**
	 * 获取120后的日期
	 * 
	 * @return
	 */
	private String get120DaysLater() {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(new Date().getTime());
		c.add(Calendar.DATE, 120);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return sdf.format(new Date(c.getTimeInMillis()));
	}

	private String getMd5(String str) throws Exception {
		StringBuilder hexString = new StringBuilder();
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] temp = md.digest(str.getBytes());
		for (int i = 0; i < temp.length; i++) {
			String hex = Integer.toString(temp[i] & 0xFF, 16);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			hexString.append(hex.toUpperCase());
		}
		return hexString.toString();
	}
}
