package com.madhouse.platform.premiummad.media.funadx;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.constant.MaterialStatusCode;
import com.madhouse.platform.premiummad.constant.MediaMapping;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dao.MaterialMapper;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.media.funadx.constant.IFunadxEnum;
import com.madhouse.platform.premiummad.media.funadx.request.FunadxStatusRequest;
import com.madhouse.platform.premiummad.media.funadx.response.FunadxDetailResponse;
import com.madhouse.platform.premiummad.media.funadx.response.FunadxStatusResponse;
import com.madhouse.platform.premiummad.media.funadx.response.FunadxStatusSuccessDetailResponse;
import com.madhouse.platform.premiummad.media.funadx.response.FunadxStatusSuccessResponse;
import com.madhouse.platform.premiummad.model.MaterialAuditResultModel;
import com.madhouse.platform.premiummad.service.IMaterialService;
import com.madhouse.platform.premiummad.service.IMediaService;
import com.madhouse.platform.premiummad.util.HttpUtils;

@Component
public class FunadxMaterialStatusApiTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(FunadxMaterialStatusApiTask.class);

	@Value("${funadx.materialStatusUrl}")
	private String materialStatusUrl;

	@Value("${funadx.dspid}")
	private String dspid;

	@Value("${funadx.token}")
	private String token;

	@Autowired
	private MaterialMapper materialDao;

	@Autowired
	private IMaterialService materialService;

	@Value("${material_meidaGroupMapping_funadx}")
	private String mediaGroupStr;

	@Autowired
	private IMediaService mediaService;

	public void getStatus() {
		LOGGER.info("++++++++++Funadx get material status begin+++++++++++");

		// 根据媒体组ID和审核对象获取具体的媒体ID
		int[] mediaIds = mediaService.getMeidaIds(mediaGroupStr, SystemConstant.MediaAuditObject.MATERIAL);

		// 媒体组没有映射到具体的媒体不处理
		if (mediaIds == null || mediaIds.length < 1) {
			return;
		}

		// 获取审核中的素材
		List<Material> unauditMaterials = materialDao.selectMaterialsByMeidaIds(mediaIds, MaterialStatusCode.MSC10003.getValue());
		if (unauditMaterials == null || unauditMaterials.isEmpty()) {
			LOGGER.info(MediaMapping.FUNADX.getDescrip() + "无需要审核的素材");
			return;
		}

		// 获取媒体方的素材ID
		List<String> crids = new ArrayList<String>();
		for (Material material : unauditMaterials) {
			crids.add(material.getMediaQueryKey());
		}

		// 设置request参数
		FunadxStatusRequest funadxStatusRequest = new FunadxStatusRequest();
		funadxStatusRequest.setCrid(crids);
		funadxStatusRequest.setDspid(dspid);
		funadxStatusRequest.setToken(token);

		// 发送请求
		String requestJson = JSON.toJSONString(funadxStatusRequest);
		LOGGER.info("FunadxStatusApiRequestJson: " + requestJson);
		String responseJson = HttpUtils.post(materialStatusUrl, requestJson);
		LOGGER.info("FunadxStatusApiResponseJson: " + responseJson);

		// 处理审核结果
		FunadxStatusResponse funadxStatusResponse = JSON.parseObject(responseJson, FunadxStatusResponse.class);
		Integer result = funadxStatusResponse.getResult();
		// 如果返回的result==0，说明接口调用成功
		if (IFunadxEnum.RESPONSE_SUCCESS.getValue() == result) {
			FunadxStatusSuccessResponse funadxStatusSuccessResponse = JSON.parseObject(responseJson, FunadxStatusSuccessResponse.class);
			LOGGER.info("FunadxStatus Api success: " + funadxStatusSuccessResponse.getResult());
			FunadxStatusSuccessDetailResponse funadxStatusSuccessDetailResponse = funadxStatusSuccessResponse.getMessage();
			if (funadxStatusSuccessDetailResponse != null) {
				List<FunadxDetailResponse> funadxDetailResponses = funadxStatusSuccessDetailResponse.getRecords();
				List<MaterialAuditResultModel> auditResults = new ArrayList<MaterialAuditResultModel>();
				// 请求成功物料状态明细列表
				for (FunadxDetailResponse funadxDetailResponse : funadxDetailResponses) {
					String crid = funadxDetailResponse.getCrid().trim();
					Integer status = funadxDetailResponse.getResult();

					MaterialAuditResultModel auditItem = new MaterialAuditResultModel();
					auditItem.setMediaQueryKey(crid);
					auditItem.setMediaId(String.valueOf(MediaMapping.FUNADX.getValue()));

					if (IFunadxEnum.M_STATUS_APPROVED.getValue() == status) { // 审核已通过
						auditItem.setStatus(MaterialStatusCode.MSC10004.getValue());
						auditResults.add(auditItem);
					} else if (IFunadxEnum.M_STATUS_REFUSED.getValue() == status) { // 审核未通过
						auditItem.setStatus(MaterialStatusCode.MSC10001.getValue());
						auditItem.setErrorMessage(funadxDetailResponse.getReason());
						auditResults.add(auditItem);
					} else if (IFunadxEnum.M_STATUS_UNAUDITED.getValue() == status) { // 待审核
						LOGGER.info("FunadxMaterialStatus:materialID=" + crid + " " + status);
					}
				}
			} else {
				LOGGER.info("++++++++++Funadx get material status failed+++++++++++");
			}
		} else {
			LOGGER.info("++++++++++Funadx get material status failed+++++++++++");
		}
		LOGGER.info("++++++++++Funadx get material status end+++++++++++");
	}
}
