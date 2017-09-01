package com.madhouse.platform.premiummad.media.funadx;

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
import com.madhouse.platform.premiummad.constant.MaterialStatusCode;
import com.madhouse.platform.premiummad.constant.MediaMapping;
import com.madhouse.platform.premiummad.dao.AdvertiserMapper;
import com.madhouse.platform.premiummad.dao.MaterialMapper;
import com.madhouse.platform.premiummad.entity.Advertiser;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.media.funadx.constant.IFunadxConstant;
import com.madhouse.platform.premiummad.media.funadx.request.FunadxMaterialRequest;
import com.madhouse.platform.premiummad.media.funadx.request.FunadxPMRequest;
import com.madhouse.platform.premiummad.media.funadx.request.FunadxUploadRequest;
import com.madhouse.platform.premiummad.media.funadx.response.FunadxUploadResponse;
import com.madhouse.platform.premiummad.service.IMaterialService;
import com.madhouse.platform.premiummad.util.DateUtils;
import com.madhouse.platform.premiummad.util.HttpUtils;
import com.madhouse.platform.premiummad.util.StringUtils;

@Component
public class FunadxUploadMaterialApiTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(FunadxUploadMaterialApiTask.class);
	
	@Value("${funadx.uploadMaterialUrl}")
	private String uploadMaterialUrl;
	
	@Value("${funadx.dspid}")
	private String dspid;
	
	@Value("${funadx.token}")
	private String token;
	
	@Autowired
	private MaterialMapper materialDao;
	
	@Autowired
	private IMaterialService materialService;
	
	@Autowired
	private AdvertiserMapper advertiserDao;
	
	public void uploadMaterial(){
		LOGGER.info("++++++++++Funadx upload material begin+++++++++++");
		// 查询所有待审核且媒体的素材的审核状态是媒体审核的
		List<Material> unSubmitMaterials = materialDao.selectMediaMaterials(MediaMapping.FUNADX.getValue(), MaterialStatusCode.MSC10002.getValue());
		if (unSubmitMaterials == null || unSubmitMaterials.isEmpty()) {
			LOGGER.info("风行没有未上传的广告主");
			LOGGER.info("++++++++++Funadx News upload material end+++++++++++");
			return;
		}
		
		// 上传到媒体
		LOGGER.info("FunadxUploadMaterialApiTask-Funadx", unSubmitMaterials.size());

		Map<Integer, String[]> materialIdKeys = new HashMap<Integer, String[]>();
		for (Material material : unSubmitMaterials) {
			FunadxUploadRequest uploadRequest = buildUploadRequest(material);
			// 请求接口
			String requestJson = JSON.toJSONString(uploadRequest);
			LOGGER.info("FunadxUpload request Info: " + requestJson);
			String responseJson = HttpUtils.post(uploadMaterialUrl, requestJson);
			LOGGER.info("FunadxUpload response Info: " + responseJson);

			// 处理返回的结果
			if (!StringUtils.isEmpty(responseJson)) {
				FunadxUploadResponse uploadResponse = JSON.parseObject(responseJson, FunadxUploadResponse.class);
				Integer result = uploadResponse.getResult();
				LOGGER.info("FunadxUpload api result: " + result);
				// 返回结果result==0为接口调用成功
				if (IFunadxConstant.RESPONSE_SUCCESS.getValue() == result) {
					String[] mediaQueryAndMaterialKeys = {String.valueOf(material.getId())};
					materialIdKeys.put(material.getId(), mediaQueryAndMaterialKeys);
				} else {
					LOGGER.error("素材[materialId=" + material.getId() + "]上传失败-" + result + " " + uploadResponse.getMessage());
				}
			} else {
				LOGGER.error("素材[materialId=" + material.getId() + "]上传失败");
			}
		}

		// 更新我方素材信息
		if (!materialIdKeys.isEmpty()) {
			materialService.updateStatusAfterUpload(materialIdKeys);
		}
		
		LOGGER.info("++++++++++Funadx upload material end+++++++++++");
	}
	
	/**
	 * 构造上传素材请求
	 * 
	 * @param material
	 * @return
	 */
	private FunadxUploadRequest buildUploadRequest(Material material) {
		FunadxUploadRequest uploadRequest = new FunadxUploadRequest();
		List<FunadxMaterialRequest> materialRequests = new ArrayList<>();
		
		FunadxMaterialRequest materialRequest = new FunadxMaterialRequest();
		materialRequest.setAdm(material.getAdMaterials());
		// 获取该素材的广告主
		String[] advertiserKeys = { material.getAdvertiserKey() };
		List<Advertiser> advertisers = advertiserDao.selectByAdvertiserKeysAndDspId(advertiserKeys, String.valueOf(material.getDspId()), material.getMediaId());
		materialRequest.setAdvertiser((advertisers == null || advertisers.size() != 1) ? "" : advertisers.get(0).getAdvertiserName());
		// 点击检测也可以多个，以换行符分割
		String clkUrl = material.getClkUrls() == null ? "" : material.getClkUrls();
		materialRequest.setCm(Arrays.asList(clkUrl.split("\\|")));
		materialRequest.setCrid(String.valueOf(material.getId()));
		materialRequest.setDuration(material.getDuration());
		materialRequest.setEnddate(DateUtils.getFormatStringByPattern("yyyy-MM-dd", material.getEndDate()));
		// 展示检测可以多个，以换行符分割
		String impUrl = material.getImpUrls() == null ? "" : material.getImpUrls();
		String[] impUrls = impUrl.split("\\|");
		List<FunadxPMRequest> pmRequests = new ArrayList<>();
		for (int i = 0; i < impUrls.length;) {
			FunadxPMRequest pmRequest = new FunadxPMRequest();
			// 监测时间点
			if (impUrls[i].matches("^-?\\d+$")) {
				pmRequest.setPoint(Integer.valueOf(impUrls[i]));
				i++;
			}
			pmRequest.setUrl(impUrls[i]);
			pmRequests.add(pmRequest);
			i++;
		}
		materialRequest.setPm(pmRequests);
		materialRequest.setStartdate(material.getStartDate() != null ? DateUtils.getFormatStringByPattern("yyyy-MM-dd", material.getStartDate()) : null);
		// 物料类型 TODO
		materialRequest.setType("");
		materialRequest.setLandingpage(material.getLpgUrl());
		materialRequests.add(materialRequest);
		uploadRequest.setMaterial(materialRequests);
		uploadRequest.setDspid(dspid);
		uploadRequest.setToken(token);
		return uploadRequest;
	}
}
