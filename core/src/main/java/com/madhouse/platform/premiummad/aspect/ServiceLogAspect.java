package com.madhouse.platform.premiummad.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.madhouse.platform.premiummad.constant.SystemConstant;

/**
 * 打印service方法的参数和返回值
 */
@Component
@Aspect
public class ServiceLogAspect {
	
	private static final Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);

	@Pointcut(SystemConstant.Logging.AOP_SERVICE_IMPL_EXPR)
	public void pointCut() {

	}
	
	@Before("pointCut()")
	public void before(JoinPoint joinPoint) throws NoSuchMethodException, SecurityException{
//		String methodName = joinPoint.getSignature().getName();
//		Method[] methods = joinPoint.getSignature().getDeclaringType().getMethods();
//		Comment comment = null;
//		for(Method method: methods){
//			if(method.getName() != null && method.getName().equals(methodName) && method.getDeclaredAnnotation(Comment.class) != null){
//				comment = method.getDeclaredAnnotation(Comment.class);
//				break;
//			}
//		}
//		if(comment!= null){
//			String value = comment.value();
//			logger.debug(value);
//		}
		
		Object[] args = joinPoint.getArgs();
		StringBuffer sb = new StringBuffer("Begin service: ")
				.append(joinPoint.toLongString()).append("\n");
		for(Object arg: args){
			sb.append(arg);
		}
		
		logger.debug(sb.toString());
	}
	
	@After("pointCut()")
	public void after(JoinPoint joinPoint) {
		logger.debug("End service");
	}

	@AfterReturning(value="pointCut()", returning="retVal", argNames="retVal")
	public void afterReturn(JoinPoint joinPoint, Object retVal){
		logger.debug("Service result: " + retVal.toString());
	}
}
