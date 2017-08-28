package com.madhouse.platform.premiummad.media.sohu.news;

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
import com.alibaba.fastjson.JSONObject;
import com.madhouse.platform.premiummad.constant.MaterialStatusCode;
import com.madhouse.platform.premiummad.constant.MediaMapping;
import com.madhouse.platform.premiummad.dao.AdvertiserMapper;
import com.madhouse.platform.premiummad.dao.MaterialMapper;
import com.madhouse.platform.premiummad.entity.Advertiser;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.media.sohu.response.SohuContentMaterialResponse;
import com.madhouse.platform.premiummad.media.sohu.response.SohuResponse;
import com.madhouse.platform.premiummad.media.sohu.response.SohuStatusDetailResponse;
import com.madhouse.platform.premiummad.media.sohu.util.SohuNewsAuth;
import com.madhouse.platform.premiummad.model.MaterialAuditResultModel;
import com.madhouse.platform.premiummad.service.IMaterialService;
import com.madhouse.platform.premiummad.util.HttpUtils;
import com.madhouse.platform.premiummad.util.StringUtils;

@Component
public class SohuNewsStatusApiTask {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SohuNewsStatusApiTask.class);

	@Value("${sohu.material.list}")
	private String materialListUrl;
	
	@Autowired
	private SohuNewsAuth sohuAuth;
	
	@Autowired
	private MaterialMapper materialDao;
	
	@Autowired
	private IMaterialService materialService;
	
	@Autowired
	private AdvertiserMapper advertiserDao;
	
	/**
	 * 获取素材审核结果
	 */
	public void getStatusDetail() {
		LOGGER.info("++++++++++Sohu News get material list begin+++++++++++");

		// 我方系统未审核的素材
		List<Material> unAuditMaterials = materialDao.selectMediaMaterials(MediaMapping.SOHUNEWS.getValue(), MaterialStatusCode.MSC10003.getValue());
		if (unAuditMaterials == null || unAuditMaterials.isEmpty()) {
			LOGGER.info("++++++++++Sohu News no materials need to audit+++++++++++");
			return;
		}
		
		List<MaterialAuditResultModel> auditResults = new ArrayList<MaterialAuditResultModel>();
		for (Material item : unAuditMaterials) {
			// 获取该素材的广告主,若广告主不存在不做处理
			String[] advertiserKeys = { item.getAdvertiserKey() };
			List<Advertiser> advertisers = advertiserDao.selectByAdvertiserKeysAndDspId(advertiserKeys, String.valueOf(item.getDspId()), item.getMediaId());
			if (advertisers == null || advertisers.size() != 1) {
				LOGGER.error("广告主不存在[advertiserKey=" + item.getAdvertiserKey() + "dspId=" + item.getDspId() + "]");
				break;
			}
			
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("customer_key", advertisers.get(0).getMediaAdvertiserKey());
			paramMap.put("file_source", item.getMediaQueryKey());
			paramMap.put("perpage", 50);
			paramMap.put("page", 1);
			
			String request = sohuAuth.setHttpMethod("GET").setApiUrl(materialListUrl).setParamMap(paramMap).buildRequest();
			String url = materialListUrl + "?" + request;
			Map<String, Object> objectMap = HttpUtils.get(url);

			if (objectMap.get(HttpUtils.RESPONSE_BODY_KEY) == null) {
				LOGGER.info("SoHuNewsMaterialStatus-responseJson info： " + objectMap.get(HttpUtils.RESPONSE_BODY_KEY) + "is NULL");
				return;
			}

			String getResult = objectMap.get(HttpUtils.RESPONSE_BODY_KEY).toString();
			LOGGER.info("SoHuNewsMaterialStatus-responseJson info： " + getResult);

			if (!StringUtils.isEmpty(getResult)) {
				SohuResponse sohuResponse = JSON.parseObject(getResult, SohuResponse.class);
				if (sohuResponse != null) {
					// 成功,更新物料任务表状态：为3或者4
					if (sohuResponse.isStatus() && (sohuResponse.getContent() != null && !sohuResponse.getContent().equals(""))) {
						SohuContentMaterialResponse contentResponse = JSONObject.parseObject(sohuResponse.getContent().toString(), SohuContentMaterialResponse.class);
						if (contentResponse != null && contentResponse.getItems() != null) {
							handleResults(item, contentResponse.getItems(), auditResults);
						}
					}
				}
			}
		}
		
		// 更新我方数据库
		if (!auditResults.isEmpty()) {
			materialService.updateStatusToMedia(auditResults);
		}
	}
	
	/**
	 * 处理查询结果
	 * 
	 * @param auditMaterial
	 * @param list
	 */
	private void handleResults(Material auditMaterial, List<SohuStatusDetailResponse> list, List<MaterialAuditResultModel> auditResults) {
		if (list == null || list.size() != 1) {
			LOGGER.info("返回结果有误");
			return;
		}

		// 返回结果处理     
		SohuStatusDetailResponse statusDetail = list.get(0);
		if (statusDetail.getFile_source().equals(auditMaterial.getMediaQueryKey())) {
			MaterialAuditResultModel auditItem = new MaterialAuditResultModel();
			auditItem.setId(auditMaterial.getId().toString());
			auditItem.setMediaId(auditMaterial.getMediaId().toString());
			auditItem.setMediaQueryKey(auditMaterial.getMediaQueryKey());
			
			// 根据返回的审核结果设置内容
			int statusNet = statusDetail.getStatus();
			if (statusNet == 1) {// 审核通过
				auditItem.setStatus(MaterialStatusCode.MSC10004.getValue());
				auditResults.add(auditItem);
			} else if (statusNet == 2) {// 拒绝
				auditItem.setStatus(MaterialStatusCode.MSC10001.getValue());
				auditItem.setErrorMessage(statusDetail.getAudit_info());
				auditResults.add(auditItem);
			} else if (statusNet == 0) {// 未审核
				LOGGER.info("SoHuNewsMaterialStatus :" + statusDetail.getFile_source() + "Not audit");
			}
		} else {
			LOGGER.info("返回结果与请求不匹配");
		}
	}
}
