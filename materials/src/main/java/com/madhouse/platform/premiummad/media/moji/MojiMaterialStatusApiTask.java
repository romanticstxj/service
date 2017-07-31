package com.madhouse.platform.premiummad.media.moji;

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
import com.madhouse.platform.premiummad.media.constant.IMojiConstant;
import com.madhouse.platform.premiummad.media.model.MojiMaterialStatusResponse;
import com.madhouse.platform.premiummad.media.util.MojiHttpUtil;
import com.madhouse.platform.premiummad.media.util.Sha1;
import com.madhouse.platform.premiummad.model.MaterialAuditResultModel;
import com.madhouse.platform.premiummad.service.IMaterialService;
import com.madhouse.platform.premiummad.util.StringUtils;

@Component
public class MojiMaterialStatusApiTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(MojiMaterialStatusApiTask.class);

	@Value("${moji.statusUrl}")
	private String getMaterialStatusUrl;

	@Value("${moji.source}")
	private String source;

	@Value("${moji.secret}")
	private String secret;

	@Autowired
	private MojiHttpUtil mojiHttpUtil;

	@Autowired
	private Sha1 sha1;

	@Autowired
	private MaterialMapper materialDao;

	@Autowired
	private IMaterialService materialService;

	/**
	 * 墨迹天气上传素材
	 * 
	 * @throws Exception
	 */
	public void getStatusResponse() throws Exception {
		LOGGER.info("++++++++++moji get material status begin+++++++++++");

		// 获取审核中的素材
		List<Material> unauditMaterials = materialDao.selectMediaMaterials(MediaMapping.MOJI.getValue(), MaterialStatusCode.MSC10003.getValue());
		if (unauditMaterials == null || unauditMaterials.isEmpty()) {
			LOGGER.info(MediaMapping.MOJI.getDescrip() + "无需要审核的素材");
			return;
		}

		// 向媒体获取审核状态
		for (Material item : unauditMaterials) {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("source", source);
			paramMap.put("time_stamp", System.currentTimeMillis() / 1000 + "");
			paramMap.put("advert_id", item.getMediaMaterialKey());
			paramMap.put("sign", sha1.SHA1(paramMap));

			String getResult = mojiHttpUtil.get(getMaterialStatusUrl, paramMap);
			LOGGER.info("墨迹MaterialStatusTask-response:" + getResult);

			if (!StringUtils.isEmpty(getResult)) {
				MojiMaterialStatusResponse response = JSON.parseObject(getResult, MojiMaterialStatusResponse.class);
				processResult(response, item);
			}
		}

		LOGGER.info("++++++++++moji get material status end+++++++++++");
	}

	/**
	 * 处理墨迹天气返回的审核结果
	 * 
	 * @param response
	 * @param item
	 */
	private void processResult(MojiMaterialStatusResponse response, Material item) {
		String code = response.getCode();

		List<MaterialAuditResultModel> auditResults = new ArrayList<MaterialAuditResultModel>();
		MaterialAuditResultModel auditItem = new MaterialAuditResultModel();
		auditItem.setMediaMaterialKey(String.valueOf(response.getData().getPosition_id()));
		auditItem.setMediaId(String.valueOf(MediaMapping.DIANPING.getValue()));

		if (code.equals(String.valueOf(IMojiConstant.M_STATUS_SUCCESS.getValue()))) { // 审核成功
			auditItem.setStatus(MaterialStatusCode.MSC10004.getValue());
			auditResults.add(auditItem);
		} else if (code.equals(IMojiConstant.M_STATUS_ERROR.getValue() + "")) { // 审核失败
			auditItem.setStatus(MaterialStatusCode.MSC10001.getValue());
			auditItem.setErrorMessage(response.getData().getReject_reason());
			auditResults.add(auditItem);
		} else if (code.equals(String.valueOf(IMojiConstant.M_STATUS_UNAUDITED.getValue()))) {// 未审核
			LOGGER.info("墨迹MaterialStatusTask-response:物料未审核,id为{}" + item.getId());
		} else {
			LOGGER.info(response.getMessage() + "id为{}" + item.getId());
		}

		// 更新数据库
		if (!auditResults.isEmpty()) {
			materialService.updateStatusToMedia(auditResults);
		}
	}
}
