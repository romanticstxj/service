package com.madhouse.platform.premiummad.media.util;

import java.io.IOException;
import java.util.Map;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.madhouse.platform.premiummad.util.StringUtils;

@Component
public class IQiYiHttpUtils {

	@Value("${iqiyi.token}")
	private String iQiYiToken;

	@Value("${iqiyi.connectionTime}")
	private int connectionTime;

	@Value("${iqiyi.socketTime}")
	private int socketTime;

	@Value("${iqiyi.connectionRequest}")
	private int connectionRequest;

	/**
	 * get请求爱奇艺api
	 * 
	 * @param apiUrl
	 *            api地址
	 * @param paramMap
	 *            参数map
	 * @return 返回json
	 */
	public String get(String apiUrl, Map<String, String> paramMap) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		String result = "";
		try {
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connectionTime).setSocketTimeout(socketTime).setConnectionRequestTimeout(connectionRequest).build();
			httpClient = HttpClients.createDefault();
			StringBuilder url = new StringBuilder(apiUrl);
			url.append("?dsp_token=").append(iQiYiToken);
			for (Map.Entry<String, String> entry : paramMap.entrySet()) {
				url.append("&").append(entry.getKey()).append("=").append(entry.getValue());
			}
			HttpGet get = new HttpGet(url.toString());
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

	/**
	 * post请求爱奇艺api
	 * 
	 * @param apiUrl
	 *            api地址
	 * @param paramMap
	 *            参数map
	 * @param bytes
	 *            上传文件的二进制内容
	 * @return 返回json
	 */
	public String post(String apiUrl, Map<String, String> paramMap, byte[] bytes) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		String result = "";
		try {
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connectionTime).setSocketTimeout(socketTime).setConnectionRequestTimeout(connectionRequest).build();
			httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
			HttpPost post = new HttpPost(apiUrl);
			post.setConfig(requestConfig);
			post.setHeader("dsp_token", iQiYiToken);
			if (bytes != null && bytes.length > 0)
				post.setEntity(new ByteArrayEntity(bytes));
			for (Map.Entry<String, String> entry : paramMap.entrySet()) {
				post.setHeader(entry.getKey(), StringUtils.encode(entry.getValue()));
			}
			response = httpClient.execute(post);
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
