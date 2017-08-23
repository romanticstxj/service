package com.madhouse.platform.premiummad.media.momo;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.constant.MaterialStatusCode;
import com.madhouse.platform.premiummad.constant.MediaMapping;
import com.madhouse.platform.premiummad.dao.AdvertiserMapper;
import com.madhouse.platform.premiummad.dao.MaterialMapper;
import com.madhouse.platform.premiummad.entity.Advertiser;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.media.momo.constant.MomoIndustryMapping;
import com.madhouse.platform.premiummad.media.momo.request.MomoUploadRequest;
import com.madhouse.platform.premiummad.media.momo.request.MomoUploadRequest.NativeCreativeBean.ImageBean;
import com.madhouse.platform.premiummad.media.momo.response.MomoUploadResponse;
import com.madhouse.platform.premiummad.media.momo.util.MomoHttpUtils;
import com.madhouse.platform.premiummad.model.MaterialAuditResultModel;
import com.madhouse.platform.premiummad.service.IMaterialService;
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

	@Autowired
	private MomoHttpUtils momoHttpUtil;

	@Autowired
	private MaterialMapper materialDao;

	@Autowired
	private IMaterialService materialService;

	@Autowired
	private AdvertiserMapper advertiserDao;

	/**
	 * 上传物料
	 */
	public void uploadMaterial() {
		LOGGER.info("++++++++++Momo upload material begin+++++++++++");
		// 查询所有待审核且媒体的素材的审核状态是媒体审核的
		List<Material> unSubmitMaterials = materialDao.selectMediaMaterials(MediaMapping.MOMO.getValue(), MaterialStatusCode.MSC10002.getValue());
		if (unSubmitMaterials == null || unSubmitMaterials.isEmpty()) {
			LOGGER.info(MediaMapping.MOMO.getDescrip() + "没有未上传的素材");
			LOGGER.info("++++++++++Momo upload material end+++++++++++");
			return;
		}

		// 上传到媒体
		LOGGER.info("MomoMaterialUploadApiTask-Momo", unSubmitMaterials.size());

		List<MaterialAuditResultModel> rejusedMaterials = new ArrayList<MaterialAuditResultModel>();
		Map<Integer, String[]> materialIdKeys = new HashMap<Integer, String[]>();
		for (Material material : unSubmitMaterials) {
			MomoUploadRequest request = new MomoUploadRequest();
			String errorMsg = buildUploadMaterialRequest(material, request);
			if (!StringUtils.isBlank(errorMsg)) {
				// 广告主不存在自动驳回
				MaterialAuditResultModel rejuseItem = new MaterialAuditResultModel();
				rejuseItem.setId(String.valueOf(material.getId()));
				rejuseItem.setStatus(MaterialStatusCode.MSC10001.getValue());
				rejuseItem.setMediaId(String.valueOf(MediaMapping.MOMO.getValue()));
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
					String[] mediaMaterialIdKeys = {material.getMediaMaterialKey()};
					materialIdKeys.put(material.getId(), mediaMaterialIdKeys);
				} else {
					// 自动驳回
					MaterialAuditResultModel rejuseItem = new MaterialAuditResultModel();
					rejuseItem.setId(String.valueOf(material.getId()));
					rejuseItem.setStatus(MaterialStatusCode.MSC10001.getValue());
					rejuseItem.setMediaId(String.valueOf(MediaMapping.MOMO.getValue()));
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
		material.setMediaMaterialKey(crid);
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
		// 根据后缀判断是图片还是视频
		if (!material.getAdMaterials().endsWith("mp4")) {
			// image
			imageBean.setUrl(material.getAdMaterials());
			imageBean.setHeight(Integer.valueOf(material.getSize().split("\\*")[1]));
			imageBean.setWidth(Integer.valueOf(material.getSize().split("\\*")[0]));
			creativeBean.setImage(Collections.singletonList(imageBean));
			creativeBean.setNative_format("FEED_LANDING_PAGE_LARGE_IMG"); // 广告样式
		} else {
			// video
			imageBean.setUrl(material.getCover());
			imageBean.setHeight(Integer.valueOf(material.getSize().split("\\*")[1]));
			imageBean.setWidth(Integer.valueOf(material.getSize().split("\\*")[0]));
			videoBean.setCover_img(imageBean);

			videoBean.setUrl(material.getAdMaterials());
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
		creativeBean.setDesc(material.getDescription());
		creativeBean.setTitle(material.getTitle());

		// logo
		logoBean.setUrl(material.getIcon());
		logoBean.setHeight(150);
		logoBean.setWidth(150);
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
