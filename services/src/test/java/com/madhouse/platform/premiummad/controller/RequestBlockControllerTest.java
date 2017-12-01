package com.madhouse.platform.premiummad.controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.dto.RequestBlockDto;

public class RequestBlockControllerTest {
	@Test
	public void add(){
		RequestBlockDto dto = new RequestBlockDto();
		dto.setCode("2000-2000");
		dto.setType(2);
		String url = "http://localhost:8080/services/reqBlock/create";
//		String url = "http://172.16.25.48:8080/services/dsp/create";
		String str = JSON.toJSONString(dto);
		HttpUtilTest.httpPost(url, str);
	}
	
	@Test
	public void updateStatus(){
		RequestBlockDto dto = new RequestBlockDto();
		dto.setId(5);
		dto.setStatus(0);
		dto.setCode("2001-2000");
		dto.setType(2);
		String url = "http://localhost:8080/services/reqBlock/updateStatus";
		HttpUtilTest.httpPost(url, JSON.toJSONString(dto));
	}
	
	@Test
	public void list(){
		String url = "http://localhost:8080/services/reqBlock/list";
		HttpUtilTest.httpGet(url);
	}
	
}
