package com.madhouse.platform.premiummad.service;

import java.util.List;

import com.madhouse.platform.premiummad.entity.ReportCriterion;
import com.madhouse.platform.premiummad.entity.ReportMedia;

public interface IReportService{

	List<ReportMedia> queryMediaReport(ReportCriterion reportCriterion);
	
}
