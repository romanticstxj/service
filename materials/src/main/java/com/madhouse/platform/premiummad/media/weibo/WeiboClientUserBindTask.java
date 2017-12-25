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
import com.madhouse.platform.premiummad.media.weibo.request.WeiboClientUserBindItem;
import com.madhouse.platform.premiummad.media.weibo.request.WeiboClientUserBindRequest;
import com.madhouse.platform.premiummad.media.weibo.request.WeiboQualificationFile;
import com.madhouse.platform.premiummad.media.weibo.response.WeiboAdvertiserUserBindErrorResponse;
import com.madhouse.platform.premiummad.media.weibo.response.WeiboAdvertiserUserBindResponseDetail;
import com.madhouse.platform.premiummad.media.weibo.response.WeiboResponse;
import com.madhouse.platform.premiummad.model.AdvertiserUserAuditResultModel;
import com.madhouse.platform.premiummad.service.IAdvertiserUserService;
import com.madhouse.platform.premiummad.util.HttpUtils;
import com.madhouse.platform.premiummad.util.StringUtils;

@Component
public class WeiboClientUserBindTask {

	private Logger LOGGER = LoggerFactory.getLogger(WeiboClientUserBindTask.class);

	@Value("${weibo.clientUserBindUrl}")
	private String clientUserBindUrl;

	@Value("${weibo.dspid}")
	private String dspid;

	@Value("${weibo.token}")
	private String token;

	@Autowired
	private AdvertiserUserMapper advertiserUserDao;

	@Autowired
	private IAdvertiserUserService advertiserUserService;
	
	public void bindClientUser() {
		LOGGER.info("++++++++++Weibo bind client user begin+++++++++++");

		// 查询所有状态为待提交的 广告主用户绑定关系，其中广告主审核通过
		List<AdvertiserUserUnion> selectUnsubmitAdUsers = advertiserUserDao.selectUnprocessAdUser(AdvertiserUserStatusCode.AUC10002.getValue());
		if (selectUnsubmitAdUsers == null || selectUnsubmitAdUsers.isEmpty()) {
			LOGGER.info("Weibo没有未提交的广告主与用户的用户id绑定关系 ");
			return;
		}

		List<AdvertiserUserAuditResultModel> processAdvertiserUsers = new ArrayList<AdvertiserUserAuditResultModel>();
		// 一次上传一条
		for (AdvertiserUserUnion advertiserUserUnion : selectUnsubmitAdUsers) {
			// 构造请求对象
			WeiboClientUserBindRequest request = buildRequest(advertiserUserUnion);

			// 调用接口
			String requestJson = JSON.toJSONString(request);
			LOGGER.info("request:" + requestJson);
			String responseJson = HttpUtils.post(clientUserBindUrl, requestJson);
			LOGGER.info("response:" + responseJson);

			// 更新我方库状态为待审核
			if (!StringUtils.isEmpty(responseJson)) {
				WeiboResponse weiboResponse = JSON.parseObject(responseJson, WeiboResponse.class);
				if (WeiboConstant.RESPONSE_SUCCESS.getValue() == weiboResponse.getRet_code().intValue()) {
					// 处理成功
					AdvertiserUserAuditResultModel successfulItem = new AdvertiserUserAuditResultModel();
					successfulItem.setAdvertiserId(advertiserUserUnion.getAdvertiserId());
					successfulItem.setMediaAdvertiserKey(advertiserUserUnion.getMediaAdvertiserKey());
					successfulItem.setUserId(advertiserUserUnion.getUserId());
					successfulItem.setStatus(AdvertiserUserStatusCode.AUC10003.getValue());
					processAdvertiserUsers.add(successfulItem);
				} else if (WeiboConstant.RESPONSE_PARAS_ERROR.getValue() == weiboResponse.getRet_code().intValue()) {
					// 业务参数异常
					WeiboAdvertiserUserBindErrorResponse errorResponse = JSON.parseObject(responseJson, WeiboAdvertiserUserBindErrorResponse.class);
					if (errorResponse.getRet_msg() != null && !errorResponse.getRet_msg().isEmpty()) {
						WeiboAdvertiserUserBindResponseDetail errorItem = errorResponse.getRet_msg().get(0);
						AdvertiserUserAuditResultModel rejusedItem = new AdvertiserUserAuditResultModel();
						rejusedItem.setAdvertiserId(advertiserUserUnion.getAdvertiserId());
						rejusedItem.setMediaAdvertiserKey(advertiserUserUnion.getMediaAdvertiserKey());
						rejusedItem.setUserId(advertiserUserUnion.getUserId());
						rejusedItem.setStatus(AdvertiserUserStatusCode.AUC10001.getValue());
						rejusedItem.setErrorMessage(WeiboErrorCode.getDescrip(errorItem.getErr_code().intValue()));
						processAdvertiserUsers.add(rejusedItem);
					} else {
						LOGGER.error(WeiboConstant.RESPONSE_PARAS_ERROR.getDescription() + "{" + weiboResponse.getRet_msg() + "}");
					}
				} else if (WeiboConstant.RESPONSE_OATH_FAILUE.getValue() == weiboResponse.getRet_code().intValue()) {
					// 系统错误不处理，记录错误日志
					LOGGER.error(WeiboConstant.RESPONSE_OATH_FAILUE.getDescription() + "{" + weiboResponse.getRet_msg() + "}");
				} else if (WeiboConstant.RESPONSE_OTHER_ERROR.getValue() == weiboResponse.getRet_code().intValue()) {
					// 其他错误不处理，记录错误日志
					LOGGER.error(WeiboConstant.RESPONSE_OTHER_ERROR.getDescription() + "{" + weiboResponse.getRet_msg() + "}");
				}
			}
		}

		// 更新我方系统状态 - 包含业务驳回和审核通过
		if (!processAdvertiserUsers.isEmpty()) {
			advertiserUserService.updateStatus(processAdvertiserUsers);
		}
		LOGGER.info("++++++++++Weibo bind client user end+++++++++++");
	}

	/**
	 * 构建请求对象
	 * 
	 * @param advertiserUserUnion
	 * @return
	 */
	private WeiboClientUserBindRequest buildRequest(AdvertiserUserUnion advertiserUserUnion) {
		WeiboClientUserBindRequest request = new WeiboClientUserBindRequest();
		List<WeiboClientUserBindItem> clientUsers = new ArrayList<WeiboClientUserBindItem>();
		request.setDspid(dspid);
		request.setToken(token);
		
		// 构造请求体
		WeiboClientUserBindItem item = new WeiboClientUserBindItem();
		item.setClient_id(advertiserUserUnion.getMediaAdvertiserKey());
		item.setClient_name(advertiserUserUnion.getAdvertiserName());
		item.setUid(advertiserUserUnion.getUserId());
		// 认证文件
		if (!StringUtils.isBlank(advertiserUserUnion.getQualificationFile())) {
			List<WeiboQualificationFile> qualificationFiles = new ArrayList<WeiboQualificationFile>();
			String[] licences = advertiserUserUnion.getQualificationFile().split("\\|");
			for (String licence : licences) {
				WeiboQualificationFile file = new WeiboQualificationFile();
				file.setFile_url(licence);
				file.setFile_name("资质文件"); // 默认写死
				qualificationFiles.add(file);
			}
			item.setQualification_files(qualificationFiles);
		}
		clientUsers.add(item);

		request.setClient_user(clientUsers);
		return request;
	}
}
