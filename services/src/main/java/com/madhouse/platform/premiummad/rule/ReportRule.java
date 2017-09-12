package com.madhouse.platform.premiummad.rule;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dto.ReportDto;
import com.madhouse.platform.premiummad.entity.ReportCriterion;
import com.madhouse.platform.premiummad.entity.ReportMedia;
import com.madhouse.platform.premiummad.exception.BusinessException;
import com.madhouse.platform.premiummad.util.BeanUtils;
import com.madhouse.platform.premiummad.util.DateUtils;
import com.madhouse.platform.premiummad.util.StringUtils;

public class ReportRule extends BaseRule{
	
	public static void validateDto(ReportDto reportDto){
		String fieldName = BeanUtils.hasEmptyField(reportDto);
        if (fieldName != null)
        	throw new BusinessException(StatusCode.SC20002, fieldName + " cannot be null");
        
        if(reportDto.getType() < SystemConstant.DB.TYPE_MIN_VAL || reportDto.getType() > SystemConstant.DB.TYPE_MAX_VAL){
        	throw new BusinessException(StatusCode.SC20503);
        }
        
        if(reportDto.getDims() < SystemConstant.DB.DIM_MIN_VAL || reportDto.getDims() > SystemConstant.DB.DIM_MAX_VAL){
        	throw new BusinessException(StatusCode.SC20502);
        }
        
        if(reportDto.getRealtime() != SystemConstant.DB.IS_OFFLINE && reportDto.getRealtime() != SystemConstant.DB.IS_REALTIME){
        	throw new BusinessException(StatusCode.SC20504);
        }
	}
	
	public static void validateDashboardReportDto(ReportDto reportDto) throws ParseException{
		reportDto.setType(SystemConstant.DB.TYPE_DEFAULT);
		String fieldName = BeanUtils.hasEmptyField(reportDto);
        if (fieldName != null)
        	throw new BusinessException(StatusCode.SC20002, fieldName + " cannot be null");
        
        if(reportDto.getType() < SystemConstant.DB.TYPE_MIN_VAL || reportDto.getType() > SystemConstant.DB.TYPE_MAX_VAL){
        	throw new BusinessException(StatusCode.SC20503);
        }
        
        if(reportDto.getDims() < SystemConstant.DB.DIM_MIN_VAL || reportDto.getDims() > SystemConstant.DB.DIM_HOUR){
        	throw new BusinessException(StatusCode.SC20502);
        }
        
        Date startDate = DateUtils.getFormatDateByPattern("yyyyMMdd", reportDto.getStartDate());
		Date endDate = DateUtils.getFormatDateByPattern("yyyyMMdd", reportDto.getEndDate());
        if(reportDto.getDims() != null && reportDto.getDims().intValue() == SystemConstant.DB.DIM_HOUR){
        	if(DateUtils.getDateSubtract(startDate, endDate) > 0){ //如果是按小时维度返回chart报表，只能选一天的报表
        		throw new BusinessException(StatusCode.SC20507);
        	}
        }
        
        if(reportDto.getRealtime() != SystemConstant.DB.IS_OFFLINE && reportDto.getRealtime() != SystemConstant.DB.IS_REALTIME){
        	throw new BusinessException(StatusCode.SC20504);
        }
	}

	public static ReportCriterion convertToModel(ReportDto dto, ReportCriterion entity) throws ParseException {
		BeanUtils.copyProperties(dto, entity);
        BeanUtils.setCreateParam(entity);
        
		Date startDate = DateUtils.getFormatDateByPattern("yyyyMMdd", dto.getStartDate());
		Date endDate = DateUtils.getFormatDateByPattern("yyyyMMdd", dto.getEndDate());
		if(endDate.after(DateUtils.getCurrentDate())){ //结束日期不能在当天以后
			throw new BusinessException(StatusCode.SC20506);
		} else if(startDate.after(endDate)){ //开始日期不能在结束日期之后
			throw new BusinessException(StatusCode.SC20505);
		}
		
		entity.setStartDate(startDate);
		entity.setEndDate(endDate);
        
        transformDimensions(entity);
		return entity;
	}

