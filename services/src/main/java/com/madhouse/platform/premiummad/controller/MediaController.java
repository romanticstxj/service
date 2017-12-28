package com.madhouse.platform.premiummad.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.dto.MediaDto;
import com.madhouse.platform.premiummad.dto.ResponseDto;
import com.madhouse.platform.premiummad.entity.Media;
import com.madhouse.platform.premiummad.rule.BaseRule;
import com.madhouse.platform.premiummad.service.IMediaService;
import com.madhouse.platform.premiummad.service.IUserAuthService;
import com.madhouse.platform.premiummad.util.BeanUtils;
import com.madhouse.platform.premiummad.util.ObjectUtils;
import com.madhouse.platform.premiummad.util.ResponseUtils;
import com.madhouse.platform.premiummad.validator.Insert;
import com.madhouse.platform.premiummad.validator.Update;
import com.madhouse.platform.premiummad.validator.UpdateStatus;

@RestController
@RequestMapping("/media")
public class MediaController {
	
	@Autowired
    private IMediaService mediaService;
	
	@Autowired
    private IUserAuthService userAuthService;
	
	/**
	 * 媒体列表接口，1）若无ids参数，则查询当前用户下所有权限的媒体 2）若有ids参数（ids是用逗号分隔的媒体id组），则查询部分媒体数据，但查询出的也都是当前用户的权限下的媒体
	 * @param userId, category(媒体分类), 内部userid的暗门
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
    public ResponseDto<MediaDto> list(@RequestParam(value="ids", required=false) String mediaIds,
    		@RequestParam(value="category", required=false) Integer category,
    		@RequestParam(value="userId", required=false) Integer userIdByGet,
    		@RequestHeader(value="X-User-Id", required=false) Integer userId) throws Exception {
		//获得userId，可以从url中获得（方便通过get请求获取数据），更为一般的是从requestHeader里获取
		if(userIdByGet != null){ //优先获取get请求的userId参数
			userId = userIdByGet;
		}
		
		List<Integer> mediaIdList = userAuthService.queryMediaIdList(userId, mediaIds);
		return listByMediaIds(mediaIdList, category);
    }
	
	/**
     * 供应方媒体列表，内部调用接口（含参ids和category）
     * @return ResponseDto
	 * @throws Exception 
     */
    private ResponseDto<MediaDto> listByMediaIds(List<Integer> mediaIdList, Integer category) throws Exception {
		if(ObjectUtils.isEmpty(mediaIdList)){ //无权限查看任何媒体
	        return ResponseUtils.response(StatusCode.SC20000, new ArrayList<MediaDto>());
		} else{
			List<Media> medias = mediaService.queryAll(mediaIdList, category);
			List<MediaDto> mediaDtos = new ArrayList<>();
	        BeanUtils.copyList(medias,mediaDtos,MediaDto.class);
	        return ResponseUtils.response(StatusCode.SC20000,mediaDtos);
		}
		
    }
	
	/**
	 * 添加媒体
	 * @param mediaDto
	 * @return
	 */
	@RequestMapping("/create")
    public ResponseDto<MediaDto> addMedia(@RequestBody @Validated(Insert.class) MediaDto mediaDto) {
		BaseRule.validateDto(mediaDto);
        Integer count = mediaService.checkName(mediaDto.getName().trim());
        if (count > 0) //检查名称
            return ResponseUtils.response(StatusCode.SC20101,null);
        Media media = new Media();
        BeanUtils.copyProperties(mediaDto, media);
        BeanUtils.setCommonParam(media, true);
        mediaService.insert(media);
        List<MediaDto> result = convertResult(media, new MediaDto());
        return ResponseUtils.response(StatusCode.SC20000, result);
	}
	
	/**
	 * 通过id查询某个媒体
	 * @param id 媒体ID
	 * @return
	 */
	@RequestMapping("/detail")
    public ResponseDto<MediaDto> getMedia(@RequestParam(value="id", required=true) Integer id,
    		@RequestHeader(value="X-User-Id", required=false) Integer userId) {
    	//权限check
		List<Integer> mediaIdList = userAuthService.queryMediaIdList(userId, String.valueOf(id));
		if(ObjectUtils.isEmpty(mediaIdList) || mediaIdList.get(0).intValue() != id.intValue()){
			return ResponseUtils.response(StatusCode.SC20001, null);
		}
		
        Media media = mediaService.queryById(id);
        List<MediaDto> result = convertResult(media, new MediaDto());
        return ResponseUtils.response(StatusCode.SC20000, result);
    }
	
	/**
	 * 更新媒体或更新媒体状态
	 * @param mediaDto， 其中的updateType必填，表示更新的类型
	 * @return
	 */
	@RequestMapping("/update")
    public ResponseDto<MediaDto> updateMedia(@RequestBody @Validated(Update.class) MediaDto mediaDto,
    		@RequestHeader(value="X-User-Id", required=false) Integer userId) {
		BaseRule.validateDto(mediaDto);
		//权限check
		Integer id = mediaDto.getId();
		List<Integer> mediaIdList = userAuthService.queryMediaIdList(userId, String.valueOf(id));
		if(ObjectUtils.isEmpty(mediaIdList) || mediaIdList.get(0).intValue() != id.intValue()){
			return ResponseUtils.response(StatusCode.SC20001, null);
		}
		
        Media media = mediaService.queryById(mediaDto.getId());
        if (media == null)
            return ResponseUtils.response(StatusCode.SC20003, null);
        if (!mediaDto.getName().equals(media.getName())) { //名称不相等,检查名称
            Integer count = mediaService.checkName(mediaDto.getName().trim());
            if (count > 0)
                return ResponseUtils.response(StatusCode.SC20101,null);
        }
        BeanUtils.copyProperties(mediaDto, media);
        BeanUtils.setCommonParam(media, false);
        mediaService.update(media);
        List<MediaDto> result = convertResult(media, new MediaDto());
        return ResponseUtils.response(StatusCode.SC20000, result);
    }
	
	/**
	 * 更新媒体状态
	 * @param mediaDto
	 * @param userId
	 * @return
	 */
	@RequestMapping("/updateStatus")
    public ResponseDto<MediaDto> updateMediaStatus(
    		@RequestBody @Validated(value={UpdateStatus.class}) MediaDto mediaDto,
    		@RequestHeader(value="X-User-Id", required=false) Integer userId) {
		//权限check
		Integer id = mediaDto.getId();
		List<Integer> mediaIdList = userAuthService.queryMediaIdList(userId, String.valueOf(id));
		if(ObjectUtils.isEmpty(mediaIdList) || mediaIdList.get(0).intValue() != id.intValue()){
			return ResponseUtils.response(StatusCode.SC20001, null);
		}
				
		//更新媒体的状态
		Media media = mediaService.queryById(mediaDto.getId());
        if (media == null)
            return ResponseUtils.response(StatusCode.SC20003, null);
        BeanUtils.copyProperties(mediaDto, media);
        BeanUtils.setCommonParam(media, false);
        mediaService.updateStatus(media);
        List<MediaDto> result = convertResult(media, new MediaDto());
        return ResponseUtils.response(StatusCode.SC20000, result);
    }
	
	private List<MediaDto> convertResult(Media media, MediaDto mediaDto) {
        BeanUtils.copyProperties(media,mediaDto);
        List<MediaDto> mediaDtos = new ArrayList<>();
        mediaDtos.add(mediaDto);
        return mediaDtos;
	}
	
}
