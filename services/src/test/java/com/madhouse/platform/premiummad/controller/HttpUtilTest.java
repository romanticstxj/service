package com.madhouse.platform.premiummad.controller;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpUtilTest {

	public static void httpGet(String link) {
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();

			HttpGet get = new HttpGet(link);

			get.setHeader("X-From", "exchange.dev.onemad.com");
			get.setHeader("X-User-Id", "1303");
			get.setHeader("Content-Disposition", "form-data; name='attachment;'");

			CloseableHttpResponse response = httpclient.execute(get);
			// 打印ResponseBody
			HttpEntity responseEntity = response.getEntity();
			String result = EntityUtils.toString(responseEntity);
			System.out.println(result);
			httpclient.close();
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void httpPost(String link, String entity) {
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			StringEntity stringEntity = new StringEntity(entity, "UTF-8");
			System.out.println(entity);
			HttpPost post = new HttpPost(link);
			post.setHeader("X-From", "exchange.dev.onemad.com");
			post.setHeader("X-User-Id", "1303");
			post.setHeader("Content-Type", "application/json");

			post.setEntity(stringEntity);
			CloseableHttpResponse response = httpclient.execute(post);
//			response.setHeader("Content-Type", "application/json");
			// 打印ResponseBody
			HttpEntity responseEntity = response.getEntity();
			String result = EntityUtils.toString(responseEntity);
			System.out.println("返回的结果:\n"+result);
			httpclient.close();
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}