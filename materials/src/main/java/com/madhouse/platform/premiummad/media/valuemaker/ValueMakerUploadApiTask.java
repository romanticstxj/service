package com.madhouse.platform.premiummad.media.valuemaker;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.alibaba.fastjson.JSONObject;
import com.madhouse.platform.premiummad.constant.Layout;
import com.madhouse.platform.premiummad.constant.MaterialStatusCode;
import com.madhouse.platform.premiummad.constant.MediaMapping;
import com.madhouse.platform.premiummad.constant.MediaTypeMapping;
import com.madhouse.platform.premiummad.dao.AdvertiserMapper;
import com.madhouse.platform.premiummad.dao.MaterialMapper;
import com.madhouse.platform.premiummad.entity.Advertiser;
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.media.valuemaker.constant.ValueMakerConstant;
import com.madhouse.platform.premiummad.media.valuemaker.constant.ValuekerIndustryMapping;
import com.madhouse.platform.premiummad.media.valuemaker.request.ValueMakerMaterialUploadRequest;
import com.madhouse.platform.premiummad.media.valuemaker.response.ValuekerResponse;
import com.madhouse.platform.premiummad.media.valuemaker.util.ValueMakerHttpUtil;
import com.madhouse.platform.premiummad.model.MaterialAuditResultModel;
import com.madhouse.platform.premiummad.service.IMaterialService;
import com.madhouse.platform.premiummad.util.StringUtils;

