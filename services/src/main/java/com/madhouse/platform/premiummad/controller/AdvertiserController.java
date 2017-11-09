package com.madhouse.platform.premiummad.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.dto.AuditDto;
import com.madhouse.platform.premiummad.dto.ResponseDto;
import com.madhouse.platform.premiummad.entity.Advertiser;
import com.madhouse.platform.premiummad.rule.AdvertiserAuditRule;
import com.madhouse.platform.premiummad.rule.MaterialAuditRule;
import com.madhouse.platform.premiummad.service.IAdvertiserService;
import com.madhouse.platform.premiummad.service.IUserAuthService;
import com.madhouse.platform.premiummad.util.ObjectUtils;
import com.madhouse.platform.premiummad.util.ResponseUtils;
import com.madhouse.platform.premiummad.util.StringUtils;

@RestController
@RequestMapping("/advertiser")
public class AdvertiserController {
	
	@Autowired
	private IAdvertiserService advertiserService;
	
	@Autowired
    private IUserAuthService userAuthService;
	
	@RequestMapping("/list")
    public ResponseDto<Advertiser> list(@RequestHeader(value="X-User-Id", required=false) Integer userId,
    		@RequestParam(value="userId", required=false) Integer userIdByGet) throws Exception {
		//获得userId，可以从url中获得（方便通过get请求获取数据），更为一般的是从requestHeader里获取
		if(userIdByGet != null){ //优先获取get请求的userId参数
			userId = userIdByGet;
		}
		List<Integer> mediaIdList = userAuthService.queryMediaIdList(userId, null);
		return listByMediaIds(mediaIdList);
    }
	
	private ResponseDto<Advertiser> listByMediaIds(List<Integer> mediaIdList) {
		if(ObjectUtils.isEmpty(mediaIdList)){ //无权限查看任何媒体
	        return ResponseUtils.response(StatusCode.SC20000, new ArrayList<Advertiser>());
		} else{
			List<Advertiser> advertisers = advertiserService.queryAll(mediaIdList);
			AdvertiserAuditRule.convertToDtoList(advertisers);
	        return ResponseUtils.response(StatusCode.SC20000,advertisers);
		}
	}

	@RequestMapping("/detail")
    public ResponseDto<Advertiser> detail(@RequestParam(value="id", required=true) Integer id) throws Exception {
		Advertiser advertiser = advertiserService.queryById(id);
		List<Advertiser> result = AdvertiserAuditRule.convertToDto(advertiser);
        return ResponseUtils.response(StatusCode.SC20000, result);
    }
	
	@RequestMapping("/audit")
    public ResponseDto<Advertiser> audit(@RequestBody AuditDto dto,
    		@RequestHeader(value="X-User-Id", required=false) Integer userId) throws Exception {
		MaterialAuditRule.validateDto(dto);
		String idsStr = dto.getIds();
		Integer status = dto.getStatus();
		String reason = dto.getReason();
		String[] ids = StringUtils.splitToStringArray(idsStr);
		boolean isAllAudited = advertiserService.auditAdvertiser(ids, status, reason, userId);
		if(isAllAudited)
			return ResponseUtils.response(StatusCode.SC20000, null);
		else
			return ResponseUtils.response(StatusCode.SC423, null);
    }
}
