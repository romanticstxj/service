package com.madhouse.platform.premiummad.dao;

import java.util.List;

import com.madhouse.platform.premiummad.entity.ReportCriterion;
import com.madhouse.platform.premiummad.entity.ReportMediaCsv;

public interface ReportMediaDao {
    
	List<ReportMediaCsv> queryMediaReport(ReportCriterion reportCriterion);
	
	List<ReportMediaCsv> queryRtMediaReport(ReportCriterion reportCriterion);

	List<ReportMediaCsv> queryMediaLocationReport(ReportCriterion reportCriterion);

	List<ReportMediaCsv> queryMediaReportDashboard(ReportCriterion reportCriterion);
}