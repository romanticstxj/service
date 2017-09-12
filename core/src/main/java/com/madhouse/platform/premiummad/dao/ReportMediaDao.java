package com.madhouse.platform.premiummad.dao;

import java.util.List;

import com.madhouse.platform.premiummad.entity.ReportCriterion;
import com.madhouse.platform.premiummad.entity.ReportMedia;

public interface ReportMediaDao {
    
	List<ReportMedia> queryMediaReport(ReportCriterion reportCriterion);
	
	List<ReportMedia> queryRtMediaReport(ReportCriterion reportCriterion);

	List<ReportMedia> queryMediaLocationReport(ReportCriterion reportCriterion);

	List<ReportMedia> queryMediaReportDashboard(ReportCriterion reportCriterion);
}