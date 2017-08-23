package com.madhouse.platform.premiummad.controller;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.entity.UserAuth;

public class UserAuthControllerTest {

	@Test
	public void testMedia(){
		UserAuth auth = new UserAuth();
//		auth.setUserId(1);
		auth.setIsAdmin(1);
		auth.setMediaIds(new Integer[]{1,2,3});
		String link = "http://localhost:8080/services/userauth/media/update";
		HttpUtilTest.httpPost(link, JSON.toJSONString(auth));
	}
	
	@Test
	public void testPolicy(){
		UserAuth auth = new UserAuth();
		auth.setUserId(9);
		auth.setIsAdmin(0);
		auth.setPolicyIds(new Integer[]{1,2,3});
		String link = "http://localhost:8080/services/userauth/policy/update";
		HttpUtilTest.httpPost(link, JSON.toJSONString(auth));
	}
	
}
