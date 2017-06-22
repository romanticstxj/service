package com.madhouse.platform.smartexchange.controller;

import org.junit.Test;

public class TestControllerTest {
	
	@Test
	public void test(){
		String link = "http://localhost:8080/smartexchange-services/test";
		HttpUtilTest.httpGet(link);
	}
}
