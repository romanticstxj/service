package com.madhouse.platform.premiummad.media.weibo;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.constant.MaterialStatusCode;
import com.madhouse.platform.premiummad.constant.MediaMapping;
import com.madhouse.platform.premiummad.dao.MaterialMapper;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.media.weibo.constant.WeiboConstant;
import com.madhouse.platform.premiummad.media.weibo.request.WeiboMaterialStatusRequest;
import com.madhouse.platform.premiummad.media.weibo.response.WeiboMaterialStatusMessageDetail;
import com.madhouse.platform.premiummad.media.weibo.response.WeiboMaterialStatusResponse;
import com.madhouse.platform.premiummad.model.MaterialAuditResultModel;
import com.madhouse.platform.premiummad.service.IMaterialService;
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

	@Autowired
	private MaterialMapper materialDao;

	@Autowired
	private IMaterialService materialService;
	
	@Autowired
	private WeiboAdCreativeMidApiTask weiboAdCreativeMidApiTask;

	public void getStatus() {
		LOGGER.info("++++++++++Weibo get material status begin+++++++++++");

		// 我方系统未审核的素材
		List<Material> unAuditMaterials = materialDao.selectMediaMaterials(MediaMapping.WEIBO.getValue(), MaterialStatusCode.MSC10003.getValue());
		if (unAuditMaterials == null || unAuditMaterials.isEmpty()) {
			LOGGER.info("新浪微博没有素材需要审核");
			return;
		}

		// 设置request参数
		List<String> creative_ids = new ArrayList<String>();
		for (Material material : unAuditMaterials) {
			creative_ids.add(material.getMediaQueryKey());
		}
		WeiboMaterialStatusRequest weiboMaterialStatusRequest = new WeiboMaterialStatusRequest();
		weiboMaterialStatusRequest.setCreative_ids(creative_ids);
		weiboMaterialStatusRequest.setDspid(dspid);
		weiboMaterialStatusRequest.setToken(token);

		// 向媒体发送请求
		String requestJson = JSON.toJSONString(weiboMaterialStatusRequest);
		LOGGER.info("WeiboStatusApiRequestJson: " + requestJson);
		String responseJson = HttpUtils.post(materialStatusUrl, requestJson);
		LOGGER.info("WeiboStatusApiResponseJson: " + responseJson);

		// 处理我方数据
		if (!StringUtils.isEmpty(responseJson)) {
			WeiboMaterialStatusResponse weiboMaterialStatusResponse = JSON.parseObject(responseJson, WeiboMaterialStatusResponse.class);
			Integer retCode = weiboMaterialStatusResponse.getRet_code();
			if (WeiboConstant.RESPONSE_SUCCESS.getValue() == retCode) {
				handleSuccessResult(weiboMaterialStatusResponse);
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
	private void handleSuccessResult(WeiboMaterialStatusResponse weiboMaterialStatusResponse) {
		// 请求成功物料状态明细列表
		List<WeiboMaterialStatusMessageDetail> weiboMaterialStatusMessageDetails = weiboMaterialStatusResponse.getRet_msg().getRecords();

		// 处理审核结果
		List<MaterialAuditResultModel> auditResults = new ArrayList<MaterialAuditResultModel>();
		for (WeiboMaterialStatusMessageDetail weiboMaterialStatusMessageDetail : weiboMaterialStatusMessageDetails) {
			String crid = weiboMaterialStatusMessageDetail.getCreative_id().trim();
			String status = weiboMaterialStatusMessageDetail.getStatus();

			MaterialAuditResultModel auditItem = new MaterialAuditResultModel();
			auditItem.setMediaQueryKey(crid);
			auditItem.setMediaId(String.valueOf(MediaMapping.WEIBO.getValue()));

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
