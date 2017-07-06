package com.madhouse.platform.premiummad.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madhouse.platform.premiummad.dao.AdvertiserMediaMapper;
import com.madhouse.platform.premiummad.entity.Advertiser;
import com.madhouse.platform.premiummad.entity.AdvertiserMedia;
import com.madhouse.platform.premiummad.entity.AdvertiserMediaUnion;
import com.madhouse.platform.premiummad.model.AdvertiserMediaAuditResultModel;
import com.madhouse.platform.premiummad.model.AdvertiserMediaModel;
import com.madhouse.platform.premiummad.model.OperationResultModel;
import com.madhouse.platform.premiummad.rule.AdvertiserMediaRule;
import com.madhouse.platform.premiummad.service.IAdvertiserMediaService;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AdvertiserMediaServiceImpl implements IAdvertiserMediaService {
	
	@Autowired
	private AdvertiserMediaMapper advertiserMediaDao;
	
	/**
	 * 根据 DSP 广告主Key 查询广告主是否被媒体审核通过 
	 * 一个广告主只能对应一个媒体？
	 * @param ids
	 * @return
	 */
	@Override
	public List<AdvertiserMediaAuditResultModel> getAdvertiserMediaAuditResult(String ids) {
		// 解析传入的广告主Key
		String[] idStrs = AdvertiserMediaRule.parseStringToArray(ids);

		// 查询广告主的审核结果
		List<AdvertiserMediaAuditResultModel> results = new ArrayList<AdvertiserMediaAuditResultModel>();
		if (idStrs != null && idStrs.length > 1) {
			List<AdvertiserMediaUnion> selectAdvertiserMedias = advertiserMediaDao.selectAdvertiserMedias(idStrs);
			AdvertiserMediaRule.convert(selectAdvertiserMedias, results);
		}

		return results;
	}
	
	/**
	 * DSP端上传广告主
	 * @param entity
	 * @return operationResultModel
	 */
	@Override
	public OperationResultModel upload(AdvertiserMediaModel entity) {
		OperationResultModel operationResult = new OperationResultModel();
		
		// 判断广告主与媒体是否已存在
		// TODO
		
		// 审核中，审核通过提示
		// TODO
		
		// 驳回的将状态置为初始状态
		// TODO
		
		// 构建广告主 和 广告主与媒体实体
		Advertiser advertiser = new Advertiser();
		AdvertiserMedia advertiserMedia = new AdvertiserMedia();
		AdvertiserMediaRule.buildEnties(entity, advertiser, advertiserMedia);
		
		//新增或修改 TODO
		
		return operationResult;
	}
}
