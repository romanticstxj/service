package com.madhouse.platform.premiummad.media.funadx;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dao.AdvertiserMapper;
import com.madhouse.platform.premiummad.dao.MaterialMapper;
import com.madhouse.platform.premiummad.entity.Advertiser;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.media.funadx.constant.IFunadxConstant;
import com.madhouse.platform.premiummad.media.funadx.constant.IFunadxEnum;
import com.madhouse.platform.premiummad.media.funadx.request.FunadxMaterialRequest;
import com.madhouse.platform.premiummad.media.funadx.request.FunadxPMRequest;
import com.madhouse.platform.premiummad.media.funadx.request.FunadxUploadRequest;
import com.madhouse.platform.premiummad.media.funadx.response.FunadxUploadResponse;
import com.madhouse.platform.premiummad.model.MaterialAuditResultModel;
import com.madhouse.platform.premiummad.service.IMaterialService;
import com.madhouse.platform.premiummad.service.IMediaService;
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

	@Value("${material_meidaGroupMapping_funadx}")
	private String mediaGroupStr;
	
	@Autowired
	private IMediaService mediaService;
	
	public void uploadMaterial(){
		LOGGER.info("++++++++++Funadx upload material begin+++++++++++");

		// 根据媒体组ID和审核对象获取具体的媒体ID
		int[] mediaIds = mediaService.getMeidaIds(mediaGroupStr, SystemConstant.MediaAuditObject.MATERIAL);

		// 媒体组没有映射到具体的媒体不处理
		if (mediaIds == null || mediaIds.length < 1) {
			return;
		}

		// 查询所有待审核且媒体的素材的审核状态是媒体审核的
		List<Material> unSubmitMaterials = materialDao.selectMaterialsByMeidaIds(mediaIds, MaterialStatusCode.MSC10002.getValue());
		if (unSubmitMaterials == null || unSubmitMaterials.isEmpty()) {
			/* LOGGER.info(MediaMapping.getDescrip(mediaIds) + "没有未上传的素材"); */
			LOGGER.info("Funadx 没有未上传的素材");
			return;
		}

		// 上传到媒体
		LOGGER.info("FunadxUploadMaterialApiTask-Funadx", unSubmitMaterials.size());

		List<MaterialAuditResultModel> rejusedMaterials = new ArrayList<MaterialAuditResultModel>();
		Map<Integer, String[]> materialIdKeys = new HashMap<Integer, String[]>();
		for (Material material : unSubmitMaterials) {
			FunadxUploadRequest uploadRequest = new FunadxUploadRequest();
			String errMsg = buildUploadRequest(uploadRequest, material);
			if (!StringUtils.isBlank(errMsg)) {
				MaterialAuditResultModel rejuseItem = new MaterialAuditResultModel();
				rejuseItem.setId(String.valueOf(material.getId()));
				rejuseItem.setStatus(MaterialStatusCode.MSC10001.getValue());
				rejuseItem.setMediaIds(mediaIds);
				rejuseItem.setErrorMessage(errMsg);
				rejusedMaterials.add(rejuseItem);
				LOGGER.error(rejuseItem.getErrorMessage());
				continue;
			}
			
			// 请求接口
			String requestJson = JSON.toJSONString(uploadRequest);
			LOGGER.info("FunadxUpload request Info: " + requestJson);
			String responseJson = HttpUtils.post(uploadMaterialUrl, requestJson);
			LOGGER.info("FunadxUpload response Info: " + responseJson);

			// 处理返回的结果
			if (!StringUtils.isEmpty(responseJson)) {
				Map<String, Object> maps = (Map<String, Object>)JSON.parse(responseJson); 
				if (!"0".equals(maps.get("result").toString())) {
					if (!StringUtils.isBlank(maps.get("message").toString())) {
						MaterialAuditResultModel rejuseItem = new MaterialAuditResultModel();
						rejuseItem.setId(String.valueOf(material.getId()));
						rejuseItem.setStatus(MaterialStatusCode.MSC10001.getValue());
						rejuseItem.setMediaIds(mediaIds);
						rejuseItem.setErrorMessage(maps.get("message").toString());
						rejusedMaterials.add(rejuseItem);
						continue;
					}
				}
				FunadxUploadResponse uploadResponse = JSON.parseObject(responseJson, FunadxUploadResponse.class);
				Integer result = uploadResponse.getResult();
				LOGGER.info("FunadxUpload api result: " + result);
				// 返回结果result==0为接口调用成功
				if (IFunadxEnum.RESPONSE_SUCCESS.getValue() == result.intValue()) {
					if (uploadResponse.getMessage() == null || uploadResponse.getMessage().isEmpty()) {
						LOGGER.error("素材[materialId=" + material.getId() + "]上传失败-返回异常" );
						continue;
					}
					
					// 一次上传一条素材
					Integer uploadResult = uploadResponse.getMessage().get(0).getResult();
					
					if (IFunadxEnum.UPLOAD_SUCCESS.getValue() == uploadResult.intValue()) {
						// 上传成功
						String[] mediaQueryAndMaterialKeys = { String.valueOf(material.getId()) };
						materialIdKeys.put(material.getId(), mediaQueryAndMaterialKeys);
					} else if (IFunadxEnum.UPLOAD_PARAS_ERROR.getValue() == uploadResult.intValue() || 
							IFunadxEnum.UPLOAD_UNSUPPORT_TYPE.getValue() == uploadResult.intValue() || 
							IFunadxEnum.UPLOAD_DOLOAD_FAILURE.getValue() == uploadResult.intValue() || 
							IFunadxEnum.UPLOAD_OVER_SIZE.getValue() == uploadResult.intValue()) {
						// 已知业务错误-自动驳回
						MaterialAuditResultModel rejuseItem = new MaterialAuditResultModel();
						rejuseItem.setId(String.valueOf(material.getId()));
						rejuseItem.setStatus(MaterialStatusCode.MSC10001.getValue());
						rejuseItem.setMediaIds(mediaIds);
						rejuseItem.setErrorMessage(uploadResponse.getMessage().get(0).getReason());
						rejusedMaterials.add(rejuseItem);
						continue;
					} else if (IFunadxEnum.UPLOAD_UNKNOW_ERROR.getValue() == uploadResult.intValue()) {
						// 未知错误
						LOGGER.error("素材[materialId=" + material.getId() + "]上传失败-" + uploadResponse.getMessage().get(0).getReason());
					}
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

		// 处理失败的结果，自动驳回 - 通过素材id更新
		if (!rejusedMaterials.isEmpty()) {
			materialService.updateStatusToMediaByMaterialId(rejusedMaterials);
		}

		LOGGER.info("++++++++++Funadx upload material end+++++++++++");
	}
	
	/**
	 * 构造上传素材请求
	 * 
	 * @param uploadRequest
	 * @param material
	 * @return
	 */
	private String buildUploadRequest(FunadxUploadRequest uploadRequest, Material material) {
		uploadRequest.setDspid(dspid);
		uploadRequest.setToken(token);
		
		List<FunadxMaterialRequest> materialRequests = new ArrayList<>();
		FunadxMaterialRequest materialRequest = new FunadxMaterialRequest();
		
		materialRequest.setCrid(String.valueOf(material.getId()));
		// 素材 URL
		materialRequest.setAdm(material.getAdMaterials().split("\\|")[0]);		
		// 获取该素材的广告主
		String[] advertiserKeys = { material.getAdvertiserKey() };
		List<Advertiser> advertisers = advertiserDao.selectByAdvertiserKeysAndDspId(advertiserKeys, String.valueOf(material.getDspId()), material.getMediaId());
		if (advertisers == null || advertisers.size() != 1) {
			return "广告主不存在[materialId=" + material.getId() + "]";
		}
		materialRequest.setAdvertiser(advertisers.get(0).getAdvertiserName());
		
		// 落地页
		materialRequest.setLandingpage(material.getLpgUrl());

		// 开始、结束日期
		materialRequest.setStartdate(DateUtils.getFormatStringByPattern("yyyy-MM-dd", material.getStartDate() != null ? material.getStartDate() : new Date()));
		materialRequest.setEnddate(material.getEndDate() != null ? DateUtils.getFormatStringByPattern("yyyy-MM-dd", material.getEndDate()) : null);

		// 贴片时长（单位s）
		materialRequest.setDuration(material.getDuration());

		// 展示检测可以多个，以换行符分割
		String impUrl = material.getImpUrls() == null ? "" : material.getImpUrls();
		String[] impTrackUrlArray = impUrl.split("\\|");
		List<FunadxPMRequest> pmRequests = new ArrayList<>();
		// 素材表里以 |分割 -> startDelay1`url1|startDelay2`url2
		if (impTrackUrlArray != null) {
			for (int i = 0; i < impTrackUrlArray.length; i++) {
				FunadxPMRequest pmRequest = new FunadxPMRequest();
				String[] track = impTrackUrlArray[i].split("`");
				// 监测时间点
				pmRequest.setPoint(covertToMeidaTime(Integer.valueOf(track[0]), material.getDuration()));
				pmRequest.setUrl(track[1]);
				pmRequests.add(pmRequest);
			}
		}
		materialRequest.setPm(pmRequests);

		// 点击检测也可以多个，以换行符分割
		String clkUrl = material.getClkUrls() == null ? "" : material.getClkUrls();
		materialRequest.setCm(Arrays.asList(clkUrl.split("\\|")));

		// 物料类型：必填，图片（image）、视频（video）、Flash（flash）
		String type = getMaterialType(materialRequest.getAdm());
		if (StringUtils.isBlank(type)) {
			return "不支持的文件格式,目前支持的文件格式：jpg,gif,png,swf,flv,mp4";
		}
		materialRequest.setType(type);
		
		materialRequests.add(materialRequest);
		uploadRequest.setMaterial(materialRequests);

		return "";
	}
	
	/**
	 * 将监测时间点转换为媒体识别的
	 * 我方- 0：开始；-1：中点；-2：结束；>0：实际秒数；
	 * 媒体方-  0,//曝光监测时点, 必须填写。-1 为终点监播
	 * 
	 * @param startDelay
	 * @return
	 */
	private int covertToMeidaTime(Integer startDelay, Integer duration) {
		if (startDelay == null) {
			return 0;
		}

		// 我方终点
		if (startDelay == -2) {
			return -1;
		}

		// 我方中点
		if (startDelay == -1) {
			if (duration == null) {
				return 0;
			}
			return duration / 2;
		}

		return startDelay;
	}
	
	/**
	 * 根据素材后缀获取素材类型 且与风行类型匹配
	 * 
	 * @param adm
	 * @return
	 */
	private String getMaterialType(String adm) {
		// 合法性校验
		if (StringUtils.isBlank(adm)) {
			return "";
		}

		int suffixIndex = adm.lastIndexOf(".");
		if (suffixIndex < 0 || suffixIndex + 1 > adm.length()) {
			return "";
		}

		String suffix = adm.substring(suffixIndex + 1, adm.length());
		if (suffix.toLowerCase().equals("mp4")) {
			return IFunadxConstant.MATERIAL_TYPE_VIDEO;
		} else if (suffix.toLowerCase().equals("flv") || suffix.toLowerCase().equals("swf")) {
			return IFunadxConstant.MATERIAL_TYPE_FLASH;
		} else if (suffix.toLowerCase().equals("jpg") || suffix.toLowerCase().equals("png") || suffix.toLowerCase().equals("gif")) {
			return IFunadxConstant.MATERIAL_TYPE_IMAGE;
		}
		
		// 如果其他类型，返回空
		return "";
	}
}
