package com.madhouse.platform.premiummad.media.sohu;

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
import com.madhouse.platform.premiummad.media.model.SohutvResponse;
import com.madhouse.platform.premiummad.service.IAdvertiserService;
import com.madhouse.platform.premiummad.util.HttpUtils;

@Component
public class SohuNewsCustomerCreateApiTask {

	private Logger LOGGER = LoggerFactory.getLogger(SohuNewsCustomerCreateApiTask.class);

	@Value("${sohu.customer.create}")
	private String customerCreateUrl;

	@Autowired
	private SohuNewsAuth sohuAuth;

	@Autowired
	private AdvertiserMapper advertiserDao;

	@Autowired
	private IAdvertiserService advertiserService;

	/**
	 * 上传搜狐新闻广告主
	 */
	public void create() {
		LOGGER.info("++++++++++Sohu News upload advertiser begin+++++++++++");

		// 查询所有待审核且媒体的广告主的审核状态是媒体审核的
		List<Advertiser> unSubmitAdvertisers = advertiserDao.selectMediaAdvertisers(MediaMapping.SOHUNEWS.getValue(), AdvertiserStatusCode.ASC10002.getValue());
		if (unSubmitAdvertisers == null || unSubmitAdvertisers.isEmpty()) {
			LOGGER.info("搜狐没有未上传的广告主");
			LOGGER.info("++++++++++Sohu News upload advertiser end+++++++++++");
			return;
		}

		// 上传到媒体
		LOGGER.info("SohuCustomerCreateApiTask-sohuNews", unSubmitAdvertisers.size());
		Map<Integer, String> advertiserIdKeys = new HashMap<Integer, String>();
		for (Advertiser advertiser : unSubmitAdvertisers) {
			Map<String, Object> paramMap = buildCreatePara(advertiser);
			String request = sohuAuth.setHttpMethod("POST").setApiUrl(customerCreateUrl).setParamMap(paramMap).buildRequest();
			LOGGER.info("SohuCustomerCreateApiTask.reqquest: {}", request);
			String result = HttpUtils.post(customerCreateUrl, request);
			LOGGER.info("SohuCustomerCreateApiTask.udpate http post:{}. result json: {}", customerCreateUrl, result);
			SohutvResponse sohutvResponse = JSONObject.parseObject(result, SohutvResponse.class);
			if (sohutvResponse != null) {
				if (sohutvResponse.isStatus()) {
					String customKey = sohutvResponse.getContent().toString();
					advertiserIdKeys.put(advertiser.getId(), customKey);
				} else {
					LOGGER.error("广告主[advertiserId=" + advertiser.getId() + "]上传失败");
				}
			}
		}

		// 更新我方系统状态为审核中
		if (!advertiserIdKeys.isEmpty()) {
			advertiserService.updateStatusAfterUpload(advertiserIdKeys);
		}

		LOGGER.info("++++++++++Sohu News upload advertiser end+++++++++++");
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
