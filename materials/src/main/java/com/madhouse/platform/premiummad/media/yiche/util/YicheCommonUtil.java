package com.madhouse.platform.premiummad.media.yiche.util;

import java.io.File;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class YicheCommonUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(YicheCommonUtil.class);

	// 加密算法RSA
	public static final String KEY_ALGORITHM = "RSA";

	private static CloseableHttpClient closeableHttpClient;

	private static RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000).setSocketTimeout(10000).setConnectionRequestTimeout(10000).build();

	public static String getRequest(Map<String, String> params) {
		List<String> keyList = new ArrayList<>();
		keyList.addAll(params.keySet());
		Collections.sort(keyList);
		List<String> paramList = new ArrayList<>();
		for (String key : keyList) {
			paramList.add(key + "=" + params.get(key));
		}
		return org.apache.commons.lang3.StringUtils.join(paramList, "&");
	}
	
	/**
	 * 普通的Get和POST
	 * 
	 * @param params
	 * @param signKey
	 * @return
	 */
	public static String getSign(Map<String, String> params, String signKey) {
		Set<String> keySet = params.keySet();
		List<String> keyList = new ArrayList<>();
		keyList.addAll(keySet);
		Collections.sort(keyList);
		List<String> paramList = new ArrayList<>();
		for (String key : keyList) {
			paramList.add(key + "=" + params.get(key));
		}
		String str = org.apache.commons.lang3.StringUtils.join(paramList, "&");
		String sign = DigestUtils.md5Hex(str + signKey);
		return sign;
	}
	
	public static String postForm(String fileUploadUrl, String fileUrl, String bucket, String password, String rsa_oss_ciphertext) {
		HttpPost httpPost = new HttpPost(fileUploadUrl);
		httpPost.setConfig(requestConfig);
		String result = "";
		try {
			URL url = new URL(fileUrl);
			String[] fileArray = fileUrl.split("/");
			File tempFile = new File(fileArray[fileArray.length - 1]);
			FileUtils.copyURLToFile(url, tempFile);
			HttpEntity httpEntity = MultipartEntityBuilder.create().setBoundary("----WebKitFormBoundaryxm5WVcWYV2GjKuXT").addTextBody("bucket", bucket).addTextBody("rsa-oss-ciphertext", YicheCommonUtil.encryptByPublicKey(rsa_oss_ciphertext, bucket + password)).addBinaryBody("file", tempFile).build();
			httpPost.setEntity(httpEntity);
			closeableHttpClient = HttpClients.createDefault();
			CloseableHttpResponse response = closeableHttpClient.execute(httpPost);

			HttpEntity responseEntity = response.getEntity();
			result = EntityUtils.toString(responseEntity, Consts.UTF_8);
			response.close();
		} catch (Exception e) {
			LOGGER.error("上传文件异常" + e.getMessage());
		}

		return result;
	}

	/**
	 * POST JSON
	 * 
	 * @param obj
	 * @param signKey
	 * @return
	 */
	public static String getSign(JSONObject obj, String signKey) {
		String str = obj.toJSONString();
		String sign = DigestUtils.md5Hex(str + signKey);
		return sign;
	}

	/**
	 * 使用公钥对明文进行加密，返回 BASE64 编码的字符串
	 * 
	 * @param rsaOssCiphertext
	 *            密钥字符串（经过 base64 编码）
	 * @param plainText
	 * @return
	 */
	public static String encryptByPublicKey(String rsaOssCiphertext, String plainText) {
		try {
			byte[] keyBytes = Base64Utils.decode(rsaOssCiphertext);
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			Key publicK = keyFactory.generatePublic(x509KeySpec);
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.ENCRYPT_MODE, publicK);
			byte[] enBytes = cipher.doFinal(plainText.getBytes());
			return new String(Base64Utils.encode(enBytes));
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
