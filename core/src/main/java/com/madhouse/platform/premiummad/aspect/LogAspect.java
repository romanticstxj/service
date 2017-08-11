package com.madhouse.platform.premiummad.aspect;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.madhouse.platform.premiummad.constant.SystemConstant;

/**
 * 打印service方法的参数和返回值
 */
@Component
@Aspect
public class LogAspect {
	
	private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

	@Pointcut(SystemConstant.Logging.AOP_SERVICE_IMPL_EXPR)
	public void pointCutService() {

	}
	
	@Before("pointCutService()")
	public void beforeService(JoinPoint joinPoint) throws NoSuchMethodException, SecurityException{
		Object[] args = joinPoint.getArgs();
		StringBuffer sb = new StringBuffer("Begin service: ")
				.append(joinPoint.toShortString()).append(" with parameters of (");
		for(Object arg: args){
			sb.append(arg).append(" ");
		}
		sb.append(")");
		logger.debug(sb.toString());
	}
	
	@After("pointCutService()")
	public void afterService(JoinPoint joinPoint) {
		logger.debug("End service");
	}

	@AfterReturning(value="pointCutService()", returning="retVal", argNames="retVal")
	public void afterReturnService(JoinPoint joinPoint, Object retVal){
		logger.debug("Service result: " + retVal.toString());
	}
	
	@Pointcut(SystemConstant.Logging.AOP_SERVICE_CNTR_EXPR)
	public void pointCutController() {

	}

	@Before("pointCutController()")
	public void beforeController(JoinPoint joinPoint) throws Throwable {
		Object[] objects = joinPoint.getArgs();
		StringBuffer sb = new StringBuffer("Begin Controller: ").append(joinPoint.toShortString())
				.append(" with parameters of (");
		for (Object obj : objects) {
			if (!(ServletRequest.class.isAssignableFrom(obj.getClass()) || ServletResponse.class.isAssignableFrom(obj.getClass()) || MultipartFile.class.isAssignableFrom(obj.getClass()) || WebDataBinder.class.isAssignableFrom(obj.getClass()))) {
				sb.append(JSON.toJSONString(obj)).append(" ");
			}
		}
		sb.append(")");
		logger.debug(sb.toString());
	}
}
