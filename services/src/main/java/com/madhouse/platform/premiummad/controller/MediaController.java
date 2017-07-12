package com.madhouse.platform.premiummad.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dto.MediaDto;
import com.madhouse.platform.premiummad.dto.ResponseDto;
import com.madhouse.platform.premiummad.entity.Media;
import com.madhouse.platform.premiummad.service.IMediaService;
import com.madhouse.platform.premiummad.service.IUserAuthService;
import com.madhouse.platform.premiummad.util.BeanUtils;
import com.madhouse.platform.premiummad.util.ResponseUtils;
import com.madhouse.platform.premiummad.util.StringUtils;
import com.madhouse.platform.premiummad.validator.Update;

@Controller
@RequestMapping("/media")
public class MediaController {
	
	@Autowired
    private IMediaService mediaService;
	
	@Autowired
    private IUserAuthService userAuthService;
	
	/**
	 * 媒体列表接口，若无参ids则通过Header中的userId来查询此用户所拥有的所有媒体id，若含参ids则直接调用内部媒体列表接口
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
    public Object list(@RequestParam(value="ids", required=false) String ids,
    		@RequestHeader(value="X-User-Id", required=false) Integer userId) throws Exception {
		//有ids参数，跳过权限check
		if(ids != null && ids.length()>0){
			return "redirect:/media/listByMediaIds?ids=" + ids;
		}
				
		// id未传值，获取当前用户所有权限的媒体列表
		List<Integer> mediaIdList = userAuthService.queryMediaIdListByUserId(userId);
		//此用户有权限访问媒体
		if(mediaIdList != null && mediaIdList.size()> 0){
			//系统管理员，有访问所有媒体的权限
			String mediaIds = StringUtils.getIdsStr(mediaIdList);
			return "redirect:/media/listByMediaIds?ids=" + mediaIds;
			
		} else{ //用户没有权限访问媒体
			return "redirect:/media/listByMediaIds";
		}
    }
	
	/**
     * 供应方媒体列表，内部调用接口（含参ids）
     * @return ResponseDto
	 * @throws Exception 
     */
	@ResponseBody
	@RequestMapping("/listByMediaIds")
    public ResponseDto<MediaDto> listByMediaIds(@RequestParam(value="ids", required=false) String ids) throws Exception {
		//无权限查看任何媒体
		if(ids == null || ids.equals("")){
	        return ResponseUtils.response(StatusCode.SC20003, null);
		} else{ // admin权限，查询所有媒体;非admin，有部分媒体权限
			if(ids.equals(SystemConstant.SYSTEM_ADMIN_MEDIA_ID)){ //如果是管理员
				ids = null;
			}
			List<Media> medias = mediaService.queryAll(ids);
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
	@ResponseBody
	@RequestMapping("/create")
    public ResponseDto<MediaDto> addMedia(@RequestBody MediaDto mediaDto) {
		String fieldName = BeanUtils.hasEmptyField(mediaDto);
        if (fieldName != null)
            return ResponseUtils.response(StatusCode.SC20001, null, fieldName + " cannot be null");
        Integer count = mediaService.checkName(mediaDto.getName().trim());
        if (count > 0) //检查名称
            return ResponseUtils.response(StatusCode.SC20101,null);
        Media media = new Media();
        BeanUtils.copyProperties(mediaDto, media);
        BeanUtils.setCreateParam(media);
        mediaService.insert(media);
        List<MediaDto> list = new ArrayList<MediaDto>();
        mediaDto.setId(media.getId());
        list.add(mediaDto);
        return ResponseUtils.response(StatusCode.SC20000,list);
	}
	
	/**
	 * 通过id查询某个媒体
	 * @param id 媒体ID
	 * @return
	 */
	@ResponseBody
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
	@ResponseBody
	@RequestMapping("/update")
    public ResponseDto<Boolean> updateMedia(@RequestBody @Validated(Update.class) MediaDto mediaDto) {
		Integer updateType = mediaDto.getUpdateType();
		//更新类型未设置，或设置得不正确
		if(updateType == null || (!updateType.equals(1) && !updateType.equals(2))){
			return ResponseUtils.response(StatusCode.SC20004, null);
		}
		
		//更新媒体
		if(updateType.equals(1)){
			String fieldName = BeanUtils.hasEmptyField(mediaDto);
	        if (fieldName != null)
	            return ResponseUtils.response(StatusCode.SC20001, null, fieldName + " cannot be null");
	        Media media = mediaService.queryMediaById(mediaDto.getId());
	        if (media == null)
	            return ResponseUtils.response(StatusCode.SC20002, null);
	        if (!mediaDto.getName().equals(media.getName())) { //名称不相等,检查名称
	            Integer count = mediaService.checkName(mediaDto.getName().trim());
	            if (count > 0)
	                return ResponseUtils.response(StatusCode.SC20101,null);
	        }
	        BeanUtils.copyProperties(mediaDto, media);
	        BeanUtils.setUpdateParam(media);
	        mediaService.update(media);
	        return ResponseUtils.response(StatusCode.SC20000,null);
		} else{
			//更新媒体的状态
			Media media = mediaService.queryMediaById(mediaDto.getId());
	        if (media == null)
	            return ResponseUtils.response(StatusCode.SC20002, null);
	        BeanUtils.copyProperties(mediaDto, media);
	        BeanUtils.setUpdateParam(media);
	        mediaService.updateStatus(media);
	        return ResponseUtils.response(StatusCode.SC20000,null);
		}
    }
	
}
