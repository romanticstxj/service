package com.madhouse.platform.premiummad.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.exception.BusinessException;
import com.madhouse.platform.premiummad.util.StringUtils;

public class CommonInterceptor extends HandlerInterceptorAdapter{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SystemConstant.Logging.LOGGER_PREMIUMMAD);
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		String userId = request.getHeader(SystemConstant.Request.USERID);
		String xForm = request.getHeader(SystemConstant.Request.XFROM);
//		String requestBody = HttpUtils.getBodyString(request.getReader());
//		MyRequestWrapper myRequestWrapper = new MyRequestWrapper((HttpServletRequest) request);
//        String requestBody = myRequestWrapper.getBody();
		LOGGER.debug("Request Header, userId:"+userId + ", xform:" + xForm);
//		LOGGER.debug("Request body: " + requestBody);
		if(userId != null){ //userId非空的情况下判断是否数字
			if(!StringUtils.isNumeric(userId)){
				throw new BusinessException(StatusCode.SC31007);
			}
		}
		
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		super.afterCompletion(request, response, handler, ex);
	}
	
}


