package com.madhouse.platform.premiummad.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dto.ResponseDto;
import com.madhouse.platform.premiummad.entity.UserAuth;
import com.madhouse.platform.premiummad.rule.BaseRule;
import com.madhouse.platform.premiummad.service.IUserAuthService;
import com.madhouse.platform.premiummad.util.BeanUtils;
import com.madhouse.platform.premiummad.util.ObjectUtils;
import com.madhouse.platform.premiummad.util.ResponseUtils;

@RestController
@RequestMapping("/userauth")
public class UserAuthController {

	@Autowired
    private IUserAuthService userAuthService;
	
	@RequestMapping("/media/query")
    public ResponseDto<UserAuth> queryMediaAuth(@RequestParam Integer specifiedUserId) {
		List<Integer> mediaIdList = userAuthService.queryAuthorizedMediaIdList(specifiedUserId);
		List<UserAuth> result = convertToDto(mediaIdList, specifiedUserId);
        return ResponseUtils.response(StatusCode.SC20000, result);
	}
	
	private List<UserAuth> convertToDto(List<Integer> mediaIdList, Integer specifiedUserId) {
		List<UserAuth> result = new ArrayList<UserAuth>();
		UserAuth userAuth = new UserAuth();
		result.add(userAuth);
		userAuth.setSpecifiedUserId(specifiedUserId);
		if(ObjectUtils.isEmpty(mediaIdList)){ //无权限查看任何媒体
			userAuth.setIsAdmin(0);
		} else{
			if(mediaIdList.size() == 1 && 
					String.valueOf(mediaIdList.get(0)).equals(SystemConstant.OtherConstant.SYSTEM_ADMIN_MEDIA_ID)){
				//Admin
				userAuth.setIsAdmin(1);
			} else{
				Integer[] mediaIds = mediaIdList.toArray(new Integer[]{});
				userAuth.setIsAdmin(0);
				userAuth.setMediaIds(mediaIds);
			}
		}
		
		return result;
	}
	
	@RequestMapping("/policy/query")
    public ResponseDto<UserAuth> queryPolicyAuth(@RequestParam Integer specifiedUserId) {
		List<Integer> policyIdList = userAuthService.queryAuthorizedPolicyIdList(specifiedUserId);
		List<UserAuth> result = convertToPolicyDto(policyIdList, specifiedUserId);
        return ResponseUtils.response(StatusCode.SC20000, result);
	}
	
	private List<UserAuth> convertToPolicyDto(List<Integer> policyIdList, Integer specifiedUserId) {
		List<UserAuth> result = new ArrayList<UserAuth>();
		UserAuth userAuth = new UserAuth();
		result.add(userAuth);
		userAuth.setSpecifiedUserId(specifiedUserId);
		if(ObjectUtils.isEmpty(policyIdList)){ //无权限查看任何媒体
			userAuth.setIsAdmin(0);
		} else{
			if(policyIdList.size() == 1 && 
					String.valueOf(policyIdList.get(0)).equals(SystemConstant.OtherConstant.SYSTEM_ADMIN_POLICY_ID)){
				userAuth.setIsAdmin(1);
			} else{
				Integer[] policyIds = policyIdList.toArray(new Integer[]{});
				userAuth.setIsAdmin(0);
				userAuth.setPolicyIds(policyIds);
			}
		}
		
		return result;
	}

	@RequestMapping("/media/update")
    public ResponseDto<Void> updateMedia(@RequestBody UserAuth userAuth) {
		BaseRule.validateDto(userAuth);
		BeanUtils.setCommonParam(userAuth, true);
		userAuthService.updateUserMediaAuth(userAuth);
        return ResponseUtils.response(StatusCode.SC20000, null);
	}
	
	@RequestMapping("/policy/update")
    public ResponseDto<Void> updatePolicy(@RequestBody UserAuth userAuth) {
		BaseRule.validateDto(userAuth);
		BeanUtils.setCommonParam(userAuth, true);
		userAuthService.updateUserPolicyAuth(userAuth);
        return ResponseUtils.response(StatusCode.SC20000, null);
	}
}
