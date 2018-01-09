package com.madhouse.platform.premiummad.media.yiche;

import java.io.InputStream;
import java.util.List;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.madhouse.platform.premiummad.constant.MaterialStatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dao.MaterialMapper;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.service.IMediaService;

@Component
public class YicheFileUploadApiTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(YicheFileUploadApiTask.class);

	@Value("${yiche.fileUploadUrl}")
	private String fileUploadUrl;

	@Value("${yiche.bucket}")
	private String bucket;

	@Value("${yiche.rsa_oss_ciphertext}")
	private String rsa_oss_ciphertext;

	@Value("${material_meidaGroupMapping_yiche}")
	private String mediaGroupStr;

	@Autowired
	private MaterialMapper materialDao;

	@Autowired
	private IMediaService mediaService;
	
	private static HttpClient httpClient = new HttpClient();

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
			String[] adUrls = material.getAdMaterials().split("//|");
			String mediaUrls = "";
			for (String adUrl : adUrls) {
				// 上传到媒体
				String mediaUrl = uploadToMedia(adUrl);
				mediaUrls = mediaUrls + "|" + mediaUrl;
			}
			// 更新我方数据
			Material updateMeterialMediaUrlItem = new Material();
			updateMeterialMediaUrlItem.setMediaMaterialUrl(mediaUrls.substring(1));
			updateMeterialMediaUrlItem.setId(material.getId());
			materialDao.updateByPrimaryKeySelective(updateMeterialMediaUrlItem);
		}
		LOGGER.info("++++++++++yiche upload file status end+++++++++++");
	}

	/**
	 * 上传到媒体
	 * 
	 * @param fileUrl
	 * @return
	 */
	private String uploadToMedia(String fileUrl) {
		// TODO
		return "";
	}
	
	/**
	 * 获取流文件内容
	 * 
	 * @param filePath
	 * @return
	 */
	private InputStream getStreamContent(String filePath) {
		GetMethod getMethod = new GetMethod(filePath);
		InputStream inputStream = null;
		try {
			if (httpClient.executeMethod(getMethod) == HttpStatus.SC_OK) {
				inputStream = getMethod.getResponseBodyAsStream();
			}
		} catch (Exception e) {
			LOGGER.info("获取视频出现异常-" + e.getMessage());
		}
		return inputStream;
	}
}
