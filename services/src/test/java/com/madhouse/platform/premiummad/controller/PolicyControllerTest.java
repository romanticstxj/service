package com.madhouse.platform.premiummad.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.dto.PolicyAdspaceDto;
import com.madhouse.platform.premiummad.dto.PolicyDspDto;
import com.madhouse.platform.premiummad.dto.PolicyDto;
import com.madhouse.platform.premiummad.util.DateUtils;

public class PolicyControllerTest {
	
	@Test
	public void add() throws ParseException{
		PolicyDto policyDto = new PolicyDto();
		policyDto.setName("mypolicy1");
		policyDto.setWeight(10);
		policyDto.setStartDate(DateUtils.getFormatDateByPattern("yyyy-MM-dd", "2017-07-20"));
		policyDto.setIsEndDate(0);
		policyDto.setIsTimeTargeting(0);
		policyDto.setIsLocationTargeting(0);
		policyDto.setConnTargeting("1,2");
		policyDto.setOsTargeting("1");
		policyDto.setType((byte) 2);
		policyDto.setIsQuantityLimit(1);
		policyDto.setLimitType((byte) 2);
		policyDto.setLimitReqs(1000);
		policyDto.setLimitSpeed((byte) 2);
		
		List<PolicyAdspaceDto> policyAdspaceDtos = new ArrayList<PolicyAdspaceDto>();
		policyDto.setPolicyAdspaces(policyAdspaceDtos);
		PolicyAdspaceDto policyAdspaceDto = new PolicyAdspaceDto();
		policyAdspaceDto.setAdspaceId(200000);
		policyAdspaceDto.setBidType((byte) 1);
		policyAdspaceDto.setBidFloor(1.50);
		policyAdspaceDto.setMediaDealId("1000001");
		policyAdspaceDtos.add(policyAdspaceDto);
		policyAdspaceDto = new PolicyAdspaceDto();
		policyAdspaceDto.setAdspaceId(200006);
		policyAdspaceDto.setBidType((byte) 2);
		policyAdspaceDto.setBidFloor(2.50);
		policyAdspaceDto.setMediaDealId("1000002");
		policyAdspaceDtos.add(policyAdspaceDto);
//		policyAdspaceDto.setAdspace(adspace);
		
		PolicyDspDto policyDspDto = new PolicyDspDto();
		policyDspDto.setDspId(600002);
		policyDspDto.setStatus((byte) 1);
		policyDto.setPolicyDsp(policyDspDto);
		
		String link = "http://localhost:8080/services/policy/create";
		HttpUtilTest.httpPost(link, JSON.toJSONString(policyDto));
	}
	
	@Test
	public void detail(){
		String link = "http://localhost:8080/services/policy/detail?id=500004&type=2";
		HttpUtilTest.httpGet(link);
	}
}
