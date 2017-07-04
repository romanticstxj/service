package com.madhouse.platform.premiummad.controller;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.dto.MediaDto;

public class DictControllerTest {
	
	
	@Test
	public void listAdspaceLayout(){
		String link = "http://localhost:8080/services/dict/list?type=2&terminalType=1&adType=1";
		HttpUtilTest.httpGet(link);
	}
	
	
}
