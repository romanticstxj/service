package com.madhouse.platform.premiummad.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dto.DictDto;
import com.madhouse.platform.premiummad.dto.ResponseDto;
import com.madhouse.platform.premiummad.entity.Dict;
import com.madhouse.platform.premiummad.service.IDictService;
import com.madhouse.platform.premiummad.util.BeanUtils;
import com.madhouse.platform.premiummad.util.ResponseUtils;

@RestController
@RequestMapping("/dict")
public class DictionaryController {
	
	@Autowired
	private IDictService dictService;
	
	@RequestMapping(value="/list", method = RequestMethod.GET)
    public ResponseDto<DictDto> list(@RequestParam(value="type", required=true) Integer type,
    		@RequestParam(value="terminalType", required=false) Integer terminalType,
    		@RequestParam(value="adType", required=false) Integer adType) throws Exception {
		
		ResponseDto<DictDto> result = null;
		
		switch(type){
			case SystemConstant.DICT_MEDIA_CATEGORY: //媒体分类
				result = listMediaCategories();
				break;
			case SystemConstant.DICT_ADSPACE_LAYOUT: //广告位形式
				result = listAdspaceLayout(terminalType, adType);
				break;
			default: 
				result = ResponseUtils.response(StatusCode.SC20001, null);
		}
		
		return result;
    }
	
	private ResponseDto<DictDto> listMediaCategories(){
		
		List<Dict> dicts = dictService.queryAllMediaCategories();
		List<DictDto> dictDtos = new ArrayList<>();
		BeanUtils.copyList(dicts,dictDtos,DictDto.class);
		return ResponseUtils.response(StatusCode.SC20000,dictDtos);
	}
	
	private ResponseDto<DictDto> listAdspaceLayout(Integer terminalType, Integer adType) {
		Dict dict = new Dict();
		dict.setTerminalType(terminalType);
		dict.setAdType(adType);
		List<Dict> dicts = dictService.queryAllAdspaceLayout(dict);
		List<DictDto> dictDtos = new ArrayList<>();
		BeanUtils.copyList(dicts,dictDtos,DictDto.class);
		return ResponseUtils.response(StatusCode.SC20000,dictDtos);
	}
}
