package com.madhouse.platform.premiummad.media.tencent;

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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.madhouse.platform.premiummad.constant.MaterialStatusCode;
import com.madhouse.platform.premiummad.constant.MediaMapping;
import com.madhouse.platform.premiummad.dao.MaterialMapper;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.media.model.AdvertBatchStatusResponse;
import com.madhouse.platform.premiummad.media.util.TencentHttpUtil;
import com.madhouse.platform.premiummad.model.MaterialAuditResultModel;
import com.madhouse.platform.premiummad.service.IMaterialService;

/**
 * 批量获取广告的审核状态(物料审核)
 */
@Component
public class AdvertBatchStatusApiTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(AdvertBatchStatusApiTask.class);
	private static final int ITERATOR_TIMES = 2;
	private static final int TECENT_OTV_ITERATOR = 1;

	@Value("${tencent.advertStatus}")
	private String advertStatusUrl;

	@Autowired
	private TencentHttpUtil tencentHttpUtil;

	@Autowired
	private MaterialMapper materialDao;

	@Autowired
	private IMaterialService materialService;

	public void advertBatchStatus() {
		// TENCENT 对应两个媒体 OTV 和 非 OTV
		for (int mediaType = 0; mediaType < ITERATOR_TIMES; mediaType++) {
			int mediaId = 0;
			if (mediaType != TECENT_OTV_ITERATOR) {
				mediaId = MediaMapping.TENCENT_NOT_OTV.getValue();
			} else {
				mediaId = MediaMapping.TENCENT.getValue();
			}
			LOGGER.info(MediaMapping.getDescrip(mediaId) + " AdvertBatchStatusApiTask-advertBatchStatus start");
			// 获取审核中的素材
			List<Material> unauditMaterials = materialDao.selectMediaMaterials(mediaId, MaterialStatusCode.MSC10003.getValue());
			if (unauditMaterials == null || unauditMaterials.isEmpty()) {
				LOGGER.info(MediaMapping.getDescrip(mediaId) + "无需要审核的素材");
				return;
			}

			// 获取腾讯方的 dspOrderId
			List<String> dspOrderIds = new ArrayList<>();
			for (Material material : unauditMaterials) {
				dspOrderIds.add(material.getMediaMaterialKey());
			}

			// 向腾讯发请求批量获取状态
			String requstJson = JSONObject.toJSONString(dspOrderIds);
			Map<String, String> paramMap = new HashMap<>();
			paramMap.put("dsp_order_id_info", requstJson);
			LOGGER.info("Tencent批量获取广告的审核请求：{}", requstJson);
			String responseJson = tencentHttpUtil.post(advertStatusUrl, paramMap);
			LOGGER.info("Tencent批量获取广告的审核状态返回:{}", responseJson);

			// 处理返回的结果
			AdvertBatchStatusResponse advertBatchStatusResponse = JSON.parseObject(responseJson, AdvertBatchStatusResponse.class);
			if (advertBatchStatusResponse.getRet_code() == 0) {
				Map<String, Object> records = advertBatchStatusResponse.getRet_msg().getRecords();
				if (records != null && records.size() > 0) {
					List<MaterialAuditResultModel> auditResults = new ArrayList<MaterialAuditResultModel>();
					for (Map.Entry<String, Object> entry : records.entrySet()) {
						String materialIdStr = entry.getKey();
						JSONArray value = ((JSONArray) entry.getValue());
						if (value != null && value.size() > 0) {
							MaterialAuditResultModel auditItem = new MaterialAuditResultModel();
							auditItem.setMediaMaterialKey(materialIdStr);
							auditItem.setMediaId(String.valueOf(mediaId));

							Map map = (Map) value.get(0);
							String status = (String) map.get("status");
							if ("审核通过".equals(status)) {
								auditItem.setStatus(MaterialStatusCode.MSC10004.getValue());
								auditResults.add(auditItem);
							} else if ("审核不通过".equals(status)) {
								auditItem.setStatus(MaterialStatusCode.MSC10001.getValue());
								auditItem.setErrorMessage((String) map.get("reason"));
								auditResults.add(auditItem);
							}
						}
					}

					// 更新数据库
					if (!auditResults.isEmpty()) {
						materialService.updateStatusToMedia(auditResults);
					}
				} else {
					LOGGER.info("Tencent Records is null or size < 0");
				}
			}

			LOGGER.info(MediaMapping.getDescrip(mediaId) + " AdvertBatchStatusApiTask-advertBatchStatus end");
		}
	}
}
