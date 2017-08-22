package com.madhouse.platform.premiummad.media.iqiyi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
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
import com.madhouse.platform.premiummad.media.constant.IQiYiConstant;
import com.madhouse.platform.premiummad.media.model.IQiyiUploadMaterialRequest;
import com.madhouse.platform.premiummad.media.model.IQiyiUploadMaterialResponse;
import com.madhouse.platform.premiummad.media.util.IQiYiHttpUtils;
import com.madhouse.platform.premiummad.model.MaterialAuditResultModel;
import com.madhouse.platform.premiummad.service.IMaterialService;
import com.madhouse.platform.premiummad.util.DateUtils;
import com.madhouse.platform.premiummad.util.StringUtils;

@Component("iQiyiMaterialUploadApiTask")
public class IQiyiMaterialUploadApiTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(IQiyiMaterialUploadApiTask.class);

	@Value("${iqiyi.material.create}")
	private String materialCreateUrl;

	//@Value("${file.material.root:/mnt/vda}")
	private String materialFileRoot = "";

	@Autowired
	private IQiYiHttpUtils iQiYiHttpUtils;

	@Autowired
	private MaterialMapper materialDao;

	@Autowired
	private IMaterialService materialService;

	@Autowired
	private AdvertiserMapper advertiserDao;

	/**
	 * 上传广告物料
	 */
	public void uploadMaterial() {
		LOGGER.info("++++++++++iqiyi upload material begin+++++++++++");

		// 查询所有待审核且媒体的素材的审核状态是媒体审核的
		List<Material> unSubmitMaterials = materialDao.selectMediaMaterials(MediaMapping.IQYI.getValue(), MaterialStatusCode.MSC10002.getValue());
		if (unSubmitMaterials == null || unSubmitMaterials.isEmpty()) {
			LOGGER.info("爱奇艺没有未上传的广告主");
			LOGGER.info("++++++++++iqiyi upload material end+++++++++++");
			return;
		}

		// 上传到媒体
		LOGGER.info("IQiyiMaterialUploadApiTask-iqiyi", unSubmitMaterials.size());
		List<MaterialAuditResultModel> rejusedMaterials = new ArrayList<MaterialAuditResultModel>();
		Map<Integer, String> materialIdKeys = new HashMap<Integer, String>();
		for (Material material : unSubmitMaterials) {
			String postResult = handleMaterialRequest(material);
			if (!StringUtils.isEmpty(postResult)) {
				IQiyiUploadMaterialResponse iqiyiUploadMaterialResponse = JSON.parseObject(postResult, IQiyiUploadMaterialResponse.class);
				if (iqiyiUploadMaterialResponse != null) {
					String code = iqiyiUploadMaterialResponse.getCode();
					LOGGER.info("IQiYiUploadMaterial-code:" + code);
					// 上传物料成功
					if (code.equals(String.valueOf(IQiYiConstant.RESPONSE_SUCCESS.getValue()))) {
						materialIdKeys.put(material.getId(), iqiyiUploadMaterialResponse.getM_id());
						LOGGER.info("IQiYiUploadMaterial-Add-thirdResponse: Success,materialId：" + material.getId());
					} else {
						MaterialAuditResultModel rejuseItem = new MaterialAuditResultModel();
						rejuseItem.setId(String.valueOf(material.getId()));
						rejuseItem.setStatus(MaterialStatusCode.MSC10001.getValue());
						rejuseItem.setMediaId(String.valueOf(MediaMapping.IQYI.getValue()));
						rejuseItem.setErrorMessage(iqiyiUploadMaterialResponse.getDesc());
						rejusedMaterials.add(rejuseItem);
						LOGGER.error("素材[materialId=" + material.getId() + "]上传失败-" + iqiyiUploadMaterialResponse.getDesc());
					}
				} else {
					LOGGER.error("素材[materialId=" + material.getId() + "]上传失败");
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
		
		LOGGER.info("++++++++++iqiyi upload material end+++++++++++");
	}

	/**
	 * 处理上传物料api的请求json
	 * 
	 * @param material
	 * @return
	 */
	private String handleMaterialRequest(Material material) {
		// 获取该素材的广告主,若广告主不存在不做处理
		String[] advertiserKeys = { material.getAdvertiserKey() };
		List<Advertiser> advertisers = advertiserDao.selectByAdvertiserKeysAndDspId(advertiserKeys, String.valueOf(material.getDspId()), material.getMediaId());
		if (advertisers == null || advertisers.size() != 1) {
			LOGGER.error("广告主不存在[advertiserKey=" + material.getAdvertiserKey() + "dspId=" + material.getDspId() + "]");
			return null;
		}

		IQiyiUploadMaterialRequest request = new IQiyiUploadMaterialRequest();
		// 媒体方广告主
		request.setAd_id(Integer.valueOf(advertisers.get(0).getMediaAdvertiserKey()));
		// 点击广告后着陆地址
		request.setClick_url(material.getLpgUrl());
		// 物料id
		request.setVideo_id(String.valueOf(material.getId()));
		// 文件名称,名称必须带有效后缀
		String fileName = material.getMaterialName();
		if (material.getAdMaterials().endsWith("\\.jpg")) {
			fileName = fileName + ".jpg";
		} else if (material.getAdMaterials().endsWith("\\.png")) {
			fileName = fileName + ".png";
		} else if (material.getAdMaterials().endsWith("\\.mp4")) {
			fileName = fileName + ".mp4";
		} else if (material.getAdMaterials().endsWith("\\.flv")) {
			fileName = fileName + ".flv";
		}
		request.setFile_name(fileName);
		// 广告投放平台
		request.setPlatform(IQiYiConstant.PLATFORM_MOBILE.getValue());
		request.setDuration(material.getDuration());
		request.setDpi(material.getSize());
		request.setEnd_date(DateUtils.getFormatStringByPattern("yyyyMMdd", material.getEndDate()));
		// 投放方式
		request.setIs_pmp(getMediaDeliveryType(material.getDeliveryType()));

		// 前贴，中贴，后贴：贴片，支持格式：mp4、flv、mov、avi、mpeg、swf、jpg、jpeg
		if (Layout.LO20001.getValue() == material.getLayout().intValue() || Layout.LO20002.getValue() == material.getLayout().intValue() || Layout.LO20003.getValue() == material.getLayout().intValue()) {
			request.setAd_type(IQiYiConstant.AD_TYPE_1.getValue());
		} else if (Layout.LO20004.getValue() == material.getLayout().intValue()) { // 暂停广告创意格式有：jpg、jpeg、swf、png
			request.setAd_type(IQiYiConstant.AD_TYPE_2.getValue());
		}

		// 物料上传地址
		String url = materialFileRoot + material.getAdMaterials();
		LOGGER.info("materialFileRoot前:" + materialFileRoot);
		LOGGER.info("materialPath前:" + material.getAdMaterials());
		LOGGER.info("url前:" + url);
		byte[] contentBytes = null;
		try {
			LOGGER.info("materialFileRoot中:" + materialFileRoot);
			LOGGER.info("materialPath中:" + material.getAdMaterials());
			LOGGER.info("url中:" + url);
			contentBytes = FileUtils.readFileToByteArray(new File(url));
			LOGGER.info("IQiYiUploadMaterial-materialSize: " + contentBytes.length);
		} catch (IOException e) {
			LOGGER.info("materialFileRoot里:" + materialFileRoot);
			LOGGER.info("materialPath里:" + material.getAdMaterials());
			LOGGER.info("url里:" + url);
			e.printStackTrace();
		}
		LOGGER.info("materialFileRoot后:" + materialFileRoot);
		LOGGER.info("materialPath后:" + material.getAdMaterials());
		LOGGER.info("url后:" + url);

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("click_url", request.getClick_url());
		paramMap.put("video_id", request.getVideo_id());
		paramMap.put("file_name", request.getFile_name());
		paramMap.put("platform", String.valueOf(request.getPlatform()));
		paramMap.put("duration", String.valueOf(request.getDuration()));
		paramMap.put("dpi", request.getDpi());
		paramMap.put("end_date", request.getEnd_date());
		paramMap.put("ad_id", String.valueOf(request.getAd_id()));
		paramMap.put("ad_type", String.valueOf(request.getAd_type()));
		paramMap.put("is_pmp", String.valueOf(request.getIs_pmp()));

		LOGGER.info("IQiYiUploadMaterial-buildRequest info: " + paramMap);
		String postResult = iQiYiHttpUtils.post(materialCreateUrl, paramMap, contentBytes);
		LOGGER.info("IQiYiUploadMaterial-buildResponse info: " + postResult);

		return postResult;
	}

	/**
	 * 获取媒体方的投放方式 媒体方：RTB 还 是 PMP（PDB+PD）,0 代表 PMP素材，1 代表 RTB 素材
	 * 我方：1：PDB、2：PD、4、PMP、8：RTB
	 * 
	 * @param deliveryType
	 * @return
	 */
	private int getMediaDeliveryType(int ourDeliveryType) {
		// PMP
		if (ourDeliveryType == 8) {
			return 1;
		}
		// 其他类型
		return 0;
	}
}
