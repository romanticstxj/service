package com.madhouse.platform.premiummad.media.util;

import java.io.IOException;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.media.model.TencentCommonRequest;
import com.madhouse.platform.premiummad.util.StringUtils;

/**
 * desc : 腾讯发送请求http
 */
@Component
public class TencentHttpUtil {

	@Value("${tencent.dsp_id}")
	private String dsp_id;
	@Value("${tencent.token}")
	private String token;

	public String post(String url, TencentCommonRequest<?> request) {
		String time = getTimestamp();
		request.setDsp_id(dsp_id);
		request.setToken(token);
		request.setTime(time);
		request.setSig(StringUtils.getMD5(dsp_id + token + time).toLowerCase());

		return this.post(url, JSON.toJSONString(request));
	}
	
	public String post(String url, String paramStr) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000).setSocketTimeout(80000).setConnectionRequestTimeout(6000).build();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setConfig(requestConfig);

		String result = "";

		try {
			httpPost.setEntity(new StringEntity(paramStr, Consts.UTF_8));
			httpClient = HttpClients.createDefault();
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
	
	/**
	 * 生产UNIX时间戳,容忍时差2分钟
	 * 
	 * @return 秒
	 */
	private String getTimestamp() {
		return String.valueOf(System.currentTimeMillis() / 1000);
	}
}
