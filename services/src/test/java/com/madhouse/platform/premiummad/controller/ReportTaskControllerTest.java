package com.madhouse.platform.premiummad.controller;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.dto.ReportTaskDto;

public class ReportTaskControllerTest {
	
	@Test
	public void create(){
		ReportTaskDto dto = new ReportTaskDto();
		dto.setType(3);
		dto.setStartDate("20171015");
		dto.setEndDate("20171213");
//		String link = "http://localhost:8080/services/reportTask/create";
		String link = "http://172.16.25.48:8080/services/reportTask/create";
		HttpUtilTest.httpPost(link, JSON.toJSONString(dto));
	}
	
	@Test
	public void list(){
		String link = "http://localhost:8080/services/reportTask/list";
		HttpUtilTest.httpGet(link);
	}

	@Test
	public void download(){
		String link = "http://localhost:8080/services/reportTask/download?id=47";
		HttpUtilTest.httpGet(link);
	}
	
}
