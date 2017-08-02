package com.madhouse.platform.premiummad.controller;

import org.junit.Test;

public class ReportControllerTest {
	
	@Test
	public void mediaReport(){
		String link = "http://localhost:8080/services/report/media?type=16&dims=12&realtime=0&startDate=20170801"
				+ "&endDate=20170803";
		HttpUtilTest.httpGet(link);
	}
}
