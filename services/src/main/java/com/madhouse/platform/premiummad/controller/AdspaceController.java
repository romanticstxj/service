package com.madhouse.platform.premiummad.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dto.AdspaceDto;
import com.madhouse.platform.premiummad.dto.AdspaceMappingDto;
import com.madhouse.platform.premiummad.dto.DspMappingDto;
import com.madhouse.platform.premiummad.dto.ResponseDto;
import com.madhouse.platform.premiummad.entity.Adspace;
import com.madhouse.platform.premiummad.entity.AdspaceMapping;
import com.madhouse.platform.premiummad.entity.DspMapping;
import com.madhouse.platform.premiummad.rule.AdspaceRule;
import com.madhouse.platform.premiummad.service.IAdspaceService;
import com.madhouse.platform.premiummad.service.IUserAuthService;
import com.madhouse.platform.premiummad.util.BeanUtils;
import com.madhouse.platform.premiummad.util.ObjectUtils;
import com.madhouse.platform.premiummad.util.ResponseUtils;
import com.madhouse.platform.premiummad.util.StringUtils;
import com.madhouse.platform.premiummad.validator.Update;
import com.madhouse.platform.premiummad.validator.UpdateStatus;

@RestController
@RequestMapping("/adspace")
public class AdspaceController {
	
	@Autowired
    private IAdspaceService adspaceService;
	
	@Autowired
    private IUserAuthService userAuthService;
	
	/**
	 * 查询广告位list，1）若无ids参数，则查询当前用户下所有权限的广告位 2）若有ids参数（ids是用逗号分隔的媒体id组），则查询部分广告位数据，但查询出的也都是当前用户的权限下的广告位
	 * @param ids
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
    public ResponseDto<AdspaceDto> list(@RequestParam(value="ids", required=false) String mediaIds,
    		@RequestParam(value="status", required=false) Integer status,
    		@RequestParam(value="userId", required=false) Integer userIdByGet,
    		@RequestHeader(value="X-User-Id", required=false) Integer userId,
    		HttpServletRequest request) throws Exception {
		AdspaceRule.checkStatus(status);
		
		//获得userId，可以从url中获得（方便通过get请求获取数据），更为一般的是从requestHeader里获取
		if(userIdByGet != null){ //优先获取get请求的userId参数
			userId = userIdByGet;
		}
		
		List<Integer> mediaIdList = userAuthService.queryMediaIdList(userId, mediaIds);
		String returnedMediaIds = StringUtils.getIdsStr(mediaIdList);
		return listByParams(returnedMediaIds, status);
    }
	
	/**
	 * 含参ids的机接口，内部调用
	 * @param ids
	 * @return
	 */
	private ResponseDto<AdspaceDto> listByParams(String mediaIds, Integer status){
		//无权限查看任何媒体
		if(mediaIds == null || mediaIds.equals("")){
	        return ResponseUtils.response(StatusCode.SC20006, null);
		} else{ // admin权限，查询所有媒体;非admin，有部分媒体权限
			if(mediaIds.equals(SystemConstant.OtherConstant.SYSTEM_ADMIN_MEDIA_ID)){ //如果是管理员
				mediaIds = null;
			}
			List<Adspace> adspaces = adspaceService.queryAllByParams(mediaIds, status);
			List<AdspaceDto> result = AdspaceRule.convertToDtoList(adspaces, new ArrayList<AdspaceDto>());
	        return ResponseUtils.response(StatusCode.SC20000, result);
		}
	}
	
	/**
	 * 添加广告位
	 * @param adspaceDto
	 * @return
	 */
	@RequestMapping("/create")
    public ResponseDto<AdspaceDto> addAdspace(@RequestBody AdspaceDto adspaceDto, 
    		@RequestHeader(value=SystemConstant.Request.XFROM, required=true) String xFrom) {
		AdspaceRule.validateDto(adspaceDto);
        Adspace adspace = AdspaceRule.convertToModel(adspaceDto, new Adspace());
        adspaceService.insert(adspace, xFrom);
        List<AdspaceDto> result = AdspaceRule.convertToDto(adspace, new AdspaceDto());
        return ResponseUtils.response(StatusCode.SC20000, result);
	}
	
