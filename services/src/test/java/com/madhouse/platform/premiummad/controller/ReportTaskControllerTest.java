package com.madhouse.platform.premiummad.controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.dto.ReportTaskDto;

public class ReportTaskControllerTest {
	
	@Test
	public void create(){
		ReportTaskDto dto = new ReportTaskDto();
		dto.setType(1);
		dto.setStartDate("20171015");
		dto.setEndDate("20171030");
		String link = "http://localhost:8080/services/reportTask/create";
		HttpUtilTest.httpPost(link, JSON.toJSONString(dto));
	}
	
	@Test
	public void list(){
		String link = "http://localhost:8080/services/reportTask/list";
		HttpUtilTest.httpGet(link);
	}

	@Test
	public void download(){
		String link = "http://localhost:8080/services/reportTask/download?type=1";
		HttpUtilTest.httpGet(link);
	}
	
	public static void main(String[] args) {
		ExecutorService es = Executors.newCachedThreadPool();
		for(int i=0; i<20; i++){
			es.submit(new Runnable(){
				
				@Override
				public void run() {
					ReportTaskDto dto = new ReportTaskDto();
					dto.setType(1);
					dto.setStartDate("20171015");
					dto.setEndDate("20171030");
					String link = "http://localhost:8080/services/reportTask/create";
					HttpUtilTest.httpPost(link, JSON.toJSONString(dto));
				}});
			
		}
	}
}
