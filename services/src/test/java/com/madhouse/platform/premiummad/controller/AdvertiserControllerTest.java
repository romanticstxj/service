package com.madhouse.platform.premiummad.controller;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.madhouse.platform.premiummad.dto.AuditDto;

public class AdvertiserControllerTest {

	@Test
	public void list(){
		String url = "http://localhost:8080/services/advertiser/list";
		HttpUtilTest.httpGet(url);
	}
	
	@Test
	public void detail(){
		String url = "http://localhost:8080/services/advertiser/detail?id=19";
		HttpUtilTest.httpGet(url);
	}
	
	@Test
	public void audit(){
		AuditDto dto = new AuditDto();
		dto.setIds("19");
		dto.setStatus(2);
		dto.setReason("basic reason");
		String url = "http://localhost:8080/services/advertiser/audit";
		HttpUtilTest.httpPost(url, JSONObject.toJSONString(dto));
	}
}
