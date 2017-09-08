package com.madhouse.platform.premiummad.controller;

import org.junit.Test;

public class ReportControllerTest {
	
	@Test
	public void mediaReport(){
		String link = "http://localhost:8080/services/report/media?type=1&dims=1&realtime=0&startDate=20170830"
				+ "&endDate=20170901";
		HttpUtilTest.httpGet(link);
	}
	
	@Test
	public void dspReport(){
		String link = "http://localhost:8080/services/report/dsp?type=32&dims=&realtime=0&startDate=20170801"
				+ "&endDate=20170803";
		HttpUtilTest.httpGet(link);
	}
	
	@Test
	public void policyReport(){
		String link = "http://localhost:8080/services/report/policy?type=1&dims=35&realtime=0&startDate=20170801"
				+ "&endDate=20170803";
		HttpUtilTest.httpGet(link);
	}
	
}
