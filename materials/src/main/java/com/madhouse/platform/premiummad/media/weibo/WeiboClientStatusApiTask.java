package com.madhouse.platform.premiummad.media.weibo;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.constant.AdvertiserStatusCode;
import com.madhouse.platform.premiummad.constant.MediaMapping;
import com.madhouse.platform.premiummad.dao.AdvertiserMapper;
import com.madhouse.platform.premiummad.entity.Advertiser;
import com.madhouse.platform.premiummad.media.weibo.constant.WeiboConstant;
import com.madhouse.platform.premiummad.media.weibo.request.WeiboClientStatusRequest;
import com.madhouse.platform.premiummad.media.weibo.response.WeiboClientStatusDetail;
import com.madhouse.platform.premiummad.media.weibo.response.WeiboClientStatusResponse;
import com.madhouse.platform.premiummad.model.AdvertiserAuditResultModel;
import com.madhouse.platform.premiummad.service.IAdvertiserService;
import com.madhouse.platform.premiummad.util.HttpUtils;

@Component
public class WeiboClientStatusApiTask {

	private Logger LOGGER = LoggerFactory.getLogger(WeiboClientStatusApiTask.class);

	@Value("${weibo.clientStatusUrl}")
	private String clientStatusUrl;

	@Value("${weibo.dspid}")
	private String dspid;

	@Value("${weibo.token}")
	private String token;

	@Autowired
	private AdvertiserMapper advertiserDao;

	@Autowired
	private IAdvertiserService advertiserService;

	public void getStatus() {
		LOGGER.info("++++++++++Weibo get client status begin+++++++++++");

		// 获取我方媒体待审核的广告主
		List<Advertiser> unAuditAdvertisers = advertiserDao.selectMediaAdvertisers(MediaMapping.WEIBO.getValue(), AdvertiserStatusCode.ASC10003.getValue());
		if (unAuditAdvertisers == null || unAuditAdvertisers.isEmpty()) {
			LOGGER.info("++++++++++Weibo no clients need to audit+++++++++++");
			return;
		}

		// 构造需要查询的客户ID列表
		List<String> clientIds = new ArrayList<String>();
		for (Advertiser advertiser : unAuditAdvertisers) {
			clientIds.add(advertiser.getMediaAdvertiserKey());
		}

		// 构造请求
		WeiboClientStatusRequest request = new WeiboClientStatusRequest();
		request.setClient_ids(clientIds);
		request.setDspid(dspid);
		request.setToken(token);

		// 向媒体发送请求
		String requestJson = JSON.toJSONString(request);
		LOGGER.info("WeiboClientStatusApiTask request:" + requestJson);
		String responseJson = HttpUtils.post(clientStatusUrl, requestJson);
		LOGGER.info("WeiboClientStatusApiTask response:" + JSON.toJSONString(responseJson));

		// 处理我方数据
		if (!StringUtils.isEmpty(requestJson)) {
			WeiboClientStatusResponse weiboClientStatusResponse = JSON.parseObject(responseJson, WeiboClientStatusResponse.class);
			Integer retCode = weiboClientStatusResponse.getRet_code();
			if (WeiboConstant.RESPONSE_SUCCESS.getValue() == retCode) {
				List<AdvertiserAuditResultModel> auditResults = new ArrayList<AdvertiserAuditResultModel>();
				List<WeiboClientStatusDetail> weiboClientStatusDetails = weiboClientStatusResponse.getRet_msg();

				// 根据审核结果处理
				for (WeiboClientStatusDetail weiboClientStatusDetail : weiboClientStatusDetails) {
					AdvertiserAuditResultModel auditItem = new AdvertiserAuditResultModel();
					auditItem.setMediaAdvertiserKey(String.valueOf(weiboClientStatusDetail.getClient_id()));
					auditItem.setMediaId(String.valueOf(MediaMapping.WEIBO.getValue()));

					String status = weiboClientStatusDetail.getVerify_status();
					if (WeiboConstant.C_STATUS_APPROVED.getDescription().equals(status)) { // 审核通过
						auditItem.setStatus(AdvertiserStatusCode.ASC10004.getValue());
						auditResults.add(auditItem);
					} else if (WeiboConstant.C_STATUS_UNAUDITED.getDescription().equals(status)) { // 未审核
						LOGGER.info("广告主尚未审核[meidaAdvertiserKey=" + weiboClientStatusDetail.getClient_id() + "]");
					} else if (WeiboConstant.C_STATUS_REFUSED.getDescription().equals(status)) { // 驳回
						auditItem.setStatus(AdvertiserStatusCode.ASC10001.getValue());
						auditItem.setErrorMessage(weiboClientStatusDetail.getVerify_info());
						auditResults.add(auditItem);
					}
				}

				// 更新数据库
				if (!auditResults.isEmpty()) {
					advertiserService.updateStatusToMedia(auditResults);
				}
			} else {
				LOGGER.info("获取状态失败-" + requestJson);
			}
		} else {
			LOGGER.info("获取状态时请求失败");
		}

		LOGGER.info("++++++++++Weibo get client status end+++++++++++");
	}
}
