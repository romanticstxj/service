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

import com.alibaba.fastjson.JSONObject;
import com.madhouse.platform.premiummad.constant.AdvertiserStatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dao.AdvertiserMapper;
import com.madhouse.platform.premiummad.entity.Advertiser;
import com.madhouse.platform.premiummad.media.iqiyi.constant.IQiYiConstant;
import com.madhouse.platform.premiummad.media.iqiyi.response.IQiyiCustomerStatusDetail;
import com.madhouse.platform.premiummad.media.iqiyi.response.IQiyiCustomerStatusResponse;
import com.madhouse.platform.premiummad.media.iqiyi.util.IQiYiHttpUtils;
import com.madhouse.platform.premiummad.model.AdvertiserAuditResultModel;
import com.madhouse.platform.premiummad.service.IAdvertiserService;
import com.madhouse.platform.premiummad.service.IMediaService;

@Component("iQiyiCustomerStatusApiTask")
public class IQiyiCustomerStatusApiTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(IQiyiCustomerStatusApiTask.class);

	@Value("${iqiyi.customer.batchStatus}")
	private String batchStatusUrl;

	@Autowired
	private IQiYiHttpUtils iQiYiHttpUtils;

	@Autowired
	private AdvertiserMapper advertiserDao;

	@Autowired
	private IAdvertiserService advertiserService;

	@Value("${advertier_meidaGroupMapping_iqyi}")
	private String mediaGroupStr;

	@Autowired
	private IMediaService mediaService;

	public void batchStatus() {
		LOGGER.info("++++++++++iqiyi get customer status begin+++++++++++");

		// 根据媒体组ID和审核对象获取具体的媒体ID
		int[] mediaIds = mediaService.getMeidaIds(mediaGroupStr, SystemConstant.MediaAuditObject.ADVERTISER);

		// 媒体组没有映射到具体的媒体不处理
		if (mediaIds == null || mediaIds.length < 1) {
			return;
		}

		// 获取我方媒体待审核的广告主
		List<Advertiser> unAuditAdvertisers = advertiserDao.selectAdvertisersByMedias(mediaIds, AdvertiserStatusCode.ASC10003.getValue());
		if (unAuditAdvertisers == null || unAuditAdvertisers.isEmpty()) {
			LOGGER.info("++++++++++iqiyi no advertisers need to audit+++++++++++");
			return;
		}

		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < unAuditAdvertisers.size(); i++) {
			// 设定每50条提交查询一次，避免超出浏览器地址对get请求大小限制，不超过规定侧一次发送，超过侧按照规定每50条广告主id提交一次
			stringBuilder.append(unAuditAdvertisers.get(i).getId() + ",");
			if (i != 0 && i % 50 == 0) {
				executeNet(stringBuilder, unAuditAdvertisers, mediaIds);
			}
		}
		// 分页不足规定的侧直接提交
		if (stringBuilder.length() != 0) {
			executeNet(stringBuilder, unAuditAdvertisers, mediaIds);
		}

		LOGGER.info("++++++++++iqiyi get customer status end+++++++++++");
	}

	/**
	 * 批量处理
	 * 
	 * @param stringBuilder
	 * @param advertisers
	 */
	private void executeNet(StringBuilder stringBuilder, List<Advertiser> advertisers, int[] mediaIds) {
		String batchAid = stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1);
		Map<String, String> param = new HashMap<>();
		param.put("batch", batchAid);
		LOGGER.info("IQiyiCustomerStatusApiTask request:{}, param:{}", batchStatusUrl, param);
		String result = iQiYiHttpUtils.get(batchStatusUrl, param);
		LOGGER.info("IQiyiCustomerStatusApiTask response:{}", result);
		IQiyiCustomerStatusResponse iQiyiCustomerStatusResponse = JSONObject.parseObject(result, IQiyiCustomerStatusResponse.class);

		if (iQiyiCustomerStatusResponse != null) {
			String code = iQiyiCustomerStatusResponse.getCode();
			// 0 代表返回成功
			if (code.equals(String.valueOf(IQiYiConstant.RESPONSE_SUCCESS.getValue()))) {
				List<IQiyiCustomerStatusDetail> iQiyiCustomerStatusDetails = iQiyiCustomerStatusResponse.getResults();
				processResult(iQiyiCustomerStatusDetails, mediaIds);
			} else {
				LOGGER.info("获取状态失败");
			}
		}
	}

	/**
	 * 根据媒体方返回结果处理我方数据
	 * 
	 * @param iQiyiCustomerStatusDetails
	 */
	private void processResult(List<IQiyiCustomerStatusDetail> iQiyiCustomerStatusDetails, int[] mediaIds) {
		List<AdvertiserAuditResultModel> auditResults = new ArrayList<AdvertiserAuditResultModel>();
		for (IQiyiCustomerStatusDetail iQiyiCustomerStatusDetail : iQiyiCustomerStatusDetails) {
			AdvertiserAuditResultModel auditItem = new AdvertiserAuditResultModel();
			auditItem.setMediaAdvertiserKey(String.valueOf(iQiyiCustomerStatusDetail.getAd_id()));
			auditItem.setMediaIds(mediaIds);
			// 审核通过
			if ("PASS".equals(iQiyiCustomerStatusDetail.getStatus())) {
				auditItem.setStatus(AdvertiserStatusCode.ASC10004.getValue());
				auditResults.add(auditItem);
			}

			// 审核不通过
			if ("UNPASS".equals(iQiyiCustomerStatusDetail.getStatus())) {
				auditItem.setStatus(AdvertiserStatusCode.ASC10001.getValue());
				auditItem.setErrorMessage(iQiyiCustomerStatusDetail.getReason());
				auditResults.add(auditItem);
			}

			// 未审核
			if ("WAIT".equals(iQiyiCustomerStatusDetail.getStatus())) {
				LOGGER.info("广告主尚未审核[advertiserID=" + iQiyiCustomerStatusDetail.getAd_id() + "]");
			}
		}

		// 更新数据库
		if (!auditResults.isEmpty()) {
			advertiserService.updateStatusToMedia(auditResults);
		}
	}
}
