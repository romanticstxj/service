package com.madhouse.platform.premiummad.media.tencent;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.madhouse.platform.premiummad.constant.AdvertiserStatusCode;
import com.madhouse.platform.premiummad.constant.MaterialStatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dao.AdvertiserMapper;
import com.madhouse.platform.premiummad.entity.Advertiser;
import com.madhouse.platform.premiummad.media.tencent.constant.TencentAdvertiserAduitStatus;
import com.madhouse.platform.premiummad.media.tencent.request.TencentCommonRequest;
import com.madhouse.platform.premiummad.media.tencent.response.TencentAdvertiserStatusData;
import com.madhouse.platform.premiummad.media.tencent.response.TencentAdvertiserStatusResponse;
import com.madhouse.platform.premiummad.media.tencent.util.TencentHttpUtil;
import com.madhouse.platform.premiummad.model.AdvertiserAuditResultModel;
import com.madhouse.platform.premiummad.service.IAdvertiserService;
import com.madhouse.platform.premiummad.service.IMediaService;
import com.madhouse.platform.premiummad.util.StringUtils;

/**
 * 批量获取广告主的审核状态(RTB)
 */
@Component
public class TencentAdvertiserStatusApiTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(TencentAdvertiserStatusApiTask.class);
	private static final int ITERATOR_TIMES = 2;
	private static final int TECENT_OTV_ITERATOR = 1;
	private static final int PAGE_SIZE = 20;

	@Value("${tencent.advertiserList}")
	private String advertiserListUrl;

	@Value("${advertier_meidaGroupMapping_tencentNotOtv}")
	private String mediaNotOtvGroupStr;
	
	@Value("${advertier_meidaGroupMapping_tencentOtv}")
	private String mediaOtvGroupStr;
	
	@Autowired
	private TencentHttpUtil tencentHttpUtil;

	@Autowired
	private AdvertiserMapper advertiserDao;

	@Autowired
	private IAdvertiserService advertiserService;

	@Autowired
	private IMediaService mediaService;
	
	public void getAdvertiserStatus() {
		// TENCENT 对应两个媒体 OTV 和 非 OTV
		for (int mediaType = 0; mediaType < ITERATOR_TIMES; mediaType++) {
			/*代码配置处理方式
			int mediaIdGroup = 0;
			if (mediaType != TECENT_OTV_ITERATOR) {
				mediaIdGroup = MediaTypeMapping.TENCENT_NOT_OTV.getGroupId();
			} else {
				mediaIdGroup = MediaTypeMapping.TENCENT.getGroupId();
			}
			
			// 媒体组没有映射到具体的媒体不处理
			String value = MediaTypeMapping.getValue(mediaIdGroup);
			if (StringUtils.isBlank(value)) {
				return;
			}

			// 获取媒体组下的具体媒体
			int[] mediaIds = StringUtils.splitToIntArray(value);
			*/
			
			String mediaGroupStr = "";
			if (mediaType != TECENT_OTV_ITERATOR) {
				mediaGroupStr = mediaNotOtvGroupStr;
			} else {
				mediaGroupStr = mediaOtvGroupStr;
			}
			
			// 根据媒体组ID和审核对象获取具体的媒体ID
			int[] mediaIds = mediaService.getMeidaIds(mediaGroupStr, SystemConstant.MediaAuditObject.ADVERTISER);
			
			// 根据媒体组ID和审核对象获取具体的媒体ID
			if (mediaIds == null || mediaIds.length < 1) {
				return ;
			}
			
			// 获取我方媒体待审核的广告主
			List<Advertiser> unAuditAdvertisers = advertiserDao.selectAdvertisersByMedias(mediaIds, AdvertiserStatusCode.ASC10003.getValue());
			if (unAuditAdvertisers == null || unAuditAdvertisers.isEmpty()) {
				/*LOGGER.info(MediaMapping.getDescrip(mediaIds) + "无需要审核的广告主");*/
				LOGGER.info("Tencent" + mediaIds + "无需要审核的广告主");
				return;
			}

			int times = (unAuditAdvertisers.size() - 1) / PAGE_SIZE + 1;
			for (int i = 0; i < times; i++) {
				int endIndex = (i + 1) * PAGE_SIZE;
				if (endIndex > unAuditAdvertisers.size()) {
					endIndex = unAuditAdvertisers.size();
				}
				// 构造请求并获取
				List<Advertiser> advertisers = unAuditAdvertisers.subList(i * PAGE_SIZE, endIndex);
				TencentCommonRequest<List<TencentAdvertiserStatusData>> request = buildAdvertiserStatusRequest(i + 1, advertisers);
				LOGGER.info("request: ",  JSONObject.toJSONString(request));
				String responseJson = tencentHttpUtil.post(advertiserListUrl, request);
				LOGGER.info("response: ", responseJson);

				// 解析结果
				if (!StringUtils.isBlank(responseJson)) {
					TencentAdvertiserStatusResponse response = JSON.parseObject(responseJson, TencentAdvertiserStatusResponse.class);
					if (response != null && response.getRet_code() == 0) {
						List<TencentAdvertiserStatusData> records = response.getRet_msg();
						if (records != null && records.size() > 0) {
							List<AdvertiserAuditResultModel> auditResults = new ArrayList<AdvertiserAuditResultModel>();
							for (TencentAdvertiserStatusData item : records) {
								AdvertiserAuditResultModel auditItem = new AdvertiserAuditResultModel();
								int status = item.getStatus();
								auditItem.setMediaAdvertiserKey(String.valueOf(item.getId()));
								auditItem.setMediaIds(mediaIds);
								if (TencentAdvertiserAduitStatus.AUDITED.getValue() == status) { // 审核通过
									auditItem.setStatus(MaterialStatusCode.MSC10004.getValue());
									auditResults.add(auditItem);
								} else if (TencentAdvertiserAduitStatus.REJUSED.getValue() == status) { // 审核驳回
									auditItem.setStatus(MaterialStatusCode.MSC10001.getValue());
									auditItem.setErrorMessage(item.getVinfo());
									auditResults.add(auditItem);
								}
							}

							// 更新数据库
							if (!auditResults.isEmpty()) {
								advertiserService.updateStatusToMedia(auditResults);
							}
						}
					} else {
						LOGGER.info("Tencent批量获取广告的审核状态返回 null");
					}
				}
			}
			LOGGER.info("Tencent" + mediaIds + " TencentAdvertiserStatusApiTask-getAdvertiserStatus end");
		}
	}

	/**
	 * 构建获取广告主状态请求
	 * 
	 * @param pageIndex
	 * @param unAuditAdvertisers
	 * @return
	 */
	private TencentCommonRequest<List<TencentAdvertiserStatusData>> buildAdvertiserStatusRequest(int pageIndex, List<Advertiser> unAuditAdvertisers) {
		TencentCommonRequest<List<TencentAdvertiserStatusData>> request = new TencentCommonRequest<List<TencentAdvertiserStatusData>>();
		request.setPage(pageIndex);
		request.setSize(PAGE_SIZE);
		List<TencentAdvertiserStatusData> data = new ArrayList<TencentAdvertiserStatusData>();
		for (Advertiser advertiser : unAuditAdvertisers) {
			TencentAdvertiserStatusData item = new TencentAdvertiserStatusData();
			item.setId(Integer.valueOf(advertiser.getMediaAdvertiserKey()));
			item.setName(advertiser.getAdvertiserName());
			data.add(item);
		}
		request.setData(data);
		return request;
	}
}
