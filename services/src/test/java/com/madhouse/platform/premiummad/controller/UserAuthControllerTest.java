package com.madhouse.platform.premiummad.controller;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.entity.UserAuth;

public class UserAuthControllerTest {

	@Test
	public void testMedia(){
		UserAuth auth = new UserAuth();
		auth.setSpecifiedUserId(1313);
		auth.setIsAdmin(1);
		Integer[] mediaIds = new Integer[100];
		for(int i=0; i< 100; i++){
			mediaIds[i] = 500001 + i;
		}
		auth.setMediaIds(mediaIds);
//		String link = "http://172.16.25.48:8080/services/userauth/media/update";
		String link = "http://localhost:8080/services/userauth/media/update";
		String jsonStr = JSON.toJSONString(auth);
		System.out.println(jsonStr);
		HttpUtilTest.httpPost(link, jsonStr);
	}
	
	@Test
	public void testPolicy(){
		UserAuth auth = new UserAuth();
		auth.setSpecifiedUserId(10);
//		auth.setIsAdmin(0);
		auth.setPolicyIds(new Integer[]{500089});
		String link = "http://localhost:8080/services/userauth/policy/update";
		HttpUtilTest.httpPost(link, JSON.toJSONString(auth));
	}
	
	@Test
	public void testQueryMedia(){
		String link = "http://localhost:8080/services/userauth/media/query?specifiedUserId=53";
		HttpUtilTest.httpGet(link);
	}
	
	@Test
	public void testQueryPolicy(){
		String link = "http://172.16.25.48:8080/services/userauth/policy/query?specifiedUserId=12121";
//		String link = "http://localhost:8080/services/userauth/policy/query?specifiedUserId=1313";
		HttpUtilTest.httpGet(link);
	}
	
}
