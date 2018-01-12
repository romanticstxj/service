package com.madhouse.platform.premiummad.controller;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.dto.MediaWhiteListDto;

public class MediaWhiteListControllerTest {
	@Test
	public void add(){
		MediaWhiteListDto dto = new MediaWhiteListDto();
		dto.setMediaId(100019);
		dto.setDescription("hello");
		String url = "http://localhost:8080/services/mediaWhiteList/create";
		String str = JSON.toJSONString(dto);
		HttpUtilTest.httpPost(url, str);
	}
	
	@Test
	public void delete(){
		MediaWhiteListDto dto = new MediaWhiteListDto();
		dto.setId(3);
		String url = "http://localhost:8080/services/mediaWhiteList/delete";
		HttpUtilTest.httpPost(url, JSON.toJSONString(dto));
	}
	
	@Test
	public void list(){
		String url = "http://localhost:8080/services/mediaWhiteList/list?mediaCategory=5";
		HttpUtilTest.httpGet(url);
	}
	
}
