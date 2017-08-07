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
import com.madhouse.platform.premiummad.constant.MaterialStatusCode;
import com.madhouse.platform.premiummad.constant.MediaMapping;
import com.madhouse.platform.premiummad.dao.MaterialMapper;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.media.constant.IToutiaoConstant;
import com.madhouse.platform.premiummad.media.model.ToutiaoMaterialUploadRequest;
import com.madhouse.platform.premiummad.media.model.ToutiaoMaterialUploadResponse;
import com.madhouse.platform.premiummad.media.util.ToutiaoHttpUtil;
import com.madhouse.platform.premiummad.service.IMaterialService;
import com.madhouse.platform.premiummad.util.StringUtils;

@Component
public class ToutiaoMaterialUploadApiTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(ToutiaoMaterialUploadApiTask.class);

	@Value("${toutiao.uploadMaterialUrl}")
	private String uploadMaterialUrl;

	@Value("${mh_toutiao_mapping_ios_1}")
	private String mh_toutiao_mapping_ios_1;
	@Value("${mh_toutiao_mapping_android_1}")
	private String mh_toutiao_mapping_android_1;

	@Value("${mh_toutiao_mapping_ios_2}")
	private String mh_toutiao_mapping_ios_2;
	@Value("${mh_toutiao_mapping_android_2}")
	private String mh_toutiao_mapping_android_2;
	
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
		
		Map<Integer, String> materialIdKeys = new HashMap<Integer, String>();
		for (Material material : unSubmitMaterials) {
			List<ToutiaoMaterialUploadRequest> list = buildMaterialRequest(material);
			String postResult =toutiaoHttpUtil.post(uploadMaterialUrl,list);
			if (!StringUtils.isEmpty(postResult)) {
				LOGGER.info("头条response{}",postResult);
				Object object = JSON.parse(postResult);
				JSONObject jsonObject = (JSONObject)object;
				if (null != jsonObject.get("error")) {
					// 失败
					LOGGER.error("素材[materialId=" + material.getId() + "]上传失败-" + (String) jsonObject.get("error"));
				}else if(null !=jsonObject.get("success_ad_ids")){
					 List<ToutiaoMaterialUploadResponse> responseList = JSON.parseArray(jsonObject.get("success_ad_ids").toString(), ToutiaoMaterialUploadResponse.class);
					 ToutiaoMaterialUploadResponse response = responseList.get(0);
					 if(response.getAdid() != null && response.getStatus().equals(IToutiaoConstant.M_STATUS_SUCCESS.getDescription())){
       					LOGGER.info("头条物料上传成功");
       					// TODO
       					materialIdKeys.put(material.getId(), response.getAdid());
					 }
				}
			}
		}

		// 更新我方素材信息
		if (!materialIdKeys.isEmpty()) {
			materialService.updateStatusAfterUpload(materialIdKeys);
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
		request.setImg_url(material.getAdMaterials());
		// 广告类型
		request.setAd_type(getMediaAdType(material.getAdspaceId()));
		// 获胜的 url
		request.setNurl(IToutiaoConstant.NURL.getDescription().replace("{adspaceid}", getMediaNurl(material.getAdspaceId())));
		request.setAdid(material.getId() + String.valueOf(material.getAdspaceId()) + 1);
		request.setHeight(Integer.valueOf(material.getSize().split("*")[1]));
		request.setWidth(Integer.valueOf(material.getSize().split("*")[0]));
		// 素材的落地页，以审核时提交为准
		request.setClick_through_url(material.getLpgUrl());
		// 素材的标题
		request.setTitle(material.getTitle());
		// 素材的来源
		request.setSource(material.getDescription());
		request.setIs_inapp(1);
		request.setClick_url(null); // TODO
		// 展示监测 ur
		List<String> show_url = new ArrayList<String>();
		show_url.add(IToutiaoConstant.IMP_MONITOR_PM.getDescription());// 展示
		request.setShow_url(show_url);
		list.add(request);
		return list;
	}
	
	/**
	 * 与媒体方获胜的 url
	 * 一个物料对应madhouse2个广告位，对应头条一个广告位,一个物料只审核一次
	 * 
	 * @param dbAdspaceId
	 * @return
	 */
	private String getMediaNurl(Integer dbAdspaceId) {
		if (String.valueOf(dbAdspaceId).equals(mh_toutiao_mapping_ios_1) || String.valueOf(dbAdspaceId).equals(mh_toutiao_mapping_android_1)) {
			return toutiao_logickey_ios_1;
		}
		if (String.valueOf(dbAdspaceId).equals(mh_toutiao_mapping_ios_2) || String.valueOf(dbAdspaceId).equals(mh_toutiao_mapping_android_2)) {
			return toutiao_logickey_ios_2;
		}
		return null;
	}
	
	/**
	 * 获取媒体方的广告类型
	 * 
	 * @param dbAdspaceId
	 * @return
	 */
	private int getMediaAdType(Integer dbAdspaceId) {
		if (String.valueOf(dbAdspaceId).equals(mh_toutiao_mapping_ios_1) || String.valueOf(dbAdspaceId).equals(mh_toutiao_mapping_android_1)) {
			return IToutiaoConstant.TOUTIAO_FEED_LP_LARGE.getValue();
		}
		if (String.valueOf(dbAdspaceId).equals(mh_toutiao_mapping_ios_2) || String.valueOf(dbAdspaceId).equals(mh_toutiao_mapping_android_2)) {
			return IToutiaoConstant.OUTIAO_FEED_LP_SMALL.getValue();
		}
		return 0;
	}
}
