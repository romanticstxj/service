package com.madhouse.platform.premiummad.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import com.madhouse.platform.premiummad.annotation.TokenFilter;
import com.madhouse.platform.premiummad.service.impl.DspServiceImpl;

public class TokenInterceptor extends HandlerInterceptorAdapter {
	protected static final Logger log = LoggerFactory.getLogger("TokenInterceptor");

	@Autowired
	private DspServiceImpl dspService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			if (handlerMethod.getMethodAnnotation(TokenFilter.class) == null) {
				return true;
			}
		}

		String token = request.getParameter("token");
		String dspId = request.getParameter("dspId");
		dspService.checkDspPermission(dspId, token);

		return true;
	}
}
