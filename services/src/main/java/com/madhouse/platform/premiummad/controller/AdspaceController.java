package com.madhouse.platform.premiummad.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dto.AdspaceDto;
import com.madhouse.platform.premiummad.dto.AdspaceMappingDto;
import com.madhouse.platform.premiummad.dto.DspMappingDto;
import com.madhouse.platform.premiummad.dto.ResponseDto;
import com.madhouse.platform.premiummad.entity.Adspace;
import com.madhouse.platform.premiummad.entity.AdspaceMapping;
import com.madhouse.platform.premiummad.entity.DspMapping;
import com.madhouse.platform.premiummad.service.IAdspaceService;
import com.madhouse.platform.premiummad.service.IUserAuthService;
import com.madhouse.platform.premiummad.util.BeanUtils;
import com.madhouse.platform.premiummad.util.ObjectUtils;
import com.madhouse.platform.premiummad.util.ResponseUtils;
import com.madhouse.platform.premiummad.util.StringUtils;

@Controller
@RequestMapping("/adspace")
public class AdspaceController {
	
	@Autowired
    private IAdspaceService adspaceService;
	
	@Autowired
    private IUserAuthService userAuthService;
	
	/**
	 * 查询广告位list，如果无参则根据userId查询此用户的媒体Idlist，若含参ids，则直接调用含参的内部接口
	 * @param ids
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
    public Object list(@RequestParam(value="ids", required=false) String ids,
    		@RequestHeader(value="X-User-Id", required=false) Integer userId) throws Exception {
		//有ids参数，跳过权限check
		if(ids != null && ids.length()>0){
			return "redirect:/adspace/listByMediaIds?ids=" + ids;
		}
		
		// id未传值，获取当前用户所有权限的媒体列表
		List<Integer> mediaIdList = userAuthService.queryMediaIdListByUserId(userId);
		//此用户有权限访问媒体
		if(mediaIdList != null && mediaIdList.size()> 0){
			//系统管理员，有访问所有媒体的权限
			String mediaIds = StringUtils.getIdsStr(mediaIdList);
			return "redirect:/adspace/listByMediaIds?ids=" + mediaIds;
			
		} else{ //用户没有权限访问媒体
			return "redirect:/adspace/listByMediaIds";
		}
    }
	
	/**
	 * 含参ids的机接口，内部调用
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/listByMediaIds")
	public ResponseDto<AdspaceDto> listByMediaIds(@RequestParam(value="ids", required=false) String ids){
		//无权限查看任何媒体
		if(ids == null || ids.equals("")){
	        return ResponseUtils.response(StatusCode.SC20003, null);
		} else{ // admin权限，查询所有媒体;非admin，有部分媒体权限
			if(ids.equals(SystemConstant.SYSTEM_ADMIN_MEDIA_ID)){ //如果是管理员
				ids = null;
			}
			List<Adspace> adspaces = adspaceService.queryAll(ids);
			List<AdspaceDto> adspaceDtos = new ArrayList<>();
	        BeanUtils.copyList(adspaces,adspaceDtos,AdspaceDto.class,"bidFloor");
	        processAdspaceDto(adspaces, adspaceDtos);
	        return ResponseUtils.response(StatusCode.SC20000,adspaceDtos);
		}
	}
	
	private void processAdspaceDto(List<Adspace> adspaces, List<AdspaceDto> adspaceDtos) {
		//convert bid floor unit from Fen to Yuan
		for(int i=0; i<adspaceDtos.size(); i++){
			Integer bidFloorUnitFen = adspaces.get(i).getBidFloor();
			adspaceDtos.get(i).setBidFloor((double)bidFloorUnitFen / 100);
		}
	}
	
	/**
	 * 添加广告位
	 * @param adspaceDto
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/create")
    public ResponseDto<AdspaceDto> addAdspace(@RequestBody AdspaceDto adspaceDto, 
    		@RequestHeader(value=SystemConstant.XFROM, required=true) String xFrom) {
		String fieldName = BeanUtils.hasEmptyField(adspaceDto);
        if (fieldName != null)
            return ResponseUtils.response(StatusCode.SC20001, null, fieldName + " cannot be null");
        Integer count = adspaceService.checkName(adspaceDto.getName().trim());
        if (count > 0) //检查名称
            return ResponseUtils.response(StatusCode.SC20101,null);
        Adspace adspace = new Adspace();
        BeanUtils.copyProperties(adspaceDto, adspace, SystemConstant.ADSPACE_BID_FLOOR);
        BeanUtils.setCreateParam(adspace);
        adspaceService.insert(adspace, adspaceDto.getBidFloor(), xFrom);
        return ResponseUtils.response(StatusCode.SC20000,null);
	}
	
	/**
	 * 根据广告位id查询广告位
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/detail")
    public ResponseDto<AdspaceDto> getAdspace(@RequestParam(value="id", required=true) Integer id) {
		Adspace adspace = adspaceService.queryAdspaceById(id);
		AdspaceDto adspaceDto = new AdspaceDto();
        BeanUtils.copyProperties(adspace,adspaceDto,"bidFloor");
        List<AdspaceDto> adspaceDtos = new ArrayList<>();
        adspaceDtos.add(adspaceDto);
        processAdspaceDto(adspace,adspaceDto);
        return ResponseUtils.response(StatusCode.SC20000,adspaceDtos);
    }
	
	private void processAdspaceDto(Adspace adspace, AdspaceDto adspaceDto) {
		// TODO Auto-generated method stub
		Integer bidFloorUnitFen = adspace.getBidFloor();
		Double bidFloor =  (double)bidFloorUnitFen / 100;
		adspaceDto.setBidFloor(bidFloor);
	}

	/**
	 * 更新广告位，或广告位状态
	 * @param adspaceDto
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/update")
    public ResponseDto<AdspaceDto> updateAdspace(@RequestBody AdspaceDto adspaceDto) {
		Integer updateType = adspaceDto.getUpdateType();
		//更新类型未设置，或设置得不正确
		if(updateType == null || (!updateType.equals(1) && !updateType.equals(2))){
			return ResponseUtils.response(StatusCode.SC20004, null);
		}
		
		//更新广告位
		if(updateType.equals(1)){
			String fieldName = BeanUtils.hasEmptyField(adspaceDto);
	        if (fieldName != null)
	            return ResponseUtils.response(StatusCode.SC20001, null, fieldName + " cannot be null");
	        Adspace adspace = adspaceService.queryAdspaceById(adspaceDto.getId());
	        if (adspace == null)
	            return ResponseUtils.response(StatusCode.SC20002, null);
	        if (!adspaceDto.getName().equals(adspaceDto.getName())) { //名称不相等,检查名称
	            Integer count = adspaceService.checkName(adspaceDto.getName().trim());
	            if (count > 0)
	                return ResponseUtils.response(StatusCode.SC20101,null);
	        }
	        BeanUtils.copyProperties(adspaceDto, adspace);
	        BeanUtils.setUpdateParam(adspace);
	        adspaceService.update(adspace, adspaceDto.getBidFloor());
	        return ResponseUtils.response(StatusCode.SC20000,null);
		} else{
			//更新媒体的状态
			Adspace adspace = adspaceService.queryAdspaceById(adspaceDto.getId());
	        if (adspace == null)
	            return ResponseUtils.response(StatusCode.SC20002, null);
	        BeanUtils.copyProperties(adspaceDto, adspace);
	        BeanUtils.setUpdateParam(adspace);
	        adspaceService.updateStatus(adspace);
	        return ResponseUtils.response(StatusCode.SC20000,null);
		}
    }
	
	/**
	 * 添加我方广告位和媒体、dsp方广告位的映射
	 * @param adspaceMappingDto
	 * @return
	 */
	@ResponseBody
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
	@ResponseBody
	@RequestMapping("/mapping/detail")
	public ResponseDto<AdspaceMappingDto> adspaceMappingDetail(@RequestParam(value="id", required=true) Integer id) {
		AdspaceMapping adspaceMapping = adspaceService.queryAdspaceMappingById(id);
		AdspaceMappingDto adspaceMappingDto = new AdspaceMappingDto();
		BeanUtils.copyProperties(adspaceMapping, adspaceMappingDto);
		List<AdspaceMappingDto> result = new ArrayList<AdspaceMappingDto>();
		result.add(adspaceMappingDto);
		return ResponseUtils.response(StatusCode.SC20000,result);
	}
	
	/**
	 * 更新我方广告位和媒体、dsp方广告位的映射
	 * @param adspaceMappingDto
	 * @return
	 */
	@ResponseBody
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
	
}
