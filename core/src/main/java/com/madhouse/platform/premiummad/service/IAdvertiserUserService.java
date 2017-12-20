package com.madhouse.platform.premiummad.service;

import java.util.List;

import com.madhouse.platform.premiummad.model.AdvertiserUserAuditResultModel;

public interface IAdvertiserUserService {

	/**
	 * 更新广告主和媒体的绑定关系
	 * 
	 * @param processAdvertiserUsers
	 */
	void updateStatus(List<AdvertiserUserAuditResultModel> processAdvertiserUsers);
}
