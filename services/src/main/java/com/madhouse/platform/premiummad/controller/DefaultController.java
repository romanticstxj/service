package com.madhouse.platform.premiummad.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dto.ResponseDto;
import com.madhouse.platform.premiummad.util.ResponseUtils;
import com.madhouse.platform.premiummad.util.StringUtils;

/**
 * 默认Controller，所有不存在的url会被定向到这个methodHandler来处理
 * @author Michael
 *
 */
@RestController
public class DefaultController {
	
	private static final Logger logger = LoggerFactory.getLogger(SystemConstant.Logging.LOGGER_PREMIUMMAD);
	
	@RequestMapping(value = "/error")
	public ResponseDto<Void> handle(HttpServletRequest req) {
		HttpServletRequest request = (HttpServletRequest) req;
		StringBuffer sb = request.getRequestURL();
		String queryString = request.getQueryString();
		if(!StringUtils.isEmpty(queryString)){
			sb.append("?").append(queryString);
		}
		String requestUrl = sb.toString();
		logger.debug("Client launches an invalid request with url: " + requestUrl);
        return ResponseUtils.response(StatusCode.SC31008, null);
	}
}
