package com.madhouse.platform.premiummad.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dao.ReportTaskDao;
import com.madhouse.platform.premiummad.entity.ReportTask;
import com.madhouse.platform.premiummad.service.IReportTaskService;
import com.madhouse.platform.premiummad.util.DateUtils;

@Service
@Transactional(rollbackFor=RuntimeException.class)
public class ReportTaskServiceImpl implements IReportTaskService{
	
	private final static Map<Integer, String> reportNameMapping = new HashMap<>();
	
	@Autowired
	private ReportTaskDao reportTaskDao;
	
	{
		reportNameMapping.put(1, "媒体广告位报表");
		reportNameMapping.put(2, "媒体分日报表");
		
	}

	@Override
	public List<ReportTask> queryList(Integer status, Integer userId, String sorting) {
		return reportTaskDao.queryList(status, userId, sorting);
	}

	@Override
	public int insert(ReportTask reportTask) {
		reportTaskDao.insertSelective(reportTask);
		String reportUri = generateReportUri(reportTask);
		reportTask.setReportUri(reportUri);
		reportTaskDao.updateReportUri(reportTask);
		return 0;
	}
	
	private String generateReportUri(ReportTask reportTask){
		Integer id = reportTask.getId();
		String startDate = DateUtils.getFormatStringByPattern
				(SystemConstant.DatePattern.yyyyMMdd, reportTask.getStartDate());
		String endDate = DateUtils.getFormatStringByPattern
				(SystemConstant.DatePattern.yyyyMMdd, reportTask.getEndDate());
		Integer type = reportTask.getType();
		String reportUri = SystemConstant.OtherConstant.PREFIX_REPORT_TASK_URI
				+ reportNameMapping.get(type) 
				+ "_" + startDate
				+ "-" + endDate
				+ "_" + id + SystemConstant.OtherConstant.SUFFIX_CSV;
		return reportUri;
	}
	
	@Override
	public int updateReportUri(ReportTask reportTask) {
		return reportTaskDao.updateReportUri(reportTask);
	}

	@Override
	public int update(ReportTask t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateStatus(ReportTask t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ReportTask queryById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReportTask> queryAll(List<Integer> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int checkName(String name) {
		// TODO Auto-generated method stub
		return 0;
	}

}
