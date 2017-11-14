package com.madhouse.platform.premiummad.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dto.ReportDto;
import com.madhouse.platform.premiummad.dto.ResponseDto;
import com.madhouse.platform.premiummad.entity.ReportCriterion;
import com.madhouse.platform.premiummad.entity.ReportDsp;
import com.madhouse.platform.premiummad.entity.ReportMedia;
import com.madhouse.platform.premiummad.entity.ReportPolicy;
import com.madhouse.platform.premiummad.rule.ReportRule;
import com.madhouse.platform.premiummad.service.IReportService;
import com.madhouse.platform.premiummad.service.IUserAuthService;
import com.madhouse.platform.premiummad.util.ObjectUtils;
import com.madhouse.platform.premiummad.util.ResponseUtils;

@RestController
@RequestMapping("/report")
public class ReportController {
	
	@Autowired
	private IReportService reportService;
	
	@Autowired
    private IUserAuthService userAuthService;
	
	@RequestMapping("/mediaDashboard")
	public ResponseDto<ReportMedia> listMediaDashboard(@RequestParam Integer dims, @RequestParam Integer realtime,
    		@RequestParam String startDate, @RequestParam String endDate,
    		@RequestHeader(value="X-User-Id", required=false) Integer userId) throws Exception {
		List<Integer> mediaIdList = userAuthService.queryMediaIdList(userId, null);
		ReportDto dto = new ReportDto.ReportDtoBuilder(SystemConstant.DB.TYPE_DEFAULT, dims, realtime, startDate, endDate)
				.mediaIds(mediaIdList).createNewReportDto();
		return queryMediaReportDashboard(dto);
    }
	
	private ResponseDto<ReportMedia> queryMediaReportDashboard(ReportDto dto) throws Exception {
    	List<Integer> mediaIds = dto.getMediaIds();
    	//无权限查看任何媒体
		if(ObjectUtils.isEmpty(mediaIds)){
	        return ResponseUtils.response(StatusCode.SC20000, new ArrayList<ReportMedia>());
		} else{ // admin权限，查询所有媒体;非admin，有部分媒体权限
			ReportRule.validateDashboardReportDto(dto);
			ReportCriterion criterion = ReportRule.convertToModel(dto, new ReportCriterion());
			List<ReportMedia> reportMedias = reportService.queryMediaReportDashboard(criterion);
			List<ReportMedia> result =ReportRule.getPopulatedNullDateAndTime(reportMedias, criterion.getDims(), 
					criterion.getStartDate(), criterion.getEndDate());
			return ResponseUtils.response(StatusCode.SC20000, result);
		}
	}
	
	@RequestMapping("/media")
    public ResponseDto<ReportMedia> listMedia(@RequestParam Integer type, @RequestParam Integer dims, 
    		@RequestParam Integer realtime, @RequestParam(required=false) Integer mediaId, 
    		@RequestParam String startDate, @RequestParam String endDate,
    		@RequestHeader(value="X-User-Id", required=false) Integer userId) throws Exception {
		List<Integer> mediaIdList = userAuthService.queryMediaIdList(userId, null);
		ReportDto dto = new ReportDto.ReportDtoBuilder(type, dims, realtime, startDate, endDate)
				.mediaId(mediaId).mediaIds(mediaIdList).createNewReportDto();
		return queryMediaReport(dto);
    }
	
    private ResponseDto<ReportMedia> queryMediaReport(ReportDto dto) throws Exception {
    	List<Integer> mediaIds = dto.getMediaIds();
    	//无权限查看任何媒体
		if(ObjectUtils.isEmpty(mediaIds)){
	        return ResponseUtils.response(StatusCode.SC20000, new ArrayList<ReportMedia>());
		} else{ // admin权限，查询所有媒体;非admin，有部分媒体权限
			ReportRule.validateDto(dto);
			ReportCriterion criterion = ReportRule.convertToModel(dto, new ReportCriterion());
			List<ReportMedia> reportMedias = reportService.queryMediaReport(criterion);
			return ResponseUtils.response(StatusCode.SC20000, reportMedias);
		}
	}
    
    @RequestMapping("/dsp")
    public ResponseDto<ReportDsp> listDsp(@RequestParam Integer type, @RequestParam Integer dims, 
    		@RequestParam Integer realtime, @RequestParam String startDate, @RequestParam Integer dspId,
    		@RequestParam String endDate, @RequestHeader(value="X-User-Id", required=false) Integer userId) throws Exception {
		List<Integer> mediaIdList = userAuthService.queryMediaIdList(userId, null);
		ReportDto dto = new ReportDto.ReportDtoBuilder(type, dims, realtime, startDate, endDate)
				.dspId(dspId).mediaIds(mediaIdList).createNewReportDto();
		return queryDspReport(dto);
    }
	
    private ResponseDto<ReportDsp> queryDspReport(ReportDto dto) throws Exception {
    	List<Integer> mediaIds = dto.getMediaIds();
    	//无权限查看任何媒体
		if(ObjectUtils.isEmpty(mediaIds)){
	        return ResponseUtils.response(StatusCode.SC20000, new ArrayList<ReportDsp>());
		} else{ // admin权限，查询所有媒体;非admin，有部分媒体权限
			ReportRule.validateDto(dto);
			ReportCriterion criterion = ReportRule.convertToModel(dto, new ReportCriterion());
			List<ReportDsp> reportDsps = reportService.queryDspReport(criterion);
			return ResponseUtils.response(StatusCode.SC20000, reportDsps);
		}
		
	}
    
    @RequestMapping("/policy")
    public ResponseDto<ReportPolicy> listPolicy(@RequestParam Integer type, @RequestParam Integer dims, 
    		@RequestParam Integer realtime, @RequestParam String startDate, @RequestParam Integer policyId,
    		@RequestParam String endDate, @RequestHeader(value="X-User-Id", required=false) Integer userId) throws Exception {
		List<Integer> mediaIdList = userAuthService.queryMediaIdList(userId, null);
		List<Integer> policyList = userAuthService.queryPolicyIdList(userId, null);
		ReportDto dto = new ReportDto.ReportDtoBuilder(type, dims, realtime, startDate, endDate)
				.policyId(policyId).mediaIds(mediaIdList).policyIds(policyList).createNewReportDto();
		return queryPolicyReport(dto);
    }
    
    private ResponseDto<ReportPolicy> queryPolicyReport(ReportDto dto) throws Exception {
    	List<Integer> mediaIds = dto.getMediaIds();
    	List<Integer> policyIds = dto.getPolicyIds();
    	//无权限查看任何媒体
		if(ObjectUtils.isEmpty(mediaIds) || ObjectUtils.isEmpty(policyIds)){
	        return ResponseUtils.response(StatusCode.SC20000, new ArrayList<ReportPolicy>());
		} else{ // admin权限，查询所有媒体;非admin，有部分媒体权限
			ReportRule.validateDto(dto);
			ReportCriterion criterion = ReportRule.convertToModel(dto, new ReportCriterion());
			List<ReportPolicy> reportPolicies = reportService.queryPolicyReport(criterion);
			return ResponseUtils.response(StatusCode.SC20000, reportPolicies);
		}
	}
    
}
