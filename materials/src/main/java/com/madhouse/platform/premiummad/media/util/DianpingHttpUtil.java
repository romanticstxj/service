package com.madhouse.platform.premiummad.media.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 美团点评发送请求http
 */
@Component
public class DianpingHttpUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(DianpingHttpUtil.class);

	@Autowired
	DianpingCreateToken dianpingCreateToken;

	public String post(String url, Map<String, String> paramMap, String dspName) {
		LOGGER.info("into--dianpingHttp:start....");
		String token = dianpingCreateToken.createToken(dspName);
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(8000).setSocketTimeout(10000).setConnectionRequestTimeout(1000).build();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setConfig(requestConfig);
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("token", token));
		if (paramMap != null && !paramMap.isEmpty()) {
			for (Map.Entry<String, String> entry : paramMap.entrySet()) {
				params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
		}
		for (NameValuePair kv : params) {
			LOGGER.info("getValue:" + kv.getName() + kv.getValue());
		}

		String result = "";

		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			httpClient = HttpClients.createDefault();
			LOGGER.info("request:{}" + httpPost.getEntity().toString());

			response = httpClient.execute(httpPost);
			HttpEntity responseEntity = response.getEntity();
			result = EntityUtils.toString(responseEntity, Consts.UTF_8);
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
		return result;
	}

	public String get(String url, Map<String, String> paramMap, String brandType) {
		String token = dianpingCreateToken.createToken(brandType);
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		String result = "";
		try {
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(5000).setConnectionRequestTimeout(1000).build();
			httpClient = HttpClients.createDefault();
			StringBuilder stringBuilder = new StringBuilder(url);
			stringBuilder.append("?token=").append(token);
			for (Map.Entry<String, String> entry : paramMap.entrySet()) {
				stringBuilder.append("&").append(entry.getKey()).append("=").append(entry.getValue());
			}
			HttpGet get = new HttpGet(stringBuilder.toString());
			get.setConfig(requestConfig);
			response = httpClient.execute(get);
			HttpEntity responseEntity = response.getEntity();
			result = EntityUtils.toString(responseEntity, Consts.UTF_8);
		} catch (IOException e) {
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

}
