package com.madhouse.platform.premiummad.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.dto.MediaWhiteListDto;
import com.madhouse.platform.premiummad.dto.ResponseDto;
import com.madhouse.platform.premiummad.entity.MediaWhiteList;
import com.madhouse.platform.premiummad.rule.MediaWhiteListRule;
import com.madhouse.platform.premiummad.service.IMediaWhiteListService;
import com.madhouse.platform.premiummad.util.ResponseUtils;
import com.madhouse.platform.premiummad.validator.Insert;
import com.madhouse.platform.premiummad.validator.Update;

@RestController
@RequestMapping("/mediaWhiteList")
public class MediaWhiteListController {
	
	@Autowired
	private IMediaWhiteListService mediaWhiteListService;
	
	@RequestMapping("/list")
	public ResponseDto<MediaWhiteListDto> list(@RequestParam(required=false) Integer mediaCategory){
		List<MediaWhiteList> list = mediaWhiteListService.list(mediaCategory);
		List<MediaWhiteListDto> result = MediaWhiteListRule.convertToDtoList(list, new ArrayList<MediaWhiteListDto>());
		return ResponseUtils.response(StatusCode.SC20000, result);
	}
	
	@RequestMapping("/create")
	public ResponseDto<MediaWhiteListDto> insert(@RequestBody @Validated(Insert.class) MediaWhiteListDto dto){
		MediaWhiteList entity = MediaWhiteListRule.convertToModel(dto, new MediaWhiteList(), true);
		mediaWhiteListService.insert(entity);
		List<MediaWhiteListDto> result = MediaWhiteListRule.convertToDto(entity, new MediaWhiteListDto());
		return ResponseUtils.response(StatusCode.SC20000, result);
	}
	
	@RequestMapping("/delete")
	public ResponseDto<MediaWhiteListDto> delete(@RequestBody @Validated(Update.class) MediaWhiteListDto dto){
		Integer id = dto.getId();
		mediaWhiteListService.delete(id);
		return ResponseUtils.response(StatusCode.SC20000, null);
	}
}
