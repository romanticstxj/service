package com.madhouse.platform.premiummad.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.constant.SystemConstant;
import com.madhouse.platform.premiummad.dto.ResponseDto;
import com.madhouse.platform.premiummad.util.LogUtils;
import com.madhouse.platform.premiummad.util.ResponseUtils;

/**
 * 全局日常处理器
 * @author Xingjie.Teng
 */
@ControllerAdvice
public class CommonExceptionHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(SystemConstant.LOGGER_PREMIUMMAD_ERROR);
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseDto<Object> handleAllException(Exception ex) {
		logger.error(LogUtils.getDetailException(ex));
		return ResponseUtils.response(StatusCode.SC30001, null);
	}
}
