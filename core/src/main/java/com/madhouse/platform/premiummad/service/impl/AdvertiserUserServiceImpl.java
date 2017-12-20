package com.madhouse.platform.premiummad.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.madhouse.platform.premiummad.dao.AdvertiserUserMapper;
import com.madhouse.platform.premiummad.entity.AdvertiserUser;
import com.madhouse.platform.premiummad.model.AdvertiserUserAuditResultModel;
import com.madhouse.platform.premiummad.service.IAdvertiserUserService;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AdvertiserUserServiceImpl implements IAdvertiserUserService {

	private Logger LOGGER = LoggerFactory.getLogger(AdvertiserUserServiceImpl.class);
	
	@Autowired
	private AdvertiserUserMapper advertiserUserDao;
	
	@Transactional
	@Override
	public void updateStatus(List<AdvertiserUserAuditResultModel> auditResults) {
		LOGGER.info("更新绑定关系");
		
		// 参数校验
		if (auditResults == null || auditResults.isEmpty()) {
			return;
		}
		
		// 构建更新数据对象
		List<AdvertiserUser> auditAdvertiserUsers = new ArrayList<AdvertiserUser>();
		for (AdvertiserUserAuditResultModel source : auditResults) {
			AdvertiserUser item = new AdvertiserUser();
			item.setUserId(source.getUserId());
			item.setAdvertiserId(source.getAdvertiserId());
			item.setStatus((byte) source.getStatus().intValue());
			item.setReason(source.getErrorMessage() == null ? "" : source.getErrorMessage());
			item.setUpdatedTime(new Date());
			auditAdvertiserUsers.add(item);
		}

		// 批量更新
		int effectRows = advertiserUserDao.updateByBath(auditAdvertiserUsers);
		if (effectRows != auditAdvertiserUsers.size()) {
			LOGGER.info("素材部分更新失败");
		}
	}
}
