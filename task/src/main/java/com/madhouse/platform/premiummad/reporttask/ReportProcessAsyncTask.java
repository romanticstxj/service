package com.madhouse.platform.premiummad.reporttask;

import java.io.File;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.entity.ReportDspCsv;
import com.madhouse.platform.premiummad.entity.ReportMediaCsv;
import com.madhouse.platform.premiummad.entity.ReportPolicyCsv;
import com.madhouse.platform.premiummad.entity.ReportTask;
import com.madhouse.platform.premiummad.service.IReportTaskService;

@Component
public class ReportProcessAsyncTask {
	
	private static final Logger logger = LoggerFactory.getLogger(ReportProcessAsyncTask.class);
	
	@Autowired
	private IReportTaskService reportTaskService;
	
	@Value("${task.maxThreadCount}")
	private int SIZE;
	
	public void process() throws InterruptedException, ExecutionException, TimeoutException {
		logger.debug("***************begin Async task*************");
		//1.查询需要执行的报表任务
		List<ReportTask> unfinishedReportTasks = reportTaskService.queryList(
				SystemConstant.DB.REPORT_TASK_STATUS_PROCESSING, null, SystemConstant.DB.ORDER_BY_ASC);
		if(unfinishedReportTasks == null || unfinishedReportTasks.size() == 0){
			return;
		}
		
		int totalCount = unfinishedReportTasks.size();
		logger.debug("total " + totalCount + " report tasks for this time");
		ExecutorService es = Executors.newFixedThreadPool(SIZE);
		CountDownLatch latch = new CountDownLatch(totalCount);
		for(int i=0; i<totalCount; i++){
			es.execute(new ReportTaskPortion(unfinishedReportTasks.get(i), reportTaskService, latch));
		}
		
		latch.await();
		es.shutdown();
		logger.debug("***************end Async task*************");
	}
	
	static class ReportTaskPortion implements Runnable{
		private final CountDownLatch latch;
		private final ReportTask reportTask;
		private final IReportTaskService reportTaskService;

		public ReportTaskPortion(ReportTask reportTask, IReportTaskService reportTaskService, CountDownLatch latch) {
			this.reportTask = reportTask;
			this.reportTaskService = reportTaskService;
			this.latch = latch;
		}

		@Override
		public void run() {
			long beginTime = System.currentTimeMillis();
			String threadName = Thread.currentThread().getName();
			File csvReport = null;
			int taskId = reportTask.getId();
			logger.debug(threadName + " with taskId " + taskId + " begin");
			int type = reportTask.getType() == null ? 0 : reportTask.getType().intValue();
			try{
				if(type < SystemConstant.DB.REPORT_TASK_TYPE_DSP_DATEHOUR && type > 0){ //媒体报表
					//2.对每个报表任务生成查询报表的条件,并查询报表结果
					List<ReportMediaCsv> csvResult = reportTaskService.buildMediaReport(reportTask);
					//3.对每个报表条件生成Csv报表文件
					csvReport = reportTaskService.generateCsvReport(csvResult, reportTask, ReportMediaCsv.class);
				} else if(type < SystemConstant.DB.REPORT_TASK_TYPE_POLICY_DATEHOUR){ //dsp报表
					List<ReportDspCsv> csvResult = reportTaskService.buildDspReport(reportTask);
					csvReport = reportTaskService.generateCsvReport(csvResult, reportTask, ReportDspCsv.class);
				} else{ //policy报表
					List<ReportPolicyCsv> csvResult = reportTaskService.buildPolicyReport(reportTask);
					csvReport = reportTaskService.generateCsvReport(csvResult, reportTask, ReportPolicyCsv.class);
				}
				
				if(csvReport != null){ //报表正常生成，直接更新状态
					reportTask.setStatus((short) 1);
					reportTaskService.updateStatus(reportTask);
				}
				logger.debug(threadName + " with taskId " + taskId + " succeeded");
			} catch (Exception e){
				logger.debug(threadName + " with taskId " + taskId + " failed");
				logger.error(e.getMessage());
				return;
			} finally{
				latch.countDown();
				logger.debug(threadName + " with taskId " + taskId + " takes " + (System.currentTimeMillis() - beginTime) + "ms");
			}
		}
	}
	
}
