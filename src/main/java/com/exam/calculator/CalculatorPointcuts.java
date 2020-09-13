package com.exam.calculator;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class CalculatorPointcuts {
	@Pointcut("execution(* *.*(..))")
	public void loggingOperation() {}
}
