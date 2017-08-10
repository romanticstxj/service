package com.madhouse.platform.premiummad.controller;

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
	
	@RequestMapping("/media")
    public ResponseDto<ReportMedia> listMedia(@RequestParam Integer type, @RequestParam Integer dims, 
    		@RequestParam Integer realtime, @RequestParam(required=false) Integer mediaId, 
    		@RequestParam String startDate, @RequestParam String endDate,
    		@RequestHeader(value="X-User-Id", required=false) Integer userId) throws Exception {
		List<Integer> mediaIdList = userAuthService.queryMediaIdList(userId, null);
//		String returnedMediaIds = StringUtils.getIdsStr(mediaIdList);
		ReportDto dto = new ReportDto(type, dims, realtime, mediaId, startDate, endDate, mediaIdList, null);
		return queryMediaReport(dto);
    }
	
    private ResponseDto<ReportMedia> queryMediaReport(ReportDto dto) throws Exception {
    	List<Integer> mediaIds = dto.getMediaIds();
    	//无权限查看任何媒体
		if(ObjectUtils.isEmpty(mediaIds)){
	        return ResponseUtils.response(StatusCode.SC20006, null);
		} else{ // admin权限，查询所有媒体;非admin，有部分媒体权限
			if(isAdmin(mediaIds)){ //如果是管理员
				mediaIds.clear();
			}
			ReportRule.validateDto(dto);
			ReportCriterion criterion = ReportRule.convertToModel(dto, new ReportCriterion());
			List<ReportMedia> reportMedias = reportService.queryMediaReport(criterion);
			return ResponseUtils.response(StatusCode.SC20000, reportMedias);
		}
	}
    
    @RequestMapping("/dsp")
    public ResponseDto<ReportDsp> listDsp(@RequestParam Integer type, @RequestParam Integer dims, 
    		@RequestParam Integer realtime, @RequestParam String startDate, 
    		@RequestParam String endDate, @RequestHeader(value="X-User-Id", required=false) Integer userId) throws Exception {
		List<Integer> mediaIdList = userAuthService.queryMediaIdList(userId, null);
//		String returnedMediaIds = StringUtils.getIdsStr(mediaIdList);
		ReportDto dto = new ReportDto(type, dims, realtime, null, startDate, endDate, mediaIdList, null);
		return queryDspReport(dto);
    }
	
    private ResponseDto<ReportDsp> queryDspReport(ReportDto dto) throws Exception {
    	List<Integer> mediaIds = dto.getMediaIds();
    	//无权限查看任何媒体
		if(ObjectUtils.isEmpty(mediaIds)){
	        return ResponseUtils.response(StatusCode.SC20006, null);
		} else{ // admin权限，查询所有媒体;非admin，有部分媒体权限
			if(isAdmin(mediaIds)){ //如果是管理员
				mediaIds.clear();
			}
			ReportRule.validateDto(dto);
			ReportCriterion criterion = ReportRule.convertToModel(dto, new ReportCriterion());
			List<ReportDsp> reportDsps = reportService.queryDspReport(criterion);
			return ResponseUtils.response(StatusCode.SC20000, reportDsps);
		}
		
	}
    
    @RequestMapping("/policy")
    public ResponseDto<ReportPolicy> listPolicy(@RequestParam Integer type, @RequestParam Integer dims, 
    		@RequestParam Integer realtime, @RequestParam String startDate, 
    		@RequestParam String endDate, @RequestHeader(value="X-User-Id", required=false) Integer userId) throws Exception {
		List<Integer> mediaIdList = userAuthService.queryMediaIdList(userId, null);
		List<Integer> policyList = userAuthService.queryPolicyIdList(userId, null);
		ReportDto dto = new ReportDto(type, dims, realtime, null, startDate, endDate, mediaIdList, policyList);
		return queryPolicyReport(dto);
    }
    
    private ResponseDto<ReportPolicy> queryPolicyReport(ReportDto dto) throws Exception {
    	List<Integer> mediaIds = dto.getMediaIds();
    	List<Integer> policyIds = dto.getPolicyIds();
    	//无权限查看任何媒体
		if(ObjectUtils.isEmpty(mediaIds) || ObjectUtils.isEmpty(policyIds)){
	        return ResponseUtils.response(StatusCode.SC20006, null);
		} else{ // admin权限，查询所有媒体;非admin，有部分媒体权限
			if(isAdmin(mediaIds)){ //如果是媒体管理员
				mediaIds.clear();
			}
			if(isAdmin(policyIds)){ //如果是策略管理员
				policyIds.clear();
			}
			ReportRule.validateDto(dto);
			ReportCriterion criterion = ReportRule.convertToModel(dto, new ReportCriterion());
			List<ReportPolicy> reportPolicies = reportService.queryPolicyReport(criterion);
			return ResponseUtils.response(StatusCode.SC20000, reportPolicies);
		}
	}
    
    private boolean isAdmin(List<Integer> ids){
    	return ids.size() == 1 && 
    			ids.get(0).toString().equals(SystemConstant.OtherConstant.SYSTEM_ADMIN_MEDIA_ID);
    }
	
}