	/**
	 * 报表查询的维度的勾选
	 * @param entity
	 */
	private static void transformDimensions(ReportCriterion entity) {
		Integer dims = entity.getDims();
		String dimsStr = StringUtils.convertSingleChoiceToMultiChoice(dims);
		int[] dimsArray = StringUtils.splitToIntArray(dimsStr);
		
		for(int dim: dimsArray){
			switch (dim){
			case SystemConstant.DB.DIM_DATE: 
				entity.setHasDate(true);
				break;
			case SystemConstant.DB.DIM_HOUR: 
				entity.setHasHour(true);
				break;
			case SystemConstant.DB.DIM_MEDIA: 
				entity.setHasMedia(true);
				break;
			case SystemConstant.DB.DIM_ADSPACE: 
				entity.setHasAdspace(true);
				break;
			case SystemConstant.DB.DIM_POLICY: 
				entity.setHasPolicy(true);
				break;
			case SystemConstant.DB.DIM_DSP: 
				entity.setHasDsp(true);
				break;
			default: //do nothing
			}
		}
	}

	public static List<ReportMedia> getPopulatedNullDateAndTime(List<ReportMedia> reportMedias, Integer dims, 
			Date startDate, Date endDate) {
		List<ReportMedia> returnedReportMedias = new ArrayList<ReportMedia>();
		if(reportMedias == null){
			return returnedReportMedias;
		}
		
		int delta = (int) DateUtils.getDateSubtract(startDate, endDate);
		ReportMedia[][] processedReportMedias = new ReportMedia[delta+1][SystemConstant.OtherConstant.ALL_HOURS];
		for(int i=0; i<delta+1; i++){
			for(int j=0; j<SystemConstant.OtherConstant.ALL_HOURS; j++){
				ReportMedia rm = new ReportMedia();
				byte regularHour = (byte) j;
				Date regularDate = org.apache.commons.lang3.time.DateUtils.addDays(startDate, i);
				
				rm.setReqs(0L);
				rm.setBids(0L);
				rm.setClks(0L);
				rm.setImps(0L);
				rm.setErrs(0L);
				rm.setVclks(0L);
				rm.setVimps(0L);
				rm.setIncome(0.0);
				rm.setHour(regularHour);
				rm.setDate(regularDate);
				
				processedReportMedias[i][j] = rm;
			}
		}
		
		for(ReportMedia rm: reportMedias){
			if(dims != null && dims.intValue() == SystemConstant.DB.DIM_DATE){ //仅仅有日期一个维度的情况下
				Date date = rm.getDate();
				if(date != null && (!date.before(startDate) && !date.after(endDate))){
					int dur = (int) DateUtils.getDateSubtract(startDate, date);
					processedReportMedias[dur][0] = rm;
				}
			} else if(dims != null && dims.intValue() == SystemConstant.DB.DIM_HOUR){ //仅仅有小时一个维度的情况下
				int hour = rm.getHour();
				if(hour >= SystemConstant.OtherConstant.HOUR_ZERO && hour < SystemConstant.OtherConstant.ALL_HOURS){
					processedReportMedias[0][hour] = rm;
				}
			}
		}
		
		if(dims != null && dims.intValue() == SystemConstant.DB.DIM_DATE){
			for(int i=0; i<delta+1; i++){
				returnedReportMedias.add(processedReportMedias[i][0]);
			}
		} else if(dims != null && dims.intValue() == SystemConstant.DB.DIM_HOUR){
			for(int j=0; j<SystemConstant.OtherConstant.ALL_HOURS; j++){
				returnedReportMedias.add(processedReportMedias[0][j]);
			}
		}
		
		return returnedReportMedias;
	}
}
