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
import com.madhouse.platform.premiummad.dto.MaterialMediaAuditResultDto;
import com.madhouse.platform.premiummad.dto.MaterialMediaDto;
import com.madhouse.platform.premiummad.dto.MonitorDto;
import com.madhouse.platform.premiummad.dto.ResponseDto;
import com.madhouse.platform.premiummad.dto.TrackDto;
import com.madhouse.platform.premiummad.model.MaterialMediaAuditResultModel;
import com.madhouse.platform.premiummad.model.MaterialMediaModel;
import com.madhouse.platform.premiummad.model.MonitorModel;
import com.madhouse.platform.premiummad.model.OperationResultModel;
import com.madhouse.platform.premiummad.model.TrackModel;
import com.madhouse.platform.premiummad.service.impl.MaterialMediaServiceImpl;
import com.madhouse.platform.premiummad.util.BeanUtils;
import com.madhouse.platform.premiummad.util.ResponseUtils;

@RestController
@RequestMapping("/material")
public class MaterialMediaController {
	
	@Autowired
	private MaterialMediaServiceImpl materialMediaService;
	
	/**
     * DSP端提交素材
     * @return materialDto
	 * @throws Exception 
     */
	@TokenFilter
	@RequestMapping("/upload")
	public ResponseDto<Void> list(@RequestBody MaterialMediaDto materialDto, @RequestParam(value = "dspId") String dspId, @RequestParam(value = "token") String token) throws Exception {
		MaterialMediaModel entity = convert(materialDto);
		OperationResultModel result = materialMediaService.upload(entity);
		if (result.isSuccessful()) {
			return ResponseUtils.response(StatusCode.SC20000);
		} else {
			return ResponseUtils.response(StatusCode.SC410002, result.getErrorMessage());
		}
	}
	
	/**
     * DSP端查询素材审核状态
     * @param ids DSP平台定义的素材 ID ，多个素材 ID 可用半角 【,】间隔；
     * @return materialAuditResultDto
	 * @throws Exception 
     */
	@TokenFilter
	@RequestMapping("/status")
	public ResponseDto<MaterialMediaAuditResultDto> list(@RequestParam(value = "ids", required = false) String ids, @RequestParam(value = "dspId") String dspId, @RequestParam(value = "token") String token) throws Exception {
		List<MaterialMediaAuditResultModel> modelResults = materialMediaService.getMaterialMediaAuditResult(ids);
		List<MaterialMediaAuditResultDto> dtoResults = new ArrayList<MaterialMediaAuditResultDto>();
		BeanUtils.copyList(modelResults, dtoResults, MaterialMediaAuditResultDto.class);
		return ResponseUtils.response(StatusCode.SC20000, dtoResults);
	}
	
	/**
	 * 上传素材对象转换
	 * @param materialDto
	 * @return
	 */
	private MaterialMediaModel convert(MaterialMediaDto materialDto) {
		MaterialMediaModel entity = new MaterialMediaModel();
		MonitorDto monitorDto = materialDto.getMonitor();
		List<TrackDto> trackDtos = monitorDto.getImpUrls();

		// 主信息转换
		BeanUtils.copyProperties(materialDto, entity);

		// 广告监测信息转换
		MonitorModel monitorModel = new MonitorModel();
		BeanUtils.copyProperties(monitorDto, monitorModel);
		entity.setMonitor(monitorModel);

		// 展示监测信息URL
		List<TrackModel> trackModels = new ArrayList<TrackModel>();
		BeanUtils.copyList(trackDtos, trackModels, TrackModel.class);
		monitorModel.setImpUrls(trackModels);

		return entity;
	}
}
