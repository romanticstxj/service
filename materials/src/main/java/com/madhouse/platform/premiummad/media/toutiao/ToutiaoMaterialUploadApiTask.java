package com.madhouse.platform.premiummad.media.toutiao;

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
import com.madhouse.platform.premiummad.constant.Layout;
import com.madhouse.platform.premiummad.constant.MaterialStatusCode;
import com.madhouse.platform.premiummad.constant.MediaMapping;
import com.madhouse.platform.premiummad.dao.MaterialMapper;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.media.toutiao.constant.ToutiaoConstant;
import com.madhouse.platform.premiummad.media.toutiao.request.ToutiaoMaterialUploadRequest;
import com.madhouse.platform.premiummad.media.toutiao.response.ToutiaoMaterialUploadResponse;
import com.madhouse.platform.premiummad.media.toutiao.util.ToutiaoHttpUtil;
import com.madhouse.platform.premiummad.model.MaterialAuditResultModel;
import com.madhouse.platform.premiummad.service.IMaterialService;
import com.madhouse.platform.premiummad.util.StringUtils;

@Component
public class ToutiaoMaterialUploadApiTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(ToutiaoMaterialUploadApiTask.class);

	@Value("${toutiao.uploadMaterialUrl}")
	private String uploadMaterialUrl;
	
	@Value("${toutiao_logickey_ios_1}")
	private String toutiao_logickey_ios_1;
	@Value("${toutiao_logickey_ios_2}")
	private String toutiao_logickey_ios_2;

	@Autowired
    private ToutiaoHttpUtil toutiaoHttpUtil;
	
	@Autowired
	private MaterialMapper materialDao;
	
	@Autowired
	private IMaterialService materialService;	
	
	public void uploadMaterial() {
		LOGGER.info("++++++++++Toutiao upload material begin+++++++++++");
		// 查询所有待审核且媒体的素材的审核状态是媒体审核的
		List<Material> unSubmitMaterials = materialDao.selectMediaMaterials(MediaMapping.TOUTIAO.getValue(), MaterialStatusCode.MSC10002.getValue());
		if (unSubmitMaterials == null || unSubmitMaterials.isEmpty()) {
			LOGGER.info("今日头条没有未上传的广告主");
			LOGGER.info("++++++++++Toutiao upload material end+++++++++++");
			return;
		}
		
		// 上传到媒体
		LOGGER.info("ToutiaoMaterialUploadApiTask-Toutiao", unSubmitMaterials.size());
		
	    List<MaterialAuditResultModel> rejusedMaterials = new ArrayList<MaterialAuditResultModel>();
		Map<Integer, String[]> materialIdKeys = new HashMap<Integer, String[]>();
		for (Material material : unSubmitMaterials) {
			List<ToutiaoMaterialUploadRequest> list = buildMaterialRequest(material);
			String postResult = toutiaoHttpUtil.post(uploadMaterialUrl, list);
			LOGGER.info("response:" + postResult);
			if (!StringUtils.isEmpty(postResult)) {
				LOGGER.info("头条response{}",postResult);
				Object object = JSON.parse(postResult);
				JSONObject jsonObject = (JSONObject)object;
				if (null != jsonObject.get("error")) {
					// 失败
					LOGGER.error("素材[materialId=" + material.getId() + "]上传失败-" + (String) jsonObject.get("error"));
				} else if (null != jsonObject.get("success_ad_ids")) {
					List<ToutiaoMaterialUploadResponse> responseList = JSON.parseArray(jsonObject.get("success_ad_ids").toString(), ToutiaoMaterialUploadResponse.class);
					ToutiaoMaterialUploadResponse response = responseList.get(0);
					if (response.getAdid() != null && response.getStatus().equals(ToutiaoConstant.M_STATUS_SUCCESS.getDescription())) {
						LOGGER.info("头条物料上传成功");
						String[] mediaQueryAndMaterialKeys = {response.getAdid(), response.getAdid()};
						materialIdKeys.put(material.getId(), mediaQueryAndMaterialKeys);
					} else if (response.getAdid() != null && response.getStatus().equals(ToutiaoConstant.M_STATUS_FAIL.getDescription())) {
						LOGGER.info("头条物料上传失败-" + ToutiaoHttpUtil.unicodeToString(response.getMsg()));
						if (!StringUtils.isBlank(response.getMsg())) {
							MaterialAuditResultModel rejuseItem = new MaterialAuditResultModel();
							rejuseItem.setId(String.valueOf(material.getId()));
							rejuseItem.setStatus(MaterialStatusCode.MSC10001.getValue());
							rejuseItem.setMediaId(String.valueOf(MediaMapping.TOUTIAO.getValue()));
							rejuseItem.setErrorMessage(ToutiaoHttpUtil.unicodeToString(response.getMsg()));
							rejusedMaterials.add(rejuseItem);
						}
					} else {
						LOGGER.error("头条物料上传失败-" + postResult);
					}
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

		LOGGER.info("++++++++++Toutiao upload material end+++++++++++");
	}
	
	/**
	 * 构建上传物料 request 
	 * 
	 * @param material
	 * @return
	 */
	private List<ToutiaoMaterialUploadRequest> buildMaterialRequest(Material material) {
		List<ToutiaoMaterialUploadRequest> list = new ArrayList<ToutiaoMaterialUploadRequest>();
		ToutiaoMaterialUploadRequest request = new ToutiaoMaterialUploadRequest();
		// 素材的图片地址
		request.setImg_url(material.getAdMaterials().split("\\|")[0]);
		// 广告类型
		request.setAd_type(getMediaAdType(material.getLayout().intValue(), material.getSize()));
		// 获胜的 url
		request.setNurl(ToutiaoConstant.NURL.getDescription().replace("{adspaceid}", getMediaNurl(request.getAd_type())));
		request.setAdid(material.getId() + String.valueOf(material.getAdspaceId()) + 1);
		request.setHeight(Integer.valueOf(material.getSize().split("\\*")[1]));
		request.setWidth(Integer.valueOf(material.getSize().split("\\*")[0]));
		// 素材的落地页，以审核时提交为准
		request.setClick_through_url(material.getLpgUrl());
		// 素材的标题
		request.setTitle(material.getTitle());
		// 素材的来源
		request.setSource(material.getDescription());
		request.setIs_inapp(1);
		// 点击监测 url
		if (material.getImpUrls() != null && !material.getImpUrls().isEmpty()) {
			List<String> clickUrls = new ArrayList<String>();
			for (String url : material.getClkUrls().split("\\|")) {
				clickUrls.add(url);
			}
			request.setClick_url(clickUrls);
		}
		// 展示监测 url
		List<String> showUrls = new ArrayList<String>();
		if (material.getImpUrls() != null && !material.getImpUrls().isEmpty()) {
			// 素材表里以 |分割  -> startDelay1~url1|startDelay2~url2
			String[] impTrackUrlArray = material.getImpUrls().split("\\|");
			if (impTrackUrlArray != null) {
				for (int i = 0; i < impTrackUrlArray.length; i++) {
					String[] track = impTrackUrlArray[i].split("~");
					showUrls.add(track[1]);
				}
			}
		}
		showUrls.add(ToutiaoConstant.IMP_MONITOR_PM.getDescription());
		request.setShow_url(showUrls);

		list.add(request);
		return list;
	}
	
	/**
	 * 与媒体方获胜的 url
	 *
	 * @param mediaAdType
	 * @return
	 */
	private String getMediaNurl(Integer mediaAdType) {
		if (mediaAdType.intValue() == ToutiaoConstant.TOUTIAO_FEED_LP_LARGE.getValue()) {
			return toutiao_logickey_ios_1;
		}
		if (mediaAdType.intValue() == ToutiaoConstant.OUTIAO_FEED_LP_SMALL.getValue()) {
			return toutiao_logickey_ios_2;
		}
		return null;
	}
	
	/**
	 * 根据广告形式和尺寸获取媒体方的广告类型
	 * 
	 * @param layout
	 * @param size
	 * @return
	 */
	private int getMediaAdType(Integer layout, String size) {
		// 图文信息流
		if (Layout.LO30001.getValue() == layout) {
			// 690*286
			if (ToutiaoConstant.TOUTIAO_FEED_LP_LARGE.getDescription().equals(size)) {
				return ToutiaoConstant.TOUTIAO_FEED_LP_LARGE.getValue();
			}
			if (ToutiaoConstant.OUTIAO_FEED_LP_SMALL.getDescription().equals(size)) {
				return ToutiaoConstant.OUTIAO_FEED_LP_SMALL.getValue();
			}
		}
		return 0;
	}
}
