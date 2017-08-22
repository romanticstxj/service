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
import com.madhouse.platform.premiummad.constant.MaterialStatusCode;
import com.madhouse.platform.premiummad.constant.MediaMapping;
import com.madhouse.platform.premiummad.dao.AdvertiserMapper;
import com.madhouse.platform.premiummad.entity.Advertiser;
import com.madhouse.platform.premiummad.media.model.SohuResponse;
import com.madhouse.platform.premiummad.model.AdvertiserAuditResultModel;
import com.madhouse.platform.premiummad.service.IAdvertiserService;
import com.madhouse.platform.premiummad.util.HttpUtils;

@Component
public class SohuTvCustomerCreateApiTask {

	private final static Logger LOGGER = LoggerFactory.getLogger(SohuTvCustomerCreateApiTask.class);

	@Value("${sohu.customer.create}")
	private String customerCreateUrl;

	@Autowired
	private SohuAuth sohuAuth;

	@Autowired
	private AdvertiserMapper advertiserDao;

	@Autowired
	private IAdvertiserService advertiserService;

	public void create() {
		LOGGER.info("++++++++++Sohu TV upload advertiser begin+++++++++++");

		// 查询所有待审核且媒体的广告主的审核状态是媒体审核的
		List<Advertiser> unSubmitAdvertisers = advertiserDao.selectMediaAdvertisers(MediaMapping.SOHUTV.getValue(), AdvertiserStatusCode.ASC10002.getValue());
		if (unSubmitAdvertisers == null || unSubmitAdvertisers.isEmpty()) {
			LOGGER.info("搜狐TV没有未上传的广告主");
			LOGGER.info("++++++++++Sohu TV upload advertiser end+++++++++++");
			return;
		}

		// 上传到媒体
		LOGGER.info("SohuTvCustomerCreateApiTask-sohuNews", unSubmitAdvertisers.size());
		Map<Integer, String> advertiserIdKeys = new HashMap<Integer, String>();
		List<AdvertiserAuditResultModel> rejusedAdvertisers = new ArrayList<AdvertiserAuditResultModel>();
		for (Advertiser advertiser : unSubmitAdvertisers) {
			Map<String, Object> paramMap = buildCreatePara(advertiser);
			String request = sohuAuth.setHttpMethod("POST").setApiUrl(customerCreateUrl).setParamMap(paramMap).buildRequest();
			LOGGER.info("SohuTvCustomerCreateApiTask.reqquest: {}", request);
			String result = HttpUtils.post(customerCreateUrl, request);
			LOGGER.info("SohuTvCustomerCreateApiTask.udpate http post:{}. result json: {}", customerCreateUrl, result);
			SohuResponse sohutvResponse = JSONObject.parseObject(result, SohuResponse.class);
			if (sohutvResponse != null) {
				if (sohutvResponse.isStatus()) {
					String customKey = sohutvResponse.getContent().toString();
					advertiserIdKeys.put(advertiser.getId(), customKey);
				} else {
					AdvertiserAuditResultModel rejuseItem = new AdvertiserAuditResultModel();
					rejuseItem.setId(String.valueOf(advertiser.getId()));
					rejuseItem.setStatus(MaterialStatusCode.MSC10001.getValue());
					rejuseItem.setMediaId(String.valueOf(MediaMapping.SOHUTV.getValue()));
					rejuseItem.setErrorMessage(sohutvResponse.getMessage());
					rejusedAdvertisers.add(rejuseItem);
					LOGGER.error("广告主[advertiserId=" + advertiser.getId() + "]上传失败-" + result);
				}
			} else {
				LOGGER.error("广告主[advertiserId=" + advertiser.getId() + "]上传失败");
			}
		}

		// 更新我方系统状态为审核中
		if (!advertiserIdKeys.isEmpty()) {
			advertiserService.updateStatusAfterUpload(advertiserIdKeys);
		}
		
		// 处理失败的结果，自动驳回 - 通过广告位id更新
		if (!rejusedAdvertisers.isEmpty()) {
			advertiserService.updateStatusToMediaByAdvertiserId(rejusedAdvertisers);
		}

		LOGGER.info("++++++++++Sohu TV upload advertiser end+++++++++++");
	}

	/**
	 * 构造上传广告主参数
	 * 
	 * @param advertiser
	 * @return
	 */
	private Map<String, Object> buildCreatePara(Advertiser advertiser) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("customer_name", advertiser.getAdvertiserName());
		paramMap.put("customer_website", advertiser.getWebsite());
		paramMap.put("company_address", advertiser.getAddress());
		paramMap.put("contact", advertiser.getContact());
		paramMap.put("phone_number", advertiser.getPhone());
		return paramMap;
	}
}
