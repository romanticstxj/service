package com.madhouse.platform.premiummad.rule;

import java.util.Date;

import com.madhouse.platform.premiummad.constant.AdvertiserUserStatusCode;
import com.madhouse.platform.premiummad.entity.AdvertiserUser;
import com.madhouse.platform.premiummad.model.MaterialModel;

public class AdvertiserUserRule {
	
	/**
	 * 构建对象(新增或修改对象)
	 * 
	 * @param advertiserUser
	 * @param advertiserId
	 * @param entity
	 * @return
	 */
	public static AdvertiserUser buildAdvertiserUser(AdvertiserUser advertiserUser, Integer advertiserId, MaterialModel entity) {
		// 第一次新增
		if (advertiserUser == null) {
			advertiserUser = new AdvertiserUser();
			advertiserUser.setAdversterId(advertiserId);
			advertiserUser.setUserId(entity.getUserId());
			advertiserUser.setCreatedTime(new Date());
		}

		advertiserUser.setReason("");// 驳回原因初始化为空
		advertiserUser.setStatus((byte) AdvertiserUserStatusCode.AUC10002.getValue());// 初始化为待提交状态
		advertiserUser.setQualificationFile(org.springframework.util.StringUtils.collectionToDelimitedString(entity.getQualificationFile(), ","));
		advertiserUser.setUpdatedTime(new Date());

		return advertiserUser;
	}
}
