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
import com.madhouse.platform.premiummad.constant.MaterialStatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dao.AdvertiserMapper;
import com.madhouse.platform.premiummad.entity.Advertiser;
import com.madhouse.platform.premiummad.media.iqiyi.constant.IQiYiConstant;
import com.madhouse.platform.premiummad.media.iqiyi.response.IQiyiCustomerResponse;
import com.madhouse.platform.premiummad.media.iqiyi.util.IQiYiHttpUtils;
import com.madhouse.platform.premiummad.model.AdvertiserAuditResultModel;
import com.madhouse.platform.premiummad.service.IAdvertiserService;
import com.madhouse.platform.premiummad.service.IMediaService;

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

	@Value("${advertier_meidaGroupMapping_iqyi}")
	private String mediaGroupStr;

	@Autowired
	private IMediaService mediaService;

	public void createOrUpadate() {
		LOGGER.info("++++++++++iqiyi upload customer begin+++++++++++");

		// 根据媒体组ID和审核对象获取具体的媒体ID
		int[] mediaIds = mediaService.getMeidaIds(mediaGroupStr, SystemConstant.MediaAuditObject.ADVERTISER);

		// 媒体组没有映射到具体的媒体不处理
		if (mediaIds == null || mediaIds.length < 1) {
			return;
		}

		// 查询所有待审核且媒体的广告主的审核状态是媒体审核的
		List<Advertiser> unSubmitAdvertisers = advertiserDao.selectAdvertisersByMedias(mediaIds, AdvertiserStatusCode.ASC10002.getValue());
		if (unSubmitAdvertisers == null || unSubmitAdvertisers.isEmpty()) {
			LOGGER.info("爱奇艺没有未上传的广告主");
			LOGGER.info("++++++++++iqiyi upload advertiser end+++++++++++");
			return;
		}

		// 上传到媒体
		LOGGER.info("IQiyiCustomerCreateOrUpdateApiTask-Iqiyi", unSubmitAdvertisers.size());
		Map<Integer, String> advertiserIdKeys = new HashMap<Integer, String>();
		List<AdvertiserAuditResultModel> rejusedAdvertisers = new ArrayList<AdvertiserAuditResultModel>();
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
					AdvertiserAuditResultModel rejuseItem = new AdvertiserAuditResultModel();
					rejuseItem.setId(String.valueOf(advertiser.getId()));
					rejuseItem.setStatus(MaterialStatusCode.MSC10001.getValue());
					rejuseItem.setMediaIds(mediaIds);
					rejuseItem.setErrorMessage(iQiyiCustomerResponse.getDesc());
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

		LOGGER.info("++++++++++iqiyi upload customer end+++++++++++");
	}
}
