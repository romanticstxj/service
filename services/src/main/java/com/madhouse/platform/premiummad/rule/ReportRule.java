package com.madhouse.platform.premiummad.rule;

import java.text.ParseException;
import java.util.Date;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dto.ReportDto;
import com.madhouse.platform.premiummad.entity.ReportCriterion;
import com.madhouse.platform.premiummad.exception.BusinessException;
import com.madhouse.platform.premiummad.util.BeanUtils;
import com.madhouse.platform.premiummad.util.DateUtils;
import com.madhouse.platform.premiummad.util.StringUtils;

public class ReportRule extends BaseRule{
	
	public static void validateDto(ReportDto reportDto){
		String fieldName = BeanUtils.hasEmptyField(reportDto);
        if (fieldName != null)
        	throw new BusinessException(StatusCode.SC20001, fieldName + " cannot be null");
        
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
	
}
