package com.madhouse.platform.premiummad.media.yiche;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONObject;
import com.madhouse.platform.premiummad.constant.MaterialStatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dao.MaterialMapper;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.media.yiche.constant.YicheConstant;
import com.madhouse.platform.premiummad.media.yiche.response.UploadFileResponse;
import com.madhouse.platform.premiummad.media.yiche.util.YicheCommonUtil;
import com.madhouse.platform.premiummad.service.IMediaService;

@Component
public class YicheFileUploadApiTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(YicheFileUploadApiTask.class);

	@Value("${yiche.fileUploadUrl}")
	private String fileUploadUrl;

	@Value("${yiche.bucket}")
	private String bucket;

	@Value("${yiche.password}")
	private String password;
	
	@Value("${yiche.rsa_oss_ciphertext}")
	private String rsa_oss_ciphertext;

	@Value("${material_meidaGroupMapping_yiche}")
	private String mediaGroupStr;

	@Autowired
	private MaterialMapper materialDao;

	@Autowired
	private IMediaService mediaService;
	
	public void uploadFile() {
		LOGGER.info("++++++++++yiche upload file begin+++++++++++");

		// 根据媒体组ID和审核对象获取具体的媒体ID
		int[] mediaIds = mediaService.getMeidaIds(mediaGroupStr, SystemConstant.MediaAuditObject.MATERIAL);

		// 媒体组没有映射到具体的媒体不处理
		if (mediaIds == null || mediaIds.length < 1) {
			return;
		}

		// 查询所有待审核且媒体的素材的审核状态是媒体审核的
		List<Material> unUploadFileMaterials = materialDao.selectMeidaMaterials(mediaIds, MaterialStatusCode.MSC10002.getValue(), Boolean.FALSE);
		if (unUploadFileMaterials == null || unUploadFileMaterials.isEmpty()) {
			LOGGER.info("yiche没有未上传的文件");
			return;
		}

		// 解析素材图片路径并上传
		for (Material material : unUploadFileMaterials) {
			String[] adUrls = material.getAdMaterials().split("\\|");
			String mediaUrls = "";
			boolean allSuccess = true;
			for (String adUrl : adUrls) {
				String result = YicheCommonUtil.postForm(fileUploadUrl, adUrl, bucket, password, rsa_oss_ciphertext);
				UploadFileResponse response = JSONObject.parseObject(result, UploadFileResponse.class);
				LOGGER.info("response:" + result);
				if (response.getErrorCode() == YicheConstant.ErrorCode.REQUEST_SUCCESS) {
					// 成功
					mediaUrls = mediaUrls + "|" + response.getResult();
				} else {
					allSuccess = false;
					LOGGER.error("素材[id={}],文件[fileUrl={}]上传失败", material.getId(), adUrl);
					break;
				}

			}

			// 更新我方数据,只有所有素材上传成功才更新
			if (allSuccess) {
				Material updateMeterialMediaUrlItem = new Material();
				updateMeterialMediaUrlItem.setMediaMaterialUrl(mediaUrls.substring(1));
				updateMeterialMediaUrlItem.setId(material.getId());
				materialDao.updateByPrimaryKeySelective(updateMeterialMediaUrlItem);
			}
		}
		LOGGER.info("++++++++++yiche upload file status end+++++++++++");
	}
}
