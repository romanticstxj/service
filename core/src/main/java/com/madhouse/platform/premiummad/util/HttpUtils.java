package com.madhouse.platform.premiummad.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
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

	private static final Logger LOGGER = LoggerFactory.getLogger(SystemConstant.Logging.LOGGER_PREMIUMMAD);

	public static final String RESPONSE_CODE_KEY = "responseCode";

	public static final String RESPONSE_HEADERS_KEY = "responseHeaders";

	public static final String RESPONSE_BODY_KEY = "responseBody";

	private static CloseableHttpClient httpClient;
	
	private static HttpClient httpClient1 = new HttpClient();

	private static RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000).setSocketTimeout(10000)
			.setConnectionRequestTimeout(10000).build();

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

	public static String getBodyString(BufferedReader br) {
		String inputLine;
		String str = "";
		try {
			while ((inputLine = br.readLine()) != null) {
				str += inputLine;
			}
			br.close();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}
		return str;
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

	public static String post(String url, String data) {
		String result = null;
		CloseableHttpResponse response = null;
		try {
			httpClient = HttpClients.createDefault();
			StringEntity stringEntity = new StringEntity(data, Consts.UTF_8);
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
	
	/**
	 * 下载文件
	 * 
	 * @param url
	 * @param localPath
	 * @return
	 */
	public static String downloadFile(String url, String localPath) {
		GetMethod getMethod = null;

		try {
			getMethod = new GetMethod(url);

			if (httpClient1.executeMethod(getMethod) == HttpStatus.SC_OK) {
				String filePath = localPath;
				if (!filePath.endsWith("\\")) {
					filePath += "\\";
				}

				String fileName = url;
				int pos = url.lastIndexOf("/");
				if (pos > 0) {
					fileName = url.substring(pos + 1);
				}

				filePath += fileName;
				File file = new File(filePath);
				OutputStream outputStream = new FileOutputStream(file);
				InputStream inputStream = getMethod.getResponseBodyAsStream();
				System.err.println(readBytes3(inputStream).length);
				int len = 0;
				byte[] buffer = new byte[4096];
				while ((len = inputStream.read(buffer)) > 0) {
					outputStream.write(buffer, 0, len);
				}

				inputStream.close();
				outputStream.flush();
				outputStream.close();

				return filePath;
			}
		} catch (Exception ex) {
			System.err.println(ex.toString());
		} finally {
			if (getMethod != null) {
				getMethod.releaseConnection();
			}
		}

		return null;
	}
	
	public static byte[] readBytes3(InputStream in) throws IOException {  
        BufferedInputStream bufin = new BufferedInputStream(in);  
        int buffSize = 1024;  
        ByteArrayOutputStream out = new ByteArrayOutputStream(buffSize);  
  
        System.out.println("Available bytes:" + in.available());  
  
        byte[] temp = new byte[buffSize];  
        int size = 0;  
        while ((size = bufin.read(temp)) != -1) {  
            out.write(temp, 0, size);  
        }  
        bufin.close();  
  
        byte[] content = out.toByteArray();  
        System.out.println("content bytes:" + content.length);  
        return content;  
    }  
	
	public static void main(String[] args) {
		downloadFile("http://advbeta.madserving.com/material/bc1aa4fa9fb49acfb3be977d834b5190_1478593496_476350936.mp4 ", "D:\\");
	}
}
