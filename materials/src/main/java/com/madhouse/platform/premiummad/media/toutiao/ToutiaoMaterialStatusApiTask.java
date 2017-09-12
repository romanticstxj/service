package com.madhouse.platform.premiummad.media.toutiao;

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
import com.madhouse.platform.premiummad.constant.MaterialStatusCode;
import com.madhouse.platform.premiummad.constant.MediaMapping;
import com.madhouse.platform.premiummad.dao.MaterialMapper;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.media.toutiao.constant.ToutiaoConstant;
import com.madhouse.platform.premiummad.media.toutiao.response.ToutiaoMaterialStatusDetailResponse;
import com.madhouse.platform.premiummad.media.toutiao.util.ToutiaoHttpUtil;
import com.madhouse.platform.premiummad.model.MaterialAuditResultModel;
import com.madhouse.platform.premiummad.service.IMaterialService;

@Component
public class ToutiaoMaterialStatusApiTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(ToutiaoMaterialStatusApiTask.class);

	@Value("${toutiao.statusUrl}")
	private String getMaterialStatusUrl;

	@Value("${toutiao.dspid}")
	private String dspid;

	@Autowired
	private ToutiaoHttpUtil toutiaoHttpUtil;

	@Autowired
	private MaterialMapper materialDao;

	@Autowired
	private IMaterialService materialService;

	/**
	 * 定时获取今日头条广告状态。
	 */
	public void getStatusDetail() {
		LOGGER.info("++++++++++Toutiao get material status begin+++++++++++");
		// 我方系统未审核的素材
		List<Material> unAuditMaterials = materialDao.selectMediaMaterials(MediaMapping.TOUTIAO.getValue(), MaterialStatusCode.MSC10003.getValue());
		if (unAuditMaterials == null || unAuditMaterials.isEmpty()) {
			LOGGER.info("++++++++++Toutiao no materials need to audit+++++++++++");
			return;
		}

		List<MaterialAuditResultModel> auditResults = new ArrayList<MaterialAuditResultModel>();
		for (Material item : unAuditMaterials) {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("adid", item.getMediaQueryKey());
			paramMap.put("dspid", dspid);
			// 返回结果
			String getResult = toutiaoHttpUtil.get(getMaterialStatusUrl, paramMap);
			LOGGER.info("ToutiaoMaterialStatus:getResult=" + getResult);
			if (getResult != null && !getResult.isEmpty()) {
				ToutiaoMaterialStatusDetailResponse response = JSON.parseObject(getResult, ToutiaoMaterialStatusDetailResponse.class);
				MaterialAuditResultModel auditItem = new MaterialAuditResultModel();
				auditItem.setMediaQueryKey(String.valueOf(response.getAdid()));
				auditItem.setMediaId(String.valueOf(MediaMapping.TOUTIAO.getValue()));
				String status = response.getStatus();
				String error = response.getError();
				if (status != null && status.equals(ToutiaoConstant.M_STATUS_UNAUDITED.getDescription())) {// 未审核
					LOGGER.info("ToutiaoMaterialStatus:materialID=" + item.getId() + " " + status);
				} else if (status != null && status.equals(ToutiaoConstant.M_STATUS_APPROVED.getDescription())) {// 审核通过
					auditItem.setStatus(MaterialStatusCode.MSC10004.getValue());
					auditResults.add(auditItem);
				} else if (status != null && status.equals(ToutiaoConstant.M_STATUS_REFUSED.getDescription())) {// 审核未通过
					auditItem.setStatus(MaterialStatusCode.MSC10001.getValue());
					auditItem.setErrorMessage(response.getReason());
					auditResults.add(auditItem);
				} else if (status == null && error != null) {// 审核错误
					LOGGER.info(MediaMapping.TOUTIAO.getDescrip() + "获取状态失败-" + error + " " + status);
				} else {
					LOGGER.info(MediaMapping.TOUTIAO.getDescrip() + "获取状态失败");
				}
			}
		}

		// 更新数据库
		if (!auditResults.isEmpty()) {
			materialService.updateStatusToMedia(auditResults);
		}

		LOGGER.info("++++++++++Toutiao get material status end+++++++++++");
	}
}
