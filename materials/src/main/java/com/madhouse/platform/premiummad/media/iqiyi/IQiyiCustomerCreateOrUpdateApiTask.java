package com.madhouse.platform.premiummad.media.iqiyi;

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
import com.madhouse.platform.premiummad.media.constant.IQiYiConstant;
import com.madhouse.platform.premiummad.media.model.IQiyiCustomerResponse;
import com.madhouse.platform.premiummad.media.util.IQiYiHttpUtils;
import com.madhouse.platform.premiummad.service.IAdvertiserService;

@Component("iQiyiCustomerCreateOrUpdateApiTask")
public class IQiyiCustomerCreateOrUpdateApiTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(IQiyiCustomerCreateOrUpdateApiTask.class);
	
	@Value("${iqiyi.customer.createOrUpdate}")
	private String createOrUpdateUrl;
	
	@Autowired
	private IQiYiHttpUtils iQiYiHttpUtils;
	
	@Autowired
	private AdvertiserMapper advertiserDao;

	@Autowired
	private IAdvertiserService advertiserService;

	public void createOrUpadate() {
		LOGGER.info("++++++++++iqiyi upload customer begin+++++++++++");
		
		// 查询所有待审核且媒体的广告主的审核状态是媒体审核的
		List<Advertiser> unSubmitAdvertisers = advertiserDao.selectMediaAdvertisers(MediaMapping.IQYI.getValue(), AdvertiserStatusCode.ASC10002.getValue());
		if (unSubmitAdvertisers == null || unSubmitAdvertisers.isEmpty()) {
			LOGGER.info(MediaMapping.IQYI.getDescrip() + "没有未上传的广告主");
			LOGGER.info("++++++++++iqiyi upload advertiser end+++++++++++");
			return;
		}
		
		// 上传到媒体
		LOGGER.info("IQiyiCustomerCreateOrUpdateApiTask-Iqiyi", unSubmitAdvertisers.size());
		Map<Integer, String> advertiserIdKeys = new HashMap<Integer, String>();
		for (Advertiser advertiser : unSubmitAdvertisers) {
			Map<String, String> paramMap = new HashMap<>();
			paramMap.put("ad_id", advertiser.getId().toString());
			paramMap.put("name", advertiser.getAdvertiserName());
			paramMap.put("op", IQiYiConstant.AD_OP_CREATE.getDescription());
			LOGGER.info("IQiyiCustomerCreateOrUpdateApiTask request:{}, param:{}", createOrUpdateUrl, paramMap);
			String result = iQiYiHttpUtils.post(createOrUpdateUrl, paramMap, null);
			LOGGER.info("IQiyiCustomerCreateOrUpdateApiTask response:{}", result);
			IQiyiCustomerResponse iQiyiCustomerResponse = JSONObject.parseObject(result, IQiyiCustomerResponse.class);
			if (iQiyiCustomerResponse != null) {
				String code = iQiyiCustomerResponse.getCode();
				// 0 代表返回成功，其余状态代表返回失败
				if (code.equals(String.valueOf(IQiYiConstant.RESPONSE_SUCCESS.getValue()))) {
					// 媒体方的广告主ID和我方一致
					advertiserIdKeys.put(advertiser.getId(), String.valueOf(advertiser.getId()));
				} else {
					LOGGER.error("广告主[advertiserId=" + advertiser.getId() + "]上传失败-" + iQiyiCustomerResponse.getDesc());
				}
			} else {
				LOGGER.error("广告主[advertiserId=" + advertiser.getId() + "]上传失败");
			}
		}
		
		// 更新我方系统状态为审核中
		if (!advertiserIdKeys.isEmpty()) {
			advertiserService.updateStatusAfterUpload(advertiserIdKeys);
		}

		LOGGER.info("++++++++++iqiyi upload customer end+++++++++++");
	}
}
