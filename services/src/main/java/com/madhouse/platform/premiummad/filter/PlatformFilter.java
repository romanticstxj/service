package com.madhouse.platform.premiummad.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dto.ResponseDto;
import com.madhouse.platform.premiummad.dto.ResponseHeaderDto;
import com.madhouse.platform.premiummad.util.StringUtils;

/**
 * 判断header值是否为空，并且处理非空
 */
public class PlatformFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(PlatformFilter.class);

	private String exclusionsPath;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		exclusionsPath = filterConfig.getInitParameter("exclusions");
	}

	/**
	 * 处理排除的url
	 * @param requestURI web.xml配置的
	 * @return boolean
	 */
	private boolean isExclusionsPath(String requestURI) {
		boolean flag = false;
		String[] split = exclusionsPath.split(",");
		for (String path : split) {
			flag = requestURI.contains(path);
			if (flag) break;
		}
		return flag;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = null;
		HttpServletResponse res = null;

		req = (HttpServletRequest) request;
		res = (HttpServletResponse) response;
		String requestURI = req.getRequestURI();

		if (isExclusionsPath(requestURI)) {
			chain.doFilter(request, response);
		} else {
			// 判断url是否为null
			String url = req.getHeader(SystemConstant.URL);
			if (StringUtils.isEmpty(url)) {
				handleException(req, res, StatusCode.SC21013);
				return;
			}
			// 判断userId是否为null
			String userId_temp = req.getHeader((SystemConstant.USERID));
			if (StringUtils.isEmpty(userId_temp)) {
				handleException(req, res, StatusCode.SC21003);
				return;
			} else {
				try {
					Integer userId = Integer.parseInt(userId_temp); // 是否是数字
				} catch (Exception e) {
					handleException(req, res, StatusCode.SC21004);
					return;
				}
			}
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {

	}

	/**
	 * 统一处理异常
	 * @param request 	请求
	 * @param response	返回
	 * @param sc		状态码
	 * @throws IOException
	 */
	private void handleException(HttpServletRequest request, HttpServletResponse response, StatusCode sc) throws IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter writer = response.getWriter();

		ResponseHeaderDto responseHeaderDto = new ResponseHeaderDto();
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResponseHeaderDto(responseHeaderDto);

		responseHeaderDto.setResponseCode(SystemConstant.RESPONSECODE_ERROR);
		responseHeaderDto.setErrorCode(sc.getValue());
		responseHeaderDto.setErrorMsg(sc.getDescrip());

		String result = JSON.toJSONString(responseDto); // 错误结果返回
		writer.print(result);

		writer.flush();
		writer.close();

		String url = request.getRequestURL().toString();
		logger.error(url + " occure an error caused by: " + sc.getValue() + " " + sc.getDescrip());
	}

}
