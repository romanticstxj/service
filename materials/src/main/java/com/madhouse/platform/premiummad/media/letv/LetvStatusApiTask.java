package com.madhouse.platform.premiummad.media.letv;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.madhouse.platform.premiummad.constant.MaterialStatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dao.MaterialMapper;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.media.letv.constant.LetvConstant;
import com.madhouse.platform.premiummad.media.letv.request.LetvStatusRequest;
import com.madhouse.platform.premiummad.media.letv.request.LetvTokenRequest;
import com.madhouse.platform.premiummad.media.letv.response.LetvResponse;
import com.madhouse.platform.premiummad.media.letv.response.LetvStatusDetailResponse;
import com.madhouse.platform.premiummad.model.MaterialAuditResultModel;
import com.madhouse.platform.premiummad.service.IMaterialService;
import com.madhouse.platform.premiummad.service.IMediaService;
import com.madhouse.platform.premiummad.util.HttpUtils;
import com.madhouse.platform.premiummad.util.StringUtils;

@Component
public class LetvStatusApiTask {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LetvStatusApiTask.class);

	@Value("${letv.statusUrl}")
	private String statusUrl;
	
	@Autowired
    private LetvTokenRequest tokenRequest;

	@Autowired
	private MaterialMapper materialDao;
	
	@Autowired
	private IMaterialService materialService;
	
	@Value("${material_meidaGroupMapping_letv}")
	private String mediaGroupStr;
	
	@Autowired
	private IMediaService mediaService;
	
	public void getStatusDetail() {
		LOGGER.info("++++++++++Letv get material status begin+++++++++++");

		// 根据媒体组ID和审核对象获取具体的媒体ID
		int[] mediaIds = mediaService.getMeidaIds(mediaGroupStr, SystemConstant.MediaAuditObject.MATERIAL);

		// 媒体组没有映射到具体的媒体不处理
		if (mediaIds == null || mediaIds.length < 1) {
			return;
		}

		// 我方系统未审核的素材
		List<Material> unAuditMaterials = materialDao.selectMaterialsByMeidaIds(mediaIds, MaterialStatusCode.MSC10003.getValue());
		if (unAuditMaterials == null || unAuditMaterials.isEmpty()) {
			LOGGER.info("++++++++++Letv News no materials need to audit+++++++++++");
			return;
		}

		// 请求
		LetvStatusRequest letvStatusRequest = buildStatusRequest(unAuditMaterials);
		String requestJson = JSON.toJSONString(letvStatusRequest);
		LOGGER.info("LetvStatus request info " + requestJson);
		String responseJson = HttpUtils.post(statusUrl, requestJson);
		LOGGER.info("LetvStatus response info " + responseJson);

		if (!StringUtils.isEmpty(responseJson)) {
			LetvResponse letvResponse = JSON.parseObject(responseJson, LetvResponse.class);
			Integer result = letvResponse.getResult();
			Map<String, String> message = letvResponse.getMessage();// key为：total，records
			if (result.equals(LetvConstant.RESPONSE_SUCCESS.getValue())) {// 成功,更新物料任务表状态
				Object object = message.get("records");
				List<LetvStatusDetailResponse> letvStatusDetailResponse = JSONArray.parseArray(object.toString(), LetvStatusDetailResponse.class);
				// 根据返回状态处理我方数据
				List<MaterialAuditResultModel> auditResults = new ArrayList<MaterialAuditResultModel>();
				for (LetvStatusDetailResponse statusDetail : letvStatusDetailResponse) {
					MaterialAuditResultModel auditItem = new MaterialAuditResultModel();
					auditItem.setMediaQueryKey(String.valueOf(statusDetail.getUrl()));
					auditItem.setMediaIds(mediaIds);
					if (statusDetail.getResult().equals("通过")) {
						auditItem.setStatus(MaterialStatusCode.MSC10004.getValue());
						auditResults.add(auditItem);
					} else if (statusDetail.getResult().equals("未通过")) {
						auditItem.setStatus(MaterialStatusCode.MSC10001.getValue());
						auditItem.setErrorMessage(statusDetail.getReason());
						auditResults.add(auditItem);
					}
				}

				// 更新数据库
				if (!auditResults.isEmpty()) {
					materialService.updateStatusToMedia(auditResults);
				}
			} else if (result.equals(LetvConstant.RESPONSE_PARAM_CHECK_FAIL.getValue())) {
				// 失败,纪录错误信息
				LOGGER.error("素材[materialId=" + getMaterialIds(unAuditMaterials) + "]获取状态失败");
			} else {
				LOGGER.error("素材[materialId=" + getMaterialIds(unAuditMaterials) + "]获取状态失败");
			}
		}

		LOGGER.info("++++++++++Letv get material status end+++++++++++");
	}
	
	private LetvStatusRequest buildStatusRequest(List<Material> unAuditMaterials) {
		LetvStatusRequest letvStatusRequest = new LetvStatusRequest();
		List<String> adUrl = new ArrayList<String>();
		for (Material item : unAuditMaterials) {
			adUrl.add(item.getMediaQueryKey());
		}
		letvStatusRequest.setAdurl(adUrl);
    	letvStatusRequest.setDspid(tokenRequest.getDspid());
    	letvStatusRequest.setToken(tokenRequest.getToken());
		return letvStatusRequest;
	}
	
	/**
	 * 获取媒体ID列表
	 * 
	 * @param unAuditMaterials
	 * @return
	 */
	private List<Integer> getMaterialIds(List<Material> unAuditMaterials) {
		List<Integer> materialIds = new ArrayList<Integer>();
		if (unAuditMaterials == null || unAuditMaterials.isEmpty()) {
			return materialIds;
		}

		for (Material material : unAuditMaterials) {
			materialIds.add(material.getId());
		}
		return materialIds;
	}
}
