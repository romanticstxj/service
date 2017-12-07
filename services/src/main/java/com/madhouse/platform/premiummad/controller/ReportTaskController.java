package com.madhouse.platform.premiummad.controller;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import com.madhouse.platform.premiummad.util.CsvUtil;
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
    public ResponseEntity<byte[]> download(@RequestParam Integer id) throws Exception {
		 String filename = "download.csv";

	        ByteArrayOutputStream baos= CsvUtil.process();
	        baos.close();

	        HttpHeaders headers = new HttpHeaders();  
	        //下载显示的文件名，解决中文名称乱码问题  
	        String downloadFielName = new String(filename.getBytes("UTF-8"),"UTF-8");
	        //通知浏览器以attachment（下载方式）打开图片
	        headers.setContentDispositionFormData("attachment", downloadFielName); 
	        //application/octet-stream ： 二进制流数据（最常见的文件下载）。
	        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	        return new ResponseEntity<byte[]>(baos.toByteArray(),    
	                      headers, HttpStatus.CREATED);
    }
    
}
