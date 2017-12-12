package com.madhouse.platform.premiummad.service.impl;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.annotation.CsvColumn;
import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dao.ReportTaskDao;
import com.madhouse.platform.premiummad.entity.ReportCriterion;
import com.madhouse.platform.premiummad.entity.ReportDsp;
import com.madhouse.platform.premiummad.entity.ReportDspCsv;
import com.madhouse.platform.premiummad.entity.ReportMedia;
import com.madhouse.platform.premiummad.entity.ReportMediaCsv;
import com.madhouse.platform.premiummad.entity.ReportPolicy;
import com.madhouse.platform.premiummad.entity.ReportPolicyCsv;
import com.madhouse.platform.premiummad.entity.ReportTask;
import com.madhouse.platform.premiummad.exception.BusinessException;
import com.madhouse.platform.premiummad.service.IReportService;
import com.madhouse.platform.premiummad.service.IReportTaskService;
import com.madhouse.platform.premiummad.util.AlgorithmUtils;
import com.madhouse.platform.premiummad.util.BeanUtils;
import com.madhouse.platform.premiummad.util.CsvUtil;
import com.madhouse.platform.premiummad.util.DateUtils;
import com.madhouse.platform.premiummad.util.ReportTaskMapping;
import com.madhouse.platform.premiummad.util.StringUtils;

@Service
@Transactional(rollbackFor=RuntimeException.class)
public class ReportTaskServiceImpl implements IReportTaskService{
	
	private static final Logger logger = LoggerFactory.getLogger(ReportTaskServiceImpl.class);
	
	@Autowired
	private ReportTaskDao reportTaskDao;
	
