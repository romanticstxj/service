package com.madhouse.platform.premiummad.controller;

import java.text.ParseException;
import java.util.ArrayList;
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
	public ResponseDto<Void> upload(@RequestBody MaterialDto materialDto, @RequestParam(value = "dspId") String dspId, @RequestParam(value = "token") String token) throws Exception {
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

		// 主信息转换
		BeanUtils.copyProperties(materialDto, entity);

		// 广告监测信息转换
		if (materialDto.getMonitor() != null) {
			MonitorDto monitorDto = materialDto.getMonitor();
			MonitorModel monitorModel = new MonitorModel();
			// 主信息转换
			BeanUtils.copyProperties(monitorDto, monitorModel);
			entity.setMonitor(monitorModel);

			// 点击监测地址
			monitorModel.setClkUrls(monitorDto.getClkUrls());
			// 品牌安全监测
			monitorModel.setSecUrls(monitorDto.getSecUrls());

			// 展示监测信息URL
			List<TrackDto> trackDtos = monitorDto.getImpUrls();
			if (trackDtos != null) {
				List<TrackModel> trackModels = new ArrayList<TrackModel>();
				BeanUtils.copyList(trackDtos, trackModels, TrackModel.class);
				monitorModel.setImpUrls(trackModels);
			}
		}
		return entity;
	}
}
