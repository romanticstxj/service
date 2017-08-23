package com.madhouse.platform.premiummad.media.iqiyi;

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
import com.madhouse.platform.premiummad.constant.MaterialStatusCode;
import com.madhouse.platform.premiummad.constant.MediaMapping;
import com.madhouse.platform.premiummad.dao.MaterialMapper;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.media.iqiyi.constant.IQiYiConstant;
import com.madhouse.platform.premiummad.media.iqiyi.request.IQiyiMaterialStatusRequest;
import com.madhouse.platform.premiummad.media.iqiyi.response.IQiyiMaterialStatusDetailResponse;
import com.madhouse.platform.premiummad.media.iqiyi.response.IQiyiMaterialStatusResponse;
import com.madhouse.platform.premiummad.media.iqiyi.util.IQiYiHttpUtils;
import com.madhouse.platform.premiummad.model.MaterialAuditResultModel;
import com.madhouse.platform.premiummad.service.IMaterialService;
import com.madhouse.platform.premiummad.util.StringUtils;

@Component("iQiyiMaterialStatusApiTask")
public class IQiyiMaterialStatusApiTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(IQiyiMaterialStatusApiTask.class);

	@Value("${iqiyi.material.list}")
	private String materialListUrl;

	@Autowired
	private IQiYiHttpUtils iQiYiHttpUtils;

	@Autowired
	private MaterialMapper materialDao;

	@Autowired
	private IMaterialService materialService;

	public void getStatusDetail() {
		LOGGER.info("++++++++++iqiyi get material status begin+++++++++++");

		// 我方系统未审核的素材
		List<Material> unAuditMaterials = materialDao.selectMediaMaterials(MediaMapping.IQYI.getValue(), MaterialStatusCode.MSC10003.getValue());
		if (unAuditMaterials == null || unAuditMaterials.isEmpty()) {
			LOGGER.info("++++++++++IQYI no materials need to audit+++++++++++");
			return;
		}

		// 获取未审核媒体的ID列表
		String mediaMaterialIds = "";
		for (Material material : unAuditMaterials) {
			mediaMaterialIds = "," + mediaMaterialIds + material.getMediaMaterialKey();
		}

		// 调用请求
		IQiyiMaterialStatusRequest iQiyiMaterialStatusRequest = new IQiyiMaterialStatusRequest();
		iQiyiMaterialStatusRequest.setBatch(mediaMaterialIds.substring(1));

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("batch", iQiyiMaterialStatusRequest.getBatch());
		LOGGER.info("IQiyiMaterialStatusTask-request:" + paramMap);

		// 返回结果集
		String getResult = iQiYiHttpUtils.get(materialListUrl, paramMap);
		LOGGER.info("IQiyiMaterialStatusTask-response:" + getResult);
		if (!StringUtils.isEmpty(getResult)) {
			IQiyiMaterialStatusResponse response = JSON.parseObject(getResult, IQiyiMaterialStatusResponse.class);
			String code = response.getCode();
			LOGGER.info("IQiyiMaterialStatusTask-Code:" + code);
			// 成功
			if (code.equals(IQiYiConstant.RESPONSE_SUCCESS.getValue() + "")) {
				List<IQiyiMaterialStatusDetailResponse> results = response.getResults();
				processResult(results);
			} else { // 查询失败
				String message = "";
				if (code.equals(String.valueOf(IQiYiConstant.RESPONSE_PARAM_FAIL.getValue()))) {
					// 参数错误
					message = IQiYiConstant.RESPONSE_PARAM_FAIL.getDescription();
				} else if (code.equals(String.valueOf(IQiYiConstant.RESPONSE_AUTH_FAIL.getValue()))) {
					// 系统认证错误
					message = IQiYiConstant.RESPONSE_AUTH_FAIL.getDescription();
				} else if (code.equals(String.valueOf(IQiYiConstant.RESPONSE_SERVER_FAIL.getValue()))) {
					// 服务端错误
					message = IQiYiConstant.RESPONSE_SERVER_FAIL.getDescription();
				} else if (code.equals(String.valueOf(IQiYiConstant.RESPONSE_MAX_UPLOAD_SIZE_FAIL.getValue()))) {
					// 用户上传超过限制
					message = IQiYiConstant.RESPONSE_MAX_UPLOAD_SIZE_FAIL.getDescription();
				} else if (code.equals(String.valueOf(IQiYiConstant.RESPONSE_MAX_REQUEST_SIZE_FAIL.getValue()))) {
					// 应用请求超过限制
					message = IQiYiConstant.RESPONSE_MAX_REQUEST_SIZE_FAIL.getDescription();
				}
				LOGGER.info(MediaMapping.IQYI.getDescrip() + "获取状态失败-" + message);
			}
		}

		LOGGER.info("++++++++++iqiyi get material status end+++++++++++");
	}

	/**
	 * 处理返回的结果
	 * 
	 * @param results
	 */
	private void processResult(List<IQiyiMaterialStatusDetailResponse> results) {
		List<MaterialAuditResultModel> auditResults = new ArrayList<MaterialAuditResultModel>();
		for (IQiyiMaterialStatusDetailResponse item : results) {
			MaterialAuditResultModel auditItem = new MaterialAuditResultModel();
			// 媒体的素材id和我方一致
			auditItem.setId(String.valueOf(item.getM_id()));
			auditItem.setMediaId(String.valueOf(MediaMapping.IQYI.getValue()));

			// COMPLETE-通过
			if ("COMPLETE".equals(item.getStatus())) {
				auditItem.setStatus(MaterialStatusCode.MSC10004.getValue());
				// 爱奇艺奇 id，只有 status 为 COMPLETE 时才返回该项。tv_id 作为后续广告竞价的 crid 参数值
				auditItem.setMediaMaterialId(item.getTv_id());
				auditResults.add(auditItem);
				continue;
			}

			// AUDIT_UNPASS-驳回
			if ("AUDIT_UNPASS".equals(item.getStatus())) {
				auditItem.setStatus(MaterialStatusCode.MSC10001.getValue());
				auditItem.setErrorMessage(item.getReason());
				auditResults.add(auditItem);
				continue;
			}
			LOGGER.info("IQiyiMaterialStatus :materialID=" + item.getM_id() + item.getStatus());
		}

		// 更新数据库
		if (!auditResults.isEmpty()) {
			materialService.updateStatusToMediaByMaterialId(auditResults);
		}
	}
}
