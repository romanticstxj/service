package com.madhouse.platform.premiummad.reporttask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.entity.ReportDspCsv;
import com.madhouse.platform.premiummad.entity.ReportMediaCsv;
import com.madhouse.platform.premiummad.entity.ReportPolicyCsv;
import com.madhouse.platform.premiummad.entity.ReportTask;
import com.madhouse.platform.premiummad.service.IReportTaskService;

@Component
public class ReportProcessTask {
	
	@Autowired
	private IReportTaskService reportTaskService;
	
	public void process() {
		//1.查询需要执行的报表任务
		List<ReportTask> unfinishedReportTasks = reportTaskService.queryList(
				SystemConstant.DB.REPORT_TASK_STATUS_PROCESSING, null, SystemConstant.DB.ORDER_BY_ASC);
		List<ReportTask> finishedReportTasks = new ArrayList<>();
		for(int i=0; i<unfinishedReportTasks.size(); i++){
			//2.对每个报表任务生成查询报表的条件,并查询报表结果
			ReportTask unfinishedReportTask = unfinishedReportTasks.get(i);
			//3.对每个报表条件生成Csv报表文件
			File csvReport = null;
			
			int type = unfinishedReportTask.getType() == null ? 0 : unfinishedReportTask.getType().intValue();
			try{
				if(type < SystemConstant.DB.REPORT_TASK_TYPE_DSP_DATEHOUR && type > 0){ //媒体报表
					List<ReportMediaCsv> csvResult = reportTaskService.buildMediaReport(unfinishedReportTask);
					csvReport = reportTaskService.generateCsvReport(csvResult, unfinishedReportTask, ReportMediaCsv.class);
				} else if(type < SystemConstant.DB.REPORT_TASK_TYPE_POLICY_DATEHOUR){ //dsp报表
					List<ReportDspCsv> csvResult = reportTaskService.buildDspReport(unfinishedReportTask);
					csvReport = reportTaskService.generateCsvReport(csvResult, unfinishedReportTask, ReportDspCsv.class);
				} else{ //policy报表
					List<ReportPolicyCsv> csvResult = reportTaskService.buildPolicyReport(unfinishedReportTask);
					csvReport = reportTaskService.generateCsvReport(csvResult, unfinishedReportTask, ReportPolicyCsv.class);
				}
			} catch (Exception e){
				//logger
				continue;
			}
			
			if(csvReport != null){ //报表正常生成，记录下id
				ReportTask finishedReportTask = new ReportTask();
				finishedReportTask.setId(unfinishedReportTask.getId());
				finishedReportTask.setStatus((short) 1);
				finishedReportTasks.add(finishedReportTask);
			}
		}
		
		//4.回写已完成任务的状态
		reportTaskService.updateStatus(finishedReportTasks);
	}
	
}
