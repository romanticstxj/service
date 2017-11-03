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
import com.madhouse.platform.premiummad.dto.AdvertiserAuditResultDto;
import com.madhouse.platform.premiummad.dto.AdvertiserDto;
import com.madhouse.platform.premiummad.dto.ResponseDto;
import com.madhouse.platform.premiummad.model.AdvertiserAuditResultModel;
import com.madhouse.platform.premiummad.model.AdvertiserModel;
import com.madhouse.platform.premiummad.service.IAdvertiserService;
import com.madhouse.platform.premiummad.util.BeanUtils;
import com.madhouse.platform.premiummad.util.ResponseUtils;

@RestController
@RequestMapping("/advertiser")
public class AdvertiserController {

	@Autowired
	private IAdvertiserService advertiserService;

	/**
	 * DSP端提交广告主
	 * 
	 * @param advertiserDto
	 * @param dspId
	 * @param token
	 * @return
	 * @throws Exception
	 */
	@TokenFilter
	@RequestMapping("/upload")
	public ResponseDto<Void> upload(@RequestBody AdvertiserDto advertiserDto, @RequestParam(value = "dspId") String dspId, @RequestParam(value = "token") String token) throws Exception {
		AdvertiserModel entity = new AdvertiserModel();
		BeanUtils.copyProperties(advertiserDto, entity);
		entity.setDspId(dspId); // 广告主所属DSP
		advertiserService.upload(entity);
		return ResponseUtils.response(StatusCode.SC200);
	}

	/**
	 * DSP端查询广告主审核状态
	 * 
	 * @param id
	 *            DSP平台定义的广告主 ID ，多个 广告主 ID 可用半角 【,】间隔；
	 * @param dspId
	 * @param token
	 * @return
	 * @throws Exception
	 */
	@TokenFilter
	@RequestMapping("/status")
	public ResponseDto<AdvertiserAuditResultDto> list(@RequestParam(value = "id", required = false) String id, @RequestParam(value = "dspId") String dspId, @RequestParam(value = "token") String token) throws Exception {
		List<AdvertiserAuditResultModel> modelResults = advertiserService.getAdvertiserAuditResult(id, dspId);
		List<AdvertiserAuditResultDto> dtoResults = new ArrayList<AdvertiserAuditResultDto>();
		BeanUtils.copyList(modelResults, dtoResults, AdvertiserAuditResultDto.class);
		return ResponseUtils.response(StatusCode.SC200, dtoResults);
	}
}
