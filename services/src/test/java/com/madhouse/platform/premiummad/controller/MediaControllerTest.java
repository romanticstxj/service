package com.madhouse.platform.premiummad.controller;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.dto.MediaDto;

public class MediaControllerTest {
	
	@Test
	public void add(){
		MediaDto mediaDto = new MediaDto();
//		mediaDto.setId(1);
		mediaDto.setName("多对多36");
		mediaDto.setCategory(2);
		mediaDto.setType(1);
		mediaDto.setDescription("hello media");
		mediaDto.setAccessType(1);
		mediaDto.setAdvertiserAuditMode(0);
		mediaDto.setMaterialAuditMode(0);
		mediaDto.setTimeout(200);
		String link = "http://localhost:8080/services/media/create";
		HttpUtilTest.httpPost(link, JSON.toJSONString(mediaDto));
	}
	
	@Test
	public void detail(){
		String link = "http://localhost:8080/services/media/detail?id=100001";
		HttpUtilTest.httpGet(link);
	}
	
	@Test
	public void list(){
//		String link = "http://localhost:8080/services/media/list?ids=8000005,8000004";
		String link = "http://localhost:8080/services/media/list";
//		String link = "http://172.16.25.48:8080/services/dict/list?type=1";
		HttpUtilTest.httpGet(link);
	} 
	
	@Test
	public void list1(){
//		String link = "http://localhost:8080/services/media/list?ids=8000005,8000004";
		String link = "http://localhost:8080/services/media/listByMediaIds?ids=100000,100001";
//		String link = "http://172.16.25.48:8080/services/dict/list?type=1";
		HttpUtilTest.httpGet(link);
	} 
	
	
	
	@Test
	public void update(){
		MediaDto mediaDto = new MediaDto();
		mediaDto.setId(100001);
		mediaDto.setName("12");
		mediaDto.setCategory(1);
		mediaDto.setType(1);
		mediaDto.setDescription("hello media");
		mediaDto.setAccessType(1);
		mediaDto.setAdvertiserAuditMode(1);
		mediaDto.setMaterialAuditMode(1);
		mediaDto.setTimeout(30);
		mediaDto.setUpdateType(1);
		String link = "http://localhost:8080/services/media/update";
		HttpUtilTest.httpPost(link, JSON.toJSONString(mediaDto));
	}
	
	@Test
	public void updateStatus(){
		MediaDto mediaDto = new MediaDto();
		mediaDto.setId(100001);
		mediaDto.setStatus(0);
		mediaDto.setUpdateType(2);
//		String link = "http://localhost:8080/services/media/update";
		String link = "http://172.16.25.48:8080/services/media/update";
		HttpUtilTest.httpPost(link, JSON.toJSONString(mediaDto));
	}
	
	@Test
	public void testException(){
		String link = "http://localhost:8080/services/media/exceptionTest?exType=2";
		HttpUtilTest.httpGet(link);
	}
	
}
