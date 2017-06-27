package com.madhouse.platform.premiummad.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.dto.MediaDto;
import com.madhouse.platform.premiummad.dto.ResponseDto;
import com.madhouse.platform.premiummad.entity.Media;
import com.madhouse.platform.premiummad.exception.BusinessException;
import com.madhouse.platform.premiummad.service.IMediaService;
import com.madhouse.platform.premiummad.util.BeanUtils;
import com.madhouse.platform.premiummad.util.ResponseUtils;

@RestController
@RequestMapping("/media")
public class MediaController {
	
	@Autowired
    private IMediaService mediaService;
	
	/**
     * 供应方媒体列表
     * @return ResponseDto
	 * @throws Exception 
     */
	@RequestMapping("/list")
    public ResponseDto<MediaDto> list(@RequestParam(value="ids", required=false) String ids) throws Exception {
		List<Media> medias = mediaService.queryAll(ids);
        List<MediaDto> mediaDtos = new ArrayList<>();
        BeanUtils.copyList(medias,mediaDtos,MediaDto.class);
        return ResponseUtils.response(StatusCode.SC20000,mediaDtos);
    }
	
	@RequestMapping("/exceptionTest")
	public ResponseDto<MediaDto> exceptionTest(@RequestParam(value="exType", required=false) String exType) throws Exception {
		if(exType.equals("1")){
			throw new BusinessException(StatusCode.SC31001);
		} else if (exType.equals("2")){
			throw new ArithmeticException();
		} else{
			throw new Exception();
		}
    }
	
	/**
	 * 添加媒体
	 * @param mediaDto
	 * @return
	 */
	@RequestMapping("/create")
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
        int i = mediaService.insert(media);
        return ResponseUtils.response(StatusCode.SC20000,null);
	}
	
	/**
	 * 通过id查询某个媒体
	 * @param id 媒体ID
	 * @return
	 */
	@RequestMapping("/detail")
    public ResponseDto<MediaDto> getMedia(@RequestParam(value="id", required=true) Integer id) {
        Media media = mediaService.queryMediaById(id);
        MediaDto mediaDto = new MediaDto();
        BeanUtils.copyProperties(media,mediaDto);
        List<MediaDto> mediaDtos = new ArrayList<>();
        mediaDtos.add(mediaDto);
        return ResponseUtils.response(StatusCode.SC20000,mediaDtos);
    }
	
	/**
	 * 更新媒体或更新媒体状态
	 * @param mediaDto， 其中的updateType必填，表示更新的类型
	 * @return
	 */
	@RequestMapping("/update")
    public ResponseDto<MediaDto> updateMedia(@RequestBody MediaDto mediaDto) {
		Integer updateType = mediaDto.getUpdateType();
		//更新类型未设置，或设置得不正确
		if(updateType == null || (!updateType.equals(1) && !updateType.equals(2))){
			return ResponseUtils.response(StatusCode.SC21032, null);
		}
		
		//更新媒体
		if(updateType.equals(1)){
			String fieldName = BeanUtils.hasEmptyField(mediaDto);
	        if (fieldName != null)
	            return ResponseUtils.response(StatusCode.SC21004, null, fieldName + " cannot be null");
	        Media media = mediaService.queryMediaById(mediaDto.getId());
	        if (media == null)
	            return ResponseUtils.response(StatusCode.SC21024, null);
	        if (!mediaDto.getName().equals(media.getName())) { //名称不相等,检查名称
	            Integer count = mediaService.checkName(mediaDto.getName().trim());
	            if (count > 0)
	                return ResponseUtils.response(StatusCode.SC22004,null);
	        }
	        BeanUtils.copyProperties(mediaDto, media);
	        BeanUtils.setUpdateParam(media);
	        mediaService.update(media);
	        return ResponseUtils.response(StatusCode.SC20000,null);
		} else{
			//更新媒体的状态
			Media media = mediaService.queryMediaById(mediaDto.getId());
	        if (media == null)
	            return ResponseUtils.response(StatusCode.SC21024, null);
	        BeanUtils.copyProperties(mediaDto, media);
	        BeanUtils.setUpdateParam(media);
	        int i = mediaService.updateStatus(media);
	        return ResponseUtils.response(StatusCode.SC20000,null);
		}
		
    }
	
}
