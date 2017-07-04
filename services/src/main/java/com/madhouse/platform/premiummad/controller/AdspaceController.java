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
		System.out.println(jdbcSchema);
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
		//生成adspaceKey，更新入到数据库
		Integer id = adspace.getId();
		String name = adspace.getName();
		String jdbcSchema = SystemConstant.JDBC_SCHEMA;
		String combinedStr = new StringBuffer(id).append(name).append(jdbcSchema).toString();
		String adspaceKey = StringUtils.getMD5(combinedStr);
		adspace.setAdspaceKey(adspaceKey);
	}


	private void preprocessAdspaceParams(Adspace adspace, AdspaceDto adspaceDto) {
		//把页面上的底价（元）转换成数据库需要的底价（分）
		Double bidFloor = adspaceDto.getBidFloor();
		Integer bidFloorUnitFen = Integer.parseInt(new DecimalFormat("0").format(bidFloor * SystemConstant.RATIO_FEN_TO_YUAN));
		adspace.setBidFloor(bidFloorUnitFen);
		
	}
	
}
