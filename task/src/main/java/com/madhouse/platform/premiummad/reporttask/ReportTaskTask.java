package com.madhouse.platform.premiummad.reporttask;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.annotation.CsvColumn;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.entity.ReportCriterion;
import com.madhouse.platform.premiummad.entity.ReportMedia;
import com.madhouse.platform.premiummad.entity.ReportMediaCsv;
import com.madhouse.platform.premiummad.entity.ReportTask;
import com.madhouse.platform.premiummad.service.IReportService;
import com.madhouse.platform.premiummad.service.IReportTaskService;
import com.madhouse.platform.premiummad.util.CsvUtil;
import com.madhouse.platform.premiummad.util.StringUtils;

@Component
public class ReportTaskTask {
	
	@Autowired
	private IReportService reportService;
	
	@Autowired
	private IReportTaskService reportTaskService;
	
	private final Map<Integer, List<String>> csvFieldsMapping = new HashMap<>();
	
	{
		List<String> csvFields = new ArrayList<>();
		csvFields.add("mediaName");
		csvFields.add("adspaceName");
		csvFields.add("reqs");
		csvFields.add("bids");
		csvFields.add("errs");
//		csvFields.add("vimpsRate");
		csvFields.add("imps");
		csvFields.add("vimps");
//		csvFields.add("vclksRate");
		csvFields.add("clks");
		csvFields.add("vclks");
//		csvFields.add("eCPM");
//		csvFields.add("eCPC");
		csvFields.add("income");
		csvFieldsMapping.put(SystemConstant.DB.REPORT_TASK_TYPE_MEDIA_ADSPACE, csvFields);
		
	}
	
	public void test() throws NoSuchFieldException, SecurityException {
		System.out.println(this);
		System.out.println(csvFieldsMapping);
		List<ReportTask> unfinishedReportTasks = reportTaskService.queryList(
				SystemConstant.DB.REPORT_TASK_STATUS_PROCESSING, null, SystemConstant.DB.ORDER_BY_ASC);
		for(ReportTask rt: unfinishedReportTasks){
			Integer type = rt.getType();
			Date startDate = rt.getStartDate();
			Date endDate = rt.getEndDate();
			ReportCriterion reportCriterion = new ReportCriterion();
			
			
			
			if(type.intValue() == SystemConstant.DB.REPORT_TASK_TYPE_MEDIA_ADSPACE){
				reportCriterion.setType(SystemConstant.DB.TYPE_DEFAULT);
				reportCriterion.setDims(SystemConstant.DB.DIM_ADSPACE+SystemConstant.DB.DIM_MEDIA);
				reportCriterion.setRealtime(0);
				reportCriterion.setStartDate(startDate);
				reportCriterion.setEndDate(endDate);
				transformDimensions(reportCriterion);
				List<ReportMediaCsv> result =reportService.queryMediaReport(reportCriterion);
				System.out.println(JSON.toJSONString(result));
				
//				Field[] fields = ReportMedia.class.getDeclaredFields().length;
				List<String> fields = new ArrayList<>(ReportMedia.class.getDeclaredFields().length);
				List<String> columnTitles = new ArrayList<>(ReportMedia.class.getDeclaredFields().length);
				populateColumnNames(fields, columnTitles, type, ReportMedia.class);
				CsvUtil.createCSVFile(result, columnTitles, fields, "data", "hello", ReportMedia.class);
			}
			
		}
		
		System.out.println("ddd");
	}
	
	private <T> void populateColumnNames(List<String> fields, List<String> columnTitles, Integer type, Class<?> T) throws NoSuchFieldException, SecurityException {
		List<String> csvFieldNames = csvFieldsMapping.get(type);
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

	/**
	 * �����ѯ��ά�ȵĹ�ѡ
	 * @param entity
	 */
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
	
	
}
