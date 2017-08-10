package com.madhouse.platform.premiummad.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dto.AdspaceDto;
import com.madhouse.platform.premiummad.dto.AdspaceMappingDto;
import com.madhouse.platform.premiummad.dto.DspMappingDto;

import redis.clients.jedis.Jedis;

public class AdspaceControllerTest {
	
	@Test
	public void add(){
		AdspaceDto adspaceDto = new AdspaceDto();
		adspaceDto.setName("adspace55");
		adspaceDto.setMediaId(100004);
		adspaceDto.setTerminalType(1);
		adspaceDto.setTerminalOs(1);
		adspaceDto.setSupportHttps(1);
		adspaceDto.setBidType(1);
		adspaceDto.setBidFloor(3.59);
		adspaceDto.setAdType(1);
		adspaceDto.setLayout(102);
		adspaceDto.setMaterialType("2,4");
		adspaceDto.setMaterialSize("1024*768");
		adspaceDto.setMaterialMaxKbyte(300);
//		adspaceDto.setLogoType("1,2");
		adspaceDto.setVideoType("8,32");
		adspaceDto.setDescription("desc");
//		String link = "http://172.16.25.48:8080/services/adspace/create";
		String link = "http://localhost:8080/services/adspace/create";
		HttpUtilTest.httpPost(link, JSON.toJSONString(adspaceDto));
	}
	
	@Test
	public void addMapping(){
		AdspaceMappingDto amd = new AdspaceMappingDto();
		amd.setAdspaceId(200049);
//		amd.setMediaAdspaceKey("test");
		List<DspMappingDto> dsps = new ArrayList<DspMappingDto>();
		DspMappingDto dsp = new DspMappingDto();
//		dsp.setDspAdspaceKey("1234");
//		dsp.setDspMediaId("sdf");
//		dsp.setDspId(1);
//		dsps.add(dsp);
//		dsp = new DspMappingDto();
////		dsp.setDspAdspaceKey("dspAdspaceKey2");
//		dsp.setDspMediaId(300002);
//		dsp.setDspId(500002);
//		dsps.add(dsp);
//		dsp = new DspMappingDto();
//		dsp.setDspAdspaceKey("dspAdspaceKey3");
//		dsp.setDspMediaId(300003);
//		dsp.setDspId(500003);
//		dsps.add(dsp);
		amd.setDspMappings(dsps);
		String link = "http://localhost:8080/services/adspace/mapping/relate";
		HttpUtilTest.httpPost(link, JSON.toJSONString(amd));
	}
	
	@Test
	public void mappingDetail(){
//		System.out.println(SystemConstant.Logging.LOGGER_PREMIUMMAD);
//		String link = "http://172.16.25.48:8080/services/adspace/mapping/detail?id=200005";
		String link = "http://localhost:8080/services/adspace/mapping/detail?id=200005";
		HttpUtilTest.httpGet(link);
	}
	
	@Test
	public void createandupdateMapping(){
		AdspaceMappingDto amd = new AdspaceMappingDto();
		amd.setAdspaceId(3);
		amd.setMediaAdspaceKey("333");
		List<DspMappingDto> dsps = new ArrayList<DspMappingDto>();
		DspMappingDto dsp = new DspMappingDto();
		dsp.setDspAdspaceKey("dspAdspaceKey1");
		dsp.setDspMediaId("d");
		dsp.setDspId(500001);
		dsps.add(dsp);
		dsp = new DspMappingDto();
		dsp.setDspAdspaceKey("dspAdspaceKey2");
		dsp.setDspMediaId("f");
		dsp.setDspId(500002);
		dsps.add(dsp);
		dsp = new DspMappingDto();
		dsp.setDspAdspaceKey("dspAdspaceKey3");
		dsp.setDspMediaId("aaa");
		dsp.setDspId(500004);
		dsps.add(dsp);
		amd.setDspMappings(dsps);
		String link = "http://localhost:8080/services/adspace/mapping/relate";
		HttpUtilTest.httpPost(link, JSON.toJSONString(amd));
	}
	
	@Test
	public void update(){
		AdspaceDto adspaceDto = new AdspaceDto();
		adspaceDto.setId(200009);
		adspaceDto.setName("adspace43");
		adspaceDto.setMediaId(100001);
		adspaceDto.setTerminalType(1);
		adspaceDto.setTerminalOs(1);
		adspaceDto.setSupportHttps(1);
		adspaceDto.setBidType(1);
		adspaceDto.setBidFloor(21.00);
		adspaceDto.setAdType(3);
		adspaceDto.setLayout(102);
		adspaceDto.setMaterialType("1,2,4");
		adspaceDto.setMaterialSize("11*12");
		adspaceDto.setMaterialMaxKbyte(300);
		adspaceDto.setDescription("desc");
//		String link = "http://172.16.25.48:8080/services/adspace/update";
		String link = "http://localhost:8080/services/adspace/update";
		HttpUtilTest.httpPost(link, JSON.toJSONString(adspaceDto));
	}
	
	@Test
	public void detail(){
//		String link = "http://172.16.25.48:8080/services/adspace/detail?id=200000";
		String link = "http://localhost:8080/services/adspace/detail?id=200030";
		HttpUtilTest.httpGet(link);
	}
	
	@Test
	public void list(){
		String link = "http://localhost:8080/services/adspace/list";
		HttpUtilTest.httpGet(link);
		
//		int i = 1;
//		int j = 2;
//		int t = i | j;
//		int multiValueLength = Integer.parseInt(
//    			new DecimalFormat(SystemConstant.ZERO).format(Math.floor(Math.log(9)/Math.log(2))));
//		double result =Math.pow(2, multiValueLength);
//		System.out.println(StringUtils.multiValueToSingleValue(new int[]{8,16}));
//		System.out.println(StringUtils.singleValueToMultiValue(StringUtils.multiValueToSingleValue(new int[]{8,32})));
//		1 & 2;
		
		
	}
	
	@Test 
	public void test1Normal() { 
	    Jedis jedis = new Jedis("172.16.25.26"); 
	    long start = System.currentTimeMillis(); 
	    for (int i = 0; i < 100000; i++) { 
	        long result = jedis.del("n" + i); 
	    } 
	    
	    long end = System.currentTimeMillis(); 
	    System.out.println("Simple SET: " + ((end - start)/1000.0) + " seconds"); 
	    jedis.disconnect(); 
	} 
}