	/**
	 * 根据广告位id查询广告位
	 * @param id
	 * @return
	 */
	@RequestMapping("/detail")
    public ResponseDto<AdspaceDto> getAdspace(@RequestParam(value="id", required=true) Integer id,
    		@RequestHeader(value="X-User-Id", required=false) Integer userId) {
		//权限check
		if(userId == null){
			return ResponseUtils.response(StatusCode.SC20006, null);
		}
		List<Integer> adspaceIdList = userAuthService.queryAdspaceIdList(userId, String.valueOf(id));
		if(userId == null || ObjectUtils.isEmpty(adspaceIdList) || adspaceIdList.get(0).intValue() != id.intValue()){
			return ResponseUtils.response(StatusCode.SC20006, null);
		}
		
		Adspace adspace = adspaceService.queryAdspaceById(id);
		List<AdspaceDto> result = AdspaceRule.convertToDto(adspace, new AdspaceDto());
        return ResponseUtils.response(StatusCode.SC20000,result);
    }
	
	/**
	 * 更新广告位
	 * @param adspaceDto
	 * @return
	 */
	@RequestMapping("/update")
    public ResponseDto<AdspaceDto> updateAdspace(@RequestBody @Validated(Update.class) AdspaceDto adspaceDto) {
		AdspaceRule.validateDto(adspaceDto);
        Adspace adspace = AdspaceRule.convertToModel(adspaceDto, new Adspace());
        adspaceService.update(adspace);
        List<AdspaceDto> result = AdspaceRule.convertToDto(adspace, new AdspaceDto());
        return ResponseUtils.response(StatusCode.SC20000, result);
    }
	
	/**
	 * 更新广告位状态
	 * @param adspaceDto
	 * @return
	 */
	@RequestMapping("/updateStatus")
    public ResponseDto<AdspaceDto> updateAdspaceStatus(
    		@RequestBody @Validated(UpdateStatus.class) AdspaceDto adspaceDto) {
		Adspace adspace = adspaceService.queryAdspaceById(adspaceDto.getId());
        if (adspace == null)
            return ResponseUtils.response(StatusCode.SC20002, null);
        BeanUtils.copyProperties(adspaceDto, adspace);
        BeanUtils.setUpdateParam(adspace);
        adspaceService.updateStatus(adspace);
        return ResponseUtils.response(StatusCode.SC20000,null);
    }
	
	/**
	 * 添加我方广告位和媒体、dsp方广告位的映射
	 * @param adspaceMappingDto
	 * @return
	 */
	@RequestMapping("/mapping/create")
	public ResponseDto<AdspaceDto> addAdspaceMapping(@RequestBody AdspaceMappingDto adspaceMappingDto) {
		//对dto表单做check
		String fieldName = BeanUtils.hasEmptyField(adspaceMappingDto);
        if (fieldName != null)
            return ResponseUtils.response(StatusCode.SC20001, null, fieldName + " cannot be null");
        String mediaAdspaceKey = adspaceMappingDto.getMediaAdspaceKey();
        List<DspMappingDto> dspMappingDtos = adspaceMappingDto.getDspMappings();
        //媒体映射信息和dsp映射信息不能全为空
        if(StringUtils.isEmpty(mediaAdspaceKey) && ObjectUtils.isEmpty(dspMappingDtos)){
        	return ResponseUtils.response(StatusCode.SC20204, null);
        }
        
        AdspaceMapping adspaceMapping = new AdspaceMapping();
        BeanUtils.copyProperties(adspaceMappingDto, adspaceMapping, "dspMappings");
        List<DspMapping> dspMappings = new ArrayList<DspMapping>();
        BeanUtils.copyList(adspaceMappingDto.getDspMappings(), dspMappings, DspMapping.class);
        adspaceMapping.setDspMappings(dspMappings);
        BeanUtils.setCreateParam(adspaceMapping);
        //分解出广告位和dsp的关联信息，插入数据库
  		Integer adspaceId = adspaceMappingDto.getAdspaceId(); //我方广告位id
  		for(int i=0; i<dspMappings.size(); i++){ 
  			dspMappings.get(i).setAdspaceId(adspaceId);
  			BeanUtils.setCreateParam(dspMappings.get(i));
  		}
  		
  		StatusCode statusCode = adspaceService.addAdspaceMapping(adspaceMapping);
		
		return ResponseUtils.response(statusCode,null);
	}
	
