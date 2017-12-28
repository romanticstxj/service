package com.madhouse.platform.premiummad.rule;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

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
		BaseRule.validateDto(reportDto);
        
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
		BaseRule.validateDto(reportDto);
        
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
		TreeSet<Integer> dimsSorting = new TreeSet<Integer>();
		
		/** dimsSorting列表的元素会按照既定的维度顺序，设置相应的维度，比如policy规定始终是顶层维度，时间规定始终是最细级别的维度,
		 * 此处会根据前端选择的报表type和dims来设置这个报表分组和排序的维度顺序 **/
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
		
		//根据查询类型设置额外维度
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
				//type为媒体，则只添加媒体这个维度，以备从媒体维度向更细的广告位维度切换
				entity.setHasMedia(true);
				dimsSorting.add(SystemConstant.DB.DIM_SORTING_MEDIA);
				break;
			case SystemConstant.DB.TYPE_CAMPAIGN: 
				//type为活动，则在排序规则里添加活动这个列
				dimsSorting.add(SystemConstant.DB.DIM_SORTING_CAMPAIGN);
				break;
			default:
		}
		
		entity.setLastOrderPosition(dimsSorting.last());
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
