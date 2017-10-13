package com.madhouse.platform.premiummad.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/adspace")
public class AdspaceController {

	@Autowired
	private IAdspaceService adspaceService;

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
		List<AdspaceModel> modelResults = adspaceService.getAuditedAdspaces();
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
				destinationItem.setNatives(destinationNatives);
			}

			destination.add(destinationItem);
		}
		return destination;
	}
}
