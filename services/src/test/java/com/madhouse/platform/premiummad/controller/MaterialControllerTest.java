package com.madhouse.platform.premiummad.controller;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.madhouse.platform.premiummad.dto.AuditDto;

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
		AuditDto dto = new AuditDto();
		dto.setIds("1,2,3");
		dto.setStatus(1);
		dto.setReason("basic reason");
		String url = "http://localhost:8080/services/material/audit";
		HttpUtilTest.httpPost(url, JSONObject.toJSONString(dto));
	}
}
