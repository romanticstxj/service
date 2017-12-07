package com.madhouse.platform.premiummad.rule;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dto.ReportTaskDto;
import com.madhouse.platform.premiummad.entity.ReportTask;
import com.madhouse.platform.premiummad.exception.BusinessException;
import com.madhouse.platform.premiummad.util.BeanUtils;
import com.madhouse.platform.premiummad.util.DateUtils;

public class ReportTaskRule {
	
	public static ReportTask convertToModel(ReportTaskDto dto, ReportTask entity, boolean isCreate) throws ParseException {
		BeanUtils.copyProperties(dto, entity);
		BeanUtils.setCommonParam(entity, true);
        
		Date startDate = DateUtils.getFormatDateByPattern("yyyyMMdd", dto.getStartDate());
		Date endDate = DateUtils.getFormatDateByPattern("yyyyMMdd", dto.getEndDate());
		if(endDate.after(DateUtils.getCurrentDate())){ //结束日期不能在当天以后
			throw new BusinessException(StatusCode.SC20506);
		} else if(startDate.after(endDate)){ //开始日期不能在结束日期之后
			throw new BusinessException(StatusCode.SC20505);
		} else if(DateUtils.getDateSubtract(startDate, endDate) > 180){ //?开始和结束日期相差不能超过半年
			
		}
		
		entity.setStartDate(startDate);
		entity.setEndDate(endDate);
        
		return entity;
	}
	
	public static List<ReportTaskDto> convertToDtoList(List<ReportTask> entities, List<ReportTaskDto> dtos) {
        //copy entities to dtos
		BeanUtils.copyList(entities,dtos,ReportTaskDto.class);
        for(int i=0; i<dtos.size(); i++){
        	//处理日期返回yyyy-MM-dd格式
    		String startDateStr = DateUtils.getFormatStringByPattern(
    				SystemConstant.DatePattern.yyyyMMdd_DISPLAY, entities.get(i).getStartDate());
    		String endDateStr = DateUtils.getFormatStringByPattern(
    				SystemConstant.DatePattern.yyyyMMdd_DISPLAY, entities.get(i).getEndDate());
    		dtos.get(i).setStartDate(startDateStr);
    		dtos.get(i).setEndDate(endDateStr);
    		
    		//处理reportUri仅返回名字
    		String reportUri = dtos.get(i).getReportUri();
    		String reportName = extractReportNameForDisplay(reportUri);
    		dtos.get(i).setReportUri(reportName);
        }
        
        return dtos;
	}

	private static String extractReportNameForDisplay(String reportUri) {
		int beginIndex = reportUri.lastIndexOf(SystemConstant.OtherConstant.FILE_PATH_DILIMETER);
		int endIndex = reportUri.indexOf(SystemConstant.OtherConstant.SUFFIX_CSV) == -1 
				? reportUri.length() : reportUri.indexOf(SystemConstant.OtherConstant.SUFFIX_CSV);
		String reportName = reportUri.substring(beginIndex + 1, endIndex);
		return reportName;
	}
	
}
