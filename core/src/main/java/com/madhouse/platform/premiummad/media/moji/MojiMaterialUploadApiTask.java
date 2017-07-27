package com.madhouse.platform.premiummad.media.moji;

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
import com.madhouse.platform.premiummad.dao.MaterialMapper;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.media.constant.IMojiConstant;
import com.madhouse.platform.premiummad.media.model.MojiMaterialUploadRequest;
import com.madhouse.platform.premiummad.media.model.MojiMaterialUploadResponse;
import com.madhouse.platform.premiummad.media.util.MojiHttpUtil;
import com.madhouse.platform.premiummad.media.util.Sha1;
import com.madhouse.platform.premiummad.service.IMaterialService;
import com.madhouse.platform.premiummad.util.StringUtils;

@Component
public class MojiMaterialUploadApiTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(MojiMaterialUploadApiTask.class);

	@Value("#{'${moji.dataSource}'.split(',')}")
	private List<String> dataSourceList;

	@Value("${moji.uploadMaterialUrl}")
	private String uploadMaterialUrl;

	@Value("${moji.source}")
	private String source;

	@Value("${moji.secret}")
	private String secret;

	// madhouse广告位
	@Value("${mh_moji_mapping_101_android}")
	private String mh_moji_mapping_101_android;
	@Value("${mh_moji_mapping_101_ios}")
	private String mh_moji_mapping_101_ios;

	@Value("${mh_moji_mapping_1008_android}")
	private String mh_moji_mapping_1008_android;
	@Value("${mh_moji_mapping_1008_ios}")
	private String mh_moji_mapping_1008_ios;

	@Value("${mh_moji_mapping_2002_android}")
	private String mh_moji_mapping_2002_android;
	@Value("${mh_moji_mapping_2002_ios}")
	private String mh_moji_mapping_2002_ios;

	// 墨迹广告位
	@Value("${moji_ad_splash_101}")
	private String moji_ad_splash_101;
	@Value("${moji_ad_banner_1008}")
	private String moji_ad_banner_1008;
	@Value("${moji_ad_banner_2002}")
	private String moji_ad_banner_2002;

	@Autowired
	private MojiHttpUtil mojiHttpUtil;

	@Autowired
	private Sha1 sha1;

	@Autowired
	private MaterialMapper materialDao;

	@Autowired
	private IMaterialService materialService;

	public void uploadMaterial() {
		LOGGER.info("++++++++++moji get material status begin+++++++++++");

		// 查询所有待审核且媒体的素材的审核状态是媒体审核的
		List<Material> unSubmitMaterials = materialDao.selectMediaMaterials(MediaMapping.DIANPING.getValue(), MaterialStatusCode.MSC10002.getValue());
		if (unSubmitMaterials == null || unSubmitMaterials.isEmpty()) {
			LOGGER.info("墨迹天气没有未上传的广告主");
			LOGGER.info("++++++++++moji upload material end+++++++++++");
			return;
		}
		// 上传到媒体
		LOGGER.info("MojiMaterialUploadApiTask-moji", unSubmitMaterials.size());

		Map<Integer, String> materialIdKeys = new HashMap<Integer, String>();
		for (Material material : unSubmitMaterials) {
			Map<String, String> paramMap = buildUploadMaterialRequest(material);
			String postResult = mojiHttpUtil.post(uploadMaterialUrl, paramMap);
			if (!StringUtils.isEmpty(postResult)) {
				MojiMaterialUploadResponse response = JSON.parseObject(postResult, MojiMaterialUploadResponse.class);
				// 上传成功，返回200
				if (response.getCode().equals(IMojiConstant.M_STATUS_SUCCESS.getValue() + "")) {
					materialIdKeys.put(material.getId(), response.getData().getId());
				} else {
					LOGGER.error("素材[materialId=" + material.getId() + "]上传失败-" + response.getCode() + " " + response.getMessage());
				}
			} else {
				LOGGER.error("素材[materialId=" + material.getId() + "]上传失败");
			}
		}

		// 更新我方素材信息
		if (!materialIdKeys.isEmpty()) {
			materialService.updateStatusAfterUpload(materialIdKeys);
		}

		LOGGER.info("++++++++++moji get material status end+++++++++++");
	}

	/**
	 * 构建请求参数
	 * 
	 * @param material
	 * @return
	 */
	private Map<String, String> buildUploadMaterialRequest(Material material) {
		// 封装request对象
		MojiMaterialUploadRequest request = new MojiMaterialUploadRequest();
		// 客户标识
		request.setSource(source);
		// 当前时间10位时间戳
		request.setTime_stamp(Integer.parseInt(System.currentTimeMillis() / 1000 + ""));
		// 广告位id required
		request.setPosition_ids(getMediaAdspaceId(material.getAdspaceId()));
		// 广告类型
		request.setAd_type(convertMediaAdType(material.getAdspaceId()));
		// 广告样式
		request.setShow_type(IMojiConstant.SHOW_TYPE_1.getValue());
		// 跳转链接
		request.setRedirect_url(material.getLpgUrl());
		// 物料上传地址
		request.setImage_url(material.getAdMaterials());
		// 描述
		request.setDesc(material.getDescription());
		// 标题
		request.setTitle(material.getTitle());

		// 封装成 map
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("source", request.getSource());
		paramMap.put("time_stamp", request.getTime_stamp() + "");
		paramMap.put("position_ids", request.getPosition_ids());
		paramMap.put("ad_type", request.getAd_type() + "");
		paramMap.put("show_type", request.getShow_type() + "");
		paramMap.put("redirect_url", request.getRedirect_url());
		paramMap.put("image_url", request.getImage_url());
		paramMap.put("desc", request.getDesc());
		paramMap.put("title", request.getTitle());

		// 签名算法Shar1
		String sign = sha1.SHA1(paramMap);
		request.setSign(sign);
		paramMap.put("sign", request.getSign());

		return paramMap;
	}

	/**
	 * 根据我方的广告位转换成媒体方的广告类型
	 * 
	 * @param adspaceId
	 * @return
	 */
	private int convertMediaAdType(int adspaceId) {
		if (String.valueOf(adspaceId).equals(mh_moji_mapping_101_android) || String.valueOf(adspaceId).equals(mh_moji_mapping_101_ios)) {// 点评闪惠交易成功页
			return IMojiConstant.MOJI_SPLASH.getValue();
		} else if (String.valueOf(adspaceId).equals(mh_moji_mapping_1008_android) || String.valueOf(adspaceId).equals(mh_moji_mapping_1008_ios)) {// 点评电影交易成功页
			return IMojiConstant.MOJI_BANNER.getValue();
		} else if (String.valueOf(adspaceId).equals(mh_moji_mapping_2002_android) || String.valueOf(adspaceId).equals(mh_moji_mapping_2002_ios)) {// 点评电影票详情页
			return IMojiConstant.MOJI_BANNER.getValue();
		}
		return 0;
	}

	/**
	 * 获取媒体方的广告位
	 * 
	 * @param adspaceId
	 * @return
	 */
	private String getMediaAdspaceId(int adspaceId) {
		if (String.valueOf(adspaceId).equals(mh_moji_mapping_101_android) || String.valueOf(adspaceId).equals(mh_moji_mapping_101_ios)) {// 点评闪惠交易成功页
			return moji_ad_splash_101;
		} else if (String.valueOf(adspaceId).equals(mh_moji_mapping_1008_android) || String.valueOf(adspaceId).equals(mh_moji_mapping_1008_ios)) {// 点评电影交易成功页
			return moji_ad_banner_1008;
		} else if (String.valueOf(adspaceId).equals(mh_moji_mapping_2002_android) || String.valueOf(adspaceId).equals(mh_moji_mapping_2002_ios)) {// 点评电影票详情页
			return moji_ad_banner_2002;
		}
		return "";
	}
}
