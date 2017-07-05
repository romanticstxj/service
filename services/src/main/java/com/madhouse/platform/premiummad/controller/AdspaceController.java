package com.madhouse.platform.premiummad.controller;

import java.text.DecimalFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dto.AdspaceDto;
import com.madhouse.platform.premiummad.dto.MediaDto;
import com.madhouse.platform.premiummad.dto.ResponseDto;
import com.madhouse.platform.premiummad.entity.Adspace;
import com.madhouse.platform.premiummad.entity.Media;
import com.madhouse.platform.premiummad.service.IAdspaceService;
import com.madhouse.platform.premiummad.util.BeanUtils;
import com.madhouse.platform.premiummad.util.ResponseUtils;
import com.madhouse.platform.premiummad.util.StringUtils;

@RestController
@RequestMapping("/adspace")
public class AdspaceController {
	
	@Autowired
    private IAdspaceService adspaceService;
	
	@Value("${jdbc.premiummad.schema}")
	private String jdbcSchema;
	
	/**
	 * 添加广告位
	 * @param adspaceDto
	 * @return
	 */
	@RequestMapping("/create")
    public ResponseDto<MediaDto> addAdspace(@RequestBody AdspaceDto adspaceDto) {
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
        
        postprocessAdspaceParams(adspace);
        adspaceService.update(adspace);
        return ResponseUtils.response(StatusCode.SC20000,null);
	}


	private void postprocessAdspaceParams(Adspace adspace) {
		//生成adspaceKey，更新入到数据库e
		
		Integer id = adspace.getId();
		String name = adspace.getName();
		String combinedStr = new StringBuffer(id.toString()).append(name).append(jdbcSchema).toString();
		String adspaceKey = StringUtils.getMD5(combinedStr);
		adspace.setAdspaceKey(adspaceKey);
	}

	private void preprocessAdspaceParams(Adspace adspace, AdspaceDto adspaceDto) {
		//把页面上的底价（元）转换成数据库需要的底价（分）
		Double bidFloor = adspaceDto.getBidFloor();
		Integer bidFloorUnitFen = Integer.parseInt(new DecimalFormat("0").format(bidFloor * SystemConstant.RATIO_FEN_TO_YUAN));
		adspace.setBidFloor(bidFloorUnitFen);
		
	}
	
//	@RequestMapping("/update")
//    public ResponseDto<MediaDto> updateAdspace(@RequestBody AdspaceDto adspaceDto) {
//		Integer updateType = adspaceDto.getUpdateType();
//		//更新类型未设置，或设置得不正确
//		if(updateType == null || (!updateType.equals(1) && !updateType.equals(2))){
//			return ResponseUtils.response(StatusCode.SC21032, null);
//		}
//		
//		//更新广告位
//		if(updateType.equals(1)){
//			String fieldName = BeanUtils.hasEmptyField(adspaceDto);
//	        if (fieldName != null)
//	            return ResponseUtils.response(StatusCode.SC21004, null, fieldName + " cannot be null");
//	        Adspace adspace = adspaceService.queryAdspaceById(adspaceDto.getId());
//	        if (adspace == null)
//	            return ResponseUtils.response(StatusCode.SC21024, null);
//	        if (!adspaceDto.getName().equals(adspaceDto.getName())) { //名称不相等,检查名称
//	            Integer count = adspaceService.checkName(adspaceDto.getName().trim());
//	            if (count > 0)
//	                return ResponseUtils.response(StatusCode.SC22004,null);
//	        }
//	        BeanUtils.copyProperties(mediaDto, media);
//	        BeanUtils.setUpdateParam(media);
//	        mediaService.update(media);
//	        return ResponseUtils.response(StatusCode.SC20000,null);
//		} else{
//			//更新媒体的状态
//			Media media = mediaService.queryMediaById(mediaDto.getId());
//	        if (media == null)
//	            return ResponseUtils.response(StatusCode.SC21024, null);
//	        BeanUtils.copyProperties(mediaDto, media);
//	        BeanUtils.setUpdateParam(media);
//	        int i = mediaService.updateStatus(media);
//	        return ResponseUtils.response(StatusCode.SC20000,null);
//		}
//		
//    }
	
}
