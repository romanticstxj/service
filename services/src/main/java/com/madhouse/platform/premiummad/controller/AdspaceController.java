package com.madhouse.platform.premiummad.controller;

import java.text.DecimalFormat;
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
import com.madhouse.platform.premiummad.dto.MediaDto;
import com.madhouse.platform.premiummad.dto.ResponseDto;
import com.madhouse.platform.premiummad.entity.Adspace;
import com.madhouse.platform.premiummad.service.IAdspaceService;
import com.madhouse.platform.premiummad.service.IUserAuthService;
import com.madhouse.platform.premiummad.util.BeanUtils;
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
	        return ResponseUtils.response(StatusCode.SC21028, null);
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
    public ResponseDto<MediaDto> addAdspace(@RequestBody AdspaceDto adspaceDto, 
    		@RequestHeader(value=SystemConstant.XFROM, required=true) String xFrom) {
		String fieldName = BeanUtils.hasEmptyField(adspaceDto);
        if (fieldName != null)
            return ResponseUtils.response(StatusCode.SC21004, null, fieldName + " cannot be null");
        Integer count = adspaceService.checkName(adspaceDto.getName().trim());
        if (count > 0) //检查名称
            return ResponseUtils.response(StatusCode.SC22004,null);
        Adspace adspace = new Adspace();
        BeanUtils.copyProperties(adspaceDto, adspace, SystemConstant.ADSPACE_BID_FLOOR);
        BeanUtils.setCreateParam(adspace);
        preprocessAdspaceParams(adspace, adspaceDto);
        adspaceService.insert(adspace);
        
        postprocessAdspaceParams(adspace, xFrom);
        adspaceService.update(adspace);
        return ResponseUtils.response(StatusCode.SC20000,null);
	}
	private void postprocessAdspaceParams(Adspace adspace, String xFrom) {
		//生成adspaceKey，更新入到数据库e
		Integer id = adspace.getId();
		String name = adspace.getName();
		String combinedStr = new StringBuffer(id.toString()).append(name).append(xFrom).toString();
		String adspaceKey = StringUtils.getMD5(combinedStr);
		adspace.setAdspaceKey(adspaceKey);
	}

	private void preprocessAdspaceParams(Adspace adspace, AdspaceDto adspaceDto) {
		//把页面上的底价（元）转换成数据库需要的底价（分）
		Double bidFloor = adspaceDto.getBidFloor();
		Integer bidFloorUnitFen = Integer.parseInt(new DecimalFormat(SystemConstant.ZERO).format(bidFloor * SystemConstant.RATIO_FEN_TO_YUAN));
		adspace.setBidFloor(bidFloorUnitFen);
		
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
			return ResponseUtils.response(StatusCode.SC21032, null);
		}
		
		//更新广告位
		if(updateType.equals(1)){
			String fieldName = BeanUtils.hasEmptyField(adspaceDto);
	        if (fieldName != null)
	            return ResponseUtils.response(StatusCode.SC21004, null, fieldName + " cannot be null");
	        Adspace adspace = adspaceService.queryAdspaceById(adspaceDto.getId());
	        if (adspace == null)
	            return ResponseUtils.response(StatusCode.SC21024, null);
	        if (!adspaceDto.getName().equals(adspaceDto.getName())) { //名称不相等,检查名称
	            Integer count = adspaceService.checkName(adspaceDto.getName().trim());
	            if (count > 0)
	                return ResponseUtils.response(StatusCode.SC22004,null);
	        }
	        BeanUtils.copyProperties(adspaceDto, adspace);
	        BeanUtils.setUpdateParam(adspace);
	        preprocessAdspaceParams(adspace, adspaceDto);
	        adspaceService.update(adspace);
	        return ResponseUtils.response(StatusCode.SC20000,null);
		} else{
			//更新媒体的状态
			Adspace adspace = adspaceService.queryAdspaceById(adspaceDto.getId());
	        if (adspace == null)
	            return ResponseUtils.response(StatusCode.SC21024, null);
	        BeanUtils.copyProperties(adspaceDto, adspace);
	        BeanUtils.setUpdateParam(adspace);
	        adspaceService.updateStatus(adspace);
	        return ResponseUtils.response(StatusCode.SC20000,null);
		}
    }
	
	@ResponseBody
	@RequestMapping("/mapping/create")
	public ResponseDto<MediaDto> addAdspaceMapping(@RequestBody AdspaceMappingDto adspaceMappingDto) {
		
		return null;
	}
	
}
