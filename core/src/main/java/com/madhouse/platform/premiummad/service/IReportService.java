package com.madhouse.platform.premiummad.service;

import java.util.List;

import com.madhouse.platform.premiummad.entity.ReportCriterion;
import com.madhouse.platform.premiummad.entity.ReportDsp;
import com.madhouse.platform.premiummad.entity.ReportMedia;
import com.madhouse.platform.premiummad.entity.ReportPolicy;

public interface IReportService{

	List<ReportMedia> queryMediaReport(ReportCriterion reportCriterion);
	
	List<ReportDsp> queryDspReport(ReportCriterion reportCriterion);

	List<ReportPolicy> queryPolicyReport(ReportCriterion reportCriterion);

	List<ReportMedia> queryMediaReportDashboard(ReportCriterion reportCriterion);
	
}
