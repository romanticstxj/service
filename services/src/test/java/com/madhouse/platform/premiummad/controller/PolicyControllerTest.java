package com.madhouse.platform.premiummad.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.dto.AdspaceDto;
import com.madhouse.platform.premiummad.dto.PolicyAdspaceDto;
import com.madhouse.platform.premiummad.dto.PolicyDspDto;
import com.madhouse.platform.premiummad.dto.PolicyDto;
import com.madhouse.platform.premiummad.util.DateUtils;

public class PolicyControllerTest {
	
	@Test
	public void add() throws ParseException{
//		File file = new File("log");
//		file.getAbsolutePath();
		PolicyDto policyDto = new PolicyDto();
		policyDto.setName("mypolicy552");
		policyDto.setWeight(10);
		policyDto.setStartDate(DateUtils.getFormatDateByPattern("yyyyMMdd", "20170804"));
		policyDto.setIsEndDate(1);
		policyDto.setEndDate(new Date());
		policyDto.setIsTimeTargeting(1);
		policyDto.setTimeTargeting("time limit");
		policyDto.setIsLocationTargeting(1);
		policyDto.setLocationTargeting("limit");
		policyDto.setConnTargeting("1,2");
		policyDto.setOsTargeting("1");
		policyDto.setType(2);
		policyDto.setIsQuantityLimit(1);
		policyDto.setLimitType((byte) 2);
		policyDto.setLimitReqs(1000);
		policyDto.setLimitSpeed((byte) 2);
		
		List<PolicyAdspaceDto> policyAdspaceDtos = new ArrayList<PolicyAdspaceDto>();
		policyDto.setPolicyAdspaces(policyAdspaceDtos);
		PolicyAdspaceDto policyAdspaceDto = new PolicyAdspaceDto();
		AdspaceDto adspaceDto = new AdspaceDto();
		policyAdspaceDto.setAdspaceId(200006);
		policyAdspaceDto.setBidType((byte) 1);
		policyAdspaceDto.setBidFloor(1.50);
		policyAdspaceDto.setMediaDealId("1000001");
		adspaceDto.setBidFloor(1.00);
		policyAdspaceDto.setAdspace(adspaceDto);
		policyAdspaceDtos.add(policyAdspaceDto);
		policyAdspaceDto = new PolicyAdspaceDto();
		adspaceDto = new AdspaceDto();
		policyAdspaceDto.setAdspaceId(200007);
		policyAdspaceDto.setBidType((byte) 2);
		policyAdspaceDto.setBidFloor(2.50);
		policyAdspaceDto.setMediaDealId("1000002");
		adspaceDto.setBidFloor(2.00);
		policyAdspaceDto.setAdspace(adspaceDto);
		policyAdspaceDtos.add(policyAdspaceDto);
//		policyAdspaceDto.setAdspace(adspace);
		
		List<PolicyDspDto> policyDspDtos = new ArrayList<PolicyDspDto>();
		PolicyDspDto policyDspDto = new PolicyDspDto();
		policyDspDto.setDspId(222);
//		policyDspDto.setStatus((byte) 1);
		policyDspDtos.add(policyDspDto);
//		policyDspDto = new PolicyDspDto();
//		policyDspDto.setDspId(600003);
//		policyDspDto.setStatus((byte) 0);
//		policyDspDtos.add(policyDspDto);
		policyDto.setPolicyDsps(policyDspDtos);
		String link = "http://172.16.25.48:8080/services/policy/create";
		HttpUtilTest.httpPost(link, JSON.toJSONString(policyDto));
	}
	
	@Test
	public void detail(){
//		String link = "http://172.16.25.48:8080/services/policy/detail?id=500070&type=2";
		String link = "http://localhost:8080/services/policy/detail?id=500070&type=2";
		HttpUtilTest.httpGet(link);
	}
	
	@Test
	public void list(){
		String link = "http://localhost:8080/services/policy/list?type=2";
		HttpUtilTest.httpGet(link);
	}
	
	@Test
	public void updateStatus() throws ParseException{
		PolicyDto policyDto = new PolicyDto();
		policyDto.setId(5000199);
		policyDto.setType(2);
		policyDto.setStatus((byte) 0); 
		policyDto.setLocationTargeting("333");
		policyDto.setEndDate(new Date());
		String link = "http://localhost:8080/services/policy/updateStatus";
		HttpUtilTest.httpPost(link, JSON.toJSONString(policyDto));
	}
	
	@Test
	public void update() throws ParseException{
		PolicyDto policyDto = new PolicyDto();
		policyDto.setId(500076);
		policyDto.setName("mypolicy34334");
		policyDto.setWeight(10);
		policyDto.setStartDate(DateUtils.getFormatDateByPattern("yyyy-MM-dd", "2017-08-05"));
		policyDto.setIsEndDate(0);
		policyDto.setIsTimeTargeting(0);
		policyDto.setIsLocationTargeting(0);
		policyDto.setConnTargeting("1,2");
		policyDto.setOsTargeting("1");
		policyDto.setType(2);
		policyDto.setIsQuantityLimit(1);
		policyDto.setLimitType((byte) 2);
		policyDto.setLimitReqs(1000);
		policyDto.setLimitSpeed((byte) 2);
		
		List<PolicyAdspaceDto> policyAdspaceDtos = new ArrayList<PolicyAdspaceDto>();
		policyDto.setPolicyAdspaces(policyAdspaceDtos);
		PolicyAdspaceDto policyAdspaceDto = new PolicyAdspaceDto();
		AdspaceDto adspaceDto = new AdspaceDto();
		policyAdspaceDto.setAdspaceId(200006);
		policyAdspaceDto.setBidType((byte) 1);
		policyAdspaceDto.setBidFloor(1.50);
		policyAdspaceDto.setMediaDealId("1000001");
		policyAdspaceDto.setStatus((byte) 1);
		adspaceDto.setBidFloor(1.00);
		policyAdspaceDto.setAdspace(adspaceDto);
		policyAdspaceDtos.add(policyAdspaceDto);
		policyAdspaceDto = new PolicyAdspaceDto();
		adspaceDto = new AdspaceDto();
		policyAdspaceDto.setAdspaceId(200009);
		policyAdspaceDto.setBidType((byte) 2);
		policyAdspaceDto.setBidFloor(2.50);
		policyAdspaceDto.setStatus((byte) 1);
		policyAdspaceDto.setMediaDealId("1000002");
		adspaceDto.setBidFloor(2.00);
		policyAdspaceDto.setAdspace(adspaceDto);
		policyAdspaceDtos.add(policyAdspaceDto);
//		policyAdspaceDto.setAdspace(adspace);
		
		List<PolicyDspDto> policyDspDtos = new ArrayList<PolicyDspDto>();
		PolicyDspDto policyDspDto = new PolicyDspDto();
		policyDspDto.setDspId(222);
		policyDspDto.setStatus((byte) 1);
		policyDspDtos.add(policyDspDto);
		policyDto.setPolicyDsps(policyDspDtos);
		
//		String link = "http://172.16.25.48:8080/services/policy/update";
		String link = "http://localhost:8080/services/policy/update";
		HttpUtilTest.httpPost(link, JSON.toJSONString(policyDto));
	}
	
}
