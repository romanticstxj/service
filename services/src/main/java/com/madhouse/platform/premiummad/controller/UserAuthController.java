package com.madhouse.platform.premiummad.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.dto.AdspaceDto;
import com.madhouse.platform.premiummad.dto.ResponseDto;
import com.madhouse.platform.premiummad.entity.Adspace;
import com.madhouse.platform.premiummad.entity.UserAuth;
import com.madhouse.platform.premiummad.rule.AdspaceRule;
import com.madhouse.platform.premiummad.rule.BaseRule;
import com.madhouse.platform.premiummad.service.IUserAuthService;
import com.madhouse.platform.premiummad.util.ResponseUtils;

@RestController
@RequestMapping("/userauth")
public class UserAuthController {

	@Autowired
    private IUserAuthService userAuthService;
	
	@RequestMapping("/media/update")
    public ResponseDto<Void> updateMedia(@RequestBody UserAuth userAuth) {
		BaseRule.validateDto(userAuth);
		userAuthService.updateUserMediaAuth(userAuth);
        return ResponseUtils.response(StatusCode.SC20000, null);
	}
}
