package com.madhouse.platform.premiummad.dao;

import java.util.List;

import com.madhouse.platform.premiummad.entity.ReportCriterion;
import com.madhouse.platform.premiummad.entity.ReportDsp;

public interface ReportDspDao {

	List<ReportDsp> queryDspReport(ReportCriterion reportCriterion);
	
	List<ReportDsp> queryDspMediaReport(ReportCriterion reportCriterion);

	List<ReportDsp> queryDspLocationReport(ReportCriterion reportCriterion);
	
	List<ReportDsp> queryRtDspReport(ReportCriterion reportCriterion);
}