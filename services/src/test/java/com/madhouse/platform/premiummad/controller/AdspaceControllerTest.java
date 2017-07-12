package com.madhouse.platform.premiummad.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.dto.AdspaceDto;
import com.madhouse.platform.premiummad.dto.AdspaceMappingDto;
import com.madhouse.platform.premiummad.dto.DspMappingDto;

public class AdspaceControllerTest {
	
	@Test
	public void add(){
		AdspaceDto adspaceDto = new AdspaceDto();
		adspaceDto.setName("adspace6");
		adspaceDto.setMediaId(100001);
		adspaceDto.setTerminalType(1);
		adspaceDto.setTerminalOs(1);
		adspaceDto.setSupportHttps(1);
		adspaceDto.setBidType(1);
		adspaceDto.setBidFloor(3.59);
		adspaceDto.setAdType(1);
		adspaceDto.setLayout(102);
		adspaceDto.setMaterialType(1);
		adspaceDto.setMaterialSize("1024*768");
		adspaceDto.setMaterialMaxKbyte(300);
		adspaceDto.setDescription("desc");
		String link = "http://localhost:8080/services/adspace/create";
		HttpUtilTest.httpPost(link, JSON.toJSONString(adspaceDto));
	}
	
	@Test
	public void addMapping(){
		AdspaceMappingDto amd = new AdspaceMappingDto();
		amd.setAdspaceId(5);
		amd.setMediaAdspaceKey("555");
		List<DspMappingDto> dsps = new ArrayList<DspMappingDto>();
		DspMappingDto dsp = new DspMappingDto();
		dsp.setDspAdspaceKey("dspAdspaceKey1");
		dsp.setDspMediaId(300001);
		dsp.setDspId(500001);
		dsps.add(dsp);
		dsp = new DspMappingDto();
//		dsp.setDspAdspaceKey("dspAdspaceKey2");
		dsp.setDspMediaId(300002);
		dsp.setDspId(500002);
		dsps.add(dsp);
		dsp = new DspMappingDto();
		dsp.setDspAdspaceKey("dspAdspaceKey3");
		dsp.setDspMediaId(300003);
		dsp.setDspId(500003);
		dsps.add(dsp);
		amd.setDspMappings(dsps);
		String link = "http://localhost:8080/services/adspace/mapping/create";
		HttpUtilTest.httpPost(link, JSON.toJSONString(amd));
	}
	
	@Test
	public void mappingDetail(){
		String link = "http://localhost:8080/services/adspace/mapping/detail?id=3";
		HttpUtilTest.httpGet(link);
	}
	
	@Test
	public void updateMapping(){
		AdspaceMappingDto amd = new AdspaceMappingDto();
		amd.setAdspaceId(3);
		amd.setMediaAdspaceKey("555");
		List<DspMappingDto> dsps = new ArrayList<DspMappingDto>();
		DspMappingDto dsp = new DspMappingDto();
		dsp.setDspAdspaceKey("dspAdspaceKey1");
		dsp.setDspMediaId(300001);
		dsp.setDspId(500001);
		dsps.add(dsp);
		dsp = new DspMappingDto();
//		dsp.setDspAdspaceKey("dspAdspaceKey2");
		dsp.setDspMediaId(300002);
		dsp.setDspId(500002);
		dsps.add(dsp);
		dsp = new DspMappingDto();
		dsp.setDspAdspaceKey("dspAdspaceKey3");
		dsp.setDspMediaId(300003);
		dsp.setDspId(500003);
		dsps.add(dsp);
		amd.setDspMappings(dsps);
		String link = "http://localhost:8080/services/adspace/mapping/update";
		HttpUtilTest.httpPost(link, JSON.toJSONString(amd));
	}
	
	@Test
	public void update(){
		AdspaceDto adspaceDto = new AdspaceDto();
		adspaceDto.setId(200000);
		adspaceDto.setName("adspace4");
		adspaceDto.setMediaId(100001);
		adspaceDto.setTerminalType(2);
		adspaceDto.setTerminalOs(1);
		adspaceDto.setSupportHttps(1);
		adspaceDto.setBidType(1);
		adspaceDto.setBidFloor(3.55);
		adspaceDto.setAdType(3);
		adspaceDto.setLayout(102);
		adspaceDto.setMaterialType(1);
		adspaceDto.setMaterialSize("1024*768");
		adspaceDto.setMaterialMaxKbyte(300);
		adspaceDto.setDescription("desc");
		adspaceDto.setUpdateType(2);
		adspaceDto.setStatus(1);
		String link = "http://localhost:8080/services/adspace/update";
		HttpUtilTest.httpPost(link, JSON.toJSONString(adspaceDto));
	}
	
	@Test
	public void detail(){
		String link = "http://localhost:8080/services/adspace/detail?id=200000";
		HttpUtilTest.httpGet(link);
	}
	
	@Test
	public void list(){
		String link = "http://localhost:8080/services/adspace/list";
		HttpUtilTest.httpGet(link);
	}
	
	
}
