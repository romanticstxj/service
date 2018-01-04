package com.madhouse.platform.premiummad.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.dto.ResponseDto;
import com.madhouse.platform.premiummad.util.LogUtils;
import com.madhouse.platform.premiummad.util.ResponseUtils;

/**
 * 全局日常处理器
 */
@ControllerAdvice
public class CommonExceptionHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CommonExceptionHandler.class);
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseDto<Object> handleAllException(Exception ex) {
		ResponseDto<Object> response = null;
		if (ex instanceof BusinessException) {
			response = ResponseUtils.response(((BusinessException) ex).getStatusCode(), null, ex.getMessage());
		} else if (ex instanceof JSONException) {
			LOGGER.error(LogUtils.getDetailException(ex));
			response = ResponseUtils.response(StatusCode.SC400);
		} else if (ex instanceof MethodArgumentNotValidException) {
			LOGGER.error(LogUtils.getDetailException(ex));
			String[] msgArray = ex.getMessage().split(";");
			String lastMsg = msgArray[msgArray.length - 1];
			String mainMsg = lastMsg.replace("default message", "").trim();
			String errorMsg = mainMsg.substring(1, mainMsg.length() - 2);
			response = ResponseUtils.response(StatusCode.SC400, null, errorMsg);
		} else {
			LOGGER.error(LogUtils.getDetailException(ex));
			response = ResponseUtils.response(StatusCode.SC500, null, ex.getMessage());
		}
		
		LOGGER.info("DSP Exception-{}", JSON.toJSONString(response));
		return response;
	}
}
