package com.madhouse.platform.premiummad.spring;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.util.StringUtils;

public class DefaultServlet implements Servlet{
	
	private static final Logger logger = LoggerFactory.getLogger(SystemConstant.Logging.LOGGER_PREMIUMMAD);
	
	private final static String CONTENT_TYPE = "text/html;charset=UTF-8";

	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
	}

	@Override
	public ServletConfig getServletConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		HttpServletRequest request = (HttpServletRequest) req;
		StringBuffer sb = request.getRequestURL();
		String queryString = request.getQueryString();
		if(!StringUtils.isEmpty(queryString)){
			sb.append("?").append(queryString);
		}
		String requestUrl = sb.toString();
		logger.debug("Client launches an invalid request with url: " + requestUrl);
		res.setContentType(CONTENT_TYPE);
		PrintWriter out = res.getWriter();
		out.println("此接口不存在，请输入一个正确的URL，谢谢！");
	}

	@Override
	public String getServletInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}
	
}
