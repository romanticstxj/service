package com.madhouse.platform.premiummad.media.sohu;

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
import com.madhouse.platform.premiummad.dao.MaterialMapper;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.media.model.SohuContentMaterialResponse;
import com.madhouse.platform.premiummad.media.model.SohutvResponse;
import com.madhouse.platform.premiummad.media.model.SohutvStatusDetailResponse;
import com.madhouse.platform.premiummad.model.AdvertiserAuditResultModel;
import com.madhouse.platform.premiummad.util.HttpUtils;
import com.madhouse.platform.premiummad.util.StringUtils;

@Component
public class SohuNewsStatusApiTask {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SohuNewsStatusApiTask.class);

	@Value("${sohu.material.list}")
	private String materialListUrl;

	@Value("#{'${sohu.material.pageList}'.split(',')}")
	private List<String> pageList;
	
	@Autowired
	private SohuNewsAuth sohuAuth;
	
	@Autowired
	private MaterialMapper materialDao;
	
	/**
	 * 获取素材审核结果
	 */
	public void getStatusDetail() {
		LOGGER.info("++++++++++Sohu News get material list begin+++++++++++");

		for (String page : pageList) {
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("page", page);
			paramMap.put("perpage", 50);
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
				SohutvResponse sohutvResponse = JSON.parseObject(getResult, SohutvResponse.class);
				if (sohutvResponse != null) {
					// 得到状态
					boolean status = sohutvResponse.isStatus();
					// 成功,更新物料任务表状态：为3或者4
					if (status) {
						if (sohutvResponse.getContent() != null && !sohutvResponse.getContent().equals("")) {
							SohuContentMaterialResponse contentResponse = JSONObject.parseObject(sohutvResponse.getContent().toString(), SohuContentMaterialResponse.class);
							if (contentResponse != null) {
								List<SohutvStatusDetailResponse> list = contentResponse.getItems();
								if (list != null && list.size() > 0) {
									handleResults(list);
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 处理查询结果
	 * 
	 */
	private void handleResults(List<SohutvStatusDetailResponse> list) {
		if (list == null || list.isEmpty()) {
			return;
		}

		// 我方系统的广告主
		List<Material> unAuditMaterials = materialDao.selectMediaMaterials(MediaMapping.SOHUNEWS.getValue(), MaterialStatusCode.MSC10003.getValue());

		// 根据返回状态处理我方数据
		List<AdvertiserAuditResultModel> auditResults = new ArrayList<AdvertiserAuditResultModel>();
		for (Material material : unAuditMaterials) {
			String mediaMaterialKeyDb = material.getMediaMaterialKey();
			Integer statusDb = material.getStatus().intValue();
			
			for (SohutvStatusDetailResponse item : list) {
				// TODO
				String customerKeyNet = item.getFile_source();
				// 获取搜狐新闻的审核状态
				Integer statusNet = item.getStatus();
				
			}
		}
	}
}
