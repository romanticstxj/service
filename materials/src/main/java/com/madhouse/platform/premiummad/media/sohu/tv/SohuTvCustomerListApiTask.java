package com.madhouse.platform.premiummad.media.sohu.tv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.madhouse.platform.premiummad.constant.AdvertiserStatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dao.AdvertiserMapper;
import com.madhouse.platform.premiummad.entity.Advertiser;
import com.madhouse.platform.premiummad.media.sohu.response.SohuCustomerListDetail;
import com.madhouse.platform.premiummad.media.sohu.response.SohuCustomerListResponse;
import com.madhouse.platform.premiummad.media.sohu.response.SohuResponse;
import com.madhouse.platform.premiummad.media.sohu.util.SohuNewsAuth;
import com.madhouse.platform.premiummad.model.AdvertiserAuditResultModel;
import com.madhouse.platform.premiummad.service.IAdvertiserService;
import com.madhouse.platform.premiummad.service.IMediaService;
import com.madhouse.platform.premiummad.util.HttpUtils;

@Component
public class SohuTvCustomerListApiTask {

	private final static Logger LOGGER = LoggerFactory.getLogger(SohuTvCustomerListApiTask.class);

	@Value("${sohu.customer.list}")
	private String cutomerListUrl;

	@Value("${advertier_meidaGroupMapping_sohuTV}")
	private String mediaGroupStr;
	
	@Autowired
	private SohuNewsAuth sohuAuth;

	@Autowired
	private AdvertiserMapper advertiserDao;

	@Autowired
	private IAdvertiserService advertiserService;

	@Autowired
	private IMediaService mediaService;
	
	/**
	 * 查询搜狐TV广告主审核状态
	 */
	public void list() {
		LOGGER.info("++++++++++Sohu TV get advertiser list begin+++++++++++");

		/* 代码配置处理方式
		// 媒体组没有映射到具体的媒体不处理
		String value = MediaTypeMapping.getValue(MediaTypeMapping.SOHUTV.getGroupId());
		if (StringUtils.isBlank(value)) {
			return;
		}

		// 获取媒体组下的具体媒体
		int[] mediaIds = StringUtils.splitToIntArray(value);
		*/

		// 根据媒体组ID和审核对象获取具体的媒体ID
		int[] mediaIds = mediaService.getMeidaIds(mediaGroupStr, SystemConstant.MediaAuditObject.ADVERTISER);

		// 媒体组没有映射到具体的媒体不处理
		if (mediaIds == null || mediaIds.length < 1) {
			return;
		}
		// 获取我方媒体待审核的广告主
		List<Advertiser> unAuditAdvertisers = advertiserDao.selectAdvertisersByMedias(mediaIds, AdvertiserStatusCode.ASC10003.getValue());

		if (unAuditAdvertisers == null || unAuditAdvertisers.isEmpty()) {
			/*LOGGER.info(MediaMapping.getDescrip(mediaIds) + "无需要审核的广告主");*/
			LOGGER.info("Sohu TV无需要审核的广告主");
			return;
		}

		List<AdvertiserAuditResultModel> auditResults = new ArrayList<AdvertiserAuditResultModel>();
		for (Advertiser item : unAuditAdvertisers) {
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("customer_key", item.getMediaAdvertiserKey());
			paramMap.put("customer_name", item.getAdvertiserName());
			paramMap.put("perpage", 50);
			paramMap.put("page", 1);
			String request = sohuAuth.setHttpMethod("GET").setApiUrl(cutomerListUrl).setParamMap(paramMap).buildRequest();
			LOGGER.info("SohuTvCustomerListApiTask.list param request:{}", request);
			String url = cutomerListUrl + "?" + request;
			Map<String, Object> getMap = HttpUtils.get(url);
			String result = (String) getMap.get("responseBody");
			LOGGER.info("SohuTvCustomerListApiTask.list http get:{}. result json: {}", url, result);
			SohuResponse sohuResponse = JSONObject.parseObject(result, SohuResponse.class);

			if (sohuResponse != null) {
				if (sohuResponse.isStatus()) {
					SohuCustomerListResponse sohuCustomerListResponse = JSONObject.parseObject(sohuResponse.getContent().toString(), SohuCustomerListResponse.class);
					List<SohuCustomerListDetail> sohuCustomerListDetails = sohuCustomerListResponse.getItems();
					handleResults(item, sohuCustomerListDetails, auditResults);
				}
			}
		}

		// 更新数据库
		if (!auditResults.isEmpty()) {
			advertiserService.updateStatusToMedia(auditResults);
		}
	}

	/**
	 * 处理查询结果
	 * 
	 * @param unauditAdvertiser
	 * @param sohuCustomerListDetails
	 */
	private void handleResults(Advertiser unauditAdvertiser, List<SohuCustomerListDetail> sohuCustomerListDetails, List<AdvertiserAuditResultModel> auditResults) {
		if (sohuCustomerListDetails == null || sohuCustomerListDetails.size() != 1) {
			return;
		}

		// 根据返回状态处理我方数据
		String customerKeyNet = sohuCustomerListDetails.get(0).getCustomer_key();
		SohuCustomerListDetail sohuCustomerDetail = sohuCustomerListDetails.get(0);
		// 获取搜狐TV的审核状态
		Integer statusNet = sohuCustomerDetail.getStatus();
		// 根据广告key判断
		if (unauditAdvertiser.getMediaAdvertiserKey().equals(customerKeyNet)) {
			Integer changedStatusNet = null;
			// 媒体未审核
			if (statusNet == 0) {
				changedStatusNet = AdvertiserStatusCode.ASC10003.getValue();
			}
			// 媒体审核通过
			if (statusNet == 1) {
				changedStatusNet = AdvertiserStatusCode.ASC10004.getValue();
			}
			// 媒体审核拒绝
			if (statusNet == 2) {
				changedStatusNet = AdvertiserStatusCode.ASC10001.getValue();
			}

			// 状态改变的更新我方广告主信息
			if (changedStatusNet != null && unauditAdvertiser.getStatus().intValue() != changedStatusNet.intValue()) {
				AdvertiserAuditResultModel auditItem = new AdvertiserAuditResultModel();
				auditItem.setId(String.valueOf(unauditAdvertiser.getId()));
				auditItem.setMediaAdvertiserKey(unauditAdvertiser.getMediaAdvertiserKey());
				auditItem.setStatus(changedStatusNet);
				auditItem.setErrorMessage(sohuCustomerDetail.getAudit_info());
				int[] mediaIds = {unauditAdvertiser.getMediaId().intValue()};
				auditItem.setMediaIds(mediaIds);
				auditResults.add(auditItem);
			}
		} else {
			LOGGER.info("返回结果与请求不匹配");
		}
	}
}
