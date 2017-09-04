package com.madhouse.platform.premiummad.media.valuemaker;

import java.util.ArrayList;
import java.util.List;

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
import com.madhouse.platform.premiummad.media.valuemaker.constant.ValueMakerConstant;
import com.madhouse.platform.premiummad.media.valuemaker.response.ValueMakerMaterialStatusDetailResponse;
import com.madhouse.platform.premiummad.media.valuemaker.util.ValueMakerHttpUtil;
import com.madhouse.platform.premiummad.model.MaterialAuditResultModel;
import com.madhouse.platform.premiummad.service.IMaterialService;

@Component
public class ValueMakerStatusApiTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(ValueMakerStatusApiTask.class);

	@Value("${valuemaker.statusUrl}")
	private String getMaterialStatusUrl;

	@Autowired
	private ValueMakerHttpUtil valueMakerHttpUtil;

	@Autowired
	private MaterialMapper materialDao;

	@Autowired
	private IMaterialService materialService;

	/**
	 * valueMaker定时更新广告审核状态。
	 */
	public void getValueMakerMaterialStatus() {
		LOGGER.info("++++++++++ValueMaker get material status begin+++++++++++");

		// 获取审核中的素材
		List<Material> unauditMaterials = materialDao.selectMediaMaterials(MediaMapping.VALUEMAKER.getValue(), MaterialStatusCode.MSC10003.getValue());
		if (unauditMaterials == null || unauditMaterials.isEmpty()) {
			LOGGER.info(MediaMapping.VALUEMAKER.getDescrip() + "无需要审核的素材");
			return;
		}

		// 向媒体获取审核状态
		for (Material item : unauditMaterials) {
			String result = valueMakerHttpUtil.get(getMaterialStatusUrl, item.getMediaQueryKey());
			LOGGER.info("ValueMakerMaterialStatus:getResult=" + result);
			ValueMakerMaterialStatusDetailResponse responseJson = JSON.parseObject(result, ValueMakerMaterialStatusDetailResponse.class);
			String id = responseJson.getId();
			int status = responseJson.getStatus();
			if (id != null && status != 0 && isCorrectreturn(status)) {
				List<MaterialAuditResultModel> auditResults = new ArrayList<MaterialAuditResultModel>();
				MaterialAuditResultModel auditItem = new MaterialAuditResultModel();
				auditItem.setMediaQueryKey(String.valueOf(id));
				auditItem.setMediaId(String.valueOf(MediaMapping.VALUEMAKER.getValue()));
				if (status == ValueMakerConstant.M_STATUS_UNAUDITED.getValue()) {// 待审核
					LOGGER.info("ValueMakerMaterialStatus--materialId=" + item.getId() + "|创意id=" + item.getMediaQueryKey() + "|status=" + status);
				} else if (status == ValueMakerConstant.M_STATUS_APPROVED.getValue()) {// 审核通过}
					auditItem.setStatus(MaterialStatusCode.MSC10004.getValue());
					auditResults.add(auditItem);
				} else if (status == ValueMakerConstant.M_STATUS_REFUSED.getValue()) {// 审核未通过
					auditItem.setStatus(MaterialStatusCode.MSC10001.getValue());
					// 驳回原因接口不提供，媒体方确认 当前只能查到审核不通过，具体原因需要由运营单独提供
					auditItem.setErrorMessage("");
					auditResults.add(auditItem);
				}

				// 更新数据库
				if (!auditResults.isEmpty()) {
					materialService.updateStatusToMedia(auditResults);
				}
			}
		}
		LOGGER.info("++++++++++ValueMaker get material status end+++++++++++");
	}

	public boolean isCorrectreturn(int status) {
		if (status == 1 | status == 2 | status == 3) {
			return true;
		}
		return false;
	}
}
