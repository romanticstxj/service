package com.madhouse.platform.premiummad.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dto.MediaDto;
import com.madhouse.platform.premiummad.util.StringUtils;

public class MediaControllerTest {
	
	@Test
	public void add(){
		MediaDto mediaDto = new MediaDto();
//		mediaDto.setId(1);
		mediaDto.setName("test00334");
		mediaDto.setCategory(2);
		mediaDto.setType(1);
		mediaDto.setDescription("测试");
		mediaDto.setAccessType(2);
		mediaDto.setAdvertiserAuditMode(1);
		mediaDto.setMaterialAuditMode(1);
		mediaDto.setTimeout(100);
		String link = "http://172.16.25.31:8080/services/media/create";
		HttpUtilTest.httpPost(link, JSON.toJSONString(mediaDto));
	}
	
	@Test
	public void detail(){
		String link = "http://localhost:8080/services/media/detail?id=100030";
		HttpUtilTest.httpGet(link);
	}
	
	@Test
	public void list(){
//		String link = "http://localhost:8080/services/media/list?ids=8000005,8000004";
		String link = "http://172.16.25.48:8080/services/media/list";
//		String link = "http://172.16.25.48:8080/services/dict/list?type=1";
		HttpUtilTest.httpGet(link);
	} 
	
	@Test
	public void update(){
		MediaDto mediaDto = new MediaDto();
		mediaDto.setId(100030);
		mediaDto.setName("1222");
		mediaDto.setCategory(1);
		mediaDto.setType(1);
		mediaDto.setDescription("hello media");
		mediaDto.setAccessType(1);
		mediaDto.setAdvertiserAuditMode(1);
		mediaDto.setMaterialAuditMode(1);
		mediaDto.setTimeout(30);
		String link = "http://localhost:8080/services/media/update";
		HttpUtilTest.httpPost(link, JSON.toJSONString(mediaDto));
	}
	
	@Test
	public void updateStatus(){
		MediaDto mediaDto = new MediaDto();
		mediaDto.setId(100001);
		mediaDto.setStatus(0);
//		String link = "http://localhost:8080/services/media/update";
		String link = "http://localhost:8080/services/media/updateStatus";
		HttpUtilTest.httpPost(link, JSON.toJSONString(mediaDto));
	}
	
	@Test
	public void testException(){
		String link = "http://localhost:8080/services/media/exceptionTest?exType=2";
		HttpUtilTest.httpGet(link);
	}
	
	@Test
	public void test(){
		String[] ss = StringUtils.splitIds("33333,444,62");
		System.out.println(ss);
		List<Integer> list = new ArrayList<Integer>();
		list.add(2);
		list.add(3);
		String s = StringUtils.getIdsStr(list);
		System.out.println(s);
	}
	
}
