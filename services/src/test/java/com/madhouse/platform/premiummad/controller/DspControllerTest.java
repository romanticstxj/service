package com.madhouse.platform.premiummad.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.dto.DspDto;
import com.madhouse.platform.premiummad.entity.DspMedia;

public class DspControllerTest {
	@Test
	public void add(){
		DspDto dsp = new DspDto();
		DspDto.RequestUrl[] requestUrls = new DspDto.RequestUrl[3];
		DspDto.RequestUrl requestUrl = new DspDto.RequestUrl();
		requestUrl.setBidUrl("http://baidu.com");
		requestUrl.setDeliveryType(1);
		requestUrls[0] = requestUrl;
		requestUrl = new DspDto.RequestUrl();
		requestUrl.setBidUrl("hello1");
//		requestUrl.setDeliveryType(2);
		requestUrls[1] = requestUrl;
		requestUrl = new DspDto.RequestUrl();
		requestUrl.setBidUrl("hello2");
		requestUrl.setDeliveryType(3);
		requestUrls[2] = requestUrl;
		dsp.setRequestUrl(requestUrls);
		dsp.setName("dspName11222");
		dsp.setDeliveryType("1,2,8");
//		dsp.setBidUrl("http://baidu.com");
		String url = "http://localhost:8080/services/dsp/create";
//		String url = "http://172.16.25.48:8080/services/dsp/create";
		String dspStr = JSON.toJSONString(dsp);
		String ss = "[{\"bidUrl\":\"hello\",\"deliveryType\":1},{\"bidUrl\":\"hello1\",\"deliveryType\":2}]";
		HttpUtilTest.httpPost(url, dspStr);
	}
	
	@Test
	public void get(){
		String url = "http://localhost:8080/services/dsp/detail?id=600054";
		HttpUtilTest.httpGet(url);
	}
	
	@Test
	public void update(){
		DspDto dsp = new DspDto();
		DspDto.RequestUrl[] requestUrls = new DspDto.RequestUrl[3];
		DspDto.RequestUrl requestUrl = new DspDto.RequestUrl();
		requestUrl.setBidUrl("http://baidu.com");
		requestUrl.setDeliveryType(1);
		requestUrls[0] = requestUrl;
		requestUrl = new DspDto.RequestUrl();
		requestUrl.setBidUrl("hello1");
		requestUrl.setDeliveryType(2);
		requestUrls[1] = requestUrl;
		requestUrl = new DspDto.RequestUrl();
		requestUrl.setBidUrl("hello2");
		requestUrl.setDeliveryType(3);
		requestUrls[2] = requestUrl;
		dsp.setRequestUrl(requestUrls);
		dsp.setId(600055);
		dsp.setName("新增合作模式1");
		dsp.setDeliveryType("2,8");
//		dsp.setBidUrl("http://baidu.com");
		dsp.setToken("CD41036A51CDF5DA1D4A7CA4FA1F5D57");
		dsp.setStatus(0);
//		String url = "http://172.16.25.31:8080/services/dsp/update";
		String url = "http://localhost:8080/services/dsp/update";
		HttpUtilTest.httpPost(url, JSON.toJSONString(dsp));
	}
	
	@Test
	public void updateStatus(){
		DspDto dsp = new DspDto();
		dsp.setId(601056);
		dsp.setStatus(1);
		String url = "http://172.16.25.31:8080/services/dsp/updateStatus";
		HttpUtilTest.httpPost(url, JSON.toJSONString(dsp));
	}
	
	@Test
	public void list(){
		String url = "http://172.16.25.48:8080/services/dsp/list?status=1&deliveryType=8";
		HttpUtilTest.httpGet(url);
	}
	
	@Test
	public void updateDspMediaAuth(){
		List<DspMedia> dspAuths = new ArrayList<>();
		DspMedia dspAuth = new DspMedia();
		dspAuth.setDspId(600009);
		dspAuth.setAdspaceId(200036);
		dspAuth.setMediaId(100016);
		dspAuths.add(dspAuth);
		dspAuth = new DspMedia();
		dspAuth.setDspId(600009);
		dspAuth.setAdspaceId(200038);
		dspAuth.setMediaId(100016);
		dspAuths.add(dspAuth);
		dspAuth = new DspMedia();
		dspAuth.setDspId(600009);
		dspAuth.setAdspaceId(200038);
		dspAuth.setMediaId(100016);
		dspAuths.add(dspAuth);
		String url = "http://localhost:8080/services/dsp/mediaAuth/update";
		HttpUtilTest.httpPost(url, JSON.toJSONString(dspAuths));
	}
	
	@Test
	public void queryDspMediaAuth(){
		String url = "http://localhost:8080/services/dsp/mediaAuth/query?dspId=600009";
		HttpUtilTest.httpGet(url);
	}
	
}