	/**
	 * 查询广告位映射信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/mapping/detail")
	public ResponseDto<AdspaceMappingDto> adspaceMappingDetail(@RequestParam(value="id", required=true) Integer id) {
		AdspaceMapping adspaceMapping = adspaceService.queryAdspaceMappingById(id);
		AdspaceMappingDto adspaceMappingDto = new AdspaceMappingDto();
		BeanUtils.copyProperties(adspaceMapping, adspaceMappingDto);
		adspaceMappingDto.setAdspaceId(id);
		List<AdspaceMappingDto> result = new ArrayList<AdspaceMappingDto>();
		result.add(adspaceMappingDto);
		return ResponseUtils.response(StatusCode.SC20000,result);
	}
	
	/**
	 * 更新我方广告位和媒体、dsp方广告位的映射
	 * @param adspaceMappingDto
	 * @return
	 */
	@RequestMapping("/mapping/update")
	public ResponseDto<AdspaceDto> updateAdspaceMapping(@RequestBody AdspaceMappingDto adspaceMappingDto) {
		//对dto表单做check
		String fieldName = BeanUtils.hasEmptyField(adspaceMappingDto);
        if (fieldName != null)
            return ResponseUtils.response(StatusCode.SC20001, null, fieldName + " cannot be null");
        String mediaAdspaceKey = adspaceMappingDto.getMediaAdspaceKey();
        List<DspMappingDto> dspMappingDtos = adspaceMappingDto.getDspMappings();
        //媒体映射信息和dsp映射信息不能全为空
        if(StringUtils.isEmpty(mediaAdspaceKey) && ObjectUtils.isEmpty(dspMappingDtos)){
        	return ResponseUtils.response(StatusCode.SC20204, null);
        }
        
        AdspaceMapping adspaceMapping = new AdspaceMapping();
        BeanUtils.copyProperties(adspaceMappingDto, adspaceMapping, "dspMappings");
        List<DspMapping> dspMappings = new ArrayList<DspMapping>();
        BeanUtils.copyList(adspaceMappingDto.getDspMappings(), dspMappings, DspMapping.class);
        adspaceMapping.setDspMappings(dspMappings);
        BeanUtils.setCreateParam(adspaceMapping);
        //分解出广告位和dsp的关联信息，插入数据库
  		Integer adspaceId = adspaceMappingDto.getAdspaceId(); //我方广告位id
  		for(int i=0; i<dspMappings.size(); i++){ 
  			dspMappings.get(i).setAdspaceId(adspaceId);
  			BeanUtils.setCreateParam(dspMappings.get(i));
  		}
        
  		//更新映射关系
  		StatusCode statusCode = adspaceService.updateAdspaceMapping(adspaceMapping);
		return ResponseUtils.response(statusCode,null);
	}
	
	/**
	 * 添加我方广告位和媒体、dsp方广告位的映射
	 * @param adspaceMappingDto
	 * @return
	 */
	@RequestMapping("/mapping/relate")
	public ResponseDto<AdspaceDto> relateAdspaceMapping(@RequestBody AdspaceMappingDto adspaceMappingDto) {
		//对dto表单做check
		String fieldName = BeanUtils.hasEmptyField(adspaceMappingDto);
        if (fieldName != null)
            return ResponseUtils.response(StatusCode.SC20001, null, fieldName + " cannot be null");
        String mediaAdspaceKey = adspaceMappingDto.getMediaAdspaceKey();
        List<DspMappingDto> dspMappingDtos = adspaceMappingDto.getDspMappings();
        //媒体映射信息和dsp映射信息不能全为空
        if(StringUtils.isEmpty(mediaAdspaceKey) && ObjectUtils.isEmpty(dspMappingDtos)){
        	adspaceService.removeAdspaceMapping(adspaceMappingDto.getAdspaceId());
        	return ResponseUtils.response(StatusCode.SC20204, null);
        }
        
        AdspaceMapping adspaceMapping = new AdspaceMapping();
        BeanUtils.copyProperties(adspaceMappingDto, adspaceMapping, "dspMappings");
        List<DspMapping> dspMappings = new ArrayList<DspMapping>();
        BeanUtils.copyList(adspaceMappingDto.getDspMappings(), dspMappings, DspMapping.class);
        adspaceMapping.setDspMappings(dspMappings);
        BeanUtils.setCreateParam(adspaceMapping);
        //分解出广告位和dsp的关联信息，插入数据库
  		Integer adspaceId = adspaceMappingDto.getAdspaceId(); //我方广告位id
  		for(int i=0; i<dspMappings.size(); i++){ 
  			dspMappings.get(i).setAdspaceId(adspaceId);
  			BeanUtils.setCreateParam(dspMappings.get(i));
  		}
  		
  		adspaceService.createAndUpdateAdspaceMapping(adspaceMapping);
		return ResponseUtils.response(StatusCode.SC20000,null);
	}
	
}
