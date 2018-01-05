package com.madhouse.platform.premiummad.media.yiche;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.madhouse.platform.premiummad.constant.MaterialStatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dao.MaterialMapper;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.service.IMaterialService;
import com.madhouse.platform.premiummad.service.IMediaService;

@Component
public class YicheUploadFileApiTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(YicheUploadFileApiTask.class);

	@Value("${yiche.fileUploadUrl}")
	private String fileUploadUrl;

	@Value("${yiche.dspId}")
	private String dspId;

	@Value("${yiche.signKey}")
	private String signKey;

	@Value("${material_meidaGroupMapping_yiche}")
	private String mediaGroupStr;

	@Autowired
	private MaterialMapper materialDao;

	@Autowired
	private IMaterialService materialService;

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

		// TODO

		LOGGER.info("++++++++++yiche upload file status end+++++++++++");
	}
}
