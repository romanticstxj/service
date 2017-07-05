package com.madhouse.platform.premiummad.controller;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * created : heyin
 * date:2015/12/3.
 * desc:
 */
public class HttpUtilTest {

	public static void httpGet(String link) {
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();

			HttpGet get = new HttpGet(link);

			get.setHeader("url", "exchange.dev.onemad.com");
			get.setHeader("businessId", "MAHAD");
			get.setHeader("userId", "19");
			get.setHeader("systemId", "ATD");
			get.setHeader("Content-Type", "application/json;charset=UTF-8");

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
			post.setHeader("url", "exchange.dev.onemad.com");
			post.setHeader("businessId", "SMART");
			post.setHeader("userId", "666");
			post.setHeader("systemId", "PDB");
			post.setHeader("Content-Type", "application/json;charset=UTF-8");

			post.setEntity(stringEntity);
			CloseableHttpResponse response = httpclient.execute(post);
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