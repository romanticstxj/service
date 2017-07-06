package com.madhouse.platform.premiummad.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dto.AdspaceDto;
import com.madhouse.platform.premiummad.dto.MediaDto;
import com.madhouse.platform.premiummad.dto.ResponseDto;
import com.madhouse.platform.premiummad.entity.Adspace;
import com.madhouse.platform.premiummad.service.IAdspaceService;
import com.madhouse.platform.premiummad.util.BeanUtils;
import com.madhouse.platform.premiummad.util.ResponseUtils;
import com.madhouse.platform.premiummad.util.StringUtils;

@RestController
@RequestMapping("/adspace")
public class AdspaceController {
	
	@Autowired
    private IAdspaceService adspaceService;
	
	
	/**
	 * 添加广告位
	 * @param adspaceDto
	 * @return
	 */
	@RequestMapping("/create")
    public ResponseDto<MediaDto> addAdspace(@RequestBody AdspaceDto adspaceDto, @RequestHeader(value=SystemConstant.XFROM) String xFrom) {
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
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/detail")
    public ResponseDto<AdspaceDto> getAdspace(@RequestParam(value="id", required=true) Integer id) {
		Adspace adspace = adspaceService.queryAdspaceById(id);
		AdspaceDto adspaceDto = new AdspaceDto();
        BeanUtils.copyProperties(adspace,adspaceDto);
        List<AdspaceDto> adspaceDtos = new ArrayList<>();
        adspaceDtos.add(adspaceDto);
        return ResponseUtils.response(StatusCode.SC20000,adspaceDtos);
    }
	
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
	
}
