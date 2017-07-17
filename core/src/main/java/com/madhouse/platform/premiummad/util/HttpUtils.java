package com.madhouse.platform.premiummad.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.madhouse.platform.premiummad.constant.SystemConstant;

public class HttpUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(SystemConstant.LOGGER_PREMIUMMAD);

	public static final String RESPONSE_CODE_KEY = "responseCode";

	public static final String RESPONSE_HEADERS_KEY = "responseHeaders";

	public static final String RESPONSE_BODY_KEY = "responseBody";

	private static CloseableHttpClient httpClient;

	private static RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000).setSocketTimeout(10000).setConnectionRequestTimeout(10000).build();

	private static final String DEFAULT_CONTENT_TYPE = "application/json;charset=UTF-8";
	private static final String FORM_URLENCODED_CONTENT_TYPE = "application/x-www-form-urlencoded";

	private static final String SOHU = "sohu";

	private static Header getHeaderByUrl(String url) {
		Header header = new BasicHeader("Content-Type", DEFAULT_CONTENT_TYPE);
		if (url.toLowerCase().contains(SOHU)) {
			header = new BasicHeader("Content-Type", FORM_URLENCODED_CONTENT_TYPE);
		}
		return header;
	}

	public static Map<String, Object> get(String url) {
		Map<String, Object> map = new HashMap<>();
		CloseableHttpResponse response = null;
		try {
			httpClient = HttpClients.createDefault();
			HttpGet get = new HttpGet(url);
			get.setConfig(requestConfig);
			get.setHeader(getHeaderByUrl(url));
			response = httpClient.execute(get);
			HttpEntity responseEntity = response.getEntity();
			String result = EntityUtils.toString(responseEntity, Consts.UTF_8);
			map.put(RESPONSE_CODE_KEY, response.getStatusLine().getStatusCode());
			map.put(RESPONSE_HEADERS_KEY, response.getAllHeaders());
			map.put(RESPONSE_BODY_KEY, result);
			LOGGER.info("HttpRequest Url:" + url + "------" + "Response:" + result);
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
		} finally {
			if (httpClient != null) {
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return map;
	}

	public static String post(String url, String date) {
		String result = null;
		CloseableHttpResponse response = null;
		try {
			httpClient = HttpClients.createDefault();
			StringEntity stringEntity = new StringEntity(date, Consts.UTF_8);
			HttpPost post = new HttpPost(url);
			post.setConfig(requestConfig);
			post.setHeader(getHeaderByUrl(url));
			post.setEntity(stringEntity);
			response = httpClient.execute(post);
			HttpEntity responseEntity = response.getEntity();
			result = EntityUtils.toString(responseEntity, Consts.UTF_8);
			LOGGER.info("HttpRequest Url:" + url + "------" + "Response:" + result);
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
		} finally {
			if (httpClient != null) {
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return result;
	}

}