@Component
public class ValueMakerUploadApiTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(ValueMakerUploadApiTask.class);

	@Value("${valuemaker.uploadMaterialUrl}")
	private String uploadMaterialUrl;
	
	@Value("${valuemaker.updateMaterialUrl}")
	private String updateMaterialUrl;
	
	@Value("${valuemaker.vam}")
	private String vam;

	@Autowired
	private ValueMakerHttpUtil valueMakerHttpUtil;

	@Autowired
	private MaterialMapper materialDao;

	@Autowired
	private IMaterialService materialService;

	@Autowired
	private AdvertiserMapper advertiserDao;
	
	/**
	 * 支持的广告形式
	 */
	private static Set<Integer> supportedLayoutSet;

	static {
		supportedLayoutSet = new HashSet<Integer>();
		supportedLayoutSet.add(Integer.valueOf(Layout.LO10001.getValue()));// banner
		supportedLayoutSet.add(Integer.valueOf(Layout.LO10005.getValue()));// 开屏
		supportedLayoutSet.add(Integer.valueOf(Layout.LO10003.getValue()));// 插屏
		supportedLayoutSet.add(Integer.valueOf(Layout.LO30001.getValue()));// 图文信息流
	}

	/**
	 * 万流客物料上传
	 */
	public void uploadValueMakerMaterial() {
		LOGGER.info("++++++++++ValueMaker upload material begin+++++++++++");

		// 媒体组没有映射到具体的媒体不处理
		String value = MediaTypeMapping.getValue(MediaTypeMapping.VALUEMAKER.getGroupId());
		if (StringUtils.isBlank(value)) {
			return;
		}
				
		// 获取媒体组下的具体媒体
		int[] mediaIds = StringUtils.splitToIntArray(value);
		// 查询所有待审核且媒体的素材的审核状态是媒体审核的
		List<Material> unSubmitMaterials = materialDao.selectMaterialsByMeidaIds(mediaIds, MaterialStatusCode.MSC10002.getValue());
		if (unSubmitMaterials == null || unSubmitMaterials.isEmpty()) {
			LOGGER.info(MediaMapping.getDescrip(mediaIds) + "没有未上传的素材");
			return;
		}

		// 上传到媒体
		LOGGER.info("ValueMakerUploadApiTask-ValueMaker", unSubmitMaterials.size());
		List<MaterialAuditResultModel> rejusedMaterials = new ArrayList<MaterialAuditResultModel>();
		Map<Integer, String[]> materialIdKeys = new HashMap<Integer, String[]>();
		for (Material material : unSubmitMaterials) {
			// 校验广告形式是否支持
			if (!(supportedLayoutSet.contains(Integer.valueOf(material.getLayout())))) {
				MaterialAuditResultModel rejuseItem = new MaterialAuditResultModel();
				rejuseItem.setId(String.valueOf(material.getId()));
				rejuseItem.setStatus(MaterialStatusCode.MSC10001.getValue());
				rejuseItem.setMediaIds(mediaIds);
				rejuseItem.setErrorMessage("媒体只支持如下广告形式：" + Arrays.toString(supportedLayoutSet.toArray()));
				rejusedMaterials.add(rejuseItem);
				LOGGER.error(rejuseItem.getErrorMessage());
				continue;
			}
						
			// 上传过媒体，更新
			String url = uploadMaterialUrl;
			if (!StringUtils.isBlank(material.getMediaQueryKey())) {
				url = updateMaterialUrl;
			}
			
			// 向媒体发请求
			ValueMakerMaterialUploadRequest request = new ValueMakerMaterialUploadRequest();
			String errorMsg = buildUploadMaterilaRequest(material, request);
			if (!StringUtils.isBlank(errorMsg)) {
				// 广告主不存在自动驳回
				MaterialAuditResultModel rejuseItem = new MaterialAuditResultModel();
				rejuseItem.setId(String.valueOf(material.getId()));
				rejuseItem.setStatus(MaterialStatusCode.MSC10001.getValue());
				rejuseItem.setMediaIds(mediaIds);
				rejuseItem.setErrorMessage(errorMsg);
				rejusedMaterials.add(rejuseItem);
				continue;
			}
			
			Map<String, String> responseMap = valueMakerHttpUtil.post(url, request);

			// 处理结果
			int responseStatus = responseMap.get("responseStatus").equals("") ? 0 : Integer.parseInt(responseMap.get("responseStatus"));
			if (responseStatus == ValueMakerConstant.RESPONSE_STATUS_200.getValue()) {// 上传成功
				String[] mediaQueryAndMaterialKeys = {material.getMediaQueryKey()};
				materialIdKeys.put(material.getId(), mediaQueryAndMaterialKeys);
			} else if (ValueMakerConstant.RESPONSE_STATUS_422.getValue() == responseStatus) {
				// 业务异常
				String result = responseMap.get("result");
				if (!StringUtils.isBlank(result)) {
					ValuekerResponse resposne = JSONObject.parseObject(result, ValuekerResponse.class);
					// 已知业务异常直接驳回
					if (resposne != null && !StringUtils.isBlank(ValueMakerConstant.getErrorMessage(resposne.getStatus()))) {
						MaterialAuditResultModel rejuseItem = new MaterialAuditResultModel();
						rejuseItem.setId(String.valueOf(material.getId()));
						rejuseItem.setStatus(MaterialStatusCode.MSC10001.getValue());
						rejuseItem.setMediaIds(mediaIds);
						rejuseItem.setErrorMessage(ValueMakerConstant.getErrorMessage(resposne.getStatus()));
						rejusedMaterials.add(rejuseItem);
					}
				}
			} else { 
				// 非业务异常
				LOGGER.info("valueMaker上传物料请求出错，错误编码：" + responseStatus + "-" + ValueMakerConstant.getDescription(responseStatus));
			}
		}

		// 更新我方素材信息
		if (!materialIdKeys.isEmpty()) {
			materialService.updateStatusAfterUpload(materialIdKeys);
		}

		// 处理失败的结果，自动驳回 - 通过素材id更新
		if (!rejusedMaterials.isEmpty()) {
			materialService.updateStatusToMediaByMaterialId(rejusedMaterials);
		}

		LOGGER.info("++++++++++ValueMaker upload material end+++++++++++");
	}

	/**
	 * 构造上传物料的请求
	 * 
	 * @return
	 */
	private String buildUploadMaterilaRequest(Material material, ValueMakerMaterialUploadRequest request) {
		// 获取该素材的广告主,若广告主不存在不做处理
		String[] advertiserKeys = { material.getAdvertiserKey() };
		List<Advertiser> advertisers = advertiserDao.selectByAdvertiserKeysAndDspId(advertiserKeys, String.valueOf(material.getDspId()), material.getMediaId());
		if (advertisers == null || advertisers.size() != 1) {
			LOGGER.error("广告主不存在[advertiserKey=" + material.getAdvertiserKey() + "dspId=" + material.getDspId() + "]");
			return "广告主不存在[advertiserKey=" + material.getAdvertiserKey() + "]";
		}
		Advertiser relatedAdvertiser =  advertisers.get(0);
		
		// 从广告主获取所属行业
		String industry = String.valueOf(relatedAdvertiser.getIndustry());
		if (!StringUtils.isEmpty(industry)) {
			request.setCategory(ValuekerIndustryMapping.getMediaIndustryId(Integer.parseInt(industry)));
		}

		// 落地页需要做urlencode
		try {
			request.setLandingpage(vam + URLEncoder.encode(material.getLpgUrl(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("urlEncoder 出现异常[" + material.getLpgUrl() + "]", e);
			return "系统异常";
		}
		
		request.setWidth(Integer.valueOf(material.getSize().split("\\*")[0]));
		request.setHeight(Integer.valueOf(material.getSize().split("\\*")[1]));

		// 物料上传地址
		List<String> urls = new ArrayList<String>();
		if (material.getAdMaterials() != null && !material.getAdMaterials().isEmpty()) {
			// 多个物料上传地址以 |分割
			String[] adMaterialArray = material.getAdMaterials().split("\\|");
			if (adMaterialArray != null) {
				for (int i = 0; i < adMaterialArray.length; i++) {
					urls.add(adMaterialArray[i]);
					// 目前只支持一个素材
					if (urls.size() == 1) {
						break;
					}
				}
			}
		}
		request.setPic_urls(urls);

		// 媒体广告形式 
		request.setAdtype(getAdType(material.getLayout().intValue()));
				
		// 只有信息流需要标题和内容
		if (needTitle(request.getAdtype())) {
			request.setTitle(material.getTitle());
			request.setText(material.getDescription());
		}

		request.setFormat(ValueMakerConstant.VALUEMAKER_FROMAT_STATIC.getValue());

		// 万流客用于审核落地页域名  - 用 广告主 web 主页 
		List<String> adomainlist = new ArrayList<>();
		// 媒体方期望 domain字段 没有 http://www.前缀
		adomainlist.add(relatedAdvertiser.getWebsite().replace("http://www.", ""));
		request.setAdomain_list(adomainlist);

		// dsp系统中的id
		String id = MD5_16bit(String.valueOf(material.getId()) + String.valueOf(material.getAdspaceId()));
		material.setMediaQueryKey(id);
		request.setId(id);

		return "";
	}
	
	/**
	 * app banner和开屏插屏一般都是纯图片，信息流会包含图片标题文字
	 * 
	 * @return
	 */
	private boolean needTitle(int addType) {
		// 只有信息流需要提供标题文字
		if (addType == ValueMakerConstant.VALUEMAKER_AD_TYPE_INFORMATIONFLOW.getValue()) {
			return true;
		}
		return false;
	}

	/**
	 * 根据广告形式获取广告类型
	 * 1-Banner广告，2-开屏广告，3-插屏广告，4-信息流广告
	 * 
	 * @param layout
	 * @return
	 */
	private int getAdType(int layout) {
		int adtype = 0;
		if (Layout.LO10001.getValue() == layout) { //横幅
			adtype = ValueMakerConstant.VALUEMAKER_AD_TYPE_BANNER.getValue();
		} else if (Layout.LO10005.getValue() == layout) { //开屏
			adtype = ValueMakerConstant.VALUEMAKER_AD_TYPE_OPENSCREEN.getValue();
		} else if (Layout.LO10003.getValue() == layout) { //插屏
			adtype = ValueMakerConstant.VALUEMAKER_AD_TYPE_TABLEPLAQUE.getValue();
		} else if (Layout.LO30001.getValue() == layout) { //图文信息流（只支持一张图片）
			adtype = ValueMakerConstant.VALUEMAKER_AD_TYPE_INFORMATIONFLOW.getValue();
		}
		return adtype;
	}

	// 字符串MD5加密 密文=数字+字母
	public static final String MD5_16bit(String readyEncryptStr) {
		if (readyEncryptStr != null) {
			try {
				return MD5_32bit(readyEncryptStr).substring(8, 24);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			return "";
		} else {
			return "";
		}
	}

	/**
	 * MD5 32bit Encrypt Methods.
	 * 
	 * @param readyEncryptStr
	 *            ready encrypt string
	 * @return String encrypt result string
	 * @throws NoSuchAlgorithmException
	 * */
	public static final String MD5_32bit(String readyEncryptStr) throws NoSuchAlgorithmException {
		if (readyEncryptStr != null) {
			// Get MD5 digest algorithm's MessageDigest's instance.
			MessageDigest md = MessageDigest.getInstance("MD5");
			// Use specified byte update digest.
			md.update(readyEncryptStr.getBytes());
			// Get cipher text
			byte[] b = md.digest();
			// The cipher text converted to hexadecimal string
			StringBuilder su = new StringBuilder();
			// byte array switch hexadecimal number.
			for (int offset = 0, bLen = b.length; offset < bLen; offset++) {
				String haxHex = Integer.toHexString(b[offset] & 0xFF);
				if (haxHex.length() < 2) {
					su.append("0");
				}
				su.append(haxHex);
			}
			return su.toString();
		} else {
			return "";
		}
	}
}
