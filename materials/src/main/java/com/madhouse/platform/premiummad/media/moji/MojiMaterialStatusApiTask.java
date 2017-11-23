package com.madhouse.platform.premiummad.media.moji;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.constant.MaterialStatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dao.MaterialMapper;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.media.moji.constant.MojiConstant;
import com.madhouse.platform.premiummad.media.moji.response.MojiMaterialStatusResponse;
import com.madhouse.platform.premiummad.media.moji.util.MojiHttpUtil;
import com.madhouse.platform.premiummad.media.sohu.util.Sha1;
import com.madhouse.platform.premiummad.model.MaterialAuditResultModel;
import com.madhouse.platform.premiummad.service.IMaterialService;
import com.madhouse.platform.premiummad.service.IMediaService;
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

	@Value("${material_meidaGroupMapping_moji}")
	private String mediaGroupStr;
	
	@Autowired
	private IMediaService mediaService;
	
	/**
	 * 墨迹天气上传素材
	 * 
	 * @throws Exception
	 */
	public void getStatusResponse() throws Exception {
		LOGGER.info("++++++++++moji get material status begin+++++++++++");

		// 根据媒体组ID和审核对象获取具体的媒体ID
		int[] mediaIds = mediaService.getMeidaIds(mediaGroupStr, SystemConstant.MediaAuditObject.MATERIAL);

		// 媒体组没有映射到具体的媒体不处理
		if (mediaIds == null || mediaIds.length < 1) {
			return;
		}

		// 我方系统未审核的素材
		List<Material> unAuditMaterials = materialDao.selectMaterialsByMeidaIds(mediaIds, MaterialStatusCode.MSC10003.getValue());
		if (unAuditMaterials == null || unAuditMaterials.isEmpty()) {
			LOGGER.info("墨迹无需要审核的素材");
			return;
		}

		// 向媒体获取审核状态
		Set<String> mediaMaterialKeySet = new HashSet<String>();
		List<MaterialAuditResultModel> auditResults = new ArrayList<MaterialAuditResultModel>();
		for (Material item : unAuditMaterials) {
			// 两个广告位对应媒体一个只要请求一次
			if (mediaMaterialKeySet.contains(item.getMediaQueryKey())) {
				continue;
			}
			
			mediaMaterialKeySet.add(item.getMediaQueryKey());
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("source", source);
			paramMap.put("time_stamp", System.currentTimeMillis() / 1000 + "");
			paramMap.put("advert_id", item.getMediaQueryKey());
			paramMap.put("sign", sha1.SHA1(paramMap));

			String getResult = mojiHttpUtil.get(getMaterialStatusUrl, paramMap);
			LOGGER.info("墨迹MaterialStatusTask-response:" + getResult);

			if (!StringUtils.isEmpty(getResult)) {
				MojiMaterialStatusResponse response = JSON.parseObject(getResult, MojiMaterialStatusResponse.class);
				processResult(response, item, auditResults, mediaIds);
			}
		}

		// 更新数据库
		if (!auditResults.isEmpty()) {
			materialService.updateStatusToMedia(auditResults);
		}

		LOGGER.info("++++++++++moji get material status end+++++++++++");
	}

	/**
	 * 处理墨迹天气返回的审核结果
	 * 
	 * @param response
	 * @param item
	 */
	private void processResult(MojiMaterialStatusResponse response, Material item, List<MaterialAuditResultModel> auditResults, int[] mediaIds) {
		String code = response.getCode();

		MaterialAuditResultModel auditItem = new MaterialAuditResultModel();
		auditItem.setMediaQueryKey(String.valueOf(response.getData().getPosition_id()));
		auditItem.setMediaIds(mediaIds);

		if (code.equals(String.valueOf(MojiConstant.M_STATUS_SUCCESS.getValue()))) { // 审核成功
			auditItem.setStatus(MaterialStatusCode.MSC10004.getValue());
			auditResults.add(auditItem);
		} else if (code.equals(MojiConstant.M_STATUS_ERROR.getValue() + "")) { // 审核失败
			auditItem.setStatus(MaterialStatusCode.MSC10001.getValue());
			auditItem.setErrorMessage(response.getData().getReject_reason());
			auditResults.add(auditItem);
		} else if (code.equals(String.valueOf(MojiConstant.M_STATUS_UNAUDITED.getValue()))) {// 未审核
			LOGGER.info("墨迹MaterialStatusTask-response:物料未审核,id为{}" + item.getId());
		} else {
			LOGGER.info(response.getMessage() + "id为{}" + item.getId());
		}
	}
}
