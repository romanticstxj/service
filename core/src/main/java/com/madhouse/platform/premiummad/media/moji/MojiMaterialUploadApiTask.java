package com.madhouse.platform.premiummad.media.moji;

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
import com.madhouse.platform.premiummad.media.constant.IMojiConstant;
import com.madhouse.platform.premiummad.media.model.MojiMaterialUploadRequest;
import com.madhouse.platform.premiummad.media.model.MojiMaterialUploadResponse;
import com.madhouse.platform.premiummad.media.util.MojiHttpUtil;
import com.madhouse.platform.premiummad.media.util.Sha1;
import com.madhouse.platform.premiummad.service.IMaterialService;

@Component
public class MojiMaterialUploadApiTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(MojiMaterialUploadApiTask.class);

	@Value("#{'${moji.dataSource}'.split(',')}")
	private List<String> dataSourceList;

	@Value("${moji.uploadMaterialUrl}")
	private String uploadMaterialUrl;

	@Value("${moji.source}")
    private String source;
	
	@Value("${moji.secret}")
	private String secret;

	@Autowired
	private MojiHttpUtil mojiHttpUtil;

	@Autowired
	private Sha1 sha1;
	
	@Autowired
	private MaterialMapper materialDao;
	
	@Autowired
	private IMaterialService materialService;

	public void uploadMaterial() {
		LOGGER.info("++++++++++moji get material status begin+++++++++++");

		// 查询所有待审核且媒体的素材的审核状态是媒体审核的
		
		List<Material> unSubmitMaterials = materialDao.selectMediaMaterials(MediaMapping.DIANPING.getValue(), MaterialStatusCode.MSC10002.getValue());
		if (unSubmitMaterials == null || unSubmitMaterials.isEmpty()) {
			LOGGER.info("搜狐没有未上传的广告主");
			LOGGER.info("++++++++++Sohu News upload material end+++++++++++");
			return;
		}
		// 上传到媒体
		LOGGER.info("SohuNewsUploadMaterialApiTask-sohuNews", unSubmitMaterials.size());

		Map<Integer, String> materialIdKeys = new HashMap<Integer, String>();
		for (Material material : unSubmitMaterials) {
			Map<String, String> paramMap = buildUploadMaterialRequest(material);
			String postResult = mojiHttpUtil.post(uploadMaterialUrl, paramMap);
			MojiMaterialUploadResponse response = JSON.parseObject(postResult, MojiMaterialUploadResponse.class);
			// 上传成功，返回200
			if (response.getCode().equals(IMojiConstant.M_STATUS_SUCCESS.getValue() + "")) {
				materialIdKeys.put(material.getId(), response.getData().getId());
			} else {
				// TODO error log
			}
		}

		// 更新我方素材信息
		if (!materialIdKeys.isEmpty()) {
			materialService.updateStatusAfterUpload(materialIdKeys);
		}
		
		LOGGER.info("++++++++++moji get material status end+++++++++++");
	}
	
	private Map<String, String> buildUploadMaterialRequest(Material material) {
		//封装request对象
		 MojiMaterialUploadRequest request = new MojiMaterialUploadRequest();
		 
		 // 客户标示
		 request.setSource(source);
		 
		 // 参数签名 TODO
		 
		 // 当前时间10位时间戳
		 request.setTime_stamp(Integer.parseInt(System.currentTimeMillis()/1000+""));
		 
		 // 广告位id required TODO 没有该字段
		 request.setPosition_ids("");
		 
		 // 广告类型：1 文字 2 banner 3 开屏；当为文字和开屏时show_type不填 TODO
		 request.setAd_type(convertMediaAdType(material.getLayout()));
		 
		 // 广告样式 TODO
		 request.setShow_type(0);
		 
		 // 跳转链接;审核通过后在结尾会增加”sigin=xxx”返回，请求时用此处带sigin的url
		 request.setRedirect_url("");
		 
		 // 无聊上传地址
		 request.setImage_url(""); // TODO
		 
		 // 描述
		 request.setDesc(material.getDescription());
		 
		 // 标题
		 request.setTitle(material.getTitle());
		 
		 Map<String,String> paramMap = new HashMap<String, String>();
		 
		 // TODO
		 return paramMap;
	}
	
	/**
	 * 将我方的广告类型转换为媒体方
	 * 我方：    1  横幅广告 2  视频广告   3  信息流广告
	 * 媒体方: 1 文字         2  banner 3开屏
	 * @param premiumAdType
	 * @return
	 */
	private int convertMediaAdType(int premiumAdType) {
		// TODO
		return 0;
	}
}
