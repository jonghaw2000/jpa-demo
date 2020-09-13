package com.exam.calculator;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.exam.calculator.config.CalculatorConfiguration;

public class IntroductionMain {
	public static void main(String[] args) {
		ApplicationContext context =
				new AnnotationConfigApplicationContext(CalculatorConfiguration.class);
		
		ArithmeticCalculator arithmeticCalculator =
				(ArithmeticCalculator) context.getBean("arithmeticCalculator");
		
		MaxCalculator maxCalculator = (MaxCalculator) arithmeticCalculator;
		maxCalculator.max(1, 2);
		
		MinCalculator minCalculator = (MinCalculator) arithmeticCalculator;
		minCalculator.min(1, 2);
	}
}
