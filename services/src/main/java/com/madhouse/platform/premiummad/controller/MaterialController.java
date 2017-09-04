
package com.madhouse.platform.premiummad.controller;
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
import com.madhouse.platform.premiummad.entity.Material;
import com.madhouse.platform.premiummad.rule.MaterialAuditRule;
import com.madhouse.platform.premiummad.service.IMaterialService;
import com.madhouse.platform.premiummad.service.IUserAuthService;
import com.madhouse.platform.premiummad.util.ObjectUtils;
import com.madhouse.platform.premiummad.util.ResponseUtils;
import com.madhouse.platform.premiummad.util.StringUtils;

@RestController
@RequestMapping("/material")
public class MaterialController {
	
	@Autowired
	private IMaterialService materialService;
	
	@Autowired
    private IUserAuthService userAuthService;
	
	@RequestMapping("/list")
    public ResponseDto<Material> list(@RequestHeader(value="X-User-Id", required=false) Integer userId,
    		@RequestParam(value="userId", required=false) Integer userIdByGet) throws Exception {
		//获得userId，可以从url中获得（方便通过get请求获取数据），更为一般的是从requestHeader里获取
		if(userIdByGet != null){ //优先获取get请求的userId参数
			userId = userIdByGet;
		}
		List<Integer> mediaIdList = userAuthService.queryMediaIdList(userId, null);
		return listByMediaIds(mediaIdList);
    }
	
	private ResponseDto<Material> listByMediaIds(List<Integer> mediaIdList) {
		if(ObjectUtils.isEmpty(mediaIdList)){ //无权限查看任何媒体
	        return ResponseUtils.response(StatusCode.SC20001, null);
		} else{
			List<Material> materials = materialService.queryAll(mediaIdList);
			MaterialAuditRule.convertToDtoList(materials);
	        return ResponseUtils.response(StatusCode.SC20000,materials);
		}
	}

	@RequestMapping("/detail")
    public ResponseDto<Material> detail(@RequestParam(value="id", required=true) Integer id) throws Exception {
		Material material = materialService.queryById(id);
		List<Material> result = MaterialAuditRule.convertToDto(material);
        return ResponseUtils.response(StatusCode.SC20000, result);
    }
	
	@RequestMapping("/audit")
    public ResponseDto<Material> audit(@RequestBody AuditDto dto) throws Exception {
		MaterialAuditRule.validateDto(dto);
		String idsStr = dto.getIds();
		Integer status = dto.getStatus();
		String reason = dto.getReason();
		String[] ids = StringUtils.splitToStringArray(idsStr);
		materialService.auditMaterial(ids, status, reason);
        return ResponseUtils.response(StatusCode.SC20000, null);
    }
}
