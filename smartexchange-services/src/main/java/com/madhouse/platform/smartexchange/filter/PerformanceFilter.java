package com.madhouse.platform.smartexchange.filter;

import com.madhouse.platform.smartexchange.constant.SystemConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 记录请求参数,记录请求耗时
 */
public class PerformanceFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(SystemConstant.LOGGER_SMARTEXCHANGE);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		long beginTime = System.currentTimeMillis();
		LOGGER.debug("url:" + req.getHeader(SystemConstant.URL));
		LOGGER.debug("userId:"+req.getHeader(SystemConstant.USERID));

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
