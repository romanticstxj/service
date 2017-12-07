package com.madhouse.platform.premiummad.service;

import java.util.List;

import com.madhouse.platform.premiummad.entity.ReportTask;

public interface IReportTaskService extends IBaseService<ReportTask>{
	
	List<ReportTask> queryList(Integer status, Integer userId, String sorting);
	
	int updateReportUri(ReportTask reportTask);
	
}
