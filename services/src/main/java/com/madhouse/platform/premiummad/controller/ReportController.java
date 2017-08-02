package com.madhouse.platform.premiummad.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.dto.ReportDto;
import com.madhouse.platform.premiummad.dto.ResponseDto;
import com.madhouse.platform.premiummad.entity.ReportCriterion;
import com.madhouse.platform.premiummad.entity.ReportMedia;
import com.madhouse.platform.premiummad.rule.ReportRule;
import com.madhouse.platform.premiummad.service.IReportService;
import com.madhouse.platform.premiummad.util.ResponseUtils;

@RestController
@RequestMapping("/report")
public class ReportController {
	
	@Autowired
	private IReportService reportService;
	
	@RequestMapping("/media")
    public ResponseDto<ReportMedia> list(@RequestParam Integer type, @RequestParam Integer dims, 
    		@RequestParam Integer realtime, @RequestParam(required=false) Integer mediaId, 
    		@RequestParam String startDate, @RequestParam String endDate) throws Exception {
		ReportDto dto = new ReportDto(type, dims, realtime, mediaId, startDate, endDate);
		ReportRule.validateDto(dto);
		ReportCriterion criterion = ReportRule.convertToModel(dto, new ReportCriterion());
		List<ReportMedia> reportMedias = reportService.queryMediaReport(criterion);
		return ResponseUtils.response(StatusCode.SC20000, reportMedias);
	}
	
}
