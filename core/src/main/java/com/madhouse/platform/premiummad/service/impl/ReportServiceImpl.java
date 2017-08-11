package com.madhouse.platform.premiummad.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dao.ReportDspDao;
import com.madhouse.platform.premiummad.dao.ReportMediaDao;
import com.madhouse.platform.premiummad.dao.ReportPolicyDao;
import com.madhouse.platform.premiummad.entity.ReportCriterion;
import com.madhouse.platform.premiummad.entity.ReportDsp;
import com.madhouse.platform.premiummad.entity.ReportMedia;
import com.madhouse.platform.premiummad.entity.ReportPolicy;
import com.madhouse.platform.premiummad.exception.BusinessException;
import com.madhouse.platform.premiummad.service.IReportService;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class ReportServiceImpl implements IReportService {
	
	@Autowired 
	ReportMediaDao reportMediaDao;
	
	@Autowired 
	ReportDspDao reportDspDao;
	
	@Autowired 
	ReportPolicyDao reportPolicyDao;

	@Override
	public List<ReportMedia> queryMediaReport(ReportCriterion reportCriterion) {
		Integer realtime = reportCriterion.getRealtime();
		Integer type = reportCriterion.getType();
		List<ReportMedia> reportMedias = null;
		if(realtime.intValue() == SystemConstant.DB.IS_OFFLINE){ //根据实时与否决定查看离线或实时报表
			if(type.intValue() == SystemConstant.DB.TYPE_DEFAULT){ //根据查询类型决定查询哪种离线报表
				reportMedias = reportMediaDao.queryMediaReport(reportCriterion);
			} else if (type.intValue() == SystemConstant.DB.TYPE_LOCATION){
				reportMedias = reportMediaDao.queryMediaLocationReport(reportCriterion);
			}
		} else if(realtime.intValue() == SystemConstant.DB.IS_REALTIME){
			reportMedias = reportMediaDao.queryRtMediaReport(reportCriterion);
		}
		
		if(reportMedias == null){ //查询参数错误，没查任何结果
			throw new BusinessException(StatusCode.SC20501);
		}
		return reportMedias;
	}

	@Override
	public List<ReportDsp> queryDspReport(ReportCriterion reportCriterion) {
		Integer realtime = reportCriterion.getRealtime();
		Integer type = reportCriterion.getType();
		List<ReportDsp> reportDsps = null;
		if(realtime.intValue() == SystemConstant.DB.IS_OFFLINE){ //根据实时与否决定查看离线或实时报表
			if(type.intValue() == SystemConstant.DB.TYPE_DEFAULT){ //根据查询类型决定查询哪种离线报表
				reportDsps = reportDspDao.queryDspReport(reportCriterion);
			} else if (type.intValue() == SystemConstant.DB.TYPE_LOCATION){
				reportDsps = reportDspDao.queryDspLocationReport(reportCriterion);
			} else if (type.intValue() == SystemConstant.DB.TYPE_MEDIA){
				reportDsps = reportDspDao.queryDspMediaReport(reportCriterion);
			}
		} else if(realtime.intValue() == SystemConstant.DB.IS_REALTIME){
			reportDsps = reportDspDao.queryRtDspReport(reportCriterion);
		}
		return reportDsps;
	}

	@Override
	public List<ReportPolicy> queryPolicyReport(ReportCriterion reportCriterion) {
		Integer realtime = reportCriterion.getRealtime();
		Integer type = reportCriterion.getType();
		List<ReportPolicy> reportPolicyies = null;
		if(realtime.intValue() == SystemConstant.DB.IS_OFFLINE){ //根据实时与否决定查看离线或实时报表
			if(type.intValue() == SystemConstant.DB.TYPE_DEFAULT){ //根据查询类型决定查询哪种离线报表
				reportPolicyies = reportPolicyDao.queryPolicyReport(reportCriterion);
			} else if (type.intValue() == SystemConstant.DB.TYPE_LOCATION){
				reportPolicyies = reportPolicyDao.queryPolicyLocationReport(reportCriterion);
			} else if (type.intValue() == SystemConstant.DB.TYPE_MEDIA){
				reportPolicyies = reportPolicyDao.queryPolicyMediaReport(reportCriterion);
			}
		} else if(realtime.intValue() == SystemConstant.DB.IS_REALTIME){
			reportPolicyies = reportPolicyDao.queryRtPolicyReport(reportCriterion);
		}
		return reportPolicyies;
	}

}
