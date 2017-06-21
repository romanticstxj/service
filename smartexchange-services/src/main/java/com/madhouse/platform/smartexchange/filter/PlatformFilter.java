package com.madhouse.platform.smartexchange.filter;

import com.alibaba.fastjson.JSON;
import com.madhouse.platform.smartexchange.constant.StatusCode;
import com.madhouse.platform.smartexchange.constant.SystemConstant;
import com.madhouse.platform.smartexchange.dto.ResponseDto;
import com.madhouse.platform.smartexchange.dto.ResponseHeaderDto;
import com.madhouse.platform.smartexchange.exception.BusinessException;
import com.madhouse.platform.smartexchange.exception.InvalidArgumentException;
import com.madhouse.platform.smartexchange.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

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

		try {
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
		} catch (Exception e) {
			Throwable t = e.getCause();
			if (t.getClass().equals(InvalidArgumentException.class)) {
				InvalidArgumentException invalid = (InvalidArgumentException) t;
				handleException(req, res, invalid.getStatusCode());
			} else if (t.getClass().equals(BusinessException.class)) {
				BusinessException business = (BusinessException) t;
				handleException(req, res, business.getStatusCode());
			} else {
				e.printStackTrace();
				logger.error(e.getMessage());
				handleException(req, res, StatusCode.SC31001);
			}
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
