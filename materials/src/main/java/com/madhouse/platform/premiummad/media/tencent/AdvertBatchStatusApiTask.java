package com.madhouse.platform.premiummad.media.tencent;

import java.util.ArrayList;
import java.util.List;

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
import com.madhouse.platform.premiummad.media.tencent.constant.TencentAduitStatus;
import com.madhouse.platform.premiummad.media.tencent.request.AdvertBachStatusData;
import com.madhouse.platform.premiummad.media.tencent.request.TencentCommonRequest;
import com.madhouse.platform.premiummad.media.tencent.response.AdvertBatchStatusResponse;
import com.madhouse.platform.premiummad.media.tencent.response.RetMsg;
import com.madhouse.platform.premiummad.media.tencent.util.TencentHttpUtil;
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

	@Value("${tencent.adcreativeList}")
	private String advertStatusUrl;

	@Value("${tencent.dsp_id}")
	private String dsp_id;
	
	@Value("${tencent.token}")
	private String token;
	
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
				continue;
			}

			// 获取腾讯方的 dspOrderId
			List<AdvertBachStatusData> dspOrderIds = new ArrayList<AdvertBachStatusData>();
			for (Material material : unauditMaterials) {
				AdvertBachStatusData item = new AdvertBachStatusData();
				item.setDsp_order_id(material.getMediaQueryKey());
				dspOrderIds.add(item);
			}

			// 向腾讯发请求批量获取状态
			TencentCommonRequest<List<AdvertBachStatusData>> request = new TencentCommonRequest<List<AdvertBachStatusData>>();
			request.setData(dspOrderIds);
			LOGGER.info("Tencent批量获取广告的审核请求：{}",  JSONObject.toJSONString(dspOrderIds));
			String responseJson = tencentHttpUtil.post(advertStatusUrl, request);
			LOGGER.info("Tencent批量获取广告的审核状态返回:{}", responseJson);

			// 处理返回的结果
			AdvertBatchStatusResponse advertBatchStatusResponse = JSON.parseObject(responseJson, AdvertBatchStatusResponse.class);
			if (advertBatchStatusResponse.getRet_code() == 0) {
				List<RetMsg> records = advertBatchStatusResponse.getRet_msg();
				if (records != null && records.size() > 0) {
					List<MaterialAuditResultModel> auditResults = new ArrayList<MaterialAuditResultModel>();
					for (RetMsg item : records) {
						MaterialAuditResultModel auditItem = new MaterialAuditResultModel();
						int status = item.getStatus();
						auditItem.setMediaQueryKey(item.getDsp_order_id());
						auditItem.setMediaId(String.valueOf(mediaId));
						if (TencentAduitStatus.AUDITED.getValue() == status) { // 审核通过
							auditItem.setStatus(MaterialStatusCode.MSC10004.getValue());
							auditResults.add(auditItem);
						} else if (TencentAduitStatus.REJUSED.getValue() == status) { // 审核驳回
							auditItem.setStatus(MaterialStatusCode.MSC10001.getValue());
							auditItem.setErrorMessage(item.getVinfo());
							auditResults.add(auditItem);
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
