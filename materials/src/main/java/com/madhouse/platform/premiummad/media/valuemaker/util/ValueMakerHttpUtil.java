package com.madhouse.platform.premiummad.media.valuemaker.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.media.valuemaker.request.ValueMakerMaterialUploadRequest;

@Component
public class ValueMakerHttpUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(ValueMakerHttpUtil.class);
	@Value("${valuemaker.nameandpassword}")
	private String nameandpassword;

	public Map<String, String> post(String url, ValueMakerMaterialUploadRequest request) {
		LOGGER.info("into---valueMakerhttputil--start---");
		Map<String, String> resultmap = new HashMap<String, String>();
		String result = "";
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;

		try {
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(8000).setSocketTimeout(10000).setConnectionRequestTimeout(1000).build();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setConfig(requestConfig);
			httpPost.setEntity(new StringEntity(JSON.toJSONString(request), Consts.UTF_8));
			httpPost.setHeader("Content-Type", "application/json");
			httpPost.setHeader("Authorization", "Basic " + authStringEnc());
			LOGGER.info("valuemakerpost---headers:" + httpPost);
			httpClient = HttpClients.createDefault();
			LOGGER.info("valueMaker---url=" + url + "----request:{}", EntityUtils.toString(httpPost.getEntity(), Consts.UTF_8));
			response = httpClient.execute(httpPost);
			int responseStatus = response.getStatusLine().getStatusCode();
			HttpEntity responseEntity = response.getEntity();
			result = EntityUtils.toString(responseEntity, Consts.UTF_8);
			resultmap.put("result", result);
			resultmap.put("responseStatus", responseStatus + "");
			LOGGER.info("valueMaker---responseStatus=" + responseStatus + "---response:{}", result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (httpClient != null) {
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		return resultmap;
	}

	public String get(String url, String id) {
		String result = "";
		LOGGER.info("into--valueMakerHttp--get:start....");
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		try {
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(8000).setSocketTimeout(10000).setConnectionRequestTimeout(1000).build();
			httpClient = HttpClients.createDefault();
			url = url + "?id=" + id;
			LOGGER.info("ValueMakerHttp--get--url=" + url);
			HttpGet get = new HttpGet(url);
			get.setConfig(requestConfig);
			get.setHeader("Content-Type", "application/json");
			get.setHeader("Authorization", "Basic " + authStringEnc());
			response = httpClient.execute(get);
			HttpEntity responseEntity = response.getEntity();
			result = EntityUtils.toString(responseEntity, Consts.UTF_8);
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (httpClient != null) {
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public String authStringEnc() {
		byte[] authEncBytes = Base64.encodeBase64(nameandpassword.getBytes());
		String authStringEnc = new String(authEncBytes);
		LOGGER.info("valuemaker---Authorization---nameandpassword:" + nameandpassword + "|authStringEnc:" + authStringEnc);
		return authStringEnc;
	}
}
