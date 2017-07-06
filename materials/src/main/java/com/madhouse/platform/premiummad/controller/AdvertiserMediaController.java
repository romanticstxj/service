package com.madhouse.platform.premiummad.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.madhouse.platform.premiummad.annotation.TokenFilter;
import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.dto.AdvertiserMediaAuditResultDto;
import com.madhouse.platform.premiummad.dto.AdvertiserMediaDto;
import com.madhouse.platform.premiummad.dto.ResponseDto;
import com.madhouse.platform.premiummad.model.AdvertiserMediaAuditResultModel;
import com.madhouse.platform.premiummad.service.IAdvertiserMediaService;
import com.madhouse.platform.premiummad.util.BeanUtils;
import com.madhouse.platform.premiummad.util.ResponseUtils;

@RestController
@RequestMapping("/advertiser")
public class AdvertiserMediaController {
	
	@Autowired
	private IAdvertiserMediaService advertiserMediaService;
	
	/**
     * DSP端提交广告主
     * @return advertiserDto
	 * @throws Exception 
     */
	@TokenFilter
	@RequestMapping("/upload")
    public ResponseDto<AdvertiserMediaDto> list(@RequestBody AdvertiserMediaDto advertiserDto, @RequestParam(value = "dspId") String dspId, @RequestParam(value = "token") String token) throws Exception {
		// TODO
        return null;
    }
	
	/**
     * DSP端查询广告主审核状态
     * @param ids DSP平台定义的广告主 ID ，多个 广告主 ID 可用半角 【,】间隔；
     * @return advertiserAuditResultDtos
	 * @throws Exception 
     */
	@TokenFilter
	@RequestMapping("/status")
	public ResponseDto<AdvertiserMediaAuditResultDto> list(@RequestParam(value = "ids", required = false) String ids, @RequestParam(value = "dspId") String dspId, @RequestParam(value = "token") String token) throws Exception {
		List<AdvertiserMediaAuditResultModel> modelResults = advertiserMediaService.getAdvertiserMediaAuditResult(ids);
		List<AdvertiserMediaAuditResultDto> dtoResults = new ArrayList<AdvertiserMediaAuditResultDto>();
		BeanUtils.copyList(modelResults, dtoResults, AdvertiserMediaAuditResultDto.class);
		return ResponseUtils.response(StatusCode.SC20000, dtoResults);
	}
}
