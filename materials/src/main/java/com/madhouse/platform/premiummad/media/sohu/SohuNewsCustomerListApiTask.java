package com.madhouse.platform.premiummad.media.sohu;

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
import com.madhouse.platform.premiummad.constant.MediaMapping;
import com.madhouse.platform.premiummad.dao.AdvertiserMapper;
import com.madhouse.platform.premiummad.entity.Advertiser;
import com.madhouse.platform.premiummad.media.model.SohuCustomerListDetail;
import com.madhouse.platform.premiummad.media.model.SohuCustomerListResponse;
import com.madhouse.platform.premiummad.media.model.SohuResponse;
import com.madhouse.platform.premiummad.model.AdvertiserAuditResultModel;
import com.madhouse.platform.premiummad.service.IAdvertiserService;
import com.madhouse.platform.premiummad.util.HttpUtils;

@Component
public class SohuNewsCustomerListApiTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(SohuNewsCustomerListApiTask.class);

	@Value("${sohu.customer.list}")
	private String cutomerListUrl;

	@Autowired
	private SohuNewsAuth sohuAuth;

	@Autowired
	private AdvertiserMapper advertiserDao;

	@Autowired
	private IAdvertiserService advertiserService;

	/**
	 * 查询搜狐新闻广告主审核状态
	 */
	public void list() {
		LOGGER.info("++++++++++Sohu News get advertiser list begin+++++++++++");

		// 获取我方媒体待审核的广告主
		List<Advertiser> unAuditAdvertisers = advertiserDao.selectMediaAdvertisers(MediaMapping.SOHUNEWS.getValue(), AdvertiserStatusCode.ASC10003.getValue());

		if (unAuditAdvertisers == null || unAuditAdvertisers.isEmpty()) {
			LOGGER.info("++++++++++Sohu News no advertisers need to audit+++++++++++");
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
			LOGGER.info("SohuNewsCustomerListApiTask.list param request:{}", request);
			String url = cutomerListUrl + "?" + request;
			Map<String, Object> getMap = HttpUtils.get(url);
			String result = (String) getMap.get("responseBody");
			LOGGER.info("SohuNewsCustomerListApiTask.list http get:{}. result json: {}", url, result);
			SohuResponse sohutvResponse = JSONObject.parseObject(result, SohuResponse.class);

			if (sohutvResponse != null) {
				if (sohutvResponse.isStatus()) {
					SohuCustomerListResponse sohuCustomerListResponse = JSONObject.parseObject(sohutvResponse.getContent().toString(), SohuCustomerListResponse.class);
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
		// 获取搜狐新闻的审核状态
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
				auditItem.setMediaId(String.valueOf(MediaMapping.SOHUNEWS.getValue()));
				auditResults.add(auditItem);
			}
		} else {
			LOGGER.info("返回结果与请求不匹配");
		}
	}
}
