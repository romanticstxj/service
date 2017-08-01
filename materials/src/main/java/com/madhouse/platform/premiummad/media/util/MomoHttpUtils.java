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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * auth : Jay 
 * date : 2016/5/29 
 * desc : Momo发送请求http
 */
@Component
public class MomoHttpUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(MomoHttpUtils.class);

	public String post(String url, Map<String, String> paramMap) {
		LOGGER.info("info--momo  Http:start....");
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
		}

		String result = "";
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));
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

}
