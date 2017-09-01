package com.madhouse.platform.premiummad.media.moji.util;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MojiHttpUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(MojiHttpUtil.class);

	@Value("${moji.connectionTime}")
	private int connectionTime;

	@Value("${moji.socketTime}")
	private int socketTime;

	@Value("${moji.connectionRequest}")
	private int connectionRequest;

	public String get(String apiUrl, Map<String, String> paramMap) {
		LOGGER.info("into--墨迹Http--get:start....");
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		String result = "";
		try {
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connectionTime).setSocketTimeout(socketTime).setConnectionRequestTimeout(connectionRequest).build();
			httpClient = HttpClients.createDefault();
			StringBuilder url = new StringBuilder(apiUrl);
			url.append("?");
			int i = 0;
			for (Map.Entry<String, String> entry : paramMap.entrySet()) {
				if (i == 0) {
					url.append(entry.getKey()).append("=").append(entry.getValue());
				} else {
					url.append("&").append(entry.getKey()).append("=").append(entry.getValue());
				}
				i++;
			}
			LOGGER.info("墨迹Http--get--url=" + url.toString());
			System.out.println(url);
			HttpGet get = new HttpGet(url.toString());
			get.setConfig(requestConfig);
			LOGGER.info("墨迹--get--request:{}", get.toString());
			response = httpClient.execute(get);
			HttpEntity responseEntity = response.getEntity();
			result = EntityUtils.toString(responseEntity, Consts.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
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

	public String post(String url, Map<String, String> paramMap) {
		LOGGER.info("info--墨迹  Http:start....");
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(8000).setSocketTimeout(10000).setConnectionRequestTimeout(1000).build();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setConfig(requestConfig);
		List<NameValuePair> params = new ArrayList<>();
		if (paramMap != null && !paramMap.isEmpty()) {
			for (Map.Entry<String, String> entry : paramMap.entrySet()) {
				params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
		}
		for (NameValuePair kv : params) {
			LOGGER.info("getValue:" + kv.getName() + kv.getValue());
			System.out.println(kv.getName() + ":" + kv.getValue());
		}

		String result = "";
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));
			httpClient = HttpClients.createDefault();
			LOGGER.info("request:{}" + httpPost.getEntity().toString());
			// System.out.println(httpPost.getEntity().toString());

			response = httpClient.execute(httpPost);
			HttpEntity responseEntity = response.getEntity();
			result = EntityUtils.toString(responseEntity, Consts.UTF_8);
			LOGGER.info("result:{}" + result);
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

}
