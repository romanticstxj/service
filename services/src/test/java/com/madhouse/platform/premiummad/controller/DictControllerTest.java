package com.madhouse.platform.premiummad.controller;

import org.junit.Test;

public class DictControllerTest {
	
	@Test
	public void listReqBlockType(){
		String link = "http://localhost:8080/services/dict/list?type=4";
		HttpUtilTest.httpGet(link);
	}
	
	
	@Test
	public void listAdspaceLayout(){
		String link = "http://localhost:8080/services/dict/list?type=2&terminalType=1&adType=1";
		HttpUtilTest.httpGet(link);
	}
	
	@Test
	public void listLocations(){
		String link = "http://localhost:8080/services/dict/list/location";
		HttpUtilTest.httpGet(link);
	}
}
