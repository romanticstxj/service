package com.madhouse.platform.premiummad.aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * service处理完了要清空当前线程中的CustomerType
 */
@Component
@Aspect
public class ClearContextHolder {

	@Pointcut("execution(* com.madhouse.platform.smartexchange.service.impl.*.*(..))")
	public void pointCut() {

	}

	@After("pointCut()")
	public void clearContext() {
		ContextHolder.clearCustomerType();
	}

}
