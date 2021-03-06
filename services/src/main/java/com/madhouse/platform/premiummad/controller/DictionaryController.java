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
import com.madhouse.platform.premiummad.dto.LocationDto;
import com.madhouse.platform.premiummad.dto.ResponseDto;
import com.madhouse.platform.premiummad.entity.Dict;
import com.madhouse.platform.premiummad.entity.Location;
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
			case SystemConstant.OtherConstant.DICT_MEDIA_CATEGORY: //媒体分类
				result = listMediaCategories();
				break;
			case SystemConstant.OtherConstant.DICT_ADSPACE_LAYOUT: //广告位形式
				result = listAdspaceLayouts(terminalType, adType);
				break;
			case SystemConstant.OtherConstant.DICT_REQUEST_BLOCK_TYPE: //异常流量类型
				result = listRequestBlockTypes();
				break;	
			default: 
				result = ResponseUtils.response(StatusCode.SC20002, null);
		}
		
		return result;
    }
	
	@RequestMapping(value="/list/location", method = RequestMethod.GET)
    public ResponseDto<LocationDto> list() throws Exception {
		ResponseDto<LocationDto> result = listLocations();
		return result;
    }
	
	private ResponseDto<LocationDto> listLocations() {
		List<Location> dicts = dictService.queryAllLocations();
		List<LocationDto> dictDtos = new ArrayList<LocationDto>();
		BeanUtils.copyList(dicts,dictDtos,LocationDto.class);
		return ResponseUtils.response(StatusCode.SC20000, dictDtos);
	}

	private ResponseDto<DictDto> listMediaCategories(){
		List<Dict> dicts = dictService.queryAllMediaCategories();
		List<DictDto> dictDtos = new ArrayList<>();
		BeanUtils.copyList(dicts,dictDtos,DictDto.class);
		return ResponseUtils.response(StatusCode.SC20000,dictDtos);
	}
	
	private ResponseDto<DictDto> listAdspaceLayouts(Integer terminalType, Integer adType) {
		Dict dict = new Dict();
		dict.setTerminalType(terminalType);
		dict.setAdType(adType);
		List<Dict> dicts = dictService.queryAllAdspaceLayout(dict);
		List<DictDto> dictDtos = new ArrayList<>();
		BeanUtils.copyList(dicts,dictDtos,DictDto.class);
		return ResponseUtils.response(StatusCode.SC20000,dictDtos);
	}
	
	private ResponseDto<DictDto> listRequestBlockTypes() {
		List<Dict> dicts = dictService.queryAllReqBlockTypeList();
		List<DictDto> dictDtos = new ArrayList<>();
		BeanUtils.copyList(dicts,dictDtos,DictDto.class);
		return ResponseUtils.response(StatusCode.SC20000,dictDtos);
	}
}
