package com.madhouse.platform.premiummad.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.madhouse.platform.premiummad.constant.StatusCode;
import com.madhouse.platform.premiummad.dto.ResponseDto;
import com.madhouse.platform.premiummad.util.ResponseUtils;

@ControllerAdvice
public class CommonExceptionHandler {
	
	@ExceptionHandler(value = {Exception.class})  
    public ResponseDto<String> handleOtherExceptions(final Exception ex, final WebRequest req) {  
		System.out.println("go in to common ex handler");
        return ResponseUtils.response(StatusCode.SC31001, null, ex.getMessage());
    }  
}
