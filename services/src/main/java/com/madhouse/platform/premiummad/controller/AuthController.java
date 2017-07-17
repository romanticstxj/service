package com.madhouse.platform.premiummad.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.madhouse.platform.premiummad.service.IUserAuthService;
import com.madhouse.platform.premiummad.util.StringUtils;

@Controller
public class AuthController {
	
//	@Autowired
//    private IUserAuthService userAuthService;
//	
//	@RequestMapping("/media/list")
//    public Object mediaList(@RequestHeader(value="X-User-Id") Integer userId) throws Exception {
//		// id未传值，获取当前用户所有权限的媒体列表
//		List<Integer> mediaIdList = userAuthService.queryMediaIdListByUserId(userId);
//		//此用户有权限访问媒体
//		if(mediaIdList != null && mediaIdList.size()> 0){
//			//系统管理员，有访问所有媒体的权限
//			String mediaIds = StringUtils.getIdsStr(mediaIdList);
//			return "redirect:/media/listByMediaIds?ids=" + mediaIds;
//			
//		} else{ //用户没有权限访问媒体
//			return "redirect:/media/listByMediaIds";
//		}
//		
//		
//    }
//	
}
