package com.exam.calculator;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1)
public class CalculatorLoggingAspect{
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Before("CalculatorPointcuts.loggingOperation()")
	public void logJoinPoint(JoinPoint joinPoint) {
		log.info("Join point kind : {}", joinPoint.getKind());
		log.info("Signature declaring type : {}", 
				joinPoint.getSignature().getDeclaringTypeName());
		log.info("Signature name : {}", joinPoint.getSignature().getName());
		log.info("Arguments : {}", Arrays.toString(joinPoint.getArgs()));
		log.info("Target class : {}", joinPoint.getTarget().getClass().getName());
		log.info("This class : {}", joinPoint.getThis().getClass().getName());
	}
	
	@AfterReturning(
			pointcut = "CalculatorPointcuts.loggingOperation()",
			returning = "result")
	public void logAfterReturning(JoinPoint joinPoint, Object result) {
		log.info("The method " + joinPoint.getSignature().getName()
                + "() ends with " + result);
	}
}
