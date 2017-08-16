package com.madhouse.platform.premiummad.controller;

import org.junit.Test;

public class MaterialControllerTest {

	@Test
	public void list(){
		String url = "http://localhost:8080/services/material/list";
		HttpUtilTest.httpGet(url);
	}
	
	@Test
	public void detail(){
		String url = "http://localhost:8080/services/material/detail?id=1";
		HttpUtilTest.httpGet(url);
	}
}
