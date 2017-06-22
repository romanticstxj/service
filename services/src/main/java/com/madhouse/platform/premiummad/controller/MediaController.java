package com.madhouse.platform.smartexchange.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.madhouse.platform.smartexchange.constant.StatusCode;
import com.madhouse.platform.smartexchange.dto.MediaDto;
import com.madhouse.platform.smartexchange.dto.ResponseDto;
import com.madhouse.platform.smartexchange.entity.Media;
import com.madhouse.platform.smartexchange.service.IMediaService;
import com.madhouse.platform.smartexchange.util.BeanUtils;
import com.madhouse.platform.smartexchange.util.ResponseUtils;

@RestController
@RequestMapping("/media")
public class MediaController {
	
	@Autowired
    private IMediaService mediaService;
	
	/**
     * 供应方媒体列表
     * @return ResponseDto
     */
	@RequestMapping("")
    public ResponseDto<MediaDto> list() {
		List<Media> medias = mediaService.queryAll();
        List<MediaDto> mediaDtos = new ArrayList<>();
        BeanUtils.copyList(medias,mediaDtos,MediaDto.class);
        return ResponseUtils.response(StatusCode.SC20000,mediaDtos);
    }
	
	/**
	 * 添加媒体
	 * @param mediaDto
	 * @return
	 */
	@RequestMapping("/add")
    public ResponseDto<MediaDto> addMedia(@RequestBody MediaDto mediaDto) {
		String fieldName = BeanUtils.hasEmptyField(mediaDto);
        if (fieldName != null)
            return ResponseUtils.response(StatusCode.SC21004, null, fieldName + " cannot be null");
        Integer count = mediaService.checkName(mediaDto.getName().trim());
        if (count > 0) //检查名称
            return ResponseUtils.response(StatusCode.SC22004,null);
        Media media = new Media();
        BeanUtils.copyProperties(mediaDto, media);
        BeanUtils.setCreateParam(media);
        mediaService.insert(media);
        return ResponseUtils.response(StatusCode.SC20000,null);
	}
	
	@RequestMapping("/get/{mediaId}")
    public ResponseDto<SupplierMediaDto> getMedia(@PathVariable Integer supplierMediaId) {
        SupplierMedia supplierMedia = supplierMediaService.querySupplierMediaById(supplierMediaId);
        SupplierMediaDto supplierMediaDto = new SupplierMediaDto();
        BeanUtils.copyProperties(supplierMedia,supplierMediaDto);
        List<SupplierMediaDto> supplierMediaDtos = new ArrayList<>();
        supplierMediaDtos.add(supplierMediaDto);
        return ResponseUtils.response(StatusCode.SC20000,supplierMediaDtos);
    }
	
}
