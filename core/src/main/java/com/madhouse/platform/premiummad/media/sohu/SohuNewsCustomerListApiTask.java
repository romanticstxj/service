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
import com.madhouse.platform.premiummad.media.model.SohutvResponse;
import com.madhouse.platform.premiummad.model.AdvertiserAuditResultModel;
import com.madhouse.platform.premiummad.service.IAdvertiserService;
import com.madhouse.platform.premiummad.util.HttpUtils;

@Component
public class SohuNewsCustomerListApiTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(SohuNewsCustomerListApiTask.class);

	@Value("${sohu.customer.list}")
	private String cutomerListUrl;

	@Value("#{'${sohu.customer.pageList}'.split(',')}")
	private List<String> pageList;

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
		for (String page : pageList) {
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("perpage", 50);
			paramMap.put("page", page);
			String request = sohuAuth.setHttpMethod("GET").setApiUrl(cutomerListUrl).setParamMap(paramMap).buildRequest();
			LOGGER.info("SohuNewsCustomerListApiTask.list param page:{} request:{}", page, request);
			String url = cutomerListUrl + "?" + request;
			Map<String, Object> getMap = HttpUtils.get(url);
			String result = (String) getMap.get("responseBody");
			LOGGER.info("SohuNewsCustomerListApiTask.list http get:{}. result json: {}", url, result);
			SohutvResponse sohutvResponse = JSONObject.parseObject(result, SohutvResponse.class);

			if (sohutvResponse != null) {
				if (sohutvResponse.isStatus()) {
					SohuCustomerListResponse sohuCustomerListResponse = JSONObject.parseObject(sohutvResponse.getContent().toString(), SohuCustomerListResponse.class);
					List<SohuCustomerListDetail> sohuCustomerListDetails = sohuCustomerListResponse.getItems();
					handleResults(sohuCustomerListDetails);
				}
			}
		}
	}

	/**
	 * 处理查询结果
	 * 
	 * @param sohuCustomerListDetails
	 */
	private void handleResults(List<SohuCustomerListDetail> sohuCustomerListDetails) {
		if (sohuCustomerListDetails == null || sohuCustomerListDetails.isEmpty()) {
			return;
		}

		// 我方系统的广告主
		List<Advertiser> unAuditAdvertisers = advertiserDao.selectMediaAdvertisers(MediaMapping.SOHUNEWS.getValue(), AdvertiserStatusCode.ASC10003.getValue());

		// 根据返回状态处理我方数据
		List<AdvertiserAuditResultModel> auditResults = new ArrayList<AdvertiserAuditResultModel>();
		for (Advertiser advertiser : unAuditAdvertisers) {
			String mediaAdvertiserKeyDb = advertiser.getMediaAdvertiserKey();
			Integer statusDb = advertiser.getStatus().intValue();

			for (SohuCustomerListDetail sohuCustomerListDetail : sohuCustomerListDetails) {
				String customerKeyNet = sohuCustomerListDetail.getCustomer_key();
				// 获取搜狐新闻的审核状态
				Integer statusNet = sohuCustomerListDetail.getStatus();
				// 根据广告key判断
				if (mediaAdvertiserKeyDb.equals(customerKeyNet)) {
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
					if (changedStatusNet != null && statusDb.intValue() != changedStatusNet.intValue()) {
						AdvertiserAuditResultModel auditItem = new AdvertiserAuditResultModel();
						auditItem.setId(String.valueOf(advertiser.getId()));
						auditItem.setStatus(changedStatusNet);
						auditResults.add(auditItem);
					}
				}
			}
		}
		
		// 更新数据库
		if (!auditResults.isEmpty()) {
			advertiserService.updateStatusToMedia(auditResults);
		}
	}
}
