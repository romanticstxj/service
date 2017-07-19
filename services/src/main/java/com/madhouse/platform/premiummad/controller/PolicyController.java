package com.madhouse.platform.premiummad.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.dto.PolicyDto;
import com.madhouse.platform.premiummad.dto.ResponseDto;
import com.madhouse.platform.premiummad.entity.Policy;
import com.madhouse.platform.premiummad.rule.PolicyRule;
import com.madhouse.platform.premiummad.service.IPolicyService;
import com.madhouse.platform.premiummad.service.IUserAuthService;
import com.madhouse.platform.premiummad.util.BeanUtils;
import com.madhouse.platform.premiummad.util.ResponseUtils;

@RestController
@RequestMapping("/policy")
public class PolicyController {
	
	@Autowired
	private IPolicyService policyService;
	
	@Autowired
    private IUserAuthService userAuthService;
	
	@RequestMapping("/create")
    public ResponseDto<PolicyDto> addPolicy(@RequestBody PolicyDto policyDto) {
		String fieldName = BeanUtils.hasEmptyField(policyDto);
        if (fieldName != null)
            return ResponseUtils.response(StatusCode.SC20001, null, fieldName + " cannot be null");
        
        
        Policy policy = PolicyRule.convertToModel(policyDto, new Policy());
        policyService.insert(policy);
        List<PolicyDto> result = PolicyRule.convertToDto(policy, new PolicyDto());
        return ResponseUtils.response(StatusCode.SC20000, result);
	}
}
