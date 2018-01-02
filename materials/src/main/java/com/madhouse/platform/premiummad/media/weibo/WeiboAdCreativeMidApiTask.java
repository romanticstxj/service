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
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dao.MaterialMapper;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.media.weibo.constant.WeiboConstant;
import com.madhouse.platform.premiummad.media.weibo.constant.WeiboErrorCode;
import com.madhouse.platform.premiummad.media.weibo.request.WeiboAdCreativeCreateRequest;
import com.madhouse.platform.premiummad.media.weibo.response.WeiboAdCreativeCreateResponse;
import com.madhouse.platform.premiummad.media.weibo.response.WeiboAdCreativeDetail;
import com.madhouse.platform.premiummad.model.MaterialAuditResultModel;
import com.madhouse.platform.premiummad.service.IMaterialService;
import com.madhouse.platform.premiummad.service.IMediaService;
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

	@Value("${material_meidaGroupMapping_weibo}")
	private String mediaGroupStr;
	
	@Autowired
	private MaterialMapper materialDao;

	@Autowired
	private IMaterialService materialService;

	@Autowired
	private IMediaService mediaService;

	public void getMid(String[] creativeIds) {
		LOGGER.info("++++++++++Weibo get material mid begin+++++++++++");

		// 根据媒体组ID和审核对象获取具体的媒体ID
		int[] mediaIds = mediaService.getMeidaIds(mediaGroupStr, SystemConstant.MediaAuditObject.MATERIAL);

		// 媒体组没有映射到具体的媒体不处理
		if (mediaIds == null || mediaIds.length < 1) {
			return;
		}
		
		// 获取审核通过，mid没有回写的素材
		List<Material> noMidMaterials = materialDao.selectMaterials(creativeIds, mediaIds);
		if (noMidMaterials == null || noMidMaterials.isEmpty()) {
			LOGGER.info("Weibo没有需要获取 Mid 的素材");
			return;
		}

		// 设置request参数
		List<String> creative_ids = new ArrayList<String>();
		for (Material material : noMidMaterials) {
			creative_ids.add(material.getMediaQueryKey());
		}

		WeiboAdCreativeCreateRequest weiboAdCreativeCreateRequest = new WeiboAdCreativeCreateRequest();
		weiboAdCreativeCreateRequest.setCreative_ids(creative_ids);
		weiboAdCreativeCreateRequest.setDspid(dspid);
		weiboAdCreativeCreateRequest.setToken(token);

		// 向媒体发送请求
		String requestJson = JSON.toJSONString(weiboAdCreativeCreateRequest);
		LOGGER.info("request: " + requestJson);
		String responseJson = HttpUtils.post(adcreativeUrl, requestJson);
		LOGGER.info("response: " + responseJson);

		// 处理我方数据
		if (!StringUtils.isBlank(responseJson)) {
			WeiboAdCreativeCreateResponse weiboAdCreativeCreateResponse = JSON.parseObject(responseJson, WeiboAdCreativeCreateResponse.class);
			if (WeiboConstant.RESPONSE_SUCCESS.getValue() == weiboAdCreativeCreateResponse.getRet_code().intValue() && WeiboErrorCode.WEC000.getValue() == weiboAdCreativeCreateResponse.getErr_code().intValue()) {
				handleSuccessResult(weiboAdCreativeCreateResponse, mediaIds);
			} else {
				handleErrorResult(weiboAdCreativeCreateResponse, mediaIds);
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
	private void handleSuccessResult(WeiboAdCreativeCreateResponse weiboAdCreativeCreateResponse, int[] mediaIds) {
		// 请求成功物料状态明细列表
		List<WeiboAdCreativeDetail> weiboAdCreativeDetails = weiboAdCreativeCreateResponse.getRet_msg();

		// 处理审核结果
		List<MaterialAuditResultModel> auditResults = new ArrayList<MaterialAuditResultModel>();
		for (WeiboAdCreativeDetail weiboAdCreativeDetail : weiboAdCreativeDetails) {
			String crid = weiboAdCreativeDetail.getCreative_id().trim();
			String mid = weiboAdCreativeDetail.getObj_id();

			MaterialAuditResultModel auditItem = new MaterialAuditResultModel();
			auditItem.setMediaQueryKey(crid);
			auditItem.setMediaIds(mediaIds);
			// 获取 mid 后审核通过
			auditItem.setStatus(MaterialStatusCode.MSC10004.getValue());
			if (!StringUtils.isBlank(mid)) {
				auditItem.setMediaMaterialKey(mid);
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

	/**
	 * 接口调用成功后的处理
	 * 
	 * @param dataSource
	 * @param weiboAdCreativeCreateResponse
	 */
	private void handleErrorResult(WeiboAdCreativeCreateResponse weiboAdCreativeCreateResponse, int[] mediaIds) {
		List<WeiboAdCreativeDetail> details = weiboAdCreativeCreateResponse.getRet_msg();
		if (details == null || details.isEmpty()) {
			LOGGER.info("新浪微博获取Mid失败-" + JSON.toJSONString(weiboAdCreativeCreateResponse));
			return;
		}

		// 系统错误
		if (WeiboErrorCode.WEC100.getValue() == weiboAdCreativeCreateResponse.getRet_code()) {
			LOGGER.error("素材[meidaQueryKey=" + details.get(0).getCreative_id() + "]上传失败-" + WeiboErrorCode.getDescrip(weiboAdCreativeCreateResponse.getRet_code()));
			return;
		}

		// 未知错误
		if (StringUtils.isBlank(details.get(0).getErr_msg())) {
			LOGGER.error("素材[meidaQueryKey=" + details.get(0).getCreative_id() + "]新浪微博获取Mid失败-未知错误");
			return;
		}

		// 已知业务错误
		List<MaterialAuditResultModel> rejusedMaterials = new ArrayList<MaterialAuditResultModel>();
		MaterialAuditResultModel rejuseItem = new MaterialAuditResultModel();
		rejuseItem.setMediaQueryKey(details.get(0).getCreative_id());
		rejuseItem.setMediaIds(mediaIds);
		rejuseItem.setStatus(MaterialStatusCode.MSC10001.getValue());
		rejuseItem.setErrorMessage(details.get(0).getErr_msg());
		rejusedMaterials.add(rejuseItem);
		LOGGER.info("新浪微博获取Mid失败-" + details.get(0).getErr_msg());

		// 处理失败的结果，自动驳回 - 通过素材key更新
		if (!rejusedMaterials.isEmpty()) {
			materialService.updateStatusToMedia(rejusedMaterials);
		}
	}
}
