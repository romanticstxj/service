package com.madhouse.platform.premiummad.media.autohome;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.madhouse.platform.premiummad.constant.MaterialStatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dao.AdvertiserMapper;
import com.madhouse.platform.premiummad.dao.MaterialMapper;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.service.IMaterialService;
import com.madhouse.platform.premiummad.service.IMediaService;

@Component
public class AutohomeMaterialUploadApiTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(AutohomeMaterialUploadApiTask.class);
	
	@Value("${autohome.creativeUploadUrl}")
	private String creativeUploadUrl;
	
	@Value("${autohome.dspId}")
	private String dspId;

	@Value("${autohome.dispName}")
	private String dispName;

	@Value("${autohome.signKey}")
	private String signKey;
	
	@Value("${material_meidaGroupMapping_autohome}")
	private String mediaGroupStr;

	@Autowired
	private MaterialMapper materialDao;

	@Autowired
	private IMaterialService materialService;

	@Autowired
	private AdvertiserMapper advertiserDao;

	@Autowired
	private IMediaService mediaService;

	public void uploadMaterial() {
		LOGGER.info("++++++++++autohome upload material begin+++++++++++");
		
		// 根据媒体组ID和审核对象获取具体的媒体ID
		int[] mediaIds = mediaService.getMeidaIds(mediaGroupStr, SystemConstant.MediaAuditObject.MATERIAL);

		// 媒体组没有映射到具体的媒体不处理
		if (mediaIds == null || mediaIds.length < 1) {
			return;
		}

		// 查询所有待审核且媒体的素材的审核状态是媒体审核的
		List<Material> unSubmitMaterials = materialDao.selectMaterialsByMeidaIds(mediaIds, MaterialStatusCode.MSC10002.getValue());
		if (unSubmitMaterials == null || unSubmitMaterials.isEmpty()) {
			LOGGER.info("autohome没有未上传的素材");
			return;
		}

		// 上传到媒体
		LOGGER.info("AutohomeMaterialUploadApiTask-weibo", unSubmitMaterials.size());
		
		LOGGER.info("++++++++++autohome upload material end+++++++++++");
	}
}
