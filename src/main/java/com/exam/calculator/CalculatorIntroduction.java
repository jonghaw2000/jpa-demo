package com.exam.calculator;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareParents;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CalculatorIntroduction {

	@DeclareParents(
			value = "com.exam.calculator.ArithmeticCalculatorImpl",
			defaultImpl = MaxCalculatorImpl.class)
	public MaxCalculator maxCalculator;
	
	@DeclareParents(
			value = "com.exam.calculator.ArithmeticCalculatorImpl",
			defaultImpl = MinCalculatorImpl.class)
	public MinCalculator minCalculator;
}
