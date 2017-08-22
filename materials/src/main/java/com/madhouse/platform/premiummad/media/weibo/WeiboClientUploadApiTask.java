package com.madhouse.platform.premiummad.media.weibo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.constant.AdevertiserIndustry;
import com.madhouse.platform.premiummad.constant.AdvertiserStatusCode;
import com.madhouse.platform.premiummad.constant.MaterialStatusCode;
import com.madhouse.platform.premiummad.constant.MediaMapping;
import com.madhouse.platform.premiummad.dao.AdvertiserMapper;
import com.madhouse.platform.premiummad.entity.Advertiser;
import com.madhouse.platform.premiummad.media.constant.IWeiboConstant;
import com.madhouse.platform.premiummad.media.model.WeiboClientUploadRequest;
import com.madhouse.platform.premiummad.media.model.WeiboClientUploadResponse;
import com.madhouse.platform.premiummad.media.model.WeiboQualificationFile;
import com.madhouse.platform.premiummad.model.AdvertiserAuditResultModel;
import com.madhouse.platform.premiummad.service.IAdvertiserService;
import com.madhouse.platform.premiummad.util.HttpUtils;
import com.madhouse.platform.premiummad.util.StringUtils;

@Component
public class WeiboClientUploadApiTask {

	private Logger LOGGER = LoggerFactory.getLogger(WeiboClientUploadApiTask.class);

	@Value("${weibo.clientUploadUrl}")
	private String clientUploadUrl;

	@Value("${weibo.dspid}")
	private String dspid;

	@Value("${weibo.token}")
	private String token;

	@Autowired
	private AdvertiserMapper advertiserDao;

	@Autowired
	private IAdvertiserService advertiserService;

	public void uploadClient() {
		LOGGER.info("++++++++++Weibo upload client begin+++++++++++");

		// 查询所有待审核且媒体的广告主的审核状态是媒体审核的
		List<Advertiser> unSubmitAdvertisers = advertiserDao.selectMediaAdvertisers(MediaMapping.WEIBO.getValue(), AdvertiserStatusCode.ASC10002.getValue());
		if (unSubmitAdvertisers == null || unSubmitAdvertisers.isEmpty()) {
			LOGGER.info(MediaMapping.WEIBO.getDescrip() + "没有未上传的广告主");
			LOGGER.info("++++++++++Weibo upload client end+++++++++++");
			return;
		}

		// 上传到媒体
		LOGGER.info("WeiboClientUploadApiTask-weibo", unSubmitAdvertisers.size());

		Map<Integer, String> advertiserIdKeys = new HashMap<Integer, String>();
		List<AdvertiserAuditResultModel> rejusedAdvertisers = new ArrayList<AdvertiserAuditResultModel>();
		for (Advertiser advertiser : unSubmitAdvertisers) {
			WeiboClientUploadRequest request = buildRequest(advertiser);
			String requestJson = JSON.toJSONString(request);
			LOGGER.info("WeiboClientUploadApiTask request:" + requestJson);
			String responseJson = HttpUtils.post(clientUploadUrl, requestJson);
			LOGGER.info("WeiboClientUploadApiTask response:" + responseJson);

			// 更新我方信息
			if (!StringUtils.isEmpty(requestJson)) {
				WeiboClientUploadResponse weiboClientUploadResponse = JSON.parseObject(responseJson, WeiboClientUploadResponse.class);
				Integer retCode = weiboClientUploadResponse.getRet_code();
				if (IWeiboConstant.RESPONSE_SUCCESS.getValue() == retCode) {
					advertiserIdKeys.put(advertiser.getId(), String.valueOf(advertiser.getId()));
				} else {
					AdvertiserAuditResultModel rejuseItem = new AdvertiserAuditResultModel();
					rejuseItem.setId(String.valueOf(advertiser.getId()));
					rejuseItem.setStatus(MaterialStatusCode.MSC10001.getValue());
					rejuseItem.setMediaId(String.valueOf(MediaMapping.SOHUNEWS.getValue()));
					rejuseItem.setErrorMessage(weiboClientUploadResponse.getRet_msg());
					rejusedAdvertisers.add(rejuseItem);
					LOGGER.error("广告主[advertiserId=" + advertiser.getId() + "]上传失败-" + requestJson);
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

		LOGGER.info("++++++++++Weibo upload client end+++++++++++");
	}

	/**
	 * 构建上传广告主请求
	 * 
	 * @param advertiser
	 * @return
	 */
	private WeiboClientUploadRequest buildRequest(Advertiser advertiser) {
		WeiboClientUploadRequest request = new WeiboClientUploadRequest();
		
		//DSP定义的客户代码 TODO advertiserKey?
		request.setClient_id(String.valueOf(advertiser.getId()));
		// 客户名称取自广告主的名称
		request.setClient_name(advertiser.getAdvertiserName());
		// 客户行业
		request.setContent_category(AdevertiserIndustry.getDescrip(advertiser.getIndustry()));
		
		// 客户URL
		request.setUrl(advertiser.getWebsite());
		
		// 资质文件信息
		if (!StringUtils.isBlank(advertiser.getLicense())) {
			List<WeiboQualificationFile> qualificationFiles = new ArrayList<WeiboQualificationFile>();
			String[] licences = advertiser.getLicense().split("\\|");
			for (String licence : licences) {
				WeiboQualificationFile item = new WeiboQualificationFile();
				item.setFile_url(licence);
				// file name TODO
				qualificationFiles.add(item);
			}
			request.setQualification_files(qualificationFiles);
		}
		request.setDspid(dspid);
		request.setToken(token);

		return request;
	}
}
