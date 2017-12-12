package com.madhouse.platform.premiummad.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.madhouse.platform.premiummad.entity.ReportTask;

public interface ReportTaskDao {

    int insertSelective(ReportTask record);
    
    int updateReportUri(ReportTask record);
    
    List<ReportTask> queryList(@Param("status") Integer status
    		, @Param("userId")Integer userId, @Param("sorting") String sorting);

	void updateStatus(List<ReportTask> reportTasks);
}