package com.madhouse.platform.premiummad.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONException;
import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dto.ResponseDto;
import com.madhouse.platform.premiummad.util.LogUtils;
import com.madhouse.platform.premiummad.util.ResponseUtils;

/**
 * 全局日常处理器
 */
@ControllerAdvice
public class CommonExceptionHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(SystemConstant.Logging.LOGGER_PREMIUMMAD_ERROR);
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseDto<Object> handleAllException(Exception ex) {
		logger.error(LogUtils.getDetailException(ex));
		if (ex instanceof BusinessException) {
			return ResponseUtils.response(((BusinessException) ex).getStatusCode(), null, ex.getMessage());
		} else if (ex instanceof JSONException) {
			return ResponseUtils.response(StatusCode.SC400);
		} else {
			return ResponseUtils.response(StatusCode.SC500, null, ex.getMessage());
		}
	}
}
