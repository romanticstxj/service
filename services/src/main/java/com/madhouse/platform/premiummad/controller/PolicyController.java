package com.madhouse.platform.premiummad.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dto.PolicyDto;
import com.madhouse.platform.premiummad.dto.ResponseDto;
import com.madhouse.platform.premiummad.entity.Policy;
import com.madhouse.platform.premiummad.rule.PolicyRule;
import com.madhouse.platform.premiummad.service.IPolicyService;
import com.madhouse.platform.premiummad.service.IUserAuthService;
import com.madhouse.platform.premiummad.util.ObjectUtils;
import com.madhouse.platform.premiummad.util.ResponseUtils;
import com.madhouse.platform.premiummad.util.StringUtils;
import com.madhouse.platform.premiummad.validator.Update;
import com.madhouse.platform.premiummad.validator.UpdateStatus;

@RestController
@RequestMapping("/policy")
public class PolicyController {
	
	@Autowired
	private IPolicyService policyService;
	
	@Autowired
    private IUserAuthService userAuthService;
	
	@RequestMapping("/list")
    public ResponseDto<PolicyDto> list(@RequestParam(value="ids", required=false) String policyIds,
    		@RequestParam(value="status", required=false) Integer status,
    		@RequestParam(value="type", required=true) Integer type,
    		@RequestParam(value="userId", required=false) Integer userIdByGet,
    		@RequestHeader(value="X-User-Id", required=false) Integer userId) throws Exception {
		PolicyRule.checkStatus(status);
		
		//获得userId，可以从url中获得（方便通过get请求获取数据），更为一般的是从requestHeader里获取
		if(userIdByGet != null){ //优先获取get请求的userId参数
			userId = userIdByGet;
		}
		
		List<Integer> policyIdList = userAuthService.queryPolicyIdList(userId, policyIds);
		String returnedPolicyIds = StringUtils.getIdsStr(policyIdList);
		return listByParams(returnedPolicyIds, status, type);
    }
	
	private ResponseDto<PolicyDto> listByParams(String policyIds, Integer status, Integer type){
		//无权限查看任何Policy
		if(policyIds == null || policyIds.equals("")){
	        return ResponseUtils.response(StatusCode.SC20006, null);
		} else{ // admin权限，查询所有policy;非admin，有部分policy权限
			if(policyIds.equals(SystemConstant.SYSTEM_ADMIN_POLICY_ID)){ //如果是管理员
				policyIds = null;
			}
			List<Policy> policies = policyService.queryAllByParams(policyIds, status, type);
			List<PolicyDto> result = PolicyRule.convertToDtoList(policies, new ArrayList<PolicyDto>());
	        return ResponseUtils.response(StatusCode.SC20000, result);
		}
	}
	
	/**
	 * 创建策略
	 * @param policyDto
	 * @return
	 */
	@RequestMapping("/create")
    public ResponseDto<PolicyDto> addPolicy(@RequestBody PolicyDto policyDto) {
		PolicyRule.validateDto(policyDto);
        Policy policy = PolicyRule.convertToModel(policyDto, new Policy());
        policyService.insert(policy);
        List<PolicyDto> result = PolicyRule.convertToDto(policy, new PolicyDto());
        return ResponseUtils.response(StatusCode.SC20000, result);
	}
	
	/**
	 * 查询具体策略
	 * @param id
	 * @return
	 */
	@RequestMapping("/detail")
    public ResponseDto<PolicyDto> getPolicy(@RequestParam(value="id", required=true) Integer id,
    		@RequestParam(value="type", required=true) Integer type,
    		@RequestHeader(value="X-User-Id", required=false) Integer userId) {
		//权限check
		if(userId == null){
			return ResponseUtils.response(StatusCode.SC20006, null);
		}
		
		List<Integer> policyIdList = userAuthService.queryPolicyIdList(userId, String.valueOf(id));
		if(userId == null || ObjectUtils.isEmpty(policyIdList) || policyIdList.get(0).intValue() != id.intValue()){
			return ResponseUtils.response(StatusCode.SC20006, null);
		}
		
		Policy policy = policyService.queryPolicyById(id, type);
		List<PolicyDto> result = PolicyRule.convertToDto(policy, new PolicyDto());
		return ResponseUtils.response(StatusCode.SC20000, result);
	}
	
	/**
	 * 更新具体策略
	 * @param policyDto
	 * @return
	 */
	@RequestMapping("/update")
    public ResponseDto<PolicyDto> updatePolicy(@RequestBody @Validated(Update.class) PolicyDto policyDto) {
		PolicyRule.validateDto(policyDto);
        Policy policy = PolicyRule.convertToModel(policyDto, new Policy());
        policyService.update(policy);
        List<PolicyDto> result = PolicyRule.convertToDto(policy, new PolicyDto());
        return ResponseUtils.response(StatusCode.SC20000, result);
    }
	
	@RequestMapping("/updateStatus")
    public ResponseDto<PolicyDto> updatePolicyStatus(
    		@RequestBody @Validated(UpdateStatus.class) PolicyDto policyDto) {
        Policy policy = PolicyRule.convertToModel(policyDto, new Policy());
        policyService.updateStatus(policy);
        List<PolicyDto> result = PolicyRule.convertToDto(policy, new PolicyDto());
        return ResponseUtils.response(StatusCode.SC20000, result);
    }
	
}
