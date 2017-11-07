package com.madhouse.platform.premiummad.media.sohu.news;

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
import com.madhouse.platform.premiummad.media.sohu.response.SohuResponse;
import com.madhouse.platform.premiummad.media.sohu.util.SohuNewsAuth;
import com.madhouse.platform.premiummad.model.AdvertiserAuditResultModel;
import com.madhouse.platform.premiummad.service.IAdvertiserService;
import com.madhouse.platform.premiummad.service.IMediaService;
import com.madhouse.platform.premiummad.util.HttpUtils;
import com.madhouse.platform.premiummad.util.StringUtils;

@Component
public class SohuNewsCustomerCreateApiTask {

	private final static Logger LOGGER = LoggerFactory.getLogger(SohuNewsCustomerCreateApiTask.class);

	@Value("${sohu.customer.create}")
	private String customerCreateUrl;
	
	@Value("${sohu.customer.update}")
	private String customerUpdateUrl;

	@Value("${advertier_meidaGroupMapping_sohuNews}")
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
	 * 上传搜狐新闻广告主
	 */
	public void create() {
		LOGGER.info("++++++++++Sohu News upload advertiser begin+++++++++++");

		/* 代码配置处理方式
		// 媒体组没有映射到具体的媒体不处理
		String value = MediaTypeMapping.getValue(MediaTypeMapping.SOHUNEWS.getGroupId());
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
		// 查询所有待审核且媒体的广告主的审核状态是媒体审核的
		List<Advertiser> unSubmitAdvertisers = advertiserDao.selectAdvertisersByMedias(mediaIds, AdvertiserStatusCode.ASC10002.getValue());
		if (unSubmitAdvertisers == null || unSubmitAdvertisers.isEmpty()) {
			/*LOGGER.info(MediaMapping.getDescrip(mediaIds) + "没有未上传的广告主");*/
			LOGGER.info("Sohu News没有未上传的广告主");
			return;
		}

		// 上传到媒体
		LOGGER.info("SohuNewsCustomerCreateApiTask-sohuNews", unSubmitAdvertisers.size());
		Map<Integer, String> advertiserIdKeys = new HashMap<Integer, String>();
		List<AdvertiserAuditResultModel> rejusedAdvertisers = new ArrayList<AdvertiserAuditResultModel>();
		for (Advertiser advertiser : unSubmitAdvertisers) {
			Map<String, Object> paramMap = buildCreatePara(advertiser);
			// 第一次上传用新增接口，已驳回的再次上传用 更新接口
			String url = StringUtils.isBlank(advertiser.getMediaAdvertiserKey()) ? customerCreateUrl : customerUpdateUrl;
			String request = sohuAuth.setHttpMethod("POST").setApiUrl(url).setParamMap(paramMap).buildRequest();
			LOGGER.info("SohuNewsCustomerCreateApiTask.reqquest: {}", request);
			String result = HttpUtils.post(url, request);
			LOGGER.info("SohuNewsCustomerCreateApiTask.udpate http post:{}. result json: {}", url, result);
			SohuResponse sohutvResponse = JSONObject.parseObject(result, SohuResponse.class);
			if (sohutvResponse != null) {
				if (sohutvResponse.isStatus()) {
					String customKey = sohutvResponse.getContent().toString();
					advertiserIdKeys.put(advertiser.getId(), customKey);
				} else {
					AdvertiserAuditResultModel rejuseItem = new AdvertiserAuditResultModel();
					rejuseItem.setId(String.valueOf(advertiser.getId()));
					rejuseItem.setStatus(MaterialStatusCode.MSC10001.getValue());
					rejuseItem.setMediaIds(mediaIds);
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
		// 更新时使用
		if (!StringUtils.isBlank(advertiser.getMediaAdvertiserKey())) {
			paramMap.put("customer_key", advertiser.getMediaAdvertiserKey());
		}
		paramMap.put("customer_name", advertiser.getAdvertiserName());
		paramMap.put("customer_website", advertiser.getWebsite());
		paramMap.put("company_address", advertiser.getAddress());
		paramMap.put("contact", advertiser.getContact());
		paramMap.put("phone_number", advertiser.getPhone());
		
		return paramMap;
	}
}
