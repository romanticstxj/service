package com.madhouse.platform.premiummad.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.madhouse.platform.premiummad.annotation.TokenFilter;
import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.dto.AdspaceDto;
import com.madhouse.platform.premiummad.dto.ResponseDto;
import com.madhouse.platform.premiummad.model.AdspaceModel;
import com.madhouse.platform.premiummad.service.IAdspaceService;
import com.madhouse.platform.premiummad.util.BeanUtils;
import com.madhouse.platform.premiummad.util.ResponseUtils;
import com.madhouse.platform.premiummad.util.StringUtils;

@RestController
@RequestMapping("/adspace")
public class AdspaceController {

	@Autowired
	private IAdspaceService adspaceService;

	@Value("${adspace_list_allowed_dsp}")
	private String allowedDspIds;
	
	/**
	 * 获取所有启用的广告位
	 * 
	 * @param dspId
	 * @param token
	 * @return
	 * @throws Exception
	 */
	@TokenFilter
	@RequestMapping("/list")
	public ResponseDto<AdspaceDto> list(@RequestParam(value = "dspId") String dspId, @RequestParam(value = "token") String token) throws Exception {
		// 校验DSP是否在白名单内
		if (!validateWhiteList(dspId)) {
			return ResponseUtils.response(StatusCode.SC200, new ArrayList<AdspaceDto>());
		}
		List<AdspaceModel> modelResults = adspaceService.getAuditedAdspaces(dspId);
		List<AdspaceDto> dtoResults = convert(modelResults);
		return ResponseUtils.response(StatusCode.SC200, dtoResults);
	}

	/**
	 * 将 model 转换成 DTO
	 * 
	 * @param modelResults
	 * @return
	 */
	private List<AdspaceDto> convert(List<AdspaceModel> source) {
		List<AdspaceDto> destination = new ArrayList<AdspaceDto>();
		// 校验
		if (source == null || source.isEmpty()) {
			return destination;
		}

		// 转换
		for (AdspaceModel sourceItem : source) {
			AdspaceDto destinationItem = new AdspaceDto();
			AdspaceModel.Image sourceBanner = sourceItem.getBanner();
			AdspaceModel.Video sourceVideo = sourceItem.getVideo();
			AdspaceModel.Native sourceNatives = sourceItem.getNatives();
			BeanUtils.copyProperties(sourceItem, destinationItem);
			if (sourceBanner != null) {
				AdspaceDto.Image destinationBanner = destinationItem.new Image();
				BeanUtils.copyProperties(sourceBanner, destinationBanner);
				destinationItem.setBanner(destinationBanner);
			}
			if (sourceVideo != null) {
				AdspaceDto.Video destinationVideo = destinationItem.new Video();
				BeanUtils.copyProperties(sourceVideo, destinationVideo);
				destinationItem.setVideo(destinationVideo);
			}
			if (sourceNatives != null) {
				AdspaceDto.Native destinationNatives = destinationItem.new Native();
				AdspaceModel.Image sourceCover = sourceNatives.getCover();
				AdspaceModel.Image sourceIcon = sourceNatives.getIcon();
				AdspaceModel.Image sourceImage = sourceNatives.getImage();
				AdspaceModel.Video sourceNativeVideo = sourceNatives.getVideo();

				BeanUtils.copyProperties(sourceNatives, destinationNatives);
				if (sourceCover != null) {
					AdspaceDto.Image destinationCover = destinationItem.new Image();
					BeanUtils.copyProperties(sourceCover, destinationCover);
					destinationNatives.setCover(destinationCover);
				}
				if (sourceIcon != null) {
					AdspaceDto.Image destinationIcon = destinationItem.new Image();
					BeanUtils.copyProperties(sourceIcon, destinationIcon);
					destinationNatives.setIcon(destinationIcon);
				}
				if (sourceImage != null) {
					AdspaceDto.Image destinationImage = destinationItem.new Image();
					BeanUtils.copyProperties(sourceImage, destinationImage);
					destinationNatives.setImage(destinationImage);
				}
				if (sourceNativeVideo != null) {
					AdspaceDto.Video destinationNativeVideo = destinationItem.new Video();
					BeanUtils.copyProperties(sourceNativeVideo, destinationNativeVideo);
					destinationNatives.setVideo(destinationNativeVideo);
				}
				destinationItem.setNatives(destinationNatives);
			}

			destination.add(destinationItem);
		}
		return destination;
	}
	
	/**
	 * 校验DSP是否在白名单内
	 * @return
	 */
	private boolean validateWhiteList(String dspId) {
		if (StringUtils.isBlank(allowedDspIds)) {
			return false;
		}
		String[] dspIds = allowedDspIds.split(",");
		for (String item : dspIds) {
			if (dspId.equals(item)) {
				return true;
			}
		}
		return false;
	}
}
