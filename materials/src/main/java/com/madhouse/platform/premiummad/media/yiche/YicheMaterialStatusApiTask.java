package com.madhouse.platform.premiummad.media.yiche;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.madhouse.platform.premiummad.constant.MaterialStatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dao.MaterialMapper;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.media.yiche.constant.YicheConstant;
import com.madhouse.platform.premiummad.media.yiche.request.MaterialStatusRequest;
import com.madhouse.platform.premiummad.media.yiche.response.MaterialStatusResponse;
import com.madhouse.platform.premiummad.media.yiche.util.YicheCommonUtil;
import com.madhouse.platform.premiummad.model.MaterialAuditResultModel;
import com.madhouse.platform.premiummad.service.IMaterialService;
import com.madhouse.platform.premiummad.service.IMediaService;
import com.madhouse.platform.premiummad.util.HttpUtils;

@Component
public class YicheMaterialStatusApiTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(YicheMaterialStatusApiTask.class);

	@Value("${yiche.getStatusUrl}")
	private String getStatusUrl;

	@Value("${yiche.dspId}")
	private String dspId;

	@Value("${yiche.signKey}")
	private String signKey;

	@Value("${material_meidaGroupMapping_yiche}")
	private String mediaGroupStr;

	@Autowired
	private MaterialMapper materialDao;

	@Autowired
	private IMaterialService materialService;

	@Autowired
	private IMediaService mediaService;

	public void getStatus() {
		LOGGER.info("++++++++++Yiche get material status begin+++++++++++");

		// 根据媒体组ID和审核对象获取具体的媒体ID
		int[] mediaIds = mediaService.getMeidaIds(mediaGroupStr, SystemConstant.MediaAuditObject.MATERIAL);

		// 媒体组没有映射到具体的媒体不处理
		if (mediaIds == null || mediaIds.length < 1) {
			return;
		}

		// 我方系统未审核的素材
		List<Material> unAuditMaterials = materialDao.selectMaterialsByMeidaIds(mediaIds, MaterialStatusCode.MSC10003.getValue());
		if (unAuditMaterials == null || unAuditMaterials.isEmpty()) {
			LOGGER.info("Yiche没有素材需要审核");
			return;
		}

		// 单条查询
		List<MaterialAuditResultModel> auditResults = new ArrayList<MaterialAuditResultModel>();
		for (Material material : unAuditMaterials) {
			// 设置request参数
			MaterialStatusRequest request = buildRequest(material);
			String requestJson = JSON.toJSONString(request);

			// 调用接口
			LOGGER.info("request: " + requestJson);
			String result = HttpUtils.post(getStatusUrl, requestJson);
			LOGGER.info("response: " + result);

			// 解析返回结果
			if (!StringUtils.isEmpty(result)) {
				MaterialStatusResponse response = JSON.parseObject(result, MaterialStatusResponse.class);
				if (YicheConstant.ErrorCode.SUCCESS == response.getErrorCode()) {
					if (response.getResult() == null) {
						LOGGER.info("Response is empty");
						return;
					}

					// 正常返回结果
					MaterialAuditResultModel auditItem = new MaterialAuditResultModel();
					auditItem.setMediaQueryKey(request.getDepositId());
					auditItem.setMediaIds(mediaIds);

					// 根据返回的状态设置
					if (YicheConstant.AuditStatus.AUDITED.equals(response.getResult())) {
						// 审核通过
						auditItem.setStatus(MaterialStatusCode.MSC10004.getValue());
					} else if (YicheConstant.AuditStatus.REJECTED.equals(response.getResult())) {
						// 驳回
						auditItem.setStatus(MaterialStatusCode.MSC10001.getValue());
						auditItem.setErrorMessage(response.getErrorMsg());
						auditResults.add(auditItem);
					} else if (YicheConstant.AuditStatus.UNAUDIT.equals(response.getResult())) {
						// 未审核
						LOGGER.info("素材媒体未审核[meidaQueryKey=" + request.getDepositId() + "]");
					}
				} else {
					// 异常返回
					LOGGER.info("Response is exceptional." + result);
				}
			} else {
				LOGGER.info("Response is null");
			}
		}

		// 更新数据库
		if (!auditResults.isEmpty()) {
			materialService.updateStatusToMedia(auditResults);
		}

		LOGGER.info("++++++++++Yiche get material status end+++++++++++");
	}

	/**
	 * 构造请求参数
	 * 
	 * @param unAuditMaterials
	 * @return
	 */
	private MaterialStatusRequest buildRequest(Material material) {
		MaterialStatusRequest request = new MaterialStatusRequest();
		request.setDspId(dspId);
		request.setDepositId(material.getMediaQueryKey());

		// 校验串 TODO
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("dspId", dspId);
		jsonObj.put("depositId", material.getMediaQueryKey());
		request.setSign(YicheCommonUtil.getSign(jsonObj, signKey));

		// 当前请求时间戳
		request.setTimestamp(System.currentTimeMillis());
		return request;
	}
}
