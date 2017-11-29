package com.madhouse.platform.premiummad.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.dto.RequestBlockDto;
import com.madhouse.platform.premiummad.dto.ResponseDto;
import com.madhouse.platform.premiummad.entity.RequestBlock;
import com.madhouse.platform.premiummad.rule.RequestBlockRule;
import com.madhouse.platform.premiummad.service.IRequestBlockService;
import com.madhouse.platform.premiummad.util.ResponseUtils;

@RestController
@RequestMapping("/reqBlock")
public class RequestBlockController {
	
	@Autowired
	private IRequestBlockService requestBlockService;
	
	@RequestMapping("/list")
	public ResponseDto<RequestBlockDto> list(@RequestParam(required=false) Integer type){
		List<RequestBlock> list = requestBlockService.list(type);
		List<RequestBlockDto> result = RequestBlockRule.convertToDtoList(list, new ArrayList<RequestBlockDto>());
		return ResponseUtils.response(StatusCode.SC20000, result);
	}
	
	@RequestMapping("/create")
	public ResponseDto<RequestBlockDto> insert(@RequestBody RequestBlockDto dto){
		RequestBlock entity = RequestBlockRule.convertToModel(dto, new RequestBlock(), true);
		requestBlockService.insert(entity);
		List<RequestBlockDto> result = RequestBlockRule.convertToDto(entity, new RequestBlockDto());
		return ResponseUtils.response(StatusCode.SC20000, result);
	}
	
	@RequestMapping("/updateStatus")
	public ResponseDto<RequestBlockDto> updateStatus(@RequestBody RequestBlockDto dto){
		RequestBlock entity = RequestBlockRule.convertToModel(dto, new RequestBlock(), false);
		requestBlockService.updateStatus(entity);
		List<RequestBlockDto> result = RequestBlockRule.convertToDto(entity, new RequestBlockDto());
		return ResponseUtils.response(StatusCode.SC20000, result);
	}
	
}
