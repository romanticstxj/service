package com.madhouse.platform.premiummad.dao;

import java.util.List;

import com.madhouse.platform.premiummad.entity.ReportCriterion;
import com.madhouse.platform.premiummad.entity.ReportPolicy;

public interface ReportPolicyDao {

	List<ReportPolicy> queryPolicyReport(ReportCriterion reportCriterion);

	List<ReportPolicy> queryRtPolicyReport(ReportCriterion reportCriterion);

	List<ReportPolicy> queryPolicyMediaReport(ReportCriterion reportCriterion);

	List<ReportPolicy> queryPolicyLocationReport(ReportCriterion reportCriterion);
	
}