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
import com.madhouse.platform.premiummad.constant.AdvertiserStatusCode;
import com.madhouse.platform.premiummad.constant.MaterialStatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dao.AdvertiserMapper;
import com.madhouse.platform.premiummad.entity.Advertiser;
import com.madhouse.platform.premiummad.media.weibo.constant.WeiboConstant;
import com.madhouse.platform.premiummad.media.weibo.constant.WeiboIndustryMapping;
import com.madhouse.platform.premiummad.media.weibo.request.WeiboClient;
import com.madhouse.platform.premiummad.media.weibo.request.WeiboClientUploadRequest;
import com.madhouse.platform.premiummad.media.weibo.request.WeiboQualificationFile;
import com.madhouse.platform.premiummad.media.weibo.response.WeiboClientUploadResponse;
import com.madhouse.platform.premiummad.model.AdvertiserAuditResultModel;
import com.madhouse.platform.premiummad.service.IAdvertiserService;
import com.madhouse.platform.premiummad.service.IMediaService;
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

	@Value("${advertier_meidaGroupMapping_weibo}")
	private String mediaGroupStr;
	
	@Autowired
	private AdvertiserMapper advertiserDao;

	@Autowired
	private IAdvertiserService advertiserService;

	@Autowired
	private IMediaService mediaService;
	
	public void uploadClient() {
		LOGGER.info("++++++++++Weibo upload client begin+++++++++++");

		/* 代码配置处理方式
		// 媒体组没有映射到具体的媒体不处理
		String value = MediaTypeMapping.getValue(MediaTypeMapping.WEIBO.getGroupId());
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
			LOGGER.info("Weibo没有未上传的广告主");
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
				if (WeiboConstant.RESPONSE_SUCCESS.getValue() == retCode) {
					advertiserIdKeys.put(advertiser.getId(), String.valueOf(advertiser.getId()));
				} else {
					AdvertiserAuditResultModel rejuseItem = new AdvertiserAuditResultModel();
					rejuseItem.setId(String.valueOf(advertiser.getId()));
					rejuseItem.setStatus(MaterialStatusCode.MSC10001.getValue());
					rejuseItem.setMediaIds(mediaIds);
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

		// 处理失败的结果，自动驳回 - 通过广告主id更新
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
		List<WeiboClient> clients = new ArrayList<WeiboClient>();
		WeiboClient client = new WeiboClient();
		// 客户代码
		client.setClient_id(String.valueOf(advertiser.getId()));
		// 客户名称取自广告主的名称
		client.setClient_name(advertiser.getAdvertiserName());
		// 客户行业
		client.setContent_category(String.valueOf(WeiboIndustryMapping.getMediaIndustryId(advertiser.getIndustry())));
		// 客户URL
		client.setUrl(advertiser.getWebsite());
		// 资质文件信息
		if (!StringUtils.isBlank(advertiser.getLicense())) {
			List<WeiboQualificationFile> qualificationFiles = new ArrayList<WeiboQualificationFile>();
			String[] licences = advertiser.getLicense().split("\\|");
			for (String licence : licences) {
				WeiboQualificationFile item = new WeiboQualificationFile();
				item.setFile_url(licence);
				item.setFile_name("资质文件"); // 默认写死
				qualificationFiles.add(item);
			}
			client.setQualification_files(qualificationFiles);
		}
		client.setMemo("");
		clients.add(client);

		request.setClients(clients);
		request.setDspid(dspid);
		request.setToken(token);

		return request;
	}
}