	@Autowired
	private IReportService reportService;
	
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
		String typeStr = SystemConstant.OtherConstant.REPORT_TASK_TYPE_PREFIX + type;
		String reportUri = SystemConstant.OtherConstant.PREFIX_REPORT_TASK_URI
				+ ReportTaskMapping.reportNameMapping.get(typeStr) 
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
	public void updateStatus(List<ReportTask> finishedReportTasks) {
		if(finishedReportTasks != null && finishedReportTasks.size() > 0){
			reportTaskDao.updateStatus(finishedReportTasks);
		}
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

	@Override
	public List<ReportMediaCsv> buildMediaReport(ReportTask rt) {
		Integer type = rt.getType();
		Date startDate = rt.getStartDate();
		Date endDate = rt.getEndDate();
		ReportCriterion reportCriterion = new ReportCriterion();
		List<ReportMediaCsv> csvResult = null;
		if(type.intValue() == SystemConstant.DB.REPORT_TASK_TYPE_MEDIA_ADSPACE){
			reportCriterion.setType(SystemConstant.DB.TYPE_DEFAULT);
			reportCriterion.setDims(SystemConstant.DB.DIM_ADSPACE);
			reportCriterion.setRealtime(SystemConstant.DB.IS_OFFLINE);
			reportCriterion.setStartDate(startDate);
			reportCriterion.setEndDate(endDate);
			transformDimensions(reportCriterion);
			List<ReportMedia> result = reportService.queryMediaReport(reportCriterion);
			csvResult = convertToMediaCsvReport(result, type);
			System.out.println(JSON.toJSONString(csvResult));
		} else if(type.intValue() == SystemConstant.DB.REPORT_TASK_TYPE_MEDIA_DATEHOUR){
			reportCriterion.setType(SystemConstant.DB.TYPE_DEFAULT);
			reportCriterion.setDims(SystemConstant.DB.DIM_DATE);
			reportCriterion.setRealtime(SystemConstant.DB.IS_OFFLINE);
			reportCriterion.setStartDate(startDate);
			reportCriterion.setEndDate(endDate);
			transformDimensions(reportCriterion);
			List<ReportMedia> result = reportService.queryMediaReport(reportCriterion);
			csvResult = convertToMediaCsvReport(result, type);
			System.out.println(JSON.toJSONString(csvResult));
		} else if(type.intValue() == SystemConstant.DB.REPORT_TASK_TYPE_MEDIA_ADSPACE_LOCATION){
			reportCriterion.setType(SystemConstant.DB.TYPE_LOCATION);
			reportCriterion.setDims(SystemConstant.DB.DIM_ADSPACE);
			reportCriterion.setRealtime(SystemConstant.DB.IS_OFFLINE);
			reportCriterion.setStartDate(startDate);
			reportCriterion.setEndDate(endDate);
			transformDimensions(reportCriterion);
			long beginTime = System.currentTimeMillis();
			List<ReportMedia> result = reportService.queryMediaReport(reportCriterion);
			logger.debug(rt.getReportUri() + "|queryReport: " + (System.currentTimeMillis() - beginTime) + "ms"); // 每次请求换行
			beginTime = System.currentTimeMillis();
			csvResult = convertToMediaCsvReport(result, type);
			logger.debug(rt.getReportUri() + "|convertReport: " + (System.currentTimeMillis() - beginTime) + "ms"); // 每次请求换行
		}
		return csvResult;
	}
	
	private List<ReportMediaCsv> convertToMediaCsvReport(List<ReportMedia> result, Integer type) {
		List<ReportMediaCsv> targetList = new ArrayList<>();
		for(ReportMedia source: result){
			ReportMediaCsv target = new ReportMediaCsv();
			BeanUtils.copyProperties(source, target);
			
			//计算填充率、点击率、媒体收入、ECPM、ECPC
			double bidsRate = AlgorithmUtils.calPercent(target.getBids(), target.getReqs());
			String bidsRateStr = AlgorithmUtils.convertPercent(bidsRate);
			double vclksRate = AlgorithmUtils.calPercent(target.getVclks(), target.getVimps());
			String vclksRateStr = AlgorithmUtils.convertPercent(vclksRate);
			double income = target.getIncome();
			String incomeStr = AlgorithmUtils.convertCurrency(income);
			double ecpm = AlgorithmUtils.calEcpm(income, target.getVimps());
			String ecpmStr = AlgorithmUtils.convertCurrency(ecpm);
			double ecpc = AlgorithmUtils.calEcpc(income, target.getVclks());
			String ecpcStr = AlgorithmUtils.convertCurrency(ecpc);
			String dateStr = DateUtils.getFormatStringByPattern(
					SystemConstant.DatePattern.yyyyMMdd_DISPLAY_CSV, target.getDate());
			
			target.setBidsRateStr(bidsRateStr);
			target.setVclksRateStr(vclksRateStr);
			target.setIncomeStr(incomeStr);
			target.setEcpmStr(ecpmStr);
			target.setEcpcStr(ecpcStr);
			target.setDateStr(dateStr);
			targetList.add(target);
		}
		return targetList;
	}
	
	@Override
	public List<ReportDspCsv> buildDspReport(ReportTask rt) {
		Integer type = rt.getType();
		Date startDate = rt.getStartDate();
		Date endDate = rt.getEndDate();
		ReportCriterion reportCriterion = new ReportCriterion();
		if(type.intValue() == SystemConstant.DB.REPORT_TASK_TYPE_DSP_DATEHOUR){
			reportCriterion.setType(SystemConstant.DB.TYPE_DEFAULT);
			reportCriterion.setDims(SystemConstant.DB.DIM_DATE);
			reportCriterion.setRealtime(SystemConstant.DB.IS_OFFLINE);
			reportCriterion.setStartDate(startDate);
			reportCriterion.setEndDate(endDate);
			transformDimensions(reportCriterion);
		} else if(type.intValue() == SystemConstant.DB.REPORT_TASK_TYPE_DSP_LOCATION){
			reportCriterion.setType(SystemConstant.DB.TYPE_LOCATION);
			reportCriterion.setRealtime(SystemConstant.DB.IS_OFFLINE);
			reportCriterion.setStartDate(startDate);
			reportCriterion.setEndDate(endDate);
			transformDimensions(reportCriterion);
		} else if(type.intValue() == SystemConstant.DB.REPORT_TASK_TYPE_DSP_ADSPACE){
			reportCriterion.setType(SystemConstant.DB.TYPE_MEDIA);
			reportCriterion.setDims(SystemConstant.DB.DIM_ADSPACE);
			reportCriterion.setRealtime(SystemConstant.DB.IS_OFFLINE);
			reportCriterion.setStartDate(startDate);
			reportCriterion.setEndDate(endDate);
			transformDimensions(reportCriterion);
		}
		
		long beginTime = System.currentTimeMillis();
		List<ReportDsp> result = reportService.queryDspReport(reportCriterion);
		logger.debug(rt.getReportUri() + "|queryReport: " + (System.currentTimeMillis() - beginTime) + "ms");
		beginTime = System.currentTimeMillis();
		List<ReportDspCsv> csvResult = convertToDspCsvReport(result, type);
		logger.debug(rt.getReportUri() + "|convertReport: " + (System.currentTimeMillis() - beginTime) + "ms");
		return csvResult;
	}

	private List<ReportDspCsv> convertToDspCsvReport(List<ReportDsp> result, Integer type) {
		List<ReportDspCsv> targetList = new ArrayList<>();
		for(ReportDsp source: result){
			ReportDspCsv target = new ReportDspCsv();
			BeanUtils.copyProperties(source, target);
			
			//计算填充率、点击率、需求方成本、ECPM、ECPC
			double bidsRate = AlgorithmUtils.calPercent(target.getBids(), target.getReqs());
			String bidsRateStr = AlgorithmUtils.convertPercent(bidsRate);
			double vclksRate = AlgorithmUtils.calPercent(target.getVclks(), target.getVimps());
			String vclksRateStr = AlgorithmUtils.convertPercent(vclksRate);
			double cost = target.getCost();
			String costStr = AlgorithmUtils.convertCurrency(cost);
			double ecpm = AlgorithmUtils.calEcpm(cost, target.getVimps());
			String ecpmStr = AlgorithmUtils.convertCurrency(ecpm);
			double ecpc = AlgorithmUtils.calEcpc(cost, target.getVclks());
			String ecpcStr = AlgorithmUtils.convertCurrency(ecpc);
			String dateStr = DateUtils.getFormatStringByPattern(
					SystemConstant.DatePattern.yyyyMMdd_DISPLAY_CSV, target.getDate());
			
			target.setBidsRateStr(bidsRateStr);
			target.setVclksRateStr(vclksRateStr);
			target.setCostStr(costStr);
			target.setEcpmStr(ecpmStr);
			target.setEcpcStr(ecpcStr);
			target.setDateStr(dateStr);
			targetList.add(target);
		}
		return targetList;
	}

	@Override
	public List<ReportPolicyCsv> buildPolicyReport(ReportTask rt) {
		Integer type = rt.getType();
		Date startDate = rt.getStartDate();
		Date endDate = rt.getEndDate();
		ReportCriterion reportCriterion = new ReportCriterion();
		if(type.intValue() == SystemConstant.DB.REPORT_TASK_TYPE_POLICY_DATEHOUR){
			reportCriterion.setType(SystemConstant.DB.TYPE_DEFAULT);
			reportCriterion.setDims(SystemConstant.DB.DIM_DATE);
			reportCriterion.setRealtime(SystemConstant.DB.IS_OFFLINE);
			reportCriterion.setStartDate(startDate);
			reportCriterion.setEndDate(endDate);
			transformDimensions(reportCriterion);
		} else if(type.intValue() == SystemConstant.DB.REPORT_TASK_TYPE_POLICY_LOCATION){
			reportCriterion.setType(SystemConstant.DB.TYPE_LOCATION);
			reportCriterion.setRealtime(SystemConstant.DB.IS_OFFLINE);
			reportCriterion.setStartDate(startDate);
			reportCriterion.setEndDate(endDate);
			transformDimensions(reportCriterion);
		} else if(type.intValue() == SystemConstant.DB.REPORT_TASK_TYPE_POLICY_ADSPACE){
			reportCriterion.setType(SystemConstant.DB.TYPE_MEDIA);
			reportCriterion.setDims(SystemConstant.DB.DIM_ADSPACE);
			reportCriterion.setRealtime(SystemConstant.DB.IS_OFFLINE);
			reportCriterion.setStartDate(startDate);
			reportCriterion.setEndDate(endDate);
			transformDimensions(reportCriterion);
		}
		
		long beginTime = System.currentTimeMillis();
		List<ReportPolicy> result = reportService.queryPolicyReport(reportCriterion);
		logger.debug(rt.getReportUri() + "|queryReport: " + (System.currentTimeMillis() - beginTime) + "ms");
		beginTime = System.currentTimeMillis();
		List<ReportPolicyCsv> csvResult = convertToPolicyCsvReport(result, type);
		logger.debug(rt.getReportUri() + "|convertReport: " + (System.currentTimeMillis() - beginTime) + "ms");
		return csvResult;
	}
	
	private List<ReportPolicyCsv> convertToPolicyCsvReport(List<ReportPolicy> result, Integer type) {
		List<ReportPolicyCsv> targetList = new ArrayList<>();
		for(ReportPolicy source: result){
			ReportPolicyCsv target = new ReportPolicyCsv();
			BeanUtils.copyProperties(source, target);
			
			//计算填充率、点击率、需求方成本、ECPM、ECPC
			double bidsRate = AlgorithmUtils.calPercent(target.getBids(), target.getReqs());
			String bidsRateStr = AlgorithmUtils.convertPercent(bidsRate);
			double vclksRate = AlgorithmUtils.calPercent(target.getVclks(), target.getVimps());
			String vclksRateStr = AlgorithmUtils.convertPercent(vclksRate);
			double income = target.getIncome();
			String incomeStr = AlgorithmUtils.convertCurrency(income);
			double cost = target.getCost();
			String costStr = AlgorithmUtils.convertCurrency(cost);
			double priceDiff = AlgorithmUtils.calPriceDiff(cost, income);
			String priceDiffStr = AlgorithmUtils.convertCurrency(priceDiff);
			double profitRate = AlgorithmUtils.calPercent(priceDiff, income);
			String profitRateStr = AlgorithmUtils.convertPercent(profitRate);
			String dateStr = DateUtils.getFormatStringByPattern(
					SystemConstant.DatePattern.yyyyMMdd_DISPLAY_CSV, target.getDate());
			
			target.setBidsRateStr(bidsRateStr);
			target.setVclksRateStr(vclksRateStr);
			target.setIncomeStr(incomeStr);
			target.setCostStr(costStr);
			target.setPriceDiffStr(priceDiffStr);
			target.setProfitRateStr(profitRateStr);
			target.setDateStr(dateStr);
			targetList.add(target);
		}
		return targetList;
	}
	
	
	private void transformDimensions(ReportCriterion entity) {
		Integer dims = entity.getDims();
		String dimsStr = StringUtils.convertSingleChoiceToMultiChoice(dims);
		int[] dimsArray = StringUtils.splitToIntArray(dimsStr);
		TreeSet<Integer> dimsSorting = new TreeSet<Integer>();
		
		/** dimsSorting�б��Ԫ�ػᰴ�ռȶ���ά��˳��������Ӧ��ά�ȣ�����policy�涨ʼ���Ƕ���ά�ȣ�ʱ��涨ʼ������ϸ�����ά��,
		 * �˴������ǰ��ѡ��ı���type��dims����������������������ά��˳�� **/
		for(int dim: dimsArray){
			switch (dim){
			case SystemConstant.DB.DIM_DATE: 
				entity.setHasDate(true);
				dimsSorting.add(SystemConstant.DB.DIM_SORTING_DATE);
				break;
			case SystemConstant.DB.DIM_HOUR: 
				entity.setHasHour(true);
				dimsSorting.add(SystemConstant.DB.DIM_SORTING_HOUR);
				break;
			case SystemConstant.DB.DIM_MEDIA: 
				entity.setHasMedia(true);
				dimsSorting.add(SystemConstant.DB.DIM_SORTING_MEDIA);
				break;
			case SystemConstant.DB.DIM_ADSPACE: 
				entity.setHasAdspace(true);
				dimsSorting.add(SystemConstant.DB.DIM_SORTING_ADSPACE);
				break;
			case SystemConstant.DB.DIM_POLICY: 
				entity.setHasPolicy(true);
				dimsSorting.add(SystemConstant.DB.DIM_SORTING_POLICY);
				break;
			case SystemConstant.DB.DIM_DSP: 
				entity.setHasDsp(true);
				dimsSorting.add(SystemConstant.DB.DIM_SORTING_DSP);
				break;
			default: //do nothing
			}
		}
		
		//���ݲ�ѯ�������ö���ά��
		int type = entity.getType();
		switch (type){
			case SystemConstant.DB.TYPE_CARRIER: 
				entity.setHasCarrier(true);
				break;
			case SystemConstant.DB.TYPE_DEVICE: 
				entity.setHasDevice(true);
				break;
			case SystemConstant.DB.TYPE_CONN: 
				entity.setHasConn(true);
				break;
			case SystemConstant.DB.TYPE_LOCATION: 
				entity.setHasLocation(true);
				dimsSorting.add(SystemConstant.DB.DIM_SORTING_LOCATION);
				break;
			case SystemConstant.DB.TYPE_MEDIA: 
				//typeΪý�壬��ֻ���ý�����ά�ȣ��Ա���ý��ά�����ϸ�Ĺ��λά���л�
				entity.setHasMedia(true);
				dimsSorting.add(SystemConstant.DB.DIM_SORTING_MEDIA);
				break;
			default:
		}
		
		entity.setLastOrderPosition(dimsSorting.last());
	}
	
	@Override
	public <T> File generateCsvReport(List<T> csvResult, ReportTask rt, Class<?> T) throws NoSuchFieldException, SecurityException {
		Integer type = rt.getType();
		File csvReport = null;
		int length = T.getDeclaredFields().length;
		List<String> fields = new ArrayList<>(length);
		List<String> columnTitles = new ArrayList<>(length);
		populateColumnNames(fields, columnTitles, type, T);
		String reportName = extractReportNameForDisplay(rt.getReportUri());
		csvReport = CsvUtil.createCSVFile(csvResult, columnTitles, fields, "report/", reportName, T, true);
		
		return csvReport;
	}
	
	/**
	 * 准备生成csv文件的列名和相应的对象域名
	 * @param fields
	 * @param columnTitles
	 * @param type
	 * @param T
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	private <T> void populateColumnNames(List<String> fields, List<String> columnTitles, Integer type, Class<?> T) throws NoSuchFieldException, SecurityException {
		List<String> csvFieldNames = ReportTaskMapping.CSV_FIELD_MAPPING.get(type);
		//遍历内存中的csvFieldNames，填充生成csv文件需要的column名和column值所对应的Field
		if(csvFieldNames != null){
			for(String csvFieldName: csvFieldNames){
				Field csvField = T.getDeclaredField(csvFieldName);
				CsvColumn ann = csvField.getAnnotation(CsvColumn.class);
				String columnTitle = "";
				if(ann != null){
					columnTitle = ann.title();
				}
				fields.add(csvFieldName);
				columnTitles.add(columnTitle);
			}
			
		}
	}
	
	private String extractReportNameForDisplay(String reportUri) {
		if(StringUtils.isEmpty(reportUri)){
			throw new BusinessException(StatusCode.SC20701);
		}
		int beginIndex = reportUri.lastIndexOf(SystemConstant.OtherConstant.FILE_PATH_DILIMETER);
//		int endIndex = reportUri.indexOf(SystemConstant.OtherConstant.SUFFIX_CSV) == -1 
//				? reportUri.length() : reportUri.indexOf(SystemConstant.OtherConstant.SUFFIX_CSV);
		String reportName = reportUri.substring(beginIndex + 1);
		return reportName;
	}

}
