package com.madhouse.platform.premiummad.media.tencent;

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
import com.madhouse.platform.premiummad.media.tencent.constant.TencentErrorCode;
import com.madhouse.platform.premiummad.media.tencent.constant.TencentIndustryMapping;
import com.madhouse.platform.premiummad.media.tencent.request.TencentAdvertiserData;
import com.madhouse.platform.premiummad.media.tencent.request.TencentCommonRequest;
import com.madhouse.platform.premiummad.media.tencent.request.TencentQualidata;
import com.madhouse.platform.premiummad.media.tencent.response.TencentUploadAdvertiserResponse;
import com.madhouse.platform.premiummad.media.tencent.response.TencentUploadAdvertiserRetMsg;
import com.madhouse.platform.premiummad.media.tencent.util.TencentHttpUtil;
import com.madhouse.platform.premiummad.model.AdvertiserAuditResultModel;
import com.madhouse.platform.premiummad.service.IAdvertiserService;
import com.madhouse.platform.premiummad.util.StringUtils;

@Component
public class TencentUploadAdvertiserApiTask {
	private Logger LOGGER = LoggerFactory.getLogger(TencentUploadAdvertiserApiTask.class);
	private static final int ITERATOR_TIMES = 2;
	private static final int TECENT_OTV_ITERATOR = 1;

	@Value("${tencent.advertiserAdd}")
	private String advertiserAddUrl;

	@Value("${tencent.advertiserUpdate}")
	private String advertiserUpdateUrl;

	@Autowired
	private TencentHttpUtil tencentHttpUtil;

	@Autowired
	private AdvertiserMapper advertiserDao;

	@Autowired
	private IAdvertiserService advertiserService;

	/**
	 * RTB 上传广告主
	 */
	public void uploadAdvertiser() {
		// TENCENT 对应两个媒体 OTV 和 非 OTV
		for (int mediaType = 0; mediaType < ITERATOR_TIMES; mediaType++) {
			int mediaId = 0;
			if (mediaType != TECENT_OTV_ITERATOR) {
				mediaId = MediaMapping.TENCENT_NOT_OTV.getValue();
			} else {
				mediaId = MediaMapping.TENCENT.getValue();
			}
			LOGGER.info(MediaMapping.getDescrip(mediaId) + " TencentUploadAdvertiserApiTask-uploadAdvertiser start");

			// 查询所有待审核且媒体的广告主的审核状态是媒体审核的
			List<Advertiser> unSubmitAdvertisers = advertiserDao.selectMediaAdvertisers(mediaId, AdvertiserStatusCode.ASC10002.getValue());
			if (unSubmitAdvertisers == null || unSubmitAdvertisers.isEmpty()) {
				LOGGER.info(MediaMapping.getDescrip(mediaId) + "没有未上传的广告主");
				LOGGER.info("++++++++++tencent upload advertiser end+++++++++++");
				return;
			}

			// 上传到媒体
			LOGGER.info("TencentUploadAdvertiserApiTask-tencent", unSubmitAdvertisers.size());

			// 构建请求对象并上传
			Map<String, Integer> advertiserNameIdMap = new HashMap<String, Integer>();
			TencentCommonRequest<List<TencentAdvertiserData>> request = buildUploadAdvertiserRequest(unSubmitAdvertisers, advertiserNameIdMap);
			String responseJson = tencentHttpUtil.post(advertiserAddUrl, request);
			LOGGER.info("Tencent上传广告主返回信息：{}", responseJson);

			// 解析结果
			if (!StringUtils.isBlank(responseJson)) {
				try {
					TencentUploadAdvertiserResponse response = JSONObject.parseObject(responseJson, TencentUploadAdvertiserResponse.class);
					if (!request.getData().isEmpty() && response != null) {
						// 系统错误或权限校验错误
						if (TencentErrorCode.TEC100.getValue() == response.getError_code() || TencentErrorCode.TEC101.getValue() == response.getError_code()) {
							LOGGER.info("Tencent上传广告出现错误:" + (TencentErrorCode.TEC100.getDescrip()));
							return;
						}

						Map<Integer, String> advertiserIdKeys = new HashMap<Integer, String>(); // <ssp id, media id>
						List<AdvertiserAuditResultModel> rejusedAdvertisers = new ArrayList<AdvertiserAuditResultModel>();
						if (response.getRet_msg() != null && !response.getRet_msg().isEmpty()) {
							for (TencentUploadAdvertiserRetMsg item : response.getRet_msg()) {
								// 上传成功
								if (StringUtils.isBlank(item.getErr_code()) && StringUtils.isBlank(item.getErr_msg())) {
									advertiserIdKeys.put(advertiserNameIdMap.get(item.getName()), String.valueOf(item.getId()));
								} else {
									// 自动驳回
									AdvertiserAuditResultModel rejuseItem = new AdvertiserAuditResultModel();
									rejuseItem.setId(String.valueOf(advertiserNameIdMap.get(item.getName())));
									rejuseItem.setStatus(MaterialStatusCode.MSC10001.getValue());
									rejuseItem.setMediaId(String.valueOf(mediaId));
									rejuseItem.setErrorMessage(TencentErrorCode.getDescrip(Integer.valueOf(item.getErr_code())) + "[" + item.getErr_msg() + "]");
									rejusedAdvertisers.add(rejuseItem);
								}
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
					} else {
						LOGGER.info("Tencent上传广告主返回出错 : TencentUploadAdvertiserResponse is null");
					}
				} catch (Exception e) {
					LOGGER.info("Tencent上传广告主返回解析出错 : " + e.getMessage());
				}
			} else {
				LOGGER.info("Tencent上传广告主返回出错 : TencentUploadAdvertiserResponse is null");
			}

			LOGGER.info(MediaMapping.getDescrip(mediaId) + " TencentUploadAdvertiserApiTask-uploadAdvertiser end");
		}
	}

	/**
	 * 构建上传广告主请求对象
	 * 
	 * @param unSubmitAdvertisers
	 * @return
	 */
	private TencentCommonRequest<List<TencentAdvertiserData>> buildUploadAdvertiserRequest(List<Advertiser> unSubmitAdvertisers, Map<String, Integer> advertiserNameIdMap) {
		TencentCommonRequest<List<TencentAdvertiserData>> request = new TencentCommonRequest<List<TencentAdvertiserData>>();
		List<TencentAdvertiserData> data = new ArrayList<TencentAdvertiserData>();
		for (Advertiser advertiser : unSubmitAdvertisers) {
			advertiserNameIdMap.put(advertiser.getAdvertiserName(), advertiser.getId());
			TencentAdvertiserData item = new TencentAdvertiserData();
			item.setName(advertiser.getAdvertiserName());
			item.setHomeUrl(advertiser.getAddress());
			item.setVocation(TencentIndustryMapping.getMediaIndustryId(advertiser.getIndustry()));
			// 资质文件信息
			if (!StringUtils.isBlank(advertiser.getLicense())) {
				List<TencentQualidata> qualificationFiles = new ArrayList<TencentQualidata>();
				String[] licences = advertiser.getLicense().split("\\|");
				for (String licence : licences) {
					TencentQualidata qualidata = new TencentQualidata();
					qualidata.setFileUrl(licence);
					qualidata.setName("资质文件"); // 默认写死
					qualificationFiles.add(qualidata);
				}
				item.setQualidata(qualificationFiles);
			}
			data.add(item);
		}
		request.setData(data);

		return request;
	}
}
