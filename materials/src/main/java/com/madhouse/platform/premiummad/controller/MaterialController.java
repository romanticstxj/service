package com.madhouse.platform.premiummad.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.madhouse.platform.premiummad.annotation.TokenFilter;
import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.dto.MaterialAuditResultDto;
import com.madhouse.platform.premiummad.dto.MaterialDto;
import com.madhouse.platform.premiummad.dto.MonitorDto;
import com.madhouse.platform.premiummad.dto.ResponseDto;
import com.madhouse.platform.premiummad.dto.TrackDto;
import com.madhouse.platform.premiummad.model.MaterialAuditResultModel;
import com.madhouse.platform.premiummad.model.MaterialModel;
import com.madhouse.platform.premiummad.model.MonitorModel;
import com.madhouse.platform.premiummad.model.TrackModel;
import com.madhouse.platform.premiummad.service.impl.MaterialServiceImpl;
import com.madhouse.platform.premiummad.util.BeanUtils;
import com.madhouse.platform.premiummad.util.ResponseUtils;

@RestController
@RequestMapping("/material")
public class MaterialController {

	@Autowired
	private MaterialServiceImpl materialService;

	/**
	 * DSP端提交素材
	 * 
	 * @return materialDto
	 * @throws Exception
	 */
	@TokenFilter
	@RequestMapping("/upload")
	public ResponseDto<Void> list(@RequestBody MaterialDto materialDto, @RequestParam(value = "dspId") String dspId, @RequestParam(value = "token") String token) throws Exception {
		MaterialModel entity = convert(materialDto);
		entity.setDspId(dspId);
		materialService.upload(entity);
		return ResponseUtils.response(StatusCode.SC200);
	}

	/**
	 * DSP端查询素材审核状态
	 * 
	 * @param ids
	 *            DSP平台定义的素材 ID ，多个素材 ID 可用半角 【,】间隔；
	 * @return materialAuditResultDto
	 * @throws Exception
	 */
	@TokenFilter
	@RequestMapping("/status")
	public ResponseDto<MaterialAuditResultDto> list(@RequestParam(value = "id", required = false) String id, @RequestParam(value = "dspId") String dspId, @RequestParam(value = "token") String token) throws Exception {
		List<MaterialAuditResultModel> modelResults = materialService.getMaterialAuditResult(id, dspId);
		List<MaterialAuditResultDto> dtoResults = new ArrayList<MaterialAuditResultDto>();
		BeanUtils.copyList(modelResults, dtoResults, MaterialAuditResultDto.class);
		return ResponseUtils.response(StatusCode.SC200, dtoResults);
	}

	/**
	 * 上传素材对象转换
	 * 
	 * @param materialDto
	 * @return
	 * @throws ParseException
	 */
	private MaterialModel convert(MaterialDto materialDto) throws ParseException {
		MaterialModel entity = new MaterialModel();
		MonitorDto monitorDto = materialDto.getMonitor();
		List<TrackDto> trackDtos = monitorDto.getImpUrls();

		// 主信息转换
		BeanUtils.copyProperties(materialDto, entity);
		entity.setStartDate(parseToDate(materialDto.getStartDate(), "yyyy-MM-dd"));
		entity.setEndDate(parseToDate(materialDto.getEndDate(), "yyyy-MM-dd"));

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

	/**
	 * 根据指定的日期format 解析成 date 类型
	 * 
	 * @param dateStr
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	private Date parseToDate(String dateStr, String format) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(dateStr);
	}
}
