package com.madhouse.platform.premiummad.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.madhouse.platform.premiummad.dao.MaterialMediaMapper;
import com.madhouse.platform.premiummad.entity.MaterialMediaUnion;
import com.madhouse.platform.premiummad.model.MaterialMediaAuditResultModel;
import com.madhouse.platform.premiummad.rule.MaterialMediaRule;
import com.madhouse.platform.premiummad.service.IMaterialMediaService;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class MaterialMediaServiceImpl implements IMaterialMediaService {

	@Autowired
	private MaterialMediaMapper materialMediaDao;
	
	/**
	 * DSP 端查询素材审核状态
	 * @param ids DSP平台定义的素材 ID
	 * @return
	 */
	@Override
	public List<MaterialMediaAuditResultModel> getMaterialMediaAuditResult(String ids) {
		// 解析传入的素材ID
		String[] idStrs = MaterialMediaRule.parseStringToArray(ids);

		// 查询广告主的审核结果
		List<MaterialMediaAuditResultModel> results = new ArrayList<MaterialMediaAuditResultModel>();
		if (idStrs != null && idStrs.length > 1) {
			List<MaterialMediaUnion> selectAdvertiserMedias = materialMediaDao.selectAdvertiserMedias(idStrs);
			MaterialMediaRule.convert(selectAdvertiserMedias, results);
		}

		return results;
	}
}