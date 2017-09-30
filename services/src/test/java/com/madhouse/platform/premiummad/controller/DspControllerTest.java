package com.madhouse.platform.premiummad.controller;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.dto.DspDto;

public class DspControllerTest {
	@Test
	public void add(){
		DspDto dsp = new DspDto();
		dsp.setName("dspName221");
		dsp.setDeliveryType("1,2,8");
		dsp.setBidUrl("http://baidu.com");
//		String url = "http://localhost:8080/services/dsp/create";
		String url = "http://172.16.25.48:8080/services/dsp/create";
		String dspStr = JSON.toJSONString(dsp);
		HttpUtilTest.httpPost(url, dspStr);
	}
	
	@Test
	public void get(){
		String url = "http://localhost:8080/services/dsp/detail?id=600004";
		HttpUtilTest.httpGet(url);
	}
	
	@Test
	public void update(){
		DspDto dsp = new DspDto();
		dsp.setId(600004);
		dsp.setName("dspName22");
		dsp.setDeliveryType("2,8");
		dsp.setBidUrl("http://baidu.com");
		dsp.setToken("333");
		dsp.setStatus(2);
		String url = "http://localhost:8080/services/dsp/update";
		HttpUtilTest.httpPost(url, JSON.toJSONString(dsp));
	}
	
	@Test
	public void updateStatus(){
		DspDto dsp = new DspDto();
		dsp.setId(600004);
		dsp.setStatus(1);
		String url = "http://localhost:8080/services/dsp/updateStatus";
		HttpUtilTest.httpPost(url, JSON.toJSONString(dsp));
	}
	
	@Test
	public void list(){
		String url = "http://172.16.25.48:8080/services/dsp/list?status=1&deliveryType=8";
		HttpUtilTest.httpGet(url);
	}
	
}
