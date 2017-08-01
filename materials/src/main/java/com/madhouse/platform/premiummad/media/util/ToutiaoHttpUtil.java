package com.madhouse.platform.premiummad.media.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.madhouse.platform.premiummad.media.model.ToutiaoMaterialUploadRequest;

@Component
public class ToutiaoHttpUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(ToutiaoHttpUtil.class);

	@Value("${toutiao.connectionTime}")
	private int connectionTime;

	@Value("${toutiao.socketTime}")
	private int socketTime;

	@Value("${toutiao.connectionRequest}")
	private int connectionRequest;

	@Value("${toutiao.key}")
	private String key;

	@Value("${toutiao.dspid}")
	private String dspid;

	public String get(String apiUrl, Map<String, String> paramMap) {
		LOGGER.info("into--toutiaoHttp--get:start....");
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		String result = "";
		try {
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connectionTime).setSocketTimeout(socketTime).setConnectionRequestTimeout(connectionRequest).build();
			httpClient = HttpClients.createDefault();
			StringBuilder url = new StringBuilder(apiUrl);
			int i = 0;
			for (Map.Entry<String, String> entry : paramMap.entrySet()) {
				if (i == 0) {
					url.append(entry.getKey()).append("=").append(entry.getValue());
				} else {
					url.append("&").append(entry.getKey()).append("=").append(entry.getValue());
				}
				i++;
			}
			String signature = HMACSHA1.EncryptionByHmacSHA1AndBASE64(key, url.toString());
			url.append("&signature=" + signature);
			LOGGER.info("totiaoHttp--get--url=" + url.toString());
			HttpGet get = new HttpGet(url.toString());
			get.setConfig(requestConfig);
			LOGGER.info("toutiao--get--request:{}", get.toString());
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

	public String post(String url, List<ToutiaoMaterialUploadRequest> list) {
		LOGGER.info("into--toutiaoHttp:start....");
		String signature = "";
		String result = "";
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		try {

			url += "dspid=" + dspid;
			url += "&creative_num=" + 1;
			signature = HMACSHA1.EncryptionByHmacSHA1AndBASE64(key, url.toString());

			url += "&signature=" + signature;
			LOGGER.info("toutiao-request-url:{}" + url);

			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(8000).setSocketTimeout(10000).setConnectionRequestTimeout(1000).build();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setConfig(requestConfig);

			Map<String, Object> map = new HashMap<String, Object>();

			map.put("creatives", list);

			httpPost.setEntity(new StringEntity(JSON.toJSONString(map), Consts.UTF_8));

			httpClient = HttpClients.createDefault();
			LOGGER.info("request:{}", EntityUtils.toString(httpPost.getEntity(), Consts.UTF_8));
			// System.out.println(EntityUtils.toString(httpPost.getEntity(),
			// Consts.UTF_8));

			response = httpClient.execute(httpPost);
			HttpEntity responseEntity = response.getEntity();
			result = EntityUtils.toString(responseEntity, Consts.UTF_8);
			LOGGER.info("response:{}", result);

		} catch (Exception e1) {
			e1.printStackTrace();
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
