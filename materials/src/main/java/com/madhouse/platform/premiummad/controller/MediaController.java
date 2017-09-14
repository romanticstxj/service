package com.madhouse.platform.premiummad.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.madhouse.platform.premiummad.annotation.TokenFilter;
import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.dto.MediaDto;
import com.madhouse.platform.premiummad.dto.ResponseDto;
import com.madhouse.platform.premiummad.model.MediaModel;
import com.madhouse.platform.premiummad.service.IMediaService;
import com.madhouse.platform.premiummad.util.BeanUtils;
import com.madhouse.platform.premiummad.util.ResponseUtils;

@RestController
@RequestMapping("/media")
public class MediaController {

	@Autowired
	private IMediaService mediaService;

	/**
	 * 获取所有已启用的媒体
	 * 
	 * @param dspId
	 * @param token
	 * @return
	 * @throws Exception
	 */
	@TokenFilter
	@RequestMapping("/list")
	public ResponseDto<MediaDto> list(@RequestParam(value = "dspId") String dspId, @RequestParam(value = "token") String token) throws Exception {
		List<MediaModel> modelResults = mediaService.getAuditedMedia();
		List<MediaDto> dtoResults = new ArrayList<MediaDto>();
		BeanUtils.copyList(modelResults, dtoResults, MediaDto.class);
		return ResponseUtils.response(StatusCode.SC200, dtoResults);
	}
}
