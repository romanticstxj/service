package com.madhouse.platform.premiummad.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.dto.DspDto;
import com.madhouse.platform.premiummad.dto.ResponseDto;
import com.madhouse.platform.premiummad.entity.Dsp;
import com.madhouse.platform.premiummad.entity.DspMedia;
import com.madhouse.platform.premiummad.rule.BaseRule;
import com.madhouse.platform.premiummad.rule.DspRule;
import com.madhouse.platform.premiummad.service.IDspService;
import com.madhouse.platform.premiummad.util.ResponseUtils;
import com.madhouse.platform.premiummad.util.StringUtils;
import com.madhouse.platform.premiummad.validator.Update;
import com.madhouse.platform.premiummad.validator.UpdateStatus;

@RestController
@RequestMapping("/dsp")
public class DspController {
	
	@Autowired
	private IDspService dspService;
	
	@RequestMapping("/list")
    public ResponseDto<DspDto> list(@RequestParam(value="ids", required=false) String ids,
    		@RequestParam(value="status", required=false) Integer status,
    		@RequestParam(value="deliveryType", required=false) Byte deliveryType) throws Exception {
		Dsp dsp = new Dsp(deliveryType, status);
		String[] idStrs = StringUtils.splitToStringArray(ids);
		List<Dsp> dsps = dspService.queryAll(idStrs, dsp);
		List<DspDto> result = DspRule.convertToDtoList(dsps, new ArrayList<DspDto>());
        return ResponseUtils.response(StatusCode.SC20000, result);
    }
	
	/**
	 * 创建dsp
	 * @param dspDto
	 * @return
	 */
	@RequestMapping("/create")
    public ResponseDto<DspDto> addDsp(@RequestBody DspDto dspDto,
    		@RequestHeader(value="X-FROM", required=false) String xFrom) {
		BaseRule.validateDto(dspDto);
        Integer count = dspService.checkName(dspDto.getName().trim());
        if (count > 0) //检查名称
            return ResponseUtils.response(StatusCode.SC20302,null);
        Dsp dsp = DspRule.convertToModel(dspDto, new Dsp(), true);
        dspService.insertWithParamsProcess(dsp, xFrom);
        List<DspDto> result = DspRule.convertToDto(dsp, new DspDto());
        return ResponseUtils.response(StatusCode.SC20000, result);
	}
	
	/**
	 * 获得某个dsp
	 * @param id
	 * @return
	 */
	@RequestMapping("/detail")
	public ResponseDto<DspDto> getDsp(@RequestParam(value="id", required=true) Integer id) {
		Dsp dsp = dspService.queryById(id);
		List<DspDto> result = DspRule.convertToDto(dsp, new DspDto());
        return ResponseUtils.response(StatusCode.SC20000, result);
    }
	
	/**
	 * 更新dsp
	 * @param dspDto
	 * @return
	 */
	@RequestMapping("/update")
    public ResponseDto<DspDto> updateDsp(@RequestBody @Validated(Update.class) DspDto dspDto) {
		BaseRule.validateDto(dspDto);
        Dsp dsp = DspRule.convertToModel(dspDto, new Dsp(), false);
        dspService.update(dsp);
        List<DspDto> result = DspRule.convertToDto(dsp, new DspDto());
        return ResponseUtils.response(StatusCode.SC20000, result);
    }
	
	/**
	 * 更新dsp
	 * @param dspDto
	 * @return
	 */
	@RequestMapping("/updateStatus")
    public ResponseDto<DspDto> updateDspStatus(
    		@RequestBody @Validated(UpdateStatus.class) DspDto dspDto) {
		Dsp dsp = DspRule.convertToModel(dspDto, new Dsp(), false);
        dspService.updateStatus(dsp);
        List<DspDto> result = DspRule.convertToDto(dsp, new DspDto());
        return ResponseUtils.response(StatusCode.SC20000, result);
    }
	
	@RequestMapping("/mediaAuth/update")
    public ResponseDto<DspMedia> updateDspMediaAuth(@RequestBody List<DspMedia> dspAuthDtos){
		List<DspMedia> dspAuths = DspRule.convertToDspAuthModelList(dspAuthDtos, true);
		dspService.updateDspMediaAuth(dspAuths);
    	return ResponseUtils.response(StatusCode.SC20000, dspAuths);
	}
	
	@RequestMapping("/mediaAuth/query")
	public ResponseDto<DspMedia> getDspMediaAuth(@RequestParam Integer dspId) {
		List<DspMedia> dspAuths = dspService.queryDspMediaAuths(dspId);
        return ResponseUtils.response(StatusCode.SC20000, dspAuths);
    }
	
}
