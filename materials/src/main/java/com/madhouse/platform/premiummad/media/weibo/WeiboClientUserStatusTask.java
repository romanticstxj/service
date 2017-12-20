package com.madhouse.platform.premiummad.media.weibo;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.constant.AdvertiserUserStatusCode;
import com.madhouse.platform.premiummad.dao.AdvertiserUserMapper;
import com.madhouse.platform.premiummad.entity.AdvertiserUserUnion;
import com.madhouse.platform.premiummad.media.weibo.constant.WeiboConstant;
import com.madhouse.platform.premiummad.media.weibo.constant.WeiboErrorCode;
import com.madhouse.platform.premiummad.media.weibo.request.WeiboClientUserStatusItem;
import com.madhouse.platform.premiummad.media.weibo.request.WeiboClientUserStatusRequest;
import com.madhouse.platform.premiummad.media.weibo.response.WeiboClientUserStatusResponse;
import com.madhouse.platform.premiummad.media.weibo.response.WeiboClientUserStatusResponseDetail;
import com.madhouse.platform.premiummad.media.weibo.response.WeiboResponse;
import com.madhouse.platform.premiummad.model.AdvertiserUserAuditResultModel;
import com.madhouse.platform.premiummad.service.IAdvertiserUserService;
import com.madhouse.platform.premiummad.util.HttpUtils;
import com.madhouse.platform.premiummad.util.StringUtils;

@Component
public class WeiboClientUserStatusTask {

	private Logger LOGGER = LoggerFactory.getLogger(WeiboClientUserStatusTask.class);

	@Value("${weibo.clientUserStatusUrl}")
	private String clientUserStatusUrl;

	@Value("${weibo.dspid}")
	private String dspid;

	@Value("${weibo.token}")
	private String token;

	@Autowired
	private AdvertiserUserMapper advertiserUserDao;
	
	@Autowired
	private IAdvertiserUserService advertiserUserService;

	public void getClientUserStatus() {
		LOGGER.info("++++++++++Weibo bind client user begin+++++++++++");

		// 查询所有状态为待审核的 广告主用户绑定关系，其中广告主审核通过
		List<AdvertiserUserUnion> selectUnauditAdUsers = advertiserUserDao.selectUnprocessAdUser(AdvertiserUserStatusCode.AUC10003.getValue());
		if (selectUnauditAdUsers == null || selectUnauditAdUsers.isEmpty()) {
			LOGGER.info("Weibo没有未审核的广告主与用户的用户id绑定关系 ");
			return;
		}

		// 每次查询一条数据
		List<AdvertiserUserAuditResultModel> processAdvertiserUsers = new ArrayList<AdvertiserUserAuditResultModel>();
		for (AdvertiserUserUnion detail : selectUnauditAdUsers) {
			// 构造请求对象
			WeiboClientUserStatusRequest request = buildRequest(detail);

			// 调用接口
			String requestJson = JSON.toJSONString(request);
			LOGGER.info("request:" + requestJson);
			String responseJson = HttpUtils.post(clientUserStatusUrl, requestJson);
			LOGGER.info("response:" + responseJson);

			// 更新我方状态
			if (!StringUtils.isEmpty(responseJson)) {
				WeiboResponse weiboResponse = JSON.parseObject(responseJson, WeiboResponse.class);
				if (WeiboConstant.RESPONSE_SUCCESS.getValue() == weiboResponse.getRet_code().intValue()) {
					// 查询成功
					processResult(responseJson, detail, processAdvertiserUsers);
				} else if (WeiboConstant.RESPONSE_OATH_FAILUE.getValue() == weiboResponse.getRet_code().intValue()) {
					// 系统错误不处理，记录错误日志
					LOGGER.error(WeiboConstant.RESPONSE_OATH_FAILUE.getDescription() + "{" + weiboResponse.getRet_msg() + "}");
				} else if (WeiboConstant.RESPONSE_OTHER_ERROR.getValue() == weiboResponse.getRet_code().intValue()) {
					// 其他错误不处理，记录错误日志
					LOGGER.error(WeiboConstant.RESPONSE_OTHER_ERROR.getDescription() + "{" + weiboResponse.getRet_msg() + "}");
				}
			}
		}
		

		// 更新状态
		if (!processAdvertiserUsers.isEmpty()) {
			advertiserUserService.updateStatus(processAdvertiserUsers);
		}

		LOGGER.info("++++++++++Weibo bind client user end+++++++++++");
	}

	/**
	 * 返回正常结果处理
	 * 
	 * @param responseJson
	 */
	private void processResult(String responseJson, AdvertiserUserUnion advertiserUserUnion, List<AdvertiserUserAuditResultModel> processAdvertiserUsers) {
		WeiboClientUserStatusResponse response = JSON.parseObject(responseJson, WeiboClientUserStatusResponse.class);
		if (response.getRet_msg() != null && !response.getRet_msg().isEmpty()) {
			for (WeiboClientUserStatusResponseDetail detail : response.getRet_msg()) {
				AdvertiserUserAuditResultModel item = new AdvertiserUserAuditResultModel();
	
				// 组合主键设值
				item.setAdvertiserId(advertiserUserUnion.getAdvertiserId());
				item.setMediaAdvertiserKey(detail.getClient_id());
				item.setUserId(detail.getUid());

				// 状态获取
				if (detail.getErr_code() == 0) {
					// 状态获取成功
					if (WeiboConstant.C_STATUS_APPROVED.getDescription().equals(detail.getStatus())) {
						// 审核通过
						item.setStatus(AdvertiserUserStatusCode.AUC10004.getValue());
					} else if (WeiboConstant.C_STATUS_REFUSED.getDescription().equals(detail.getStatus())) {
						// 驳回
						item.setStatus(AdvertiserUserStatusCode.AUC10001.getValue());
					} else if (WeiboConstant.C_STATUS_UNAUDITED.getDescription().equals(detail.getStatus())) {
						// 未审核
						LOGGER.info("广告主与用户的绑定关系尚未审核[meidaAdvertiserKey=" + detail.getClient_id() + ",userId=" + detail.getUid() + "]");
						continue;
					}
				} else {
					// 查询数据异常
					item.setStatus(AdvertiserUserStatusCode.AUC10001.getValue());
					item.setErrorMessage(WeiboErrorCode.getDescrip(detail.getErr_code()));
				}
				processAdvertiserUsers.add(item);
			}
		} else {
			LOGGER.error(WeiboConstant.RESPONSE_PARAS_ERROR.getDescription() + "{" + responseJson + "}");
		}
	}

	/**
	 * 构造获取绑定关系的请求
	 * 
	 * @param selectUnauditAdUsers
	 * @return
	 */
	private WeiboClientUserStatusRequest buildRequest(AdvertiserUserUnion detail) {
		WeiboClientUserStatusRequest request = new WeiboClientUserStatusRequest();
		List<WeiboClientUserStatusItem> ids = new ArrayList<WeiboClientUserStatusItem>();
		request.setDspid(dspid);
		request.setToken(token);
		
		// 构建请求对象
		WeiboClientUserStatusItem item = new WeiboClientUserStatusItem();
		item.setClient_id(detail.getMediaAdvertiserKey());
		item.setUid(detail.getUserId());
		ids.add(item);

		request.setIds(ids);
		return request;
	}
}
