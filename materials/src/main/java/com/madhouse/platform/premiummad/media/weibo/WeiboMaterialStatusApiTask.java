package com.madhouse.platform.premiummad.media.weibo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.constant.MaterialStatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dao.MaterialMapper;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.media.weibo.constant.WeiboConstant;
import com.madhouse.platform.premiummad.media.weibo.request.WeiboMaterialStatusRequest;
import com.madhouse.platform.premiummad.media.weibo.response.WeiboMaterialStatusMessageDetail;
import com.madhouse.platform.premiummad.media.weibo.response.WeiboMaterialStatusResponse;
import com.madhouse.platform.premiummad.model.MaterialAuditResultModel;
import com.madhouse.platform.premiummad.service.IMaterialService;
import com.madhouse.platform.premiummad.service.IMediaService;
import com.madhouse.platform.premiummad.util.HttpUtils;

@Component
public class WeiboMaterialStatusApiTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(WeiboMaterialStatusApiTask.class);

	@Value("${weibo.materialStatusUrl}")
	private String materialStatusUrl;

	@Value("${weibo.dspid}")
	private String dspid;

	@Value("${weibo.token}")
	private String token;

	@Value("${material_meidaGroupMapping_weibo}")
	private String mediaGroupStr;

	
	@Autowired
	private MaterialMapper materialDao;

	@Autowired
	private IMaterialService materialService;
	
	@Autowired
	private WeiboAdCreativeMidApiTask weiboAdCreativeMidApiTask;
	
	@Autowired
	private IMediaService mediaService;
	
	public void getStatus() {
		LOGGER.info("++++++++++Weibo get material status begin+++++++++++");
		
		// 根据媒体组ID和审核对象获取具体的媒体ID
		int[] mediaIds = mediaService.getMeidaIds(mediaGroupStr, SystemConstant.MediaAuditObject.MATERIAL);

		// 媒体组没有映射到具体的媒体不处理
		if (mediaIds == null || mediaIds.length < 1) {
			return;
		}

		// 我方系统未审核的素材
		List<Material> unAuditMaterials = materialDao.selectMaterialsByMeidaIds(mediaIds, MaterialStatusCode.MSC10003.getValue());
		if (unAuditMaterials == null || unAuditMaterials.isEmpty()) {
			LOGGER.info("Weibo没有素材需要审核");
			return;
		}

		// 设置request参数
		Set<String> distinctIds = new HashSet<String>();
		List<String> creative_ids = new ArrayList<String>();
		for (Material material : unAuditMaterials) {
			// 去重
			if (distinctIds.contains(material.getMediaQueryKey())) {
				continue;
			}
			distinctIds.add(material.getMediaQueryKey());
			creative_ids.add(material.getMediaQueryKey());
		}
		WeiboMaterialStatusRequest weiboMaterialStatusRequest = new WeiboMaterialStatusRequest();
		weiboMaterialStatusRequest.setCreative_ids(creative_ids);
		weiboMaterialStatusRequest.setDspid(dspid);
		weiboMaterialStatusRequest.setToken(token);

		// 向媒体发送请求
		String requestJson = JSON.toJSONString(weiboMaterialStatusRequest);
		LOGGER.info("request: " + requestJson);
		String responseJson = HttpUtils.post(materialStatusUrl, requestJson);
		LOGGER.info("response: " + responseJson);

		// 处理我方数据
		if (!StringUtils.isEmpty(responseJson)) {
			WeiboMaterialStatusResponse weiboMaterialStatusResponse = JSON.parseObject(responseJson, WeiboMaterialStatusResponse.class);
			Integer retCode = weiboMaterialStatusResponse.getRet_code();
			if (WeiboConstant.RESPONSE_SUCCESS.getValue() == retCode) {
				handleSuccessResult(weiboMaterialStatusResponse, mediaIds);
			} else {
				LOGGER.info("新浪微博获取状态失败-" + responseJson);
			}
		} else {
			LOGGER.info("新浪微博获取状态失败");
		}

		LOGGER.info("++++++++++Weibo get material status end+++++++++++");
	}

	/**
	 * 接口调用成功后的处理
	 * 
	 * @param weiboMaterialStatusResponse
	 */
	private void handleSuccessResult(WeiboMaterialStatusResponse weiboMaterialStatusResponse, int[] mediaIds) {
		// 请求成功物料状态明细列表
		List<WeiboMaterialStatusMessageDetail> weiboMaterialStatusMessageDetails = weiboMaterialStatusResponse.getRet_msg().getRecords();

		// 处理审核结果
		List<MaterialAuditResultModel> auditResults = new ArrayList<MaterialAuditResultModel>();
		for (WeiboMaterialStatusMessageDetail weiboMaterialStatusMessageDetail : weiboMaterialStatusMessageDetails) {
			String crid = weiboMaterialStatusMessageDetail.getCreative_id().trim();
			String status = weiboMaterialStatusMessageDetail.getStatus();

			MaterialAuditResultModel auditItem = new MaterialAuditResultModel();
			auditItem.setMediaQueryKey(crid);
			auditItem.setMediaIds(mediaIds);

			// 根据返回的物料状态明细的materialId,遍历物料任务列表找到有响应物料的物料任务进行处理
			if (WeiboConstant.M_STATUS_APPROVED.getDescription().equals(status)) { // 审核通过
				// 成功后发送请求获取状态
				weiboAdCreativeMidApiTask.getMid(new String[]{crid});
			} else if (WeiboConstant.M_STATUS_UNAUDITED.getDescription().equals(status)) { // 未审核
				LOGGER.info("WeiboMaterialStatusApiTask:mediaMaterialKey=" + crid + " " + status);
			} else if (WeiboConstant.M_STATUS_REFUSED.getDescription().equals(status)) { // 驳回
				auditItem.setStatus(MaterialStatusCode.MSC10001.getValue());
				auditItem.setErrorMessage(weiboMaterialStatusMessageDetail.getReason());
				auditResults.add(auditItem);
			}
		}

		// 更新数据库
		if (!auditResults.isEmpty()) {
			materialService.updateStatusToMedia(auditResults);
		}
	}
}
