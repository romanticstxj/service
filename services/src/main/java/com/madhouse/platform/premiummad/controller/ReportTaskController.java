package com.madhouse.platform.premiummad.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dto.ReportTaskDto;
import com.madhouse.platform.premiummad.dto.ResponseDto;
import com.madhouse.platform.premiummad.entity.ReportTask;
import com.madhouse.platform.premiummad.exception.BusinessException;
import com.madhouse.platform.premiummad.rule.ReportTaskRule;
import com.madhouse.platform.premiummad.service.IReportTaskService;
import com.madhouse.platform.premiummad.util.ResponseUtils;

@RestController
@RequestMapping("/reportTask")
public class ReportTaskController {
	
	@Autowired
	private IReportTaskService reportTaskService;
	
	@RequestMapping("/create")
    public ResponseDto<Void> create(@RequestBody ReportTaskDto reportTaskDto) throws Exception {
		ReportTask reportTask = ReportTaskRule.convertToModel(reportTaskDto, new ReportTask(), true);
		reportTaskService.insert(reportTask);
		return ResponseUtils.response(StatusCode.SC20000, null);
    }
	
	@RequestMapping("/list")
    public ResponseDto<ReportTaskDto> list(@RequestHeader("X-User-Id") Integer userId) throws Exception {
		if(userId == null){
			throw new BusinessException(StatusCode.SC20009);
		}
		List<ReportTask> entities = reportTaskService.queryList(
				null, userId, SystemConstant.DB.ORDER_BY_DESC);
		List<ReportTaskDto> dtos = ReportTaskRule.convertToDtoList(entities, new ArrayList<ReportTaskDto>());
		return ResponseUtils.response(StatusCode.SC20000, dtos);
    }
	
	
	@RequestMapping("/download")
    public ResponseEntity<byte[]> download(@RequestParam Integer id,
    		@RequestHeader(value="X-User-Id", required=false) Integer userId,
    		@RequestParam(value="userId", required=false) Integer userIdByGet) throws Exception {
    	//获得userId，可以从url中获得（方便通过get请求获取数据），更为一般的是从requestHeader里获取
		if(userIdByGet != null){ //优先获取get请求的userId参数
			userId = userIdByGet;
		}
    	ReportTask rt = reportTaskService.queryById(id, userId);
    	
    	String reportUri = rt.getReportUri();
    	ResponseEntity<byte[]> result = reportTaskService.download(reportUri);
	    return result;
    }
    
}
