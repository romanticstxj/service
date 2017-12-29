package com.madhouse.platform.premiummad.media.momo;

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
import com.madhouse.platform.premiummad.media.momo.request.MomoGetStatusRequest;
import com.madhouse.platform.premiummad.media.momo.response.MomoGetStatusResponse;
import com.madhouse.platform.premiummad.media.momo.util.MomoHttpUtils;
import com.madhouse.platform.premiummad.model.MaterialAuditResultModel;
import com.madhouse.platform.premiummad.service.IMaterialService;
import com.madhouse.platform.premiummad.service.IMediaService;
import com.madhouse.platform.premiummad.util.StringUtils;

/**
 * desc : 获取创意状态 1-通过,0待审核,-1-驳回
 */
@Component
public class MomoGetStatusApiTask {

	private Logger LOGGER = LoggerFactory.getLogger(MomoGetStatusApiTask.class);

	@Value("${momo_status_url}")
	private String statusUrl;

	@Value("${momo_upload_dspid}")
	private String dspId;
	
	@Value("${material_meidaGroupMapping_momo}")
	private String mediaGroupStr;

	@Autowired
	private MomoHttpUtils momoHttpUtil;

	@Autowired
	private MaterialMapper materialDao;

	@Autowired
	private IMaterialService materialService;

	@Autowired
	private IMediaService mediaService;

	public void getStatusResponse() throws Exception {
		LOGGER.info("++++++++++Momo get material status begin+++++++++++");
		
		// 根据媒体组ID和审核对象获取具体的媒体ID
		int[] mediaIds = mediaService.getMeidaIds(mediaGroupStr, SystemConstant.MediaAuditObject.MATERIAL);

		// 媒体组没有映射到具体的媒体不处理
		if (mediaIds == null || mediaIds.length < 1) {
			return;
		}

		// 获取审核中的素材
		List<Material> unauditMaterials = materialDao.selectMaterialsByMeidaIds(mediaIds, MaterialStatusCode.MSC10003.getValue());
		if (unauditMaterials == null || unauditMaterials.isEmpty()) {
			LOGGER.info("Momo无需要审核的素材");
			return;
		}
		
		// 获取媒体方的素材 crid
		Set<String> crids = new HashSet<>();
		Set<String> distinctIds = new HashSet<String>();
		for (Material material : unauditMaterials) {
			// 去重
			if (distinctIds.contains(material.getMediaQueryKey())) {
				continue;
			}
			distinctIds.add(material.getMediaQueryKey());
			crids.add(material.getMediaQueryKey());
		}

		// 向陌陌发请求
		Map<String, String> param = new HashMap<String, String>();
		MomoGetStatusRequest reqParams = new MomoGetStatusRequest();
		reqParams.setDspid(dspId);
		reqParams.setCrids(new ArrayList<>(crids));
		param.put("data", JSON.toJSONString(reqParams));
		LOGGER.info("Request: " + JSON.toJSONString(reqParams));
		String response = momoHttpUtil.post(statusUrl, param);
		LOGGER.info("Response: ", response);

		// 处理返回的结果
		if (response.contains("502 Bad Gateway")) {
			LOGGER.error("服务器异常URL[" + statusUrl + "]", response);
			return;
		}
		MomoGetStatusResponse statusResponse = JSON.parseObject(response, MomoGetStatusResponse.class);
		List<MomoGetStatusResponse.DataBean> list = statusResponse.getData();
		if (!StringUtils.isEmpty(list)) {
			List<MaterialAuditResultModel> auditResults = new ArrayList<MaterialAuditResultModel>();
			for (MomoGetStatusResponse.DataBean resultListBean : list) {
				MaterialAuditResultModel auditItem = new MaterialAuditResultModel();
				auditItem.setMediaQueryKey(resultListBean.getCrid());
				auditItem.setMediaIds(mediaIds);

				String status = String.valueOf(resultListBean.getStatus());
				String reason = resultListBean.getReason();
				LOGGER.info("Momo Material Status:mediaMaterialKey=" + resultListBean.getCrid() + "- status: " + status + "- reason: " + reason);

				switch (status) {
				case "2": // 通过2
					auditItem.setStatus(MaterialStatusCode.MSC10004.getValue());
					auditResults.add(auditItem);
					break;
				case "3":// 驳回 3
					auditItem.setStatus(MaterialStatusCode.MSC10001.getValue());
					auditItem.setErrorMessage(resultListBean.getReason());
					auditResults.add(auditItem);
					break;
				case "1": // 待审核 1
					LOGGER.info("MOMO Material Status:mediaMaterialKey=" + resultListBean.getCrid() + "- status: 待审核。");
					break;
				case "4":// 删除4
					LOGGER.info("MOMO Material Status:mediaMaterialKey=" + resultListBean.getCrid() + "- status: 删除。");
					break;

				case "5": // 下线5
					LOGGER.info("MOMO Material Status:mediaMaterialKey=" + resultListBean.getCrid() + "- status: 下线。");
					break;

				default:
					LOGGER.info("MOMO Material Status:mediaMaterialKey=" + resultListBean.getCrid() + "- status: unknow.");
				}
			}

			// 更新数据库
			if (!auditResults.isEmpty()) {
				materialService.updateStatusToMedia(auditResults);
			}
		}
		LOGGER.info("++++++++++Momo get material status end+++++++++++");
	}
}
