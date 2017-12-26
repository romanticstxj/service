package com.madhouse.platform.premiummad.media.autohome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.constant.MaterialStatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dao.AdvertiserMapper;
import com.madhouse.platform.premiummad.dao.MaterialMapper;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.media.autohome.constant.AutohomeConstant;
import com.madhouse.platform.premiummad.media.autohome.response.CreativeAuditResultData;
import com.madhouse.platform.premiummad.media.autohome.response.CreativeStatusData;
import com.madhouse.platform.premiummad.media.autohome.response.CreativeStatusResponse;
import com.madhouse.platform.premiummad.media.autohome.util.AutohomeCommonUtil;
import com.madhouse.platform.premiummad.model.MaterialAuditResultModel;
import com.madhouse.platform.premiummad.service.IMaterialService;
import com.madhouse.platform.premiummad.service.IMediaService;
import com.madhouse.platform.premiummad.util.HttpUtils;

@Component
public class AutohomeMaterialStatusApiTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(AutohomeMaterialStatusApiTask.class);

	@Value("${autohome.getAuditInfoUrl}")
	private String getAuditInfoUrl;

	@Value("${autohome.dspId}")
	private String dspId;

	@Value("${autohome.dspName}")
	private String dspName;

	@Value("${autohome.signKey}")
	private String signKey;

	@Value("${material_meidaGroupMapping_autohome}")
	private String mediaGroupStr;

	@Autowired
	private MaterialMapper materialDao;

	@Autowired
	private IMaterialService materialService;

	@Autowired
	private AdvertiserMapper advertiserDao;

	@Autowired
	private IMediaService mediaService;

	public void getStatus() {
		LOGGER.info("++++++++++Autohome get material status begin+++++++++++");

		// 根据媒体组ID和审核对象获取具体的媒体ID
		int[] mediaIds = mediaService.getMeidaIds(mediaGroupStr, SystemConstant.MediaAuditObject.MATERIAL);

		// 媒体组没有映射到具体的媒体不处理
		if (mediaIds == null || mediaIds.length < 1) {
			return;
		}

		// 我方系统未审核的素材
		List<Material> unAuditMaterials = materialDao.selectMaterialsByMeidaIds(mediaIds, MaterialStatusCode.MSC10003.getValue());
		if (unAuditMaterials == null || unAuditMaterials.isEmpty()) {
			LOGGER.info("Autohome没有素材需要审核");
			return;
		}

		// 设置request参数
		Map<String, String> paramMap = buildRequest(unAuditMaterials);
		String request = AutohomeCommonUtil.getRequest(paramMap);

		// 调用接口
		LOGGER.info("request: " + request);
		Map<String, Object> resultMap = HttpUtils.get(getAuditInfoUrl + "?" + request);
		String result = resultMap.get(HttpUtils.RESPONSE_BODY_KEY).toString();
		LOGGER.info("response: " + result);

		// 解析返回结果
		if (!StringUtils.isEmpty(result)) {
			CreativeStatusResponse response = JSON.parseObject(result, CreativeStatusResponse.class);
			if (AutohomeConstant.RetCode.AUTOHOME_STATUS_SUCCESS == response.getStatus()) {
				if (response.getData() == null || response.getData().getCreative() == null) {
					LOGGER.info("Response is empty");
					return;
				}
				// 正常返回结果
				processResult(response.getData(), mediaIds);
			} else {
				// 异常返回
				LOGGER.info("Response is exceptional." + result);
			}
		} else {
			LOGGER.info("Response is null");
		}

		LOGGER.info("++++++++++Autohome get material status end+++++++++++");
	}

	/**
	 * 处理正常返回结果
	 * 
	 * @param data
	 * @param mediaIds
	 */
	private void processResult(CreativeStatusData data, int[] mediaIds) {
		List<MaterialAuditResultModel> auditResults = new ArrayList<MaterialAuditResultModel>();
		for (CreativeAuditResultData item : data.getCreative()) {
			MaterialAuditResultModel auditItem = new MaterialAuditResultModel();
			auditItem.setMediaQueryKey(String.valueOf(item.getId()));
			auditItem.setMediaIds(mediaIds);

			// 根据返回的状态设置
			if (AutohomeConstant.AuditStatus.AUDITED == item.getAuditStatus()) {
				// 审核通过
				auditItem.setStatus(MaterialStatusCode.MSC10004.getValue());
			} else if (AutohomeConstant.AuditStatus.REJECTED == item.getAuditStatus()) {
				// 驳回
				auditItem.setStatus(MaterialStatusCode.MSC10001.getValue());
				auditItem.setErrorMessage(item.getAuditComment());
				auditResults.add(auditItem);
			} else if (AutohomeConstant.AuditStatus.UNAUDIT == item.getAuditStatus()) {
				// 未审核
				LOGGER.info("素材媒体未审核[meidaQueryKey=" + item.getId() + "]");
			}
		}

		// 更新数据库
		if (!auditResults.isEmpty()) {
			materialService.updateStatusToMedia(auditResults);
		}
	}

	/**
	 * 构造请求参数
	 * 
	 * @param unAuditMaterials
	 * @return
	 */
	private Map<String, String> buildRequest(List<Material> unAuditMaterials) {
		Map<String, String> paramMap = new HashMap<String, String>();
		List<String> creativeId = new ArrayList<String>();
		for (Material material : unAuditMaterials) {
			creativeId.add(material.getMediaQueryKey());
		}
		paramMap.put("creativeId", StringUtils.collectionToDelimitedString(creativeId, ","));
		paramMap.put("dspId", dspId);
		paramMap.put("sign", AutohomeCommonUtil.getSign(paramMap, signKey)); // TODO
		return paramMap;
	}
}
