package com.madhouse.platform.premiummad.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.madhouse.platform.premiummad.constant.SystemConstant;

/**
 * 记录请求参数,记录请求耗时
 */
public class PerformanceFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(SystemConstant.Logging.LOGGER_PREMIUMMAD);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		long beginTime = System.currentTimeMillis();
//		LOGGER.debug("url:" + req.getHeader(SystemConstant.URL));
		LOGGER.debug("userId:"+req.getHeader(SystemConstant.Request.USERID));

		chain.doFilter(request, response);

		long endTime = System.currentTimeMillis();
		String url = req.getRequestURL().toString();
		String lineSeparator = System.getProperty("line.separator", "\n");
		LOGGER.debug(url + "  " + (endTime - beginTime) + "ms" + lineSeparator); // 每次请求换行
	}

	@Override
	public void destroy() {
		
	}

}
