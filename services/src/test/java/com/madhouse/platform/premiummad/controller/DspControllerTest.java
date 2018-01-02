package com.madhouse.platform.premiummad.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.dto.DspDto;
import com.madhouse.platform.premiummad.entity.DspAuth;

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
		dsp.setId(601056);
		dsp.setName("新增合作模式1");
		dsp.setDeliveryType("2,8");
		dsp.setBidUrl("http://baidu.com");
		dsp.setToken("CD41036A51CDF5DA1D4A7CA4FA1F5D57");
		dsp.setStatus(0);
		String url = "http://172.16.25.31:8080/services/dsp/update";
//		String url = "http://localhost:8080/services/dsp/update";
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
		List<DspAuth> dspAuths = new ArrayList<>();
		DspAuth dspAuth = new DspAuth();
		dspAuth.setDspId(600009);
		dspAuth.setAdspaceId(200036);
		dspAuth.setMediaId(100016);
		dspAuths.add(dspAuth);
		dspAuth = new DspAuth();
		dspAuth.setDspId(600009);
		dspAuth.setAdspaceId(200038);
		dspAuth.setMediaId(100016);
		dspAuths.add(dspAuth);
		dspAuth = new DspAuth();
		dspAuth.setDspId(600009);
		dspAuth.setAdspaceId(200037);
		dspAuth.setMediaId(100017);
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
