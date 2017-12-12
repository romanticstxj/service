package com.madhouse.platform.premiummad.service;

import java.io.File;
import java.util.List;

import com.madhouse.platform.premiummad.entity.ReportDspCsv;
import com.madhouse.platform.premiummad.entity.ReportMediaCsv;
import com.madhouse.platform.premiummad.entity.ReportPolicyCsv;
import com.madhouse.platform.premiummad.entity.ReportTask;

public interface IReportTaskService extends IBaseService<ReportTask>{
	
	List<ReportTask> queryList(Integer status, Integer userId, String sorting);
	
	int updateReportUri(ReportTask reportTask);

	void updateStatus(List<ReportTask> finishedReportTasks);

	List<ReportMediaCsv> buildMediaReport(ReportTask unfinishedReportTask);

	List<ReportDspCsv> buildDspReport(ReportTask unfinishedReportTask);

	List<ReportPolicyCsv> buildPolicyReport(ReportTask unfinishedReportTask);

	<T> File generateCsvReport(List<T> csvResult, ReportTask unfinishedReportTask, 
			Class<?> T) throws NoSuchFieldException, SecurityException;

	
}
