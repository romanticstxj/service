package com.madhouse.platform.premiummad.controller;

import org.junit.Test;

public class ReportControllerTest {
	
	@Test
	public void mediaReport(){
		String link = "http://localhost:8080/services/report/media?type=1&dims=8&realtime=0&startDate=20171015"
				+ "&endDate=20171025";
//		String link = "http://172.16.25.48:8080/services/report/media?type=1&dims=1&realtime=1&startDate=20170925"
//				+ "&endDate=20170925";
		HttpUtilTest.httpGet(link);
	}
	
	@Test
	public void mediaDashboardReport(){
		String link = "http://localhost:8080/services/report/mediaDashboard?dims=1&realtime=1&startDate=20170909"
				+ "&endDate=20170912";
		HttpUtilTest.httpGet(link);
	}
	
	@Test
	public void dspReport(){
//		String link = "http://172.16.25.31:8080/services/report/dsp?type=16&dims=35&realtime=0&startDate=20170801"
//				+ "&endDate=20170925";
		String link = "http://localhost:8080/services/report/dsp?type=64&dims=32&realtime=0&startDate=20170801"
				+ "&endDate=20171225&dspId=600021";
		HttpUtilTest.httpGet(link);
	}
	
	@Test
	public void policyReport(){
		String link = "http://localhost:8080/services/report/policy?type=32&dims=32&realtime=0&startDate=20170801"
				+ "&endDate=20170803";
		HttpUtilTest.httpGet(link);
	}
	
}
