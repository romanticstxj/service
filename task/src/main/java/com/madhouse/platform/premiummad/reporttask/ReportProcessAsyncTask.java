package com.madhouse.platform.premiummad.reporttask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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
	
	private static final Logger logger = LoggerFactory.getLogger("performance");
	
	@Autowired
	private IReportTaskService reportTaskService;
	
	@Value("${task.maxThreadCount}")
	private int SIZE;
	
	public void process() throws InterruptedException, ExecutionException, TimeoutException {
		//1.查询需要执行的报表任务
		List<ReportTask> unfinishedReportTasks = reportTaskService.queryList(
				SystemConstant.DB.REPORT_TASK_STATUS_PROCESSING, null, SystemConstant.DB.ORDER_BY_ASC);
		Map<Integer, List<ReportTask>> portionedTaskMap = new HashMap<>();
		List<ReportTask> portionedTaskList = null;
		for(ReportTask rt: unfinishedReportTasks){
			//计算每个需要执行的report任务的id的模
			int id = rt.getId();
			int mod = id % SIZE;
			
			//把相同模的task分配给相同的容器
			portionedTaskList = portionedTaskMap.get(mod);
			if(portionedTaskList == null){
				portionedTaskList = new ArrayList<>();
				portionedTaskMap.put(mod, portionedTaskList);
			}
			portionedTaskList.add(rt);
		}
		
		List<ReportTask> finishedReportTasks = new ArrayList<>();
		ExecutorService exec = Executors.newCachedThreadPool();
		//All must share a single Countdownlatch object
		CountDownLatch latch = new CountDownLatch(portionedTaskMap.size());
		//遍历所有的portion task，把相同id模的task分别分配给这些线程进行处理
		List<Future<List<ReportTask>>> resultList = new ArrayList<>();
		Iterator<Entry<Integer, List<ReportTask>>> it = portionedTaskMap.entrySet().iterator();
		while(it.hasNext()){ //遍历查询出来的需要执行的task
			Entry<Integer, List<ReportTask>> entry = it.next();
			int threadId = entry.getKey(); //线程id
			portionedTaskList = entry.getValue(); //每个线程需要执行的task列表
			Future<List<ReportTask>> future = exec.submit(new ReportTaskPortion(threadId, portionedTaskList, latch, reportTaskService));
			resultList.add(future);
		}
		
		latch.await();
		long beginTime = System.currentTimeMillis();
		//4.回写已完成任务的状态
		for(Future<List<ReportTask>> future: resultList){
			if(future.isDone() && !future.isCancelled()){
				finishedReportTasks.addAll(future.get());
			}
		}
		reportTaskService.updateStatus(finishedReportTasks);
		exec.shutdown();
		
//		CompletionService<String> completionService = new ExecutorCompletionService<String>(exec);
//		completionService.take();
	}
	
	//Performs some portion of a task
	static class ReportTaskPortion implements Callable<List<ReportTask>>{
		private final CountDownLatch latch;
		private final int taskMod;
		private final List<ReportTask> portionedTaskList;
		private final IReportTaskService reportTaskService;

		public ReportTaskPortion(int taskMod, List<ReportTask> portionedTaskList, CountDownLatch latch,
				IReportTaskService reportTaskService) {
			this.taskMod = taskMod;
			this.portionedTaskList = portionedTaskList;
			this.latch = latch;
			this.reportTaskService = reportTaskService;
		}

		@Override
		public List<ReportTask> call() {
			long beginTime = System.currentTimeMillis();
			List<ReportTask> finishedPortionedReportTasks = new ArrayList<>();
			for(int i=0; i<portionedTaskList.size(); i++){
				//2.对每个报表任务生成查询报表的条件,并查询报表结果
				ReportTask unfinishedReportTask = portionedTaskList.get(i);
				//3.对每个报表条件生成Csv报表文件
				File csvReport = null;
				int taskId = unfinishedReportTask.getId();
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
					logger.debug("report task " + taskId + " failed");
					logger.error(e.getMessage());
					continue;
				}
				
				if(csvReport != null){ //报表正常生成，记录下id
					ReportTask finishedReportTask = new ReportTask();
					finishedReportTask.setId(taskId);
					finishedReportTask.setStatus((short) 1);
					logger.debug("report task " + taskId + " succeeded");
					finishedPortionedReportTasks.add(finishedReportTask);
				}
			}
			
			latch.countDown();
			logger.debug((System.currentTimeMillis() - beginTime) + "ms for thread " + this);
			return finishedPortionedReportTasks;
		}
		
		@Override
		public String toString() {
			return String.format("%1$-3d ", taskMod);
		}
	}
}


