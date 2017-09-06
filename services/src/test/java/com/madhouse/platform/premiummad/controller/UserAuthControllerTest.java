package com.madhouse.platform.premiummad.controller;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.entity.UserAuth;

public class UserAuthControllerTest {

	@Test
	public void testMedia(){
		UserAuth auth = new UserAuth();
		auth.setSpecifiedUserId(1312);
//		auth.setIsAdmin(1);
		auth.setMediaIds(new Integer[]{1});
		String link = "http://172.16.25.48:8080/services/userauth/media/update";
//		String link = "http://localhost:8080/services/userauth/media/update";
		HttpUtilTest.httpPost(link, JSON.toJSONString(auth));
	}
	
	@Test
	public void testPolicy(){
		UserAuth auth = new UserAuth();
		auth.setSpecifiedUserId(10);
//		auth.setIsAdmin(0);
		auth.setPolicyIds(new Integer[]{});
		String link = "http://localhost:8080/services/userauth/policy/update";
		HttpUtilTest.httpPost(link, JSON.toJSONString(auth));
	}
	
}
