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
import com.madhouse.platform.premiummad.constant.AdvertiserStatusCode;
import com.madhouse.platform.premiummad.constant.MaterialStatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dao.AdvertiserMapper;
import com.madhouse.platform.premiummad.dao.MaterialMapper;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.media.tencent.constant.TencentMaterialAduitStatus;
import com.madhouse.platform.premiummad.media.tencent.request.TencentCommonRequest;
import com.madhouse.platform.premiummad.media.tencent.request.TencentMaterialStatusData;
import com.madhouse.platform.premiummad.media.tencent.response.TencentMaterailStatusRetMsg;
import com.madhouse.platform.premiummad.media.tencent.response.TencentMaterialStatusResponse;
import com.madhouse.platform.premiummad.media.tencent.util.TencentHttpUtil;
import com.madhouse.platform.premiummad.model.MaterialAuditResultModel;
import com.madhouse.platform.premiummad.service.IAdvertiserService;
import com.madhouse.platform.premiummad.service.IMaterialService;
import com.madhouse.platform.premiummad.service.IMediaService;

/**
 * 批量获取广告的审核状态(物料审核)
 */
@Component
public class TencentMaterialStatusApiTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(TencentMaterialStatusApiTask.class);
	private static final int ITERATOR_TIMES = 2;
	private static final int TECENT_OTV_ITERATOR = 1;

	@Value("${tencent.adcreativeList}")
	private String advertStatusUrl;
	
	@Value("${material_meidaGroupMapping_tencentNotOtv}")
	private String mediaNotOtvGroupStr;

	@Value("${material_meidaGroupMapping_tencentOtv}")
	private String mediaOtvGroupStr;
	
	@Autowired
	private TencentHttpUtil tencentHttpUtil;

	@Autowired
	private MaterialMapper materialDao;

	@Autowired
	private IMaterialService materialService;

	@Autowired
	private AdvertiserMapper advertiserDao;

	@Autowired
	private IAdvertiserService advertiserService;

	@Autowired
	private IMediaService mediaService;

	public void getMaterialStatus() {
		// TENCENT 对应两个媒体 OTV 和 非 OTV
		for (int mediaType = 0; mediaType < ITERATOR_TIMES; mediaType++) {
			/*代码配置处理方式
			int mediaIdGroup = 0;
			if (mediaType != TECENT_OTV_ITERATOR) {
				mediaIdGroup = MediaTypeMapping.TENCENT_NOT_OTV.getGroupId();
			} else {
				mediaIdGroup = MediaTypeMapping.TENCENT.getGroupId();
			}

			// 媒体组没有映射到具体的媒体不处理
			String value = MediaTypeMapping.getValue(mediaIdGroup);
			if (StringUtils.isBlank(value)) {
				return;
			}

			// 获取媒体组下的具体媒体
			int[] mediaIds = StringUtils.splitToIntArray(value);
			*/
			
			String mediaGroupStr = "";
			if (mediaType != TECENT_OTV_ITERATOR) {
				mediaGroupStr = mediaNotOtvGroupStr;
			} else {
				mediaGroupStr = mediaOtvGroupStr;
			}
			// 根据媒体组ID和审核对象获取具体的媒体ID
			int[] mediaIds = mediaService.getMeidaIds(mediaGroupStr, SystemConstant.MediaAuditObject.MATERIAL);
			
			// 获取审核中的素材
			List<Material> unauditMaterials = materialDao.selectMaterialsByMeidaIds(mediaIds, MaterialStatusCode.MSC10003.getValue());
			if (unauditMaterials == null || unauditMaterials.isEmpty()) {
				/*LOGGER.info(MediaMapping.getDescrip(mediaIds) + "无需要审核的素材");*/
				LOGGER.info("Tencent" + mediaIds + "无需要审核的素材");
				continue;
			}

			// 获取腾讯方的 dspOrderId
			List<TencentMaterialStatusData> dspOrderIds = new ArrayList<TencentMaterialStatusData>();
			for (Material material : unauditMaterials) {
				TencentMaterialStatusData item = new TencentMaterialStatusData();
				item.setDsp_order_id(material.getMediaQueryKey());
				dspOrderIds.add(item);
			}

			// 向腾讯发请求批量获取状态
			TencentCommonRequest<List<TencentMaterialStatusData>> request = new TencentCommonRequest<List<TencentMaterialStatusData>>();
			request.setData(dspOrderIds);
			LOGGER.info("Tencent批量获取广告的审核请求：{}",  JSONObject.toJSONString(dspOrderIds));
			String responseJson = tencentHttpUtil.post(advertStatusUrl, request);
			LOGGER.info("Tencent批量获取广告的审核状态返回:{}", responseJson);

			// 处理返回的结果
			TencentMaterialStatusResponse advertBatchStatusResponse = JSON.parseObject(responseJson, TencentMaterialStatusResponse.class);
			if (advertBatchStatusResponse.getRet_code() == 0) {
				List<TencentMaterailStatusRetMsg> records = advertBatchStatusResponse.getRet_msg();
				if (records != null && records.size() > 0) {
					List<MaterialAuditResultModel> auditResults = new ArrayList<MaterialAuditResultModel>();
					for (TencentMaterailStatusRetMsg item : records) {
						MaterialAuditResultModel auditItem = new MaterialAuditResultModel();
						int status = item.getStatus();
						auditItem.setMediaQueryKey(item.getDsp_order_id());
						auditItem.setMediaIds(mediaIds);
						if (TencentMaterialAduitStatus.AUDITED.getValue() == status) { // 审核通过
							auditItem.setStatus(AdvertiserStatusCode.ASC10004.getValue());
							auditResults.add(auditItem);
						} else if (TencentMaterialAduitStatus.REJUSED.getValue() == status) { // 审核驳回
							auditItem.setStatus(AdvertiserStatusCode.ASC10001.getValue());
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

			LOGGER.info("Tencent" + mediaIds + " TencentMaterialStatusApiTask-getMaterialStatus end");
		}
	}
}
