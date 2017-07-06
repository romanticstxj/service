package com.madhouse.platform.premiummad.controller;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.dto.AdspaceDto;

public class AdspaceControllerTest {
	
	@Test
	public void add(){
		AdspaceDto adspaceDto = new AdspaceDto();
		adspaceDto.setName("adspace3");
		adspaceDto.setMediaId(100001);
		adspaceDto.setTerminalType(1);
		adspaceDto.setTerminalOs(1);
		adspaceDto.setSupportHttps(1);
		adspaceDto.setBidType(1);
		adspaceDto.setBidFloor(3.55);
		adspaceDto.setAdType(1);
		adspaceDto.setLayout(102);
		adspaceDto.setMaterialType(1);
		adspaceDto.setMaterialSize("1024*768");
		adspaceDto.setMaterialMaxKbyte(300);
		adspaceDto.setDescription("desc");
		String link = "http://localhost:8080/services/adspace/create";
		HttpUtilTest.httpPost(link, JSON.toJSONString(adspaceDto));
	}
	
	
}
