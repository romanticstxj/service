package com.madhouse.platform.premiummad.media.dianping;

import java.net.URLEncoder;
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
import com.madhouse.platform.premiummad.media.dianping.response.DianpingGetStatusResponse;
import com.madhouse.platform.premiummad.media.dianping.util.DianpingHttpUtil;
import com.madhouse.platform.premiummad.model.MaterialAuditResultModel;
import com.madhouse.platform.premiummad.service.IMaterialService;
import com.madhouse.platform.premiummad.util.StringUtils;

/**
 * desc : 获取创意状态 1-通过,0待审核,-1-驳回
 */
@Component
public class DianpingGetStatusApiTask {

	private Logger LOGGER = LoggerFactory.getLogger(DianpingGetStatusApiTask.class);

	@Value("${dianping.statusUrl}")
    private String statusUrl;

    @Autowired
    private DianpingHttpUtil dianpingHttpUtil;
    
    @Autowired
    private MaterialMapper materialDao;
    
    @Autowired
    private IMaterialService materialService;

	public void getStatusResponse() throws Exception {
		LOGGER.info("++++++++++Dianping get material status begin+++++++++++");
		
		// 获取审核中的素材
		List<Material> unauditMaterials = materialDao.selectMediaMaterials(MediaMapping.DIANPING.getValue(), MaterialStatusCode.MSC10003.getValue());
		if (unauditMaterials == null || unauditMaterials.isEmpty()) {
			LOGGER.info(MediaMapping.DIANPING.getDescrip() + "无需要审核的素材");
			return;
		}
		
		// 获取媒体方的素材ID
		List<String> creativeIdList = new ArrayList<String>();
		for (Material material : unauditMaterials) {
			creativeIdList.add(material.getMediaMaterialKey());
		}
		LOGGER.info("美团点评获取创意审核状态信息的请求创意ID列表:{}", creativeIdList.toString());
		
		// 发请求
		Map<String, String> param = new HashMap<String, String>();
        param.put("creativeIdList", URLEncoder.encode(creativeIdList.toString(), "utf-8"));
        String brandType = "001";// TODO
        String response = dianpingHttpUtil.get(statusUrl, param, brandType);
        LOGGER.info("美团点评获取创意审核状态信息请求返回:{}", response);
        
        // 更新我方素材信息
        DianpingGetStatusResponse dianpingGetStatusResponse = JSON.parseObject(response, DianpingGetStatusResponse.class);
        List<DianpingGetStatusResponse.DataBean.ResultListBean> list = dianpingGetStatusResponse.getData().getResultList();
        if (!StringUtils.isEmpty(list)) {
        	// 根据返回状态处理我方数据
    		List<MaterialAuditResultModel> auditResults = new ArrayList<MaterialAuditResultModel>();
    		for (DianpingGetStatusResponse.DataBean.ResultListBean item : list) {
    			MaterialAuditResultModel auditItem = new MaterialAuditResultModel();
    			auditItem.setMediaMaterialKey(String.valueOf(item.getCreativeId()));
    			auditItem.setMediaId(String.valueOf(MediaMapping.DIANPING.getValue()));
				
    			// 1-通过
    			if (item.getStatus() == 1) {
    				auditItem.setStatus(MaterialStatusCode.MSC10004.getValue());
    				auditResults.add(auditItem);
    			}
    			
    			// -1-驳回
    			if (item.getStatus() == 1) {
    				auditItem.setStatus(MaterialStatusCode.MSC10001.getValue());
    				auditItem.setErrorMessage(item.getReason());
    				auditResults.add(auditItem);
    			}
    		}
    		
    		// 更新数据库
     		if (!auditResults.isEmpty()) {
     			materialService.updateStatusToMedia(auditResults);
     		}
        }
        
		LOGGER.info("++++++++++Dianping get material status end+++++++++++");
	}
}
