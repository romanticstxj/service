package com.madhouse.platform.premiummad.media.iqiyi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.madhouse.platform.premiummad.constant.MaterialStatusCode;
import com.madhouse.platform.premiummad.constant.MediaMapping;
import com.madhouse.platform.premiummad.dao.MaterialMapper;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.media.util.IQiYiHttpUtils;
import com.madhouse.platform.premiummad.service.IMaterialService;

@Component("iQiyiMaterialUploadApiTask")
public class IQiyiMaterialUploadApiTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(IQiyiMaterialUploadApiTask.class);

	@Value("${iqiyi.material.create}")
	private String materialCreateUrl;

	@Value("${file.material.root:/mnt/vda}")
	private String materialFileRoot;

	@Autowired
	private IQiYiHttpUtils iQiYiHttpUtils;

	@Autowired
	private MaterialMapper materialDao;
	
	@Autowired
	private IMaterialService materialService;
	
	/**
	 * 上传广告物料
	 */
	public void uploadMaterial() {
		LOGGER.info("++++++++++iqiyi upload material begin+++++++++++");
		
		// 查询所有待审核且媒体的素材的审核状态是媒体审核的
		List<Material> unSubmitMaterials = materialDao.selectMediaMaterials(MediaMapping.SOHUNEWS.getValue(), MaterialStatusCode.MSC10002.getValue());
		if (unSubmitMaterials == null || unSubmitMaterials.isEmpty()) {
			LOGGER.info("搜狐没有未上传的广告主");
			LOGGER.info("++++++++++iqiyi upload material end+++++++++++");
			return;
		}

		// 上传到媒体
		LOGGER.info("IQiyiMaterialUploadApiTask-iqiyi", unSubmitMaterials.size());
		Map<Integer, String> materialIdKeys = new HashMap<Integer, String>();
		for (Material material : unSubmitMaterials) {
			Map<String, Object> paramMap = buildMaterialRequest(material);
			if (paramMap == null) {
				continue;
			}
		}
		LOGGER.info("++++++++++iqiyi upload material end+++++++++++");
	}
	
	/**
	 * 处理上传物料api的请求json
	 * 
	 * @param material
	 * @return
	 */
	private Map<String, Object> buildMaterialRequest(Material material) {
		// TODO
		return null;
	}
	
}
