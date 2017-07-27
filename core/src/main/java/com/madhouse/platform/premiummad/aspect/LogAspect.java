package com.madhouse.platform.premiummad.aspect;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.constant.SystemConstant;

/**
 * 记录controller的请求参数，需要在spring-mvc.xml中配置aop
 */

@Component
@Aspect
public class LogAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(SystemConstant.Logging.LOGGER_PREMIUMMAD);

	@Pointcut("execution(* com.madhouse.platform.premiummad.controller.*.*(..))")
	public void logPointCut() {

	}

	@Around("logPointCut()")
	public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
		Object[] objects = joinPoint.getArgs();
		if (objects != null) {
			for (Object obj : objects) {
				if (obj != null) {
					if (!(ServletRequest.class.isAssignableFrom(obj.getClass()) || ServletResponse.class.isAssignableFrom(obj.getClass()) || MultipartFile.class.isAssignableFrom(obj.getClass()) || WebDataBinder.class.isAssignableFrom(obj.getClass()))) {
						try {
							LOGGER.debug(JSON.toJSONString(obj));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return joinPoint.proceed(objects);
	}
}
