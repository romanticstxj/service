package com.madhouse.platform.premiummad.controller;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.madhouse.platform.premiummad.dto.MaterialDto;

public class MaterialControllerTest {

	@Test
	public void list(){
		String url = "http://localhost:8080/services/material/list";
		HttpUtilTest.httpGet(url);
	}
	
	@Test
	public void detail(){
		String url = "http://localhost:8080/services/material/detail?id=1";
		HttpUtilTest.httpGet(url);
	}
	
	@Test
	public void audit(){
		MaterialDto dto = new MaterialDto();
		dto.setIds("1,2,3");
		dto.setStatus(1);
		String url = "http://localhost:8080/services/material/audit";
		HttpUtilTest.httpPost(url, JSONObject.toJSONString(dto));
	}
}
