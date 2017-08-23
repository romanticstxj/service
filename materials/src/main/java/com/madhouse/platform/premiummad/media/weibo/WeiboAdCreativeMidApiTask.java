package com.madhouse.platform.premiummad.media.weibo;

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
import com.madhouse.platform.premiummad.media.weibo.constant.WeiboConstant;
import com.madhouse.platform.premiummad.media.weibo.request.WeiboAdCreativeCreateRequest;
import com.madhouse.platform.premiummad.media.weibo.response.WeiboAdCreativeCreateResponse;
import com.madhouse.platform.premiummad.media.weibo.response.WeiboAdCreativeDetail;
import com.madhouse.platform.premiummad.model.MaterialAuditResultModel;
import com.madhouse.platform.premiummad.service.IMaterialService;
import com.madhouse.platform.premiummad.util.HttpUtils;
import com.madhouse.platform.premiummad.util.StringUtils;

@Component
public class WeiboAdCreativeMidApiTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(WeiboAdCreativeMidApiTask.class);

	@Value("${weibo.adcreativeUrl}")
	private String adcreativeUrl;

	@Value("${weibo.dspid}")
	private String dspid;

	@Value("${weibo.token}")
	private String token;

	@Autowired
	private MaterialMapper materialDao;

	@Autowired
	private IMaterialService materialService;

	public void getMid(List<String> creativeIds) {
		LOGGER.info("++++++++++Weibo get material mid begin+++++++++++");

		// 获取审核通过，mid没有回写的素材 TODO
		List<Material> noMidMaterials = materialDao.selectMediaMaterials(MediaMapping.WEIBO.getValue(), MaterialStatusCode.MSC10003.getValue());
		if (noMidMaterials == null || noMidMaterials.isEmpty()) {
			LOGGER.info("新浪微博没有需要获取 Mid 的素材");
			return;
		}

		// 设置request参数
		List<String> creative_ids = new ArrayList<String>();
		for (Material material : noMidMaterials) {
			creative_ids.add(material.getMediaMaterialKey());
		}

		WeiboAdCreativeCreateRequest weiboAdCreativeCreateRequest = new WeiboAdCreativeCreateRequest();
		weiboAdCreativeCreateRequest.setCreative_ids(creative_ids);
		weiboAdCreativeCreateRequest.setDspid(dspid);
		weiboAdCreativeCreateRequest.setToken(token);

		// 向媒体发送请求
		String requestJson = JSON.toJSONString(weiboAdCreativeCreateRequest);
		LOGGER.info("WeiboAdCreativeMidApiRequestJson: " + requestJson);
		String responseJson = HttpUtils.post(adcreativeUrl, requestJson);
		LOGGER.info("WeiboAdCreativeMidApiResponseJson: " + responseJson);

		// 处理我方数据
		if (!StringUtils.isBlank(responseJson)) {
			WeiboAdCreativeCreateResponse weiboAdCreativeCreateResponse = JSON.parseObject(responseJson, WeiboAdCreativeCreateResponse.class);
			Integer retCode = weiboAdCreativeCreateResponse.getRet_code();
			if (WeiboConstant.RESPONSE_SUCCESS.getValue() == retCode) {
				handleSuccessResult(weiboAdCreativeCreateResponse);
			} else {
				LOGGER.info("新浪微博获取Mid失败-" + responseJson);
			}
		} else {
			LOGGER.info("新浪微博获取Mid失败");
		}

		LOGGER.info("++++++++++Weibo get material mid end+++++++++++");
	}

	/**
	 * 接口调用成功后的处理
	 * 
	 * @param dataSource
	 * @param weiboAdCreativeCreateResponse
	 */
	private void handleSuccessResult(WeiboAdCreativeCreateResponse weiboAdCreativeCreateResponse) {
		// 请求成功物料状态明细列表
		List<WeiboAdCreativeDetail> weiboAdCreativeDetails = weiboAdCreativeCreateResponse.getRet_msg();

		// 处理审核结果
		List<MaterialAuditResultModel> auditResults = new ArrayList<MaterialAuditResultModel>();
		for (WeiboAdCreativeDetail weiboAdCreativeDetail : weiboAdCreativeDetails) {
			String crid = weiboAdCreativeDetail.getCreative_id().trim();
			String mid = weiboAdCreativeDetail.getObj_id();

			MaterialAuditResultModel auditItem = new MaterialAuditResultModel();
			auditItem.setMediaMaterialKey(crid);
			auditItem.setMediaId(String.valueOf(MediaMapping.WEIBO.getValue()));

			if (!StringUtils.isBlank(mid)) {
				auditItem.setMediaMaterialId(mid);
				auditResults.add(auditItem);
			} else {
				LOGGER.info("获取Mid失败[meidaMaterialId=" + crid + "]-" + weiboAdCreativeDetail.getErr_msg());
			}
		}

		// 更新数据库
		if (!auditResults.isEmpty()) {
			materialService.updateStatusToMedia(auditResults);
		}
	}
}